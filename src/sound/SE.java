package sound;

import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SE implements LineListener {

	private final int MAX = 256;
	private HashMap<String, Clip> soundMap;
	private int count = 0;

	/* HashMapの作成 */
	public SE() {
		soundMap = new HashMap<String, Clip>(MAX);
	}

	/* WAVファイルロード */
	public void load(String name, String filename) {
		if (count == MAX) {
			System.out.println("Error! OVER CAPACITY...");
			return;
		}

		try {
			// ファイル情報
			AudioInputStream stream = AudioSystem
					.getAudioInputStream(getClass().getResourceAsStream(
							filename));

			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);

			// クリップ作成・登録
			Clip cp = (Clip) AudioSystem.getLine(info);
			cp.addLineListener(this);
			cp.open(stream);
			soundMap.put(name, cp);
			stream.close();

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/* SEの再生 */
	public void play(String name) {
		Clip cp = soundMap.get(name);
		if (cp != null) {
			cp.start();
		}
	}

	/* 巻き戻し */
	public void update(LineEvent event) {
		if (event.getType() == LineEvent.Type.STOP) {
			Clip cp = (Clip) event.getSource();
			cp.stop();
			cp.setFramePosition(0);
		}
	}

}
