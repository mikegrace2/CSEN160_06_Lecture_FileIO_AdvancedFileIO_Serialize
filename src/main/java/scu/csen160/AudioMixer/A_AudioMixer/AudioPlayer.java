package scu.csen160.AudioMixer.A_AudioMixer;

import java.io.File;
import java.io.Serializable;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

@SuppressWarnings("serial")
public class AudioPlayer implements Serializable, LineListener {
	public static final int HEADER_SIZE = 44;
	
	private File audioFile;

	public void setAudioFile(File f) {
		audioFile = f;
	}	

	@Override
	public void update(LineEvent event) {
		if (event.getType() == LineEvent.Type.STOP) {
			System.exit(0);
		}
	}
	
	public void play() throws Exception {
		AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile);
		Clip clip = (Clip) AudioSystem.getClip();

		// line listener causes program to exit after play is completed.
		clip.addLineListener(this);
		

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
			AudioPlayer readyPlayerOne = new AudioPlayer();
			
			// Play a wave file
			System.out.print("Enter name of file to play: ");
			String input = "audio/" + myScan.next();
			System.out.println("File name is: " + input);
			readyPlayerOne.setAudioFile(new File(input));
			readyPlayerOne.play();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			myScan.close();
		}
	}
}