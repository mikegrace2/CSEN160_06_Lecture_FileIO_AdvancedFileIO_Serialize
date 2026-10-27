package scu.csen160.FileIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class D_ScannerExample {
	public static void main(String[] args) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("info_short.txt"));

			while (scanner.hasNext()) {
				System.out.println(scanner.next());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}
}