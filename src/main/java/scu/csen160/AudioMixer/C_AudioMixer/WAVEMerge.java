package scu.csen160.AudioMixer.C_AudioMixer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

@SuppressWarnings("serial")
public class WAVEMerge implements Serializable {
	public static final int HEADER_SIZE    = 44;
	private static final int LITTLE_ENDIAN =  0;
	private static final int BIG_ENDIAN    =  1;
	
	private File audioFile;

	public void setAudioFile(File f) {
		audioFile = f;
	}

	// returns the integer value of a byte array
	private int getDecimalValueOfByteArray(byte[] array, int order) {
		if (order == BIG_ENDIAN) {
			BigInteger number = new BigInteger(array);
			
			return number.intValue();
		} else {
			// order is LITTLE_ENDIAN
			int length = array.length;

			// create a new array called array1
			byte[] array1 = new byte[length];

			// reverse the contents of array and put in array1
			int up = 0, down = length - 1;
			while (up <= down) {
				array1[up] = array[down];
				array1[down] = array[up];
				up++;
				down--;
			}
			
			BigInteger number = new BigInteger(array1);
			
			return number.intValue();
		}
	}
	
	public int getDataChunkSize(File filename) {
		int size = 0;
		
		FileInputStream fileIn         = null;
		BufferedInputStream bufferedIn = null;
		
		try {
			// create two file readers
			fileIn     = new FileInputStream(filename);
			bufferedIn = new BufferedInputStream(fileIn);

			// skip 40 bytes in header of source2
			bufferedIn.skip(HEADER_SIZE - 4);

			// get audio data size of source1
			byte[] chunkSizeArray = new byte[4];
			bufferedIn.read(chunkSizeArray);
			size = getDecimalValueOfByteArray(chunkSizeArray, LITTLE_ENDIAN);
			
		} catch (FileNotFoundException e) {
			System.err.println("File could not be found" + e.getMessage());
		} catch (IOException e) {
			System.err.println("IOException" + e.getMessage());
		} finally {
			try {
				bufferedIn.close();
			} catch (IOException e) {
				System.err.println("IOException" + e.getMessage());
			}
		}

		return size;
	}

	public void merge(File source1, File source2, File destination) {
		int size1;
		int size2;
		
		int data1 = 0;
		int data2 = 0;
		int data3 = 0;

		FileInputStream fileIn1          = null;
		FileInputStream fileIn2          = null;

		BufferedInputStream bufferedIn1  = null;
		BufferedInputStream bufferedIn2  = null;
		
		FileOutputStream fileOut         = null;
		BufferedOutputStream bufferedOut = null;
		
		try {
			// create two file readers and a file writer
			fileIn1 = new FileInputStream(source1);
			bufferedIn1 = new BufferedInputStream(fileIn1);
			
			fileIn2 = new FileInputStream(source2);
			bufferedIn2 = new BufferedInputStream(fileIn2);
			
			fileOut = new FileOutputStream(destination);
			bufferedOut = new BufferedOutputStream(fileOut);

			int bytesRead = 0, HEADER_SIZE = 44;
			byte[] buffer = new byte[HEADER_SIZE];

			// get audio data size of source files
			size1 = getDataChunkSize(source1);
			size2 = getDataChunkSize(source2);
			int minSize = Math.min(size1, size2);

			// copy header of larger source file into destination
			if (size1 >= size2) {
				bufferedIn1.read(buffer);
				bufferedOut.write(buffer);
				bufferedIn2.skip(HEADER_SIZE);
			} else {
				bufferedIn2.read(buffer);
				bufferedOut.write(buffer);
				bufferedIn1.skip(HEADER_SIZE);
			}

			// merge the audio samples in two files
			while (bytesRead < minSize) {
				data1 = bufferedIn1.read();
				data2 = bufferedIn2.read();
				data3 = data1 + data2 - 128;
				
				if (data3 > 255)
					data3 = 255;
				else if (data3 < 0)
					data3 = 0;

				// write the merged data to destination file
				bufferedOut.write(data3);
				bytesRead++;
			}

			// If one of the input files is bigger -----------------------------------
			
			// copy any remaining bytes in source1 to destination file
			while (bytesRead < size1) {
				data1 = bufferedIn1.read();
				bufferedOut.write(data1);
				bytesRead++;
			}

			// copy any remaining bytes in source2 to destination file
			while (bytesRead < size2) {
				data2 = bufferedIn2.read();
				bufferedOut.write(data2);
				bytesRead++;
			}
			
		} catch (FileNotFoundException e) {
			System.err.println("File could not be found" + e.getMessage());
		} catch (IOException e) {
			System.err.println("IOException" + e.getMessage());
		} finally {
			try {
				bufferedIn1.close();
				bufferedIn2.close();
				
				// flush bufferedOut
				bufferedOut.flush();
				bufferedOut.close();
			} catch (IOException e) {
				System.err.println("IOException" + e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		Scanner myScan = null;
		
		try {
			myScan = new Scanner(System.in);
			
			// Create AudioMixer
			WAVEMerge audioMixer = new WAVEMerge();
			
			String audio1="audio/blues.wav";
			String audio2="audio/groovy.wav";
			String audio3="audio/groblu.wav";
			
			System.out.println("Merge "+audio1+" and "+audio2+" into "+audio3);
			
			// Open blues.wav
			File musicFile = new File(audio1);

			// Open groovy.wav
			File musicFile1 = new File(audio2);
			
			// Open destination file
			File mergedMusicFile = new File(audio3);
			
			// Merge both files
			audioMixer.merge(musicFile, musicFile1, mergedMusicFile);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			myScan.close();	
		}
	}
}