package prodjogo5;

import javax.swing.JFrame;

import prodjogo5.GUI.Lobby;

public class Janela5 {

	private boolean temCache;
	private String cache[];

	private Jogo jogo;
	private JFrame janela;
	private Lobby lobby;

	public void start() {

		temCache = false;

		janela = new JFrame("Prodjogo5");
		janela.setVisible(true);
		janela.setSize(1000, 700);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setLocationRelativeTo(null);
		janela.setLayout(null);


		lobby = new Lobby(janela, this);
		lobby.setBounds(0, 0, 1000, 700);

		janela.add(lobby);


	}

	public void carregar () {

		jogo = new Jogo(janela);
		jogo.setBounds(0, 0, 1920, 1080);

		if (temCache) {

			inputaCache(Integer.parseInt(cache[0]),
					Integer.parseInt(cache[1]),
					Integer.parseInt(cache[2]),
					Integer.parseInt(cache[3]));

		} else {

			inputaCache(50, 650, 1, 0);

		}
	}

	public void setCache(boolean tCache, String[] c) {
		this.temCache = tCache;
		this.cache = c;
	}
	public void inputaCache(int x, int y, int dir, int codFase) {
		jogo.setX(x);
		jogo.setY(y);
		jogo.setDirecao(dir);
		jogo.setCodFase(codFase);

		jogo.carregarCache();

		janela.remove(lobby);
		janela.add(jogo);

		janela.setSize(1920, 1080);
		janela.setLocationRelativeTo(null);

		janela.revalidate();
		janela.repaint();

		jogo.requestFocus();
	}

}
