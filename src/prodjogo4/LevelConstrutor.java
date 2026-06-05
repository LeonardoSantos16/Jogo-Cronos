package prodjogo4;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import cenarios.*;
import controle.*;
import prodjogo4.Robo.Robo;

public class LevelConstrutor extends JPanel implements MouseListener, MouseMotionListener, Runnable {

	private Font minhaFonte = new Font("Consolas", Font.BOLD, 30);
	private ControleInvoker controle;
	ArrayList<BufferedImage> imagensTiles;
	BufferedImage tileSelecionado;
	int indiceSelecionado;

	int maiorIndiceFundo;
	int maiorIndicePlataforma;
	int maiorIndiceInimigos;
	int maiorIndiceUtilidades;
	ArrayList<Cenario> tiles;

	int mouseX, mouseY;
	int translacao;

	JButton btnSalvar = new JButton("Salvar");
	JButton btnLimpar = new JButton("Limpar");
	JButton btnCarregar = new JButton("Carregar");

	private boolean pausado = true;

	Robo javaBot;

	public LevelConstrutor(JFrame janela) {
		this.setLayout(null);
		controle = new ControleInvoker();
		javaBot = new Robo();

		controle.mapearTeclaPressionada(KeyEvent.VK_D, new MoverDireitaComando(javaBot));
		controle.mapearTeclaPressionada(KeyEvent.VK_A, new MoverEsquerdaComando(javaBot));
		controle.mapearTeclaPressionada(KeyEvent.VK_W, new PularComando(javaBot));
		controle.mapearTeclaPressionada(KeyEvent.VK_M, new MorrerComando(javaBot));
		controle.mapearTeclaPressionada(KeyEvent.VK_P, new AlternarPausaComando(javaBot, this));
		controle.mapearTeclaPressionada(KeyEvent.VK_RIGHT, new MoverCameraDireitaComando(this));
		controle.mapearTeclaPressionada(KeyEvent.VK_LEFT, new MoverCameraEsquerdaComando(this));

		controle.mapearTeclaSolta(KeyEvent.VK_A, new PararMovimentoComando(javaBot));
		controle.mapearTeclaSolta(KeyEvent.VK_D, new PararMovimentoComando(javaBot));

		java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				controle.keyPressed(e);
			} else if (e.getID() == KeyEvent.KEY_RELEASED) {
				controle.keyReleased(e);
			}
			return false;
		});
		// Botao salvar
		btnSalvar.setBounds(1700, 920, 100, 30);
		btnSalvar.addActionListener(action -> {
			String nomeDoArquivo = JOptionPane.showInputDialog("Qual o nome do arquivo?");
			try {
				File arquivo = new File(nomeDoArquivo + ".txt");
				FileWriter escreveArquivo = new FileWriter(arquivo);

				for (int i = 0; i < tiles.size(); i++) {
					Cenario cenario = tiles.get(i);
					if (cenario instanceof CenarioFundo) {
						escreveArquivo.write("#FUNDO " + cenario.toString() + "\n");
					} else if (cenario instanceof Plataforma) {
						escreveArquivo.write("#PLATAFORMA " + cenario.toString() + "\n");
					} else if (cenario instanceof CenarioInimigo) {
						escreveArquivo.write("#INIMIGO " + cenario.toString() + "\n");
					} else if (cenario instanceof CenarioUtilidades) {
						escreveArquivo.write("#UTILIDADE " + cenario.toString() + "\n");
					}
				}

				escreveArquivo.flush();
				escreveArquivo.close();

			} catch (Exception e) {
				System.out.println("Não foi possível trabalhar com o arquivo");
			}
			janela.requestFocus();
		});
		this.add(btnSalvar);
		// Botao Limpar
		btnLimpar.setBounds(1770, 960, 100, 30);
		btnLimpar.addActionListener(action -> {
			tiles.clear();
			repaint();
			janela.requestFocus();
		});
		this.add(btnLimpar);
		// Botao Carregar um jogo salvo
		btnCarregar.setBounds(1630, 960, 100, 30);
		btnCarregar.addActionListener(action -> {
			tiles.clear();
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				String caminhoLoad = System.getProperty("user.dir");
				JFileChooser escolherArquivo = new JFileChooser(caminhoLoad);
				escolherArquivo.showOpenDialog(this);
				File arquivoSelecionado = escolherArquivo.getSelectedFile();
				BufferedReader leitorDoArquivo = new BufferedReader(new FileReader(arquivoSelecionado));

				String linhaLida;
				while ((linhaLida = leitorDoArquivo.readLine()) != null) {
					String partes[] = linhaLida.split(" ");

					int indice = Integer.parseInt(partes[1]);
					int posx = Integer.parseInt(partes[2]);
					int posy = Integer.parseInt(partes[3]);
					int tamx = Integer.parseInt(partes[4]);
					int tamy = Integer.parseInt(partes[5]);

					if (partes[0].equals("#FUNDO")) {
						tiles.add(new CenarioFundo(imagensTiles.get(indice), posx, posy, tamx, tamy, indice));
					}

					if (partes[0].equals("#PLATAFORMA")) {
						tiles.add(new Plataforma(imagensTiles.get(indice), posx, posy, tamx, tamy, indice));
					}

					if (partes[0].equals("#INIMIGO")) {
						tiles.add(new CenarioInimigo(imagensTiles.get(indice), posx, posy, tamx, tamy, indice));
					}

					if (partes[0].equals("#UTILIDADE")) {
						tiles.add(new CenarioUtilidades(imagensTiles.get(indice), posx, posy, tamx, tamy, indice));
					}

				}
				leitorDoArquivo.close();

			} catch (Exception e) {
				System.out.println("Arquivo inapropriado");
			}
			janela.requestFocus();
			repaint();
		});
		this.add(btnCarregar);

		tileSelecionado = null;

		imagensTiles = new ArrayList<BufferedImage>();
		try {
			for (int i = 1; i < 7; i++) {
				imagensTiles.add(ImageIO.read(new File("imagensFases/Tiles/BGTile (" + i + ").png")));
			}

			maiorIndiceFundo = imagensTiles.size();

			for (int i = 12; i < 16; i++) {
				imagensTiles.add(ImageIO.read(new File("imagensFases/Tiles/Tile (" + i + ").png")));
			}

			maiorIndicePlataforma = imagensTiles.size();

			imagensTiles.add(ImageIO.read(new File("imagensFases/Tiles/Acid (1).png")));
			imagensTiles.add(ImageIO.read(new File("imagensFases/Tiles/Acid (2).png")));
			imagensTiles.add(ImageIO.read(new File("imagensFases/Tiles/Spike.png")));
			imagensTiles.add(ImageIO.read(new File("imagensFases/Objects/Saw.png")));
			imagensTiles.add(ImageIO.read(new File("imagensPapaiNoel/Idle (1).png")));

			maiorIndiceInimigos = imagensTiles.size();

			imagensTiles.add(ImageIO.read(new File("imagensFases/Objects/Barrel (1).png")));
			imagensTiles.add(ImageIO.read(new File("imagensFases/Objects/DoorLocked.png")));
			imagensTiles.add(ImageIO.read(new File("imagensFases/Objects/DoorOpen.png")));
			imagensTiles.add(ImageIO.read(new File("imagensFases/Objects/Switch (1).png")));
			imagensTiles.add(ImageIO.read(new File("imagensFases/Objects/Switch (2).png")));

			maiorIndiceUtilidades = imagensTiles.size();

		} catch (Exception e) {
			System.out.println("Não deu pra carregar as imagens");
		}

		tiles = new ArrayList<Cenario>();

		this.setFocusable(true);
		this.requestFocusInWindow();

		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while (true) {
			Queue<ComandoInterface> comandos = controle.getFilaComandos();
			while(!comandos.isEmpty()){
				ComandoInterface comando = comandos.poll();
				comando.executar();
			}
			if (!pausado) {

				atualiza();

				repaint();
			}
			dorme();
		}
	}

	private void dorme() {

		try {
			Thread.sleep(16);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void atualiza() {
		javaBot.atualizar();

		if (!javaBot.isMorreu()) {
			boolean conseguiu = false;

			for (int i = 0; i < tiles.size(); i++) {
				Cenario cenario = tiles.get(i);

				if (javaBot.getPosx() + javaBot.getLargura() * 2 / 3 > cenario.getPosx()
						&& javaBot.getPosx() + javaBot.getLargura() * 1 / 3 <= cenario.getPosx() + cenario.getTamx()) {

					if (cenario.solido() && cenario instanceof Plataforma) {
						if (javaBot.getPosy() + javaBot.getAltura() >= cenario.getPosy()
								&& javaBot.getPosy() + javaBot.getAltura() <= cenario.getPosy() + cenario.getTamy() - javaBot.quantoCaiu) {
							javaBot.encontrouChao();
							javaBot.setPosy(cenario.getPosy() - javaBot.getAltura() + 7);
							conseguiu = true;
						}
					}

					if (cenario.causaDano()) {
						if (javaBot.getPosy() + javaBot.getAltura() >= cenario.getPosy()
								&& javaBot.getPosy() <= cenario.getPosy() + cenario.getTamy()) {
							javaBot.morra();
						}
					}
				}
			}

			if (!conseguiu) {
				javaBot.setEstaNoChao(false);
			}
		}
	}

	@Override
	public void paintComponent(Graphics g2) {

		Graphics2D g = (Graphics2D) g2.create();

		g.setColor(Color.white);
		g.fillRect(0, 0, 1920, 1080);

		g.setColor(Color.black);
		g.drawLine(1600, 0, 1600, 1080);
		g.drawLine(1706, 0, 1706, 900);
		g.drawLine(1812, 0, 1812, 900);
		g.drawLine(1918, 0, 1918, 1080);

		for (int i = 0; i < 14; i++) {
			g.drawLine(1600, 0 + i * 75, 1918, 0 + i * 75);
		}

		g.setColor(Color.gray);
		for (int i = 0; i < 64; i++) {
			g.drawLine(i * 25, 0, i * 25, 1080);
		}
		for (int i = 0; i < 43; i++) {
			g.drawLine(0, i * 25, 1600, i * 25);
		}

		for (int i = 0; i < imagensTiles.size(); i++) {
			if (i % 3 == 0) {
				g.drawImage(imagensTiles.get(i), // imagem que será desenhada
						1617, 7 + i / 3 * 75, // posicao
						1690, 67 + i / 3 * 75, // posicao + tamanho
						0, 0, // inicio da imagem original
						imagensTiles.get(i).getWidth(), // fim da imagem original
						imagensTiles.get(i).getHeight(), null);
			} else if (i % 3 == 1) {
				g.drawImage(imagensTiles.get(i), // imagem que será desenhada
						1723, 7 + i / 3 * 75, // posicao
						1796, 67 + i / 3 * 75, // posicao + tamanho
						0, 0, // inicio da imagem original
						imagensTiles.get(i).getWidth(), // fim da imagem original
						imagensTiles.get(i).getHeight(), null);
			} else {
				g.drawImage(imagensTiles.get(i), // imagem que será desenhada
						1829, 7 + i / 3 * 75, // posicao
						1902, 67 + i / 3 * 75, // posicao + tamanho
						0, 0, // inicio da imagem original
						imagensTiles.get(i).getWidth(), // fim da imagem original
						imagensTiles.get(i).getHeight(), null);
			}
		}

		for (Cenario cenario : tiles) {
			if (cenario.getPosx() - translacao < 1550){
				cenario.pintar(g, translacao);
			}
		}

		// desenhando o objeto selecionado na tela
		if (tileSelecionado != null && mouseX < 1600) {

			g.drawImage(tileSelecionado, // imagem que será desenhada
					mouseX - 10, mouseY - 30, // posicao
					mouseX + 40, mouseY + 20, // posicao + tamanho
					0, 0, // inicio da imagem original
					tileSelecionado.getWidth(), // fim da imagem original
					tileSelecionado.getHeight(), null);

		}

		javaBot.pintar(g);

		g.setColor(Color.red);
		g.setFont(minhaFonte);
		if (pausado) {
			g.drawString("PAUSADO", 20, 30);
		} else {
			g.drawString("RODANDO", 20, 30);
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getX() > 1608 && e.getY() < 930) {
				System.out.println("Mouse: " + e.getX() + ", " + e.getY());

				int x = e.getX();
				int y = e.getY();

				x -= 1608;
				x /= 106;

				y -= 35;
				y /= 75;

				int indice = y * 3 + x;

				System.out.println("Imagem: " + indice + " X: " + x + " Y: " + y);

				if (indice < imagensTiles.size()) {
					tileSelecionado = imagensTiles.get(indice);
					indiceSelecionado = indice;
				} else {
					tileSelecionado = null;
				}

			} else {

				int x = e.getX() - 10;
				int y = e.getY() - 30;

				x = x - x % 25;
				y = y - y % 25;

				if (tileSelecionado != null) {
					Cenario novoTile = null;
					System.out.println("indice selecionado - " + indiceSelecionado);
					System.out.println("FUndo - " + maiorIndiceFundo);
					System.out.println("plataforma - " + maiorIndicePlataforma);
					System.out.println("inimigo - " + maiorIndiceInimigos);
					System.out.println("utilidades - " + maiorIndiceUtilidades);
					if (indiceSelecionado < maiorIndiceFundo) {
						novoTile = new CenarioFundo(tileSelecionado, x + translacao, y, 75, 75, indiceSelecionado);
					} else if (indiceSelecionado < maiorIndicePlataforma) {
						novoTile = new Plataforma(tileSelecionado, x + translacao, y, 75, 75, indiceSelecionado);
					} else if (indiceSelecionado < maiorIndiceInimigos) {
						int tamanho = indiceSelecionado == 14 ? 150 : 75;
						novoTile = new CenarioInimigo(tileSelecionado, x + translacao, y, tamanho, tamanho, indiceSelecionado);
					}
					else if (indiceSelecionado < maiorIndiceUtilidades) {
						int tamanhoY = (indiceSelecionado == 16 || indiceSelecionado == 17) ? 150 : 75;
						int tamanhoX = (indiceSelecionado == 16 || indiceSelecionado == 17) ? 100 : 75;
						novoTile = new CenarioUtilidades(tileSelecionado, x + translacao, y, tamanhoX, tamanhoY, indiceSelecionado);
					}

					if (novoTile != null){
						tiles.add(novoTile);
					}
				}

				// selecionado null remover o elemento
				else {
					for (int i = tiles.size() - 1; i >= 0; i--) {
						Cenario cenario = tiles.get(i);
						if (cenario.getPosx() - translacao <= x && cenario.getPosx() + cenario.getTamx() - translacao > x
								&& cenario.getPosy() <= y && cenario.getPosy() + cenario.getTamy() > y) {
							tiles.remove(i);
						}
					}
				}
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			tileSelecionado = null;
		}

		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		mouseX = e.getX();
		mouseY = e.getY();

		repaint();
	}

	public void setPausado (){
		this.pausado = !pausado;
	}

	public void setTranslacao (int translacao) {
		this.translacao +=  translacao;
	}

	public ControleInvoker getControle () {
		return this.controle;
	}
}
