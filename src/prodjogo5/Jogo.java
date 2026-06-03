package prodjogo5;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import prodjogo4.Tile;
import prodjogo4.Robo.Robo;
import prodjogo4.Robo.Tiro;
import prodjogo5.Inimigos.InimigoDino;
import prodjogo5.Inimigos.InimigoZombie;
import prodjogo5.Inimigos.Inimigos;

public class Jogo extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Controle controle;

	int x, y, direcao;

	ArrayList<Tiro> tiros;
	ArrayList<BufferedImage> tiles;
	Robo javaBot;

	Fase faseAtual;
	int codFase;
	ArrayList<Fase> fases;

	private ArrayList<Inimigos> listaInimigos;
	private ArrayList<InimigoDino> listaDinos;
	private ArrayList<InimigoZombie> listaZumbi;

	JButton btnSalvar = new JButton("Salvar");

	// Construtor recebendo um obj do tipo controle e um jframe para manipulação
	public Jogo(Controle controle, JFrame janela) {

		listaInimigos = new ArrayList<Inimigos>();
		listaDinos = new ArrayList<InimigoDino>();
		listaZumbi = new ArrayList<InimigoZombie>();

		listaInimigos.add(new Inimigos(1465, 405));
		listaInimigos.add(new Inimigos(1735, 5));
		listaZumbi.add(new InimigoZombie(1420, 100, 1280, 1500, 1));
		listaZumbi.add(new InimigoZombie(1280, 500, 1120, 1350, 2));

		tiros = new ArrayList<Tiro>();
		Tiro.carregaImagens();

		this.controle = controle;

		carregaTiles();

		javaBot = new Robo();

		fases = new ArrayList<Fase>();
		fases.add(new Fase("fase1.txt", tiles));
		fases.add(new Fase("fase2.txt", tiles));
		fases.add(new Fase("fase3.txt", tiles));
		fases.add(new Fase("faseinimigos.txt", tiles));

//		faseAtual = fases.get(codFase);

		btnSalvar.setBounds(1800, 50, 100, 30);
		btnSalvar.addActionListener(action -> {
			String nomeDoArquivo = JOptionPane.showInputDialog("Qual o nome do arquivo?");
			try {
				File arquivo = new File("saves/" + nomeDoArquivo + ".txt");
				FileWriter escreveArquivo = new FileWriter(arquivo);
				escreveArquivo.write(
						javaBot.getPosx() + " " + javaBot.getPosy() + " " + javaBot.getUltimaDirecao() + " " + codFase);
				escreveArquivo.close();
			} catch (Exception e) {
				System.out.println("Não foi possível trabalhar com o arquivo");
			}
			janela.requestFocus();
			repaint();
		});
		this.add(btnSalvar);

		Thread t = new Thread(this);
		t.start();

	}

	// Método que carrega o cache passado pelo jpanel lobby e pela classe Janela5
	public void carregarCache() {
		javaBot.setPosx(x);
		javaBot.setPosy(y);
		javaBot.setDirecao(direcao);
		if (codFase == 1) {
			listaZumbi.clear();
			listaInimigos.clear();
			faseAtual = fases.get(codFase);
			listaInimigos.add(new Inimigos(1490, 300));
			listaZumbi.add(new InimigoZombie(700, 660, 600, 760, 1));
			listaZumbi.add(new InimigoZombie(975, 660, 900, 1010, 2));

		} else if (codFase == 2) {
			listaZumbi.clear();
			listaInimigos.clear();
			faseAtual = fases.get(codFase);
			listaDinos.add(new InimigoDino(1400, 400));
			listaZumbi.add(new InimigoZombie(800, 660, 700, 850, 1));
			listaInimigos.add(new Inimigos(1750, 500));
		} else {
			faseAtual = fases.get(codFase);
		}
	}

	@Override
	public void run() {

		System.out.println(javaBot.getPosx());
		System.out.println(javaBot.getPosy());

		while (true) {

			atualiza();
			repaint();
			Utils.dorme();

		}

	}

	// Método utilizado para atualizar a tela e eventos corriqueiros
	public void atualiza() {

		// Verificar se passou de fase

		for (int i = 0; i < faseAtual.getPortas().size(); i++) {

			Tile c = faseAtual.getPortas().get(i);

			if (javaBot.getPosx() + javaBot.getLargura() * 2 / 3 > c.getPosx()
					&& javaBot.getPosx() + javaBot.getLargura() * 1 / 3 <= c.getPosx() + c.getTamx()) {

				if (javaBot.getPosy() + javaBot.getAltura() >= c.getPosy()
						&& javaBot.getPosy() <= c.getPosy() + c.getTamy()) {

					if (controle.getEntrar() == true) {
						codFase++;
						faseAtual = fases.get(codFase);
						if (codFase == 0) {
							javaBot.reiniciarRobo();
						} else if (codFase == 1) {
							listaZumbi.clear();
							listaInimigos.clear();
							javaBot.reiniciarRobo2();
							// listaZumbi = new ArrayList<InimigoZombie>();
							// listaInimigos = new ArrayList<Inimigos>();
							listaInimigos.add(new Inimigos(1490, 300));
							listaZumbi.add(new InimigoZombie(700, 660, 600, 760, 1));
							listaZumbi.add(new InimigoZombie(975, 660, 900, 1010, 2));
//                                                    
						} else if (codFase == 2) {
							listaZumbi.clear();
							listaInimigos.clear();
							listaDinos.add(new InimigoDino(1400, 400));
							listaZumbi.add(new InimigoZombie(800, 660, 700, 850, 1));
							listaInimigos.add(new Inimigos(1750, 500));
						}
					}
				}
			}
		}

		for (InimigoZombie zumbi : listaZumbi) {
			zumbi.atualizar(javaBot);
		}

		for (InimigoZombie zumbi : listaZumbi) {
			for (Tiro tiro : tiros) {
				if (tiro.collideZumbi(zumbi)) {
					if (!zumbi.isMorreu()) {
						tiros.remove(tiro);
						zumbi.morra();
					}
					break;

				}
			}
		}

		for (InimigoDino dino : listaDinos) {
			dino.atualizar(javaBot);
		}

		for (InimigoDino dino : listaDinos) {
			if (dino.collideMelle(javaBot)) {
				javaBot.morra();
			}
		}

		for (Inimigos inimigo : listaInimigos) {
			for (Tiro tiro : tiros) {
				if (tiro.collide(inimigo)) {
					if (!inimigo.isMorreu()) {
						tiros.remove(tiro);
						inimigo.morra();
						repaint();
					}
					break;
				}
			}
		}

		for (Inimigos inimigo : listaInimigos) {
			inimigo.atualizar();
		}

		for (Tiro tiro : tiros) {
			if (tiro.collideHeroi(javaBot)) {
				tiros.remove(tiro);
				javaBot.morra();
				break;
			}
		}

//		for (Tiro tiro : tiros) {
//			for (InimigoDino dino : listaDinos) {
//				if (tiro.collideDino(dino)) {
//					tiros.remove(tiro);
//				}
//				break;
//			}
//		}

		for (int i = 0; i < tiros.size(); i++) {
			tiros.get(i).atualizar();
		}

		if (controle.isMovDireita()) {
			javaBot.setDirecao(1);
		} else if (controle.isMovEsquerda()) {
			javaBot.setDirecao(-1);
		} else {
			javaBot.setDirecao(0);
		}

		if (controle.isPulo()) {
			javaBot.iniciaPulo();
			System.out.println("pulando");
		}

		if (controle.isCarrinho()) {
			javaBot.iniciaCarrinho();
		} else {
			javaBot.setCarrinho(false);
			javaBot.setAltura(150);
			javaBot.setLargura(150);
		}

		if (controle.isTiro()) {
			if (javaBot.podeAtirar()) {
				tiros.add(javaBot.atira());
			}
		}

		javaBot.atualizar();

		if (!javaBot.isMorreu()) {

			boolean conseguiuInimigo = false;
			boolean conseguiuInimigoDino = false;
			boolean conseguiuInimigoZumbi = false;
			boolean conseguiu = false;

			// Plataformas
			for (int i = 0; i < faseAtual.getPlataformas().size(); i++) {

				Tile c = faseAtual.getPlataformas().get(i);

				if (javaBot.getPosx() + javaBot.getLargura() * 2 / 3 > c.getPosx()
						&& javaBot.getPosx() + javaBot.getLargura() * 1 / 3 <= c.getPosx() + c.getTamx()) {

					if (javaBot.getPosy() + javaBot.getAltura() >= c.getPosy() && javaBot.getPosy()
							+ javaBot.getAltura() <= c.getPosy() + c.getTamy() - javaBot.quantoCaiu) {
						javaBot.encontrouChao();
						javaBot.setPosy(c.getPosy() - javaBot.getAltura() + 7);
						conseguiu = true;
					}
				}

				// Dinos

				for (InimigoDino dino : listaDinos) {
					if (dino.getPosx() + dino.getLargura() * 2 / 3 + 25 > c.getPosx()
							&& dino.getPosx() + dino.getLargura() * 1 / 3 + 25 <= c.getPosx() + c.getTamx()) {
						if (dino.getPosy() + dino.getAltura() >= c.getPosy()
								&& dino.getPosy() + dino.getAltura() <= c.getPosy() + c.getTamy()) {
							dino.encontrouChao();
							dino.setPosy(c.getPosy() - dino.getAltura() + 7);
							conseguiuInimigoDino = true;
						}
					}
				}

				for (InimigoZombie inimigoZumbi : listaZumbi) {
					if (inimigoZumbi.getPosx() + inimigoZumbi.getLargura() * 2 / 3 + 25 > c.getPosx()
							&& inimigoZumbi.getPosx() + inimigoZumbi.getLargura() * 1 / 3 + 25 <= c.getPosx()
									+ c.getTamx()) {
						if (inimigoZumbi.getPosy() + inimigoZumbi.getAltura() >= c.getPosy()
								&& inimigoZumbi.getPosy() + inimigoZumbi.getAltura() <= c.getPosy() + c.getTamy()) {
							inimigoZumbi.encontrouChao();
							// inimigoZumbi.setPosy(c.getPosy() - inimigoZumbi.getAltura() + 7);
							conseguiuInimigoZumbi = true;
						}
					}
				}

				for (Inimigos inimigo : listaInimigos) {
					if (inimigo.getPosx() + inimigo.getTamx() * 2 / 3 > c.getPosx()
							&& inimigo.getPosx() + inimigo.getTamx() * 1 / 3 <= c.getPosx() + c.getTamx()) {
						if (inimigo.getPosy() + inimigo.getTamy() >= c.getPosy()
								&& inimigo.getPosy() + inimigo.getTamy() <= c.getPosy() + c.getTamy()) {
							inimigo.encontrouChao();
							// inimigo.setPosy(c.getPosy() - inimigo.getTamy() + 7);
							conseguiuInimigo = true;
						}
					}
				}
			}

			if (!conseguiu) {
				javaBot.setEstaNoChao(false);
			}

			for (Inimigos inimigo : listaInimigos) {
				if (!conseguiuInimigo) {
					inimigo.setEstaNoChao(false);
				}
			}

			for (InimigoDino dino : listaDinos) {
				if (!conseguiuInimigoDino) {
					dino.setEstaNoChao(false);
				}
			}

			for (InimigoZombie zumbi : listaZumbi) {
				if (!conseguiuInimigoZumbi) {
					zumbi.setEstaNoChao(false);
				}
			}

			for (int j = 0; j < faseAtual.getInimigos().size(); j++) {

				Tile c2 = faseAtual.getInimigos().get(j);

				if (javaBot.getPosx() + javaBot.getLargura() * 2 / 3 > c2.getPosx()
						&& javaBot.getPosx() + javaBot.getLargura() * 1 / 3 <= c2.getPosx() + c2.getTamx()) {

					if (javaBot.getPosy() + javaBot.getAltura() >= c2.getPosy()
							&& javaBot.getPosy() <= c2.getPosy() + c2.getTamy()) {

						javaBot.morra();
					}
				}
			}
		}

	}

	public void paintComponent(Graphics g2) {
		super.paintComponent(g2);

		Graphics2D g = (Graphics2D) g2.create();

		g.setColor(Color.gray);
		g.fillRect(0, 0, 1920, 1080);

		faseAtual.pintar(g);

		javaBot.pintar(g);

		for (int i = 0; i < tiros.size(); i++) {
			tiros.get(i).pintar(g);
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
	}

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
