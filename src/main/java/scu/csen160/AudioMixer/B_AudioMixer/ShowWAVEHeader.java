package scu.csen160.AudioMixer.B_AudioMixer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;

@SuppressWarnings("serial")
public class ShowWAVEHeader implements Serializable {
	public static final int HEADER_SIZE    = 44;
	private static final int LITTLE_ENDIAN =  0;
	private static final int BIG_ENDIAN    =  1;
	
	private File audioFile;

	public void setAudioFile(File f) {
		audioFile = f;
	}
	
	// prints out the header of an uncompressed WAVE
	public void printHeader(File s) {
		FileInputStream fileIn = null;
		BufferedInputStream bufferedIn = null;

		try {
			// create a file reader
			fileIn        = new FileInputStream(s);
			bufferedIn    = new BufferedInputStream(fileIn);
			int data      = 0;
			int bytesRead = 0;

			// read until header is read fully and end-of-file is not reached
			while (bytesRead < HEADER_SIZE && (data = bufferedIn.read()) != -1) {
				String binaryData = Integer.toString(data, 2);
				System.out.println(String.format("Byte at index %d: %08d", bytesRead, Integer.valueOf(binaryData)));
				++bytesRead;
			}
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
	}
	
	private void printSummary(File s) {
		FileInputStream fileIn         = null;
		BufferedInputStream bufferedIn = null;
		byte[] array2                  = new byte[2];
		byte[] array4                  = new byte[4];

		try {
			// create a file reader
			fileIn     = new FileInputStream(s);
			bufferedIn = new BufferedInputStream(fileIn);

			// skip the first 20 bytes
			bufferedIn.skip(20);

			System.out.println("\nPrint Summary:");
			System.out.println("==============================================");
			// read audio format
			bufferedIn.read(array2);
			System.out.println("Audio Format                     = " + getDecimalValueOfByteArray(array2, LITTLE_ENDIAN));
			bufferedIn.read(array2); // read number of channels
			System.out.println("Number of Channels               = " + getDecimalValueOfByteArray(array2, LITTLE_ENDIAN));
			bufferedIn.read(array4); // read sample rate
			System.out.println("Sample Rate                      = " + getDecimalValueOfByteArray(array4, LITTLE_ENDIAN));
			bufferedIn.read(array4); // read byte rate
			System.out.println("Byte Rate                        = " + getDecimalValueOfByteArray(array4, LITTLE_ENDIAN));
			bufferedIn.read(array2); // read Bytes in each sample for all channel
			System.out.println("Bytes per sample                 = " + getDecimalValueOfByteArray(array2, LITTLE_ENDIAN));
			bufferedIn.read(array2); // read bits per sample
			System.out.println("Bits per sample for all channels = " + getDecimalValueOfByteArray(array2, LITTLE_ENDIAN));
			bufferedIn.skip(4); // skip data chunk id, don't print it out

			// read audio data size
			bufferedIn.read(array4);
			System.out.println("Total bytes of audio data        = " + getDecimalValueOfByteArray(array4, LITTLE_ENDIAN)+"\n");
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

	public static void main(String[] args) {
		try {		
			// Create AudioMixer
			ShowWAVEHeader audioMixer = new ShowWAVEHeader();
			
			// Open blues.wav
			File musicFile = new File("audio/blues.wav");
			audioMixer.printHeader(musicFile);
			audioMixer.printSummary(musicFile);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}