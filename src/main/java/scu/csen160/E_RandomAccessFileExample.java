package scu.csen160;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

// replace:                                                                          etc. (delete rest)
// Audio files can be stored in a variety of formats such as wav, wma, mp3, aiff, au and many others.
// 0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
// 0         10        20        30        40        50        60        70        80        90
public class E_RandomAccessFileExample {
	public static void main(String[] args) {
		try {			
			RandomAccessFile rafile = new RandomAccessFile(new File("text/info.txt"), "rw");
			
			rafile.seek(82);
			int ch = 'e';
			rafile.write(ch);
			ch = 't';
			rafile.write(ch);
			ch = 'c';
			rafile.write(ch);
			ch = '.';

			rafile.setLength(86);
		} catch (FileNotFoundException e) {
			System.err.println("File could not be found " + e.getMessage());
		} catch (IOException e) {
			System.err.println("IOException " + e.getMessage());
		}
	}
}