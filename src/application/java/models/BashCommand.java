package application.java.models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * This is the class for running commands in different linux commands;
 */
public class BashCommand {

	
	/** 
	 *  This method can call a certain bash cammands by passing in String as 
	 *  commands and return outputs as a list.
	 */
	
	public static List<String> executeCommand(String command){
		
		List<String> list = new ArrayList<>();
		
		try {
			
			// standard process for calling bash command
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			int exitStatus = process.waitFor();
			
			// add outputs from Bash each line into an entry in the List if no error opening the file.
			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					list.add(line);
					
				}
			} else {
				while ((stderr.readLine()) != null) {
				}
			}
			
			} catch (Exception e) {
			e.printStackTrace();
			}
	
		return list;
	}
	
	
	/**  method to check by running certain command if error is returned, return error code as
	 *   an int, 0 for correct, 1 for error. -1 for invalid.
	 * @return int
	 */
	
	public static int getErrorCode(String command) {
		//initialise errorCode to -1 for invalid.
		int errorCode = -1;
		
	try {
		
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

		Process process = pb.start();

		int exitStatus = process.waitFor();
		
		// checking error code from process.
		if (exitStatus == 0) {
			errorCode = 0;
		} else {
			errorCode = 1;
		}
		 
		} catch (Exception e) {
		e.printStackTrace();
		}
	
	return errorCode;
	}

	// getTopic method deleted due to redundent.
	
	
	/** this method gets file names from <directory specified> folder and trim .txt part.
	 * file names will be saved and returned as a List.
	 */
	 
	public static List<String> getFileNameFromDirectory(String dir){

		List<String> temp = new ArrayList<>();
		
		// checking if directory exists.
		if ((BashCommand.getErrorCode("test -d"+dir)) == 1) {
			// can rework error message to display on GUI.
			System.out.println("Directory doesn't exist");
			
			return temp;
		} else {
			
			
			// return filename and trimming the part after '.'
			temp = executeCommand("ls "+dir+" | cut  -d \".\" -f -1");
			
			//check if there are any file in directory.
			if (temp.isEmpty()) {
				//can rework error message to display on GUI.
				System.out.println("There are no files in the directory.");
				return temp;
			} else {
				return temp;
			}
			
		}
	}
	
}
