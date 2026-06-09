package prodjogo4.Robo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Robo {

	private Animacao estadoAtual;
	private int largura;
	private int altura;
	private int posx;
	private int posy;
	private int posy0;
	private int direcao;
	private int ultimaDirecao;
	private final int velocidade;
	private boolean estaNoChao;
	private boolean morreu;
	private boolean atirando;
	public int tempoPulo;
	public boolean pulou;
	public boolean pulouDenovo;
	public int quantoCaiu;
	private final BufferedImage imgMorrendo[];
	private final BufferedImage imgAtirando[];
	private int indiceImagemAtual;

	public Robo() {
		indiceImagemAtual = 0;
		estaNoChao = true;
		pulou = false;
		pulouDenovo = false;
		imgMorrendo = new BufferedImage[10];
		imgAtirando = new BufferedImage[9];
		largura = 150;
		altura = 150;
		direcao = 0;
		ultimaDirecao = 1;
		velocidade = 3;
		morreu = false;
		atirando = false;

		try {
			for (int i = 0; i < 10; i++) {
				imgMorrendo[i] = ImageIO.read(new File("imagensRobo/Dead (" + (i + 1) + ").png"));
			}
			for (int i = 0; i < 9; i++) {
				imgAtirando[i] = ImageIO.read(new File("imagensRobo/RunShoot (" + (i + 1) + ").png"));
			}
		} catch (IOException e) {
			System.out.println("Não foi possível carregar a imagem");
		}

		this.estadoAtual = new RoboCorrendo();
	}

	public void atualizar() {
		if (this.posy > 1000) {
			reiniciarRobo();
			this.estadoAtual = new RoboCorrendo();
		}
		estadoAtual.atualizar(this);
	}

	public void pintar(Graphics2D g) {
		estadoAtual.pintar(this, g);
	}

	public void dispararAcao(String acao) {
		estadoAtual.mudarEstado(this, acao);
	}

	public void iniciaPulo() {
		if (estaNoChao || !pulouDenovo) {
			if (!estaNoChao) {
				pulouDenovo = true;
			}
			estaNoChao = false;
			posy0 = posy;
			tempoPulo = 0;
			pulou = true;
			this.estadoAtual = new RoboPulando();
		}
	}

	public void iniciaCarrinho() {
		this.estadoAtual = new RoboEscorregando();
	}

	public void morra() {
		if (!(estadoAtual instanceof RoboMorrendo)) {
			this.estadoAtual = new RoboMorrendo();
			morreu = true;
		}
	}

	public Tiro atira() {
		atirando = true;
		Tiro tiro;
		if (this.ultimaDirecao == 1) {
			tiro = new Tiro(this.posx + 150, this.posy + 40, this.ultimaDirecao);
		} else {
			tiro = new Tiro(this.posx, this.posy + 40, this.ultimaDirecao);
		}
		return tiro;
	}

	public void reiniciarRobo() {
		this.posx = 50;
		this.posy = 660;
		this.estaNoChao = true;
		this.pulou = false;
		this.pulouDenovo = false;
		this.quantoCaiu = 0;
		this.morreu = false;
		this.atirando = false;
	}

	public void reiniciarRobo2() {
		this.posx = 50;
		this.posy = 100;
		this.estaNoChao = true;
		this.pulou = false;
		this.pulouDenovo = false;
		this.quantoCaiu = 0;
		this.morreu = false;
		this.atirando = false;
	}

	public void encontrouChao() {
		quantoCaiu = 0;
		estaNoChao = true;
		pulou = false;
		pulouDenovo = false;
		posy0 = posy;
		tempoPulo = 0;
	}

	public boolean podeAtirar() {
		return !atirando;
	}

	public Animacao getEstadoAtual() { return estadoAtual; }
	public void setEstadoAtual(Animacao novoEstado) { this.estadoAtual = novoEstado; }
	public int getLargura() { return largura; }
	public void setLargura(int largura) { this.largura = largura; }
	public int getAltura() { return altura; }
	public void setAltura(int altura) { this.altura = altura; }
	public int getPosx() { return posx; }
	public void setPosx(int posx) { this.posx = posx; }
	public int getPosy() { return posy; }
	public void setPosy(int posy) { this.posy = posy; }
	public int getPosy0() { return posy0; }
	public void setPosy0(int posy0) { this.posy0 = posy0; }
	public int getDirecao() { return direcao; }
	public void setDirecao(int dir) {
		this.direcao = dir;
		if (dir != 0) {
			this.ultimaDirecao = dir;
		}
	}
	public int getUltimaDirecao() { return ultimaDirecao; }
	public int getVelocidade() { return velocidade; }
	public boolean isEstaNoChao() { return estaNoChao; }
	public void setEstaNoChao(boolean estaNoChao) { this.estaNoChao = estaNoChao; }
	public boolean isMorreu() { return morreu; }
	public void setMorreu(boolean morreu) { this.morreu = morreu; }
	public boolean isAtirando() { return atirando; }
	public void setAtirando(boolean atirando) { this.atirando = atirando; }
	public BufferedImage[] getImgMorrendo() { return imgMorrendo; }
	public BufferedImage[] getImgAtirando() { return imgAtirando; }

	public void setIndiceImagemAtual(int indiceImagemAtual) {
		this.indiceImagemAtual = indiceImagemAtual;
	}

	public int getIndiceImagemAtual() {
		return this.indiceImagemAtual;
	}
}
