package bookadvisor;

import java.util.Scanner;

public class UserInputHandler {
	public String GetUserInput(String prompt){
		// intent: prompt a user to input and return their input as a string
		Scanner inputScanner = new Scanner(System.in);
		
		System.out.println(prompt);
		String input = inputScanner.nextLine();
		
		return input;
	}
	

}
