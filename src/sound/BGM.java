package sound;

import java.applet.Applet;
import java.applet.AudioClip;

public class BGM {
	private AudioClip clip;

	public BGM(String file) {
		clip = Applet.newAudioClip(getClass().getResource(file));
	}

	public void starMusic() {
		clip.play();
	}

	public void stopMusic() {
		clip.stop();
	}

	public void loopPlay() {
		clip.loop();
	}

}
