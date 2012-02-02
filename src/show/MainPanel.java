package show;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import sound.*;

public class MainPanel extends MakePanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private Image img[];
	private Opning op;
	private CountDown cd;

	/* Game */
	private String[] games = { "Addition", "Subtraction", "Multiplication" };

	/* Game Select */
	// select(add:0, sub:1, mul:2)
	private final int MOVE = 128;
	private int x = 0;
	private int selecting = 1;

	/* Music */
	private static final String[] wav = { "start", "select", "game", "inp",
			"del", "yes", "no" };
	private SE se = new SE();
	private BGM[] bgm = new BGM[2];

	/* Scene Check */
	private boolean start = true;
	private boolean select = false;
	private boolean explain = false;
	private boolean wait = false;
	private boolean ends = false;

	public MainPanel(int width, int height) {
		super(width, height);

		// KEY
		this.setFocusable(true);
		addKeyListener(this);

		op = new Opning(this);
		cd = new CountDown(this);

		bgm[0] = new BGM("sound/selecting.wav");
		bgm[1] = new BGM("sound/explain.wav");

		setImage();
		loadWave();

	}

	/* CHECK SELECT */
	public void selecting() {
		if (selecting <= 0) {
			if (selecting == 0) {
				se.play("select");
			}
			x = -MOVE;
			selecting = 0;
		}
		if (selecting == 1) {
			se.play("select");
			x = 0;
		}
		if (selecting >= 2) {
			if (selecting == 2) {
				se.play("select");
			}
			x = +MOVE;
			selecting = 2;
		}
	}

	/* Draw */
	public void paint(Graphics g) {
		// Title
		if (start) {
			g.drawImage(img[0], 0, 0, this);
			op.space(g);
		}
		// Game Select
		if (select) {
			g.drawImage(img[1], 0, 0, this);
			color(0, 0, 0, g);
			g.drawString("ゲームを選択してください", 150, 90);
			color(18, 111, 160, g);
			font(20, g);
			g.drawString("▼", 220 + x, 160);
		}
		// Explain
		if (explain) {
			g.drawImage(img[2], 0, 0, this);
			color(240, 240, 240, g);
			font(17, g);
			g.drawString(games[selecting], 30, 85);
			color(150, 150, 150, g);
			font(12, g);
			explain(selecting, g);
		}
		// Wait
		if (wait) {
			g.drawImage(img[3], 0, 0, this);
			cd.draw(g);
		}
		// GAME
		if (cd.q.state()) {
			g.drawString(cd.q.getAnswer(), 290, 200);

		}
		if (ends) {
			g.drawImage(img[4], 0, 0, this);
		}
	}

	/* Game Explain */
	public void explain(int number, Graphics g) {
		String[] game = new String[3];
		game[0] = "足し算を行います";
		game[1] = "引き算を行います";
		game[2] = "掛け算を行います";

		String[] exp = new String[4];
		exp[0] = game[number];
		exp[1] = "制限時間は一問につき5秒です";
		exp[2] = "20問解答すると終了します";
		exp[3] = "では始めてください";

		int y = 0;
		for (String v : exp) {
			g.drawString(v, 100, 160 + y);
			y += 20;
		}
		color(200, 200, 200, g);
		font(15, g);
		g.drawString("Push Space Key", 170, 310);
	}

	/* Font Change */
	public void font(int size, Graphics g) {
		Font font = new Font("Dialog", Font.PLAIN, size);
		g.setFont(font);
	}

	/* Color Change */
	public void color(int R, int G, int B, Graphics g) {
		Color color = new Color(R, G, B);
		g.setColor(color);
	}

	/* KEY event */
	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (start && e.getKeyCode() == KeyEvent.VK_SPACE) {
			select = true;
			start = false;
			op.bgm.stopMusic();
			se.play("start");
			bgm[0].loopPlay();
		} else if (select) {
			if (key == KeyEvent.VK_RIGHT) {
				selecting++;
				selecting();
			} else if (key == KeyEvent.VK_LEFT) {
				selecting--;
				selecting();
			} else if (key == KeyEvent.VK_SPACE) {
				explain = true;
				select = false;
				bgm[0].stopMusic();
				se.play("start");
				bgm[1].loopPlay();
			}
		} else if (explain) {
			if (key == KeyEvent.VK_SPACE) {
				wait = true;
				explain = false;
				bgm[1].stopMusic();
				se.play("start");
				cd.q.setQuiz(selecting);
				cd.start();

			}
		} else if (cd.check() && cd.q.state()) {
			if (key == KeyEvent.VK_0 || key == KeyEvent.VK_1
					|| key == KeyEvent.VK_2 || key == KeyEvent.VK_3
					|| key == KeyEvent.VK_4 || key == KeyEvent.VK_5
					|| key == KeyEvent.VK_6 || key == KeyEvent.VK_7
					|| key == KeyEvent.VK_8 || key == KeyEvent.VK_9
					|| key == KeyEvent.VK_MINUS) {
				se.play("inp");
				String inp;
				if (key == KeyEvent.VK_MINUS) {
					inp = "-";
				} else {

					inp = KeyEvent.getKeyText(key);
				}
				String ans = cd.q.getAnswer();
				ans = ans + inp;
				cd.q.setAnswer(ans);
			}
			if (key == KeyEvent.VK_BACK_SPACE) {
				String ans = cd.q.getAnswer();
				int l = ans.length();
				if (l > 0) {
					ans = ans.substring(0, l - 1);
					cd.q.setAnswer(ans);
					se.play("del");
				}
			}
			if (key == KeyEvent.VK_ENTER) {
				if (cd.q.getAns() == Integer.parseInt(cd.q.getAnswer())) {
					cd.q.setCorrect(true);
					if (cd.q.getCount() == 20) {
						cd.q.setAnswer("");
						cd.q.end();
					}
					if (cd.q.getCount() < 20) {
						if (cd.q.getCount() == 19) {
							cd.q.setState(false);
						}
						cd.q.setCount(1);
						cd.q.makeQuiz();
						cd.q.stop();
						cd.q.restart();

					}
					se.play("yes");
				} else {
					cd.q.setMiss(true);
					cd.q.setAnswer("");
					se.play("no");
				}
			}
		} else if (!cd.q.gameEnd() && !cd.q.state()) {
			cd.q.gameContinue(true);
			cd.q.setEnd(false);
			cd.q.stopMusic();
			if (key == KeyEvent.VK_SPACE) {
				se.play("start");
				ends = true;
				op.bgm.loopPlay();
			}
		}
		repaint();
	}

	public void keyReleased(KeyEvent e) {
	}

	/* Road Images */
	private void setImage() {
		img = new Image[5];
		img[0] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("img/op.jpg"));
		img[1] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("img/choice.jpg"));
		img[2] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("img/explain.jpg"));
		img[3] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("img/normal.jpg"));
		img[4] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("img/end.jpg"));
		MediaTracker mt = new MediaTracker(this);

		mt.addImage(img[0], 0);
		mt.addImage(img[1], 1);
		mt.addImage(img[2], 1);
		mt.addImage(img[3], 1);
		mt.addImage(img[4], 1);

		try {
			mt.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/* Road Sounds */
	private void loadWave() {
		for (int i = 0; i < wav.length; i++) {
			se.load(wav[i], "sound/" + wav[i] + ".wav");
		}
	}
}
