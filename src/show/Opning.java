package show;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import sound.BGM;

public class Opning implements ActionListener {
	private boolean flag = true;
	private Timer tim;
	private Color color;
	protected BGM bgm;
	private MainPanel p;

	public Opning(MainPanel panel) {
		bgm = new BGM("sound/opning.wav");
		bgm.loopPlay();
		tim = new Timer(900, this);
		tim.start();
		this.p = panel;
	}

	void space(Graphics g) {
		g.setColor(color);
		Font font = new Font("Dialog", Font.PLAIN, 17);
		g.setFont(font);
		g.drawString("Push Space Key …", 160, 320);
		p.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		if (flag) {
			color = Color.WHITE;
			flag = false;
		} else {
			color = Color.GRAY;
			flag = true;
		}
	}
}
