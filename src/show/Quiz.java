package show;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import sound.BGM;

public class Quiz implements ActionListener {
	private Timer tim;
	private MainPanel p;
	private BGM bgm, bgm2;
	private AudioClip start;

	private int correct = 0;
	private int miss = 0;
	private int quiz;
	private int ans;
	private int count = 1;
	private String x = "";
	private String y = "";
	private String answer = "";
	private String symbol = "";
	private boolean game = false;
	private boolean end = false;
	private boolean first = true;

	public Quiz(MainPanel panel) {
		bgm = new BGM("sound/time.wav");
		bgm2 = new BGM("sound/endgame.wav");
		start = Applet.newAudioClip(getClass().getResource("sound/game.wav"));

		this.p = panel;
		tim = new Timer(5000, this);
	}

	public void draw(Graphics g) {
		if (game) {
			g.setColor(Color.WHITE);
			Font font = new Font("Dialog", Font.PLAIN, 20);
			g.setFont(font);
			g.drawString(count + "問目   " + "正解数：" + correct, 70, 100);
			Font font1 = new Font("Dialog", Font.PLAIN, 50);
			g.setFont(font1);
			g.drawString(x, 65, 200);
			g.drawString(symbol, 125, 200);
			g.drawString(y, 180, 200);
			g.drawString("＝", 240, 200);
		} else if (end) {
			Font font = new Font("Dialog", Font.PLAIN, 30);
			g.setFont(font);
			g.drawString("結果", 80, 80);
			Font font2 = new Font("Dialog", Font.PLAIN, 20);
			g.setFont(font2);
			g.drawString("正解数　：" + correct, 100, 130);
			g.drawString("不正解数：" + miss, 100, 160);
			g.drawString("Push Space Key", 150, 300);
		}
		p.repaint();
	}

	/* 5秒に1回 */
	public void actionPerformed(ActionEvent e) {
		count++;
		// 終了判定
		if (count > 20 || !game) {
			game = false;
			tim.stop();
			end();
		}

		// 問題生成
		makeQuiz();
	}

	/* 終了 */
	public void end() {
		if (first) {
			end = true;
			start.play();
			bgm.stopMusic();
			bgm2.loopPlay();
			x = "";
			y = "";
			symbol = "";
			answer = "";
			first = false;
		}
	}

	/* 音楽停止 */
	public void stopMusic() {
		bgm2.stopMusic();
	}

	/* 実行状態 */
	public boolean state() {
		return game;
	}

	public boolean gameEnd() {
		return first;
	}

	/* 状態設定 */
	public void setEnd(boolean state) {
		end = state;
		game = state;
	}

	public void setState(boolean state) {
		game = state;
	}

	public void gameContinue(boolean yes) {
		first = yes;
	}

	/* 問題設定 */
	public void setQuiz(int number) {
		this.quiz = number;
	}

	/* 開始 */
	public void start() {
		game = true;
		bgm.loopPlay();
		tim.start();
	}

	/* 停止 */
	public void stop() {
		tim.stop();
	}

	/* 再開 */
	public void restart() {
		tim.restart();
	}

	/* 入力値を設定する */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/* 入力値を取得 */
	public String getAnswer() {
		return answer;
	}

	/* 問題の解答 */
	public int getAns() {
		return ans;
	}

	/* 正解数の設定 */
	public void setCorrect(boolean correct) {
		if (game) {
			if (correct)
				this.correct++;
		}
	}

	/* 正解数取得 */
	public int getCorrect() {
		return correct;
	}

	/* 不正解数の設定 */
	public void setMiss(boolean miss) {
		if (game) {
			if (miss) {
				this.miss++;
			}
		}
	}

	/* 不正解数の取得 */
	public int getMiss() {
		return miss;
	}

	/* 問題数の取得 */
	public int getCount() {
		return count;
	}

	/* 問題数の設定 */
	public void setCount(int count) {
		if (game) {
			this.count += count;
		}
	}

	/* 問題の作成 */
	public void makeQuiz() {
		if (game) {
			answer = "";
			switch (quiz) {
			case 0:
				ans = addition(99, 0);
				break;
			case 1:
				ans = subtraction(99, 0);
				break;
			case 2:
				ans = multip(10, 0);
				break;
			}
		}
	}

	/* 足し算の問題生成 */
	private int addition(int max, int min) {
		// 値作成
		int x = rand(max, min, 2);
		int y = rand(max, min, 2);

		// 式の表示
		this.x = Integer.toString(x);
		this.y = Integer.toString(y);
		this.symbol = "＋";

		return x + y;
	}

	/* 引き算の問題生成 */
	private int subtraction(int max, int min) {
		// 値作成
		int x = rand(max, min, 2);
		int y = rand(max, min, 2);

		// 式の表示

		this.x = Integer.toString(x);
		this.y = Integer.toString(y);
		this.symbol = "－";

		return x - y;
	}

	/* 掛け算の問題生成 */
	private int multip(int max, int min) {
		// 値作成
		int x = rand(max, min, 2);
		int y = rand(max, min, 2);

		// 式の表示
		this.x = Integer.toString(x);
		this.y = Integer.toString(y);
		this.symbol = "×";

		return x * y;
	}

	/* 乱数生成 */
	private int rand(int max, int min, int num) {
		int range = max - min;
		return (int) (Math.random() * (range + 1) + min);
	}

}
