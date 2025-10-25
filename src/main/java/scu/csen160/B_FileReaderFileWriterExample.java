package scu.csen160;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class B_FileReaderFileWriterExample {
	public static void main(String[] args) {
		FileReader reader = null;
		FileWriter writer = null;
		
		int ch;
		
		try {
			reader = new FileReader(new File("info.txt"));
			writer = new FileWriter(new File("copyOfInfo.txt"));

			long start = System.currentTimeMillis();
			
			while ((ch = reader.read()) != -1) { // read until -1 is received
				// Cast ch to type char and print it out
				writer.append((char)ch);
				//System.out.print((char) ch);
			}
			long end = System.currentTimeMillis();
			System.out.println("Time copy FileReaderWriter: "+(end-start)+" ms");
			
			reader.close(); // Always closing
			writer.close();
		
		} catch (FileNotFoundException e) {
			System.err.println("File could not be found" + e.getMessage());
		} catch (IOException e) {
			System.err.println("IOException" + e.getMessage());
		}
	}
}