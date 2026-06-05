package prodjogo4;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Janela4 {

	public static void main(String[] args) {

		JFrame janela = new JFrame("Construtor de níveis");
		janela.setSize(1920, 1080);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setLayout(null);

		LevelConstrutor construtor = new LevelConstrutor(janela);
		construtor.setBounds(0, 0, 1920, 1080);

		janela.add(construtor);
		construtor.addMouseListener(construtor);
		construtor.addMouseMotionListener(construtor);

		janela.setVisible(true); // Apenas uma vez, aqui no final!
		janela.requestFocus();
	}
}
