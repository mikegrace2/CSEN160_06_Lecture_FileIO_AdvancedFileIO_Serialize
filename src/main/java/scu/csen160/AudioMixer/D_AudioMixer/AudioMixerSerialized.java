package scu.csen160.AudioMixer.D_AudioMixer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

@SuppressWarnings("serial")
public class AudioMixerSerialized implements Serializable {
	public static final int HEADER_SIZE = 44;
	
	private File audioFile;

	public void setAudioFile(File f) {
		audioFile = f;
	}	

	public void play() throws Exception {
		AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile);
		Clip clip = (Clip) AudioSystem.getClip();

		// line listener causes program to exit after play is completed.
		clip.addLineListener(new LineListener() {
			public void update(LineEvent evt) {
				if (evt.getType() == LineEvent.Type.STOP) {
					System.exit(0);
				}
			}
		});

		// open the audio stream and start playing the clip
		clip.open(stream);
		clip.start();

		// program waits here while the music is played
		Thread.sleep(1800 * 1000);
	}

	public static void main(String[] args) {
		Scanner myScan = null;

		try {
			myScan = new Scanner(System.in);
			
			// Create player
			AudioMixerSerialized readyPlayerOne = new AudioMixerSerialized();
			
			// Play a wave file
			System.out.print("Enter name of file to play: ");
			String input = "audio/" + myScan.next();
			System.out.println("File name is: " + input);
			readyPlayerOne.setAudioFile(new File(input));
			
			// Serialize readyPlayerOne  --------------------------------------------------------------
			FileOutputStream fileOut = new FileOutputStream("serialized/readyPlayerOne.dat");
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(readyPlayerOne);
			objectOut.close();
			System.out.println("\nWritten AudioMixer object called player to file readyPlayerOne.dat");

			// Deserialize readyPlayerOne into readyPlayerTwo -----------------------------------------
			System.out.println("Create new AudioMixer object called readyPlayerTwo");
			AudioMixerSerialized readyPlayerTwo = new AudioMixerSerialized();
			System.out.println("Initialize readyPlayerTwo fields from file readyPlayerOne.dat");
			FileInputStream fileIn = new FileInputStream("serialized/readyPlayerOne.dat");
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			readyPlayerTwo = (AudioMixerSerialized) objectIn.readObject();
			objectIn.close();
			
			// Finally play the wave file - still playable!
			System.out.println("\nreadyPlayerOne.play(); Plays a song loaded with readyPlayerOne!");
			readyPlayerTwo.play(); // play the audio
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			myScan.close();
		}
	}
}