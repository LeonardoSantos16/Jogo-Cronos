package prodjogo5;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controle.*;
import prodjogo5.MotorColisaoJogo;
import prodjogo4.Tile;
import prodjogo4.Robo.Robo;
import prodjogo4.Robo.Tiro;
import prodjogo5.Inimigos.InimigoDino;
import prodjogo5.Inimigos.InimigoZombie;
import prodjogo5.Inimigos.Inimigos;

public class Jogo extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private Robo javaBot;
	private MotorColisaoJogo motorColisao;
	private ControleInvoker controleInvoker;

	private ArrayList<Tiro> tiros;
	private ArrayList<BufferedImage> tiles;
	private Fase faseAtual;
	private int codFase;
	private ArrayList<Fase> fases;

	private ArrayList<Inimigos> listaInimigos;
	private ArrayList<InimigoDino> listaDinos;
	private ArrayList<InimigoZombie> listaZumbi;

	private JButton btnSalvar = new JButton("Salvar");

	private int x, y, direcao;

	public Jogo(JFrame janela) {
		this.setLayout(null);
		inicializarControles();

		listaInimigos = new ArrayList<>();
		listaDinos = new ArrayList<>();
		listaZumbi = new ArrayList<>();
		tiros = new ArrayList<>();

		listaInimigos.add(new Inimigos(1465, 405));
		listaInimigos.add(new Inimigos(1735, 5));
		listaZumbi.add(new InimigoZombie(1420, 100, 1280, 1500, 1));
		listaZumbi.add(new InimigoZombie(1280, 500, 1120, 1350, 2));

		Tiro.carregaImagens();
		carregaTiles();
		javaBot = new Robo();
		motorColisao = new MotorColisaoJogo();

		fases = new ArrayList<>();
		fases.add(new Fase("fase1.txt", tiles));
		fases.add(new Fase("fase2.txt", tiles));
		fases.add(new Fase("fase3.txt", tiles));
		fases.add(new Fase("faseinimigos.txt", tiles));

		inicializarBotoes(janela);

		Thread t = new Thread(this);
		t.start();
	}

	private void inicializarControles() {
		this.setFocusable(true);
		this.requestFocusInWindow();

		controleInvoker = new ControleInvoker();
		controleInvoker.mapearTeclaPressionada(KeyEvent.VK_D, new MoverDireitaComando(javaBot));
		controleInvoker.mapearTeclaPressionada(KeyEvent.VK_A, new MoverEsquerdaComando(javaBot));
		controleInvoker.mapearTeclaPressionada(KeyEvent.VK_W, new PularComando(javaBot));
		controleInvoker.mapearTeclaPressionada(KeyEvent.VK_B, new DeslizarComando(javaBot));
		controleInvoker.mapearTeclaPressionada(KeyEvent.VK_SPACE, new AtirarComando(javaBot));
		controleInvoker.mapearTeclaSolta(KeyEvent.VK_A, new PararMovimentoComando(javaBot));
		controleInvoker.mapearTeclaSolta(KeyEvent.VK_D, new PararMovimentoComando(javaBot));

		javax.swing.InputMap inputMap = this.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
		javax.swing.ActionMap actionMap = this.getActionMap();

		int[] teclas = {KeyEvent.VK_D, KeyEvent.VK_A, KeyEvent.VK_W, KeyEvent.VK_B, KeyEvent.VK_SPACE};
		String[] nomesPressionar = {"andarD", "andarA", "pular", "deslizar", "atirar"};

		for (int i = 0; i < teclas.length; i++) {
			final int tecla = teclas[i];
			inputMap.put(javax.swing.KeyStroke.getKeyStroke(tecla, 0, false), nomesPressionar[i]);
			actionMap.put(nomesPressionar[i], new javax.swing.AbstractAction() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					KeyEvent keyEvent = new KeyEvent(Jogo.this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, tecla, KeyEvent.CHAR_UNDEFINED);
					controleInvoker.keyPressed(keyEvent);
				}
			});
		}

		int[] teclasSoltar = {KeyEvent.VK_D, KeyEvent.VK_A};
		String[] nomesSoltar = {"soltarD", "soltarA"};

		for (int i = 0; i < teclasSoltar.length; i++) {
			final int tecla = teclasSoltar[i];
			inputMap.put(javax.swing.KeyStroke.getKeyStroke(tecla, 0, true), nomesSoltar[i]);
			actionMap.put(nomesSoltar[i], new javax.swing.AbstractAction() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					KeyEvent keyEvent = new KeyEvent(Jogo.this, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, tecla, KeyEvent.CHAR_UNDEFINED);
					controleInvoker.keyReleased(keyEvent);
				}
			});
		}
	}

	private void inicializarBotoes(JFrame janela) {
		btnSalvar.setBounds(1800, 50, 100, 30);
		btnSalvar.setFocusable(false);
		btnSalvar.addActionListener(action -> {
			String nomeDoArquivo = JOptionPane.showInputDialog("Qual o nome do arquivo?");
			try {
				File arquivo = new File("saves/" + nomeDoArquivo + ".txt");
				FileWriter escreveArquivo = new FileWriter(arquivo);
				escreveArquivo.write(javaBot.getPosx() + " " + javaBot.getPosy() + " " + javaBot.getUltimaDirecao() + " " + codFase);
				escreveArquivo.close();
			} catch (Exception e) {
				System.out.println("Não foi possível trabalhar com o arquivo");
			}
			this.requestFocusInWindow();
			repaint();
		});
		this.add(btnSalvar);
	}

	public void carregarCache() {
		javaBot.setPosx(x);
		javaBot.setPosy(y);
		javaBot.setDirecao(direcao);

		listaZumbi.clear();
		listaInimigos.clear();
		listaDinos.clear();
		faseAtual = fases.get(codFase);

		if (codFase == 0) {
			listaInimigos.add(new Inimigos(1465, 405));
			listaInimigos.add(new Inimigos(1735, 5));
			listaZumbi.add(new InimigoZombie(1420, 100, 1280, 1500, 1));
			listaZumbi.add(new InimigoZombie(1280, 500, 1120, 1350, 2));
		} else if (codFase == 1) {
			listaInimigos.add(new Inimigos(1490, 300));
			listaZumbi.add(new InimigoZombie(700, 660, 600, 760, 1));
			listaZumbi.add(new InimigoZombie(975, 660, 900, 1010, 2));
		} else if (codFase == 2) {
			listaDinos.add(new InimigoDino(1400, 400));
			listaZumbi.add(new InimigoZombie(800, 660, 700, 850, 1));
			listaInimigos.add(new Inimigos(1750, 500));
		}

	}

	public void setDirecao(int direcao) {
		this.direcao = direcao;
	}

	@Override
	public void run() {
		while (true) {
			atualiza();
			repaint();
			Utils.dorme();
		}
	}

	public void atualiza() {
		verificarTransicaoFase();

		for (InimigoZombie zumbi : listaZumbi) {
			zumbi.atualizar(javaBot);
		}
		for (InimigoDino dino : listaDinos) {
			dino.atualizar(javaBot);
		}
		for (Inimigos inimigo : listaInimigos) {
			inimigo.atualizar();
		}
		for (Tiro tiro : tiros) {
			tiro.atualizar();
		}

		javaBot.atualizar();

		motorColisao.processarFisicaMundo(javaBot, listaInimigos, listaDinos, listaZumbi, faseAtual.getPlataformas());
		motorColisao.processarDanoETiros(javaBot, listaInimigos, listaDinos, listaZumbi, tiros, faseAtual.getInimigos());
	}

	private void verificarTransicaoFase() {
		for (Tile c : faseAtual.getPortas()) {
			if (javaBot.getPosx() + javaBot.getLargura() * 2 / 3 > c.getPosx()
					&& javaBot.getPosx() + javaBot.getLargura() * 1 / 3 <= c.getPosx() + c.getTamx()) {
				if (javaBot.getPosy() + javaBot.getAltura() >= c.getPosy()
						&& javaBot.getPosy() <= c.getPosy() + c.getTamy()) {

					if (javaBot.getDirecao() == 0) {
						codFase++;
						faseAtual = fases.get(codFase);
						listaZumbi.clear();
						listaInimigos.clear();
						listaDinos.clear();

						if (codFase == 0) {
							javaBot.reiniciarRobo();
						} else if (codFase == 1) {
							javaBot.reiniciarRobo2();
							listaInimigos.add(new Inimigos(1490, 300));
							listaZumbi.add(new InimigoZombie(700, 660, 600, 760, 1));
							listaZumbi.add(new InimigoZombie(975, 660, 900, 1010, 2));
						} else if (codFase == 2) {
							listaDinos.add(new InimigoDino(1400, 400));
							listaZumbi.add(new InimigoZombie(800, 660, 700, 850, 1));
							listaInimigos.add(new Inimigos(1750, 500));
						}
						break;
					}
				}
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g2) {
		super.paintComponent(g2);
		Graphics2D g = (Graphics2D) g2.create();

		g.setColor(Color.gray);
		g.fillRect(0, 0, 1920, 1080);

		faseAtual.pintar(g);
		javaBot.pintar(g);

		for (Tiro tiro : tiros) {
			tiro.pintar(g);
		}
		for (Inimigos inimigo : listaInimigos) {
			inimigo.pintar(g);
			if (inimigo.podeAtirar()) {
				tiros.add(inimigo.atira());
			}
		}
		for (InimigoDino dino : listaDinos) {
			dino.pintar(g, javaBot);
		}
		for (InimigoZombie zumbi : listaZumbi) {
			zumbi.pintar(g);
		}
		g.dispose();
	}

	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public void setCodFase(int codFase) { this.codFase = codFase; }

	// Método utilizado para carregar as imagens do cenário
	private void carregaTiles() {
		tiles = new ArrayList<BufferedImage>();
		try {
			for (int i = 1; i < 7; i++) {
				tiles.add(ImageIO.read(new File("imagensFases/Tiles/BGTile (" + i + ").png")));
			}

//			maiorIndiceFundo = tiles.size();

			for (int i = 12; i < 16; i++) {
				tiles.add(ImageIO.read(new File("imagensFases/Tiles/Tile (" + i + ").png")));
			}

//			maiorIndicePlataforma = tiles.size();

			tiles.add(ImageIO.read(new File("imagensFases/Tiles/Acid (1).png")));
			tiles.add(ImageIO.read(new File("imagensFases/Tiles/Acid (2).png")));
			tiles.add(ImageIO.read(new File("imagensFases/Tiles/Spike.png")));
			tiles.add(ImageIO.read(new File("imagensFases/Objects/Saw.png")));
			tiles.add(ImageIO.read(new File("imagensPapaiNoel/Idle (1).png")));

//			maiorIndiceInimigos = tiles.size();

			tiles.add(ImageIO.read(new File("imagensFases/Objects/Barrel (1).png")));
			tiles.add(ImageIO.read(new File("imagensFases/Objects/DoorLocked.png")));
			tiles.add(ImageIO.read(new File("imagensFases/Objects/DoorOpen.png")));
			tiles.add(ImageIO.read(new File("imagensFases/Objects/Switch (1).png")));
			tiles.add(ImageIO.read(new File("imagensFases/Objects/Switch (2).png")));

//			maiorIndiceUtilidades = tiles.size();

		} catch (Exception e) {
			System.out.println("Não deu pra carregar as imagens");
		}
	}

}
