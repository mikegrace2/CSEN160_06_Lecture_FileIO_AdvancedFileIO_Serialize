package scu.csen160;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class C_BufferedReaderWriterExample {
	public static void main(String[] args) {
		FileReader reader = null;
		FileWriter writer = null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		int ch;

		try {
			// create a file reader and writer
			reader = new FileReader(new File("info.txt"));
			writer = new FileWriter(new File("CopyOfInfo2.txt"));
			
			// create a buffered reader and buffered writer
			bufferedReader = new BufferedReader(reader);
			bufferedWriter = new BufferedWriter(writer);

			long start = System.currentTimeMillis();
			// read all characters in file until end-of-file
			while ((ch = bufferedReader.read()) != -1) {
				bufferedWriter.write(ch);
			}
			long end =System.currentTimeMillis();
			System.out.println("Time copy FileReaderWriter: "+(end-start)+" ms");			
			
		} catch (FileNotFoundException e) {
			System.err.println("File could not be found " + e.getMessage());
		} catch (IOException e) {
			System.err.println("IOException " + e.getMessage());
		} finally {
			try {
				// close buffered reader
				bufferedReader.close();
				// flush and close buffered writer
				bufferedWriter.flush();
				bufferedWriter.close();
			} catch (IOException e) {
				System.err.println("IOException " + e.getMessage());
			}
		}
	}
}