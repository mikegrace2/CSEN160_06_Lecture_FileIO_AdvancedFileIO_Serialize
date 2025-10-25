package scu.csen160;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class A_FileReaderExample {
	public static void main(String[] args) {
		FileReader reader = null;
		int ch;
		
		try {
			reader = new FileReader(new File("info.txt"));
			
			// read all characters in file until -1 is received
			while ((ch = reader.read()) != -1) {
				// Cast ch to type char and print it out
				System.out.print((char) ch);
			}
			
			reader.close(); // Always closing
		
		} catch (FileNotFoundException e) {
			System.err.print("File could not be found" + e.getMessage());
		} catch (IOException e) {
			System.err.println("IOException" + e.getMessage());
		}
	}
}