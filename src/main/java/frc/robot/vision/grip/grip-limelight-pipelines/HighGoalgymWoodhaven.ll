//
// Inputs
//
Inputs
{
	Mat source0;
}

//
// Variables
//
Outputs
{
	Mat blurOutput;
	Mat hsvThreshold0Output;
	Mat maskOutput;
	Mat hsvThreshold1Output;
	ContoursReport findContoursOutput;
	ContoursReport filterContoursOutput;
}

//
// Steps
//

Step Blur0
{
    Mat blurInput = source0;
    BlurType blurType = BOX;
    Double blurRadius = 0.9009009009009009;

    blur(blurInput, blurType, blurRadius, blurOutput);
}

Step HSV_Threshold0
{
    Mat hsvThreshold0Input = blurOutput;
    List hsvThreshold0Hue = [42.086330935251794, 87.57575757575756];
    List hsvThreshold0Saturation = [96.31294964028777, 255.0];
    List hsvThreshold0Value = [116.95143884892084, 248.56060606060603];

    hsvThreshold(hsvThreshold0Input, hsvThreshold0Hue, hsvThreshold0Saturation, hsvThreshold0Value, hsvThreshold0Output);
}

Step Mask0
{
    Mat maskInput = blurOutput;
    Mat maskMask = hsvThreshold0Output;

    mask(maskInput, maskMask, maskOutput);
}

Step HSV_Threshold1
{
    Mat hsvThreshold1Input = maskOutput;
    List hsvThreshold1Hue = [56.820361892304334, 167.87878787878788];
    List hsvThreshold1Saturation = [80.26079136690647, 255.0];
    List hsvThreshold1Value = [192.62589928057554, 255.0];

    hsvThreshold(hsvThreshold1Input, hsvThreshold1Hue, hsvThreshold1Saturation, hsvThreshold1Value, hsvThreshold1Output);
}

Step Find_Contours0
{
    Mat findContoursInput = hsvThreshold1Output;
    Boolean findContoursExternalOnly = false;

    findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);
}

Step Filter_Contours0
{
    ContoursReport filterContoursContours = findContoursOutput;
    Double filterContoursMinArea = 0.0;
    Double filterContoursMinPerimeter = 3.0;
    Double filterContoursMinWidth = 2.0;
    Double filterContoursMaxWidth = 11.0;
    Double filterContoursMinHeight = 2.0;
    Double filterContoursMaxHeight = 8.0;
    List filterContoursSolidity = [0, 100];
    Double filterContoursMaxVertices = 1000000.0;
    Double filterContoursMinVertices = 0.0;
    Double filterContoursMinRatio = 0.0;
    Double filterContoursMaxRatio = 1000.0;

    filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);
}




