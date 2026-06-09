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
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import armazenamentoStrategy.GerenciadorArmazenamento;
import armazenamentoStrategy.IArmazenamento;
import armazenamentoStrategy.Notas;
import cenarios.*;
import controle.*;
import ferramentaMouseEditor.FerramentaBorracha;
import ferramentaMouseEditor.FerramentaEditor;
import ferramentaMouseEditor.FerramentaPincel;
import ferramentaMouseEditor.FerramentaSelecionarCenario;
import prodjogo4.Robo.Robo;

public class LevelConstrutor extends JPanel implements MouseListener, MouseMotionListener, Runnable {

	private Font minhaFonte = new Font("Consolas", Font.BOLD, 30);
	private ControleInvoker controle;
	ArrayList<BufferedImage> imagensTiles;
	public BufferedImage tileSelecionado;
	public int indiceSelecionado;

	int maiorIndiceFundo;
	int maiorIndicePlataforma;
	int maiorIndiceInimigos;
	int maiorIndiceUtilidades;
	ArrayList<Cenario> tiles;

	int mouseX, mouseY;
	int translacao;

	JButton btnSalvar = new JButton("Salvar");
	JButton btnLimpar = new JButton("Limpar");
	JButton btnLimparSelecao = new JButton("Limpar Seleção");
	JButton btnCarregar = new JButton("Carregar");
	GerenciadorArmazenamento armazenamento;
	private boolean pausado = true;
	private MotorColisao motorColisao = new MotorColisao();
	private FerramentaEditor ferramentaAtiva;
	Robo javaBot;

	public LevelConstrutor(JFrame janela) {
		this.setLayout(null);
		javaBot = new Robo();
		armazenamento = new GerenciadorArmazenamento(new Notas());
		tileSelecionado = null;

		inicializarControles();
		inicializarBotoes(janela);
		carregarImagensCenario();


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

		motorColisao.processarColisao(javaBot, tiles);

	}

	private void inicializarBotoes(JFrame janela) {
		btnSalvar.setBounds(1700, 920, 100, 30);
		btnSalvar.addActionListener(action -> {
			String nomeDoArquivo = JOptionPane.showInputDialog("Qual o nome do arquivo?");
			armazenamento.executarSalvamento(tiles, nomeDoArquivo);
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

		btnLimparSelecao.setBounds(1630, 880, 140, 30);
		btnLimparSelecao.addActionListener(action -> {
			this.tileSelecionado = null;
			this.indiceSelecionado = -1;
			this.ferramentaAtiva = new FerramentaBorracha(tiles);
			repaint();
		});
		this.add(btnLimparSelecao);
		btnCarregar.setBounds(1630, 960, 100, 30);
		btnCarregar.addActionListener(action -> {
			tiles.clear();
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				String caminhoLoad = System.getProperty("user.dir");
				JFileChooser escolherArquivo = new JFileChooser(caminhoLoad);
				escolherArquivo.showOpenDialog(this);
				File arquivoSelecionado = escolherArquivo.getSelectedFile();

				List<Cenario> cenariosCarregados = armazenamento.executarCarregamento(imagensTiles, this, arquivoSelecionado.getAbsolutePath());
				tiles.addAll(cenariosCarregados);
			} catch (Exception e) {
				System.out.println("Arquivo inapropriado");
			}
			janela.requestFocus();
			repaint();
		});
		this.add(btnCarregar);
	}

	private void inicializarControles() {
		controle = new ControleInvoker();
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
	}

	private void carregarImagensCenario() {
		tiles = new ArrayList<Cenario>();
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

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getX() > 1608 && e.getY() < 930) {
				FerramentaSelecionarCenario seletor = new FerramentaSelecionarCenario(imagensTiles, this);
				seletor.aoClicar(e, translacao);

				if (tileSelecionado != null) {
					this.ferramentaAtiva = new FerramentaPincel(indiceSelecionado, maiorIndiceFundo, maiorIndiceInimigos,
							maiorIndicePlataforma, tileSelecionado, maiorIndiceUtilidades, tiles);
				} else {
					this.ferramentaAtiva = new FerramentaBorracha(tiles);
				}
			}
			else {
				if (ferramentaAtiva == null) {
					if (tileSelecionado != null) {
						this.ferramentaAtiva = new FerramentaPincel(indiceSelecionado, maiorIndiceFundo, maiorIndiceInimigos,
								maiorIndicePlataforma, tileSelecionado, maiorIndiceUtilidades, tiles);
					} else {
						this.ferramentaAtiva = new FerramentaBorracha(tiles);
					}
				}

				ferramentaAtiva.aoClicar(e, translacao);
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
