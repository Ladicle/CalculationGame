package show;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class CountDown implements ActionListener {
	protected Quiz q;
	private AudioClip counting;
	private Timer tim;
	private MainPanel p;
	private Image img;
	private AudioClip start;
	private Color color;

	private boolean flag;
	private boolean check = false;

	private String number = " ";
	private int count = 6;

	public CountDown(MainPanel panel) {
		q = new Quiz(panel);
		tim = new Timer(1000, this);

		start = Applet.newAudioClip(getClass().getResource("MUSIC/game.wav"));
		counting = Applet.newAudioClip(getClass()
				.getResource("MUSIC/count.wav"));
		img = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("PIC/quiz.jpg"));

		MediaTracker mt = new MediaTracker(panel);
		mt.addImage(img, 0);
		try {
			mt.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.p = panel;
	}

	public boolean check() {
		return check;
	}

	public void start() {
		tim.start();
	}

	public void draw(Graphics g) {
		g.setColor(color);
		Font font = new Font("Dialog", Font.ROMAN_BASELINE, 250);
		g.setFont(font);
		g.drawString(number, 150, 270);
		if (check) {
			g.drawImage(img, 0, 0, p);
			q.draw(g);
		}
		p.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		if (count < 2) {
			start.play();
			try {
				Thread.sleep(800);
			} catch (InterruptedException er) {
				er.printStackTrace();
			}
			check = true;
			q.start();
			tim.stop();
			q.makeQuiz();

		} else {
			count--;
			number = Integer.toString(count);
			counting.play();
		}

		if (flag) {
			color = Color.WHITE;
			flag = false;
		} else {
			color = Color.LIGHT_GRAY;
			flag = true;
		}
	}
}
