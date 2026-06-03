package prodjogo4;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Janela4 {

	public static void main(String[] args) {
		
		JFrame janela = new JFrame("Construtor de níveis");
		janela.setVisible(true);
		janela.setSize(1920, 1080);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setLayout(null);
		
		// Canvas do level builder
		LevelConstrutor construtor = new LevelConstrutor(janela);
		construtor.setBounds(0, 0, 1920, 1080);
		
		janela.add(construtor);
		janela.addMouseListener(construtor);
		janela.addMouseMotionListener(construtor);
		janela.addKeyListener(construtor);
		
		construtor.repaint();
	}
	
}
