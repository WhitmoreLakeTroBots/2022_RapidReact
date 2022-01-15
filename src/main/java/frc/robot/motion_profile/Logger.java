package frc.robot.motion_profile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import frc.robot.Constants.Profiler_Constants_DriveTrain;

public class Logger {
	// Logger to write data to the hard drive
	static ArrayList<String> entries = new ArrayList<String>();
	StringBuilder sb = new StringBuilder();
	String name;
	BufferedWriter writer = null;
	static String timeLog = new SimpleDateFormat("_MMddyyyy_HHmmss").format(Calendar.getInstance().getTime());
	static Logger log = new Logger(Profiler_Constants_DriveTrain.profileLogName);

	public Logger(String Name){
		name = Name;
	}

	public void makeEntry(String line) {
		String time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
		entries.add(time+ "\t" +  line);
	}

	public void write() {
		for (String s : entries) {
			sb.append(s);
			sb.append("\n");
		}
		try {
			File logFile = new File(name + timeLog + Profiler_Constants_DriveTrain.profileLogFileExtension);
			writer = new BufferedWriter(new FileWriter(logFile));
			writer.write(sb.toString());
			System.out.println(logFile.getCanonicalPath());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			e.printStackTrace();
			}
		}
	}
}