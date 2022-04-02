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
	Mat rgbThresholdOutput;
	Mat maskOutput;
	Mat hsvThresholdOutput;
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
    Double blurRadius = 0.9909909909909913;

    blur(blurInput, blurType, blurRadius, blurOutput);
}

Step RGB_Threshold0
{
    Mat rgbThresholdInput = blurOutput;
    List rgbThresholdRed = [80.26079136690647, 231.38888888888889];
    List rgbThresholdGreen = [6.879496402877698, 255.0];
    List rgbThresholdBlue = [52.74280575539568, 190.6060606060606];

    rgbThreshold(rgbThresholdInput, rgbThresholdRed, rgbThresholdGreen, rgbThresholdBlue, rgbThresholdOutput);
}

Step Mask0
{
    Mat maskInput = blurOutput;
    Mat maskMask = rgbThresholdOutput;

    mask(maskInput, maskMask, maskOutput);
}

Step HSV_Threshold0
{
    Mat hsvThresholdInput = maskOutput;
    List hsvThresholdHue = [22.66187050359712, 87.57575757575756];
    List hsvThresholdSaturation = [50.44964028776978, 255.0];
    List hsvThresholdValue = [192.62589928057554, 255.0];

    hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);
}

Step Find_Contours0
{
    Mat findContoursInput = hsvThresholdOutput;
    Boolean findContoursExternalOnly = false;

    findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);
}

Step Filter_Contours0
{
    ContoursReport filterContoursContours = findContoursOutput;
    Double filterContoursMinArea = 3.0;
    Double filterContoursMinPerimeter = 0.0;
    Double filterContoursMinWidth = 8.0;
    Double filterContoursMaxWidth = 90.0;
    Double filterContoursMinHeight = 0.0;
    Double filterContoursMaxHeight = 90.0;
    List filterContoursSolidity = [0, 100];
    Double filterContoursMaxVertices = 1000000.0;
    Double filterContoursMinVertices = 0.0;
    Double filterContoursMinRatio = 0.0;
    Double filterContoursMaxRatio = 1000.0;

    filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);
}




