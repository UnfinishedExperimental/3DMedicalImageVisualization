import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Question1 {

	public static void main(String[] args) {
		System.out.println("Please enter numbers");
		Scanner scanner = new Scanner(System.in);
		String inputData = scanner.nextLine();
		String[] splitedData = inputData.split(" ");
		String output = "";
		System.out.println(Arrays.toString(splitedData));
		int diff = 0;
		int prev = 0;
		if(splitedData.length >=0 ) {
			output = splitedData[0] + " ";
			prev = Integer.parseInt(splitedData[0]);
		} else {
			System.out.println("Input data is empty");
		}
		
		for( int i = 1; i <= splitedData.length - 1; i++) {
			int j = i+1;
			 prev = Integer.parseInt(splitedData[i]) - prev;
			 output += prev  + " ";
			 prev = Integer.parseInt(splitedData[i]);
		}
		
		System.out.println(output);
		String[] dataCreate = output.split(" ");
		String finalOut = dataCreate[0] + " ";
		for( int i = 1; i <= dataCreate.length - 1; i++) {
			System.out.println(Integer.parseInt(dataCreate[i]));
			if(Integer.parseInt(dataCreate[i]) > 127 || Integer.parseInt(dataCreate[i])  <-127) {
				finalOut += String.valueOf(-128) + " " + dataCreate[i] + " " ;
				
			} else {
				finalOut +=  dataCreate[i] + " " ;
			}
			//System.out.println(finalOut);
		}
		System.out.println(finalOut);
	}
	
}
