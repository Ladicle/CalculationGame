package show;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

abstract class MakePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private boolean state;
	private Dimension d;
	protected Image img;
	protected Graphics buff;

	/* パネルのサイズ設定 */
	public MakePanel(int width, int height) {
		d = new Dimension(width, height);
		setSize(d);
		state = true;
	}

	/* パネルの状態を返す */
	public boolean getState() {
		return state;
	}

	// パネルを終了
	public void sceneEnd() {
		state = false;
	}
}
