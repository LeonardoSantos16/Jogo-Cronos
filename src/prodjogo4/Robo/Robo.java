package prodjogo4.Robo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import prodjogo5.Controle;
import prodjogo5.TiroRobo.RoboAtirandoCorrendo;
import prodjogo5.TiroRobo.RoboAtirandoParado;
import prodjogo5.TiroRobo.RoboAtirandoPulando;
import prodjogo4.Robo.Tiro;

public class Robo {

	private Animacao animCorrendo;
	private Animacao animPulando;
	private Animacao animCarrinho;

	private RoboAtirandoCorrendo animAtirando;
	private RoboAtirandoParado animAtirandoParado;
	private RoboAtirandoPulando animAtirandoPulando;

	private Controle cont;

	private BufferedImage imgMorrendo[];
	private BufferedImage imgAtirando[];

	private float timer;
	private int indiceImagemAtual;
	private int indiceTiroAtual;
	private int largura;
	private int altura;

	private int posx;
	private int posy;
	private int posy0;
	public int tempoPulo;
	public boolean pulou;
	public boolean pulouDenovo;

	private int direcao;
	private int ultimaDirecao;
	private int velocidade;

	private boolean estaNoChao;

	private boolean atirando;
	private boolean morreu;
	private boolean carrinho;

	private int timerTiro;
	public int quantoCaiu;

	public Robo() {

		estaNoChao = true;

		pulou = false;
		pulouDenovo = false;

		cont = new Controle();

		animCorrendo = new RoboCorrendo();
		animPulando = new RoboPulando();
		animCarrinho = new RoboEscorregando();
		animAtirando = new RoboAtirandoCorrendo();
		animAtirandoParado = new RoboAtirandoParado();
		animAtirandoPulando = new RoboAtirandoPulando();

		imgMorrendo = new BufferedImage[10];
		imgAtirando = new BufferedImage[9];

		indiceImagemAtual = 0;
		timer = 0;
		altura = 150;
		largura = 150;

		direcao = 0;
		ultimaDirecao = 1;
		velocidade = 3;

		morreu = false;
		atirando = false;
		carrinho = false;

		try {
			// CARREGANDO IMGS Robô morrendo
			for (int i = 0; i < 10; i++) {
				String imagem = "imagensRobo/Dead (" + (i + 1) + ").png";
				imgMorrendo[i] = ImageIO.read(new File(imagem));
			}
			// CARREGANDO IMGS Robô atirando/correndo
			for (int i = 0; i < 9; i++) {
				String imagem = "imagensRobo/RunShoot (" + (i + 1) + ").png";
				imgAtirando[i] = ImageIO.read(new File(imagem));
			}

		} catch (IOException e) {
			System.out.println("Não foi possível carregar a imagem");
			e.printStackTrace();
		}

		System.out.println("Todas as imagens foram carregadas!");
	}

	public boolean isMorreu() {
		return morreu;
	}

	public void setMorreu(boolean morreu) {
		this.morreu = morreu;
	}

	public void setAtirando(boolean atirando) {
		this.atirando = atirando;
	}

	public boolean isAtirando() {
		return atirando;
	}
	// Método responsável por atualizar variáveis de controle e administrar os estados da instância do robo
	public void atualizar() {

		if (this.posy > 1000) {
			reiniciarRobo();
		}

		timer++;

		if (!morreu) {
			// ATIRANDO
			if (atirando) {
				if (this.direcao != 0) {
					animAtirando.atualizar(this);
				} else if (this.estaNoChao == false) {
					animAtirandoPulando.atualizar(this);
				} else {
					animAtirandoParado.atualizar(this);
				}

			}

			// PULANDO
			if (!estaNoChao) {

				animPulando.atualizar(this);

			} else {

				animCorrendo.atualizar(this);
			}

			if (direcao == 1) {
				posx += velocidade * cont.getAcelHorizontal();
			} else if (direcao == -1) {
				posx -= velocidade * cont.getAcelHorizontal();
			}
		} else { // MORRENDO

			if (timer >= 4) {
				indiceImagemAtual++;
				if (indiceImagemAtual >= 10) {
					indiceImagemAtual = 9;
					reiniciarRobo();
					indiceImagemAtual = 0;
				}
				timer = 0;
			}
		}
	}
	// Método que pinta o robô com base no estado e nos indices
	public void pintar(Graphics2D g) {

		if (morreu) {
			if (ultimaDirecao == 1) {
				g.drawImage(imgMorrendo[indiceImagemAtual], posx, posy, posx + largura, posy + altura, 0, 0,
						imgMorrendo[indiceImagemAtual].getWidth(), imgMorrendo[indiceImagemAtual].getHeight(), null);
			} else if (ultimaDirecao == -1) {
				g.drawImage(imgMorrendo[indiceImagemAtual], posx, posy, posx + largura, posy + altura,
						imgMorrendo[indiceImagemAtual].getWidth(), 0, 0, imgMorrendo[indiceImagemAtual].getHeight(),
						null);
			}
		} else if (atirando && this.estaNoChao == false) {

			animAtirandoPulando.pintar(this, g);

		} else if (atirando) {
			if (this.direcao != 0) {
				animAtirando.pintar(this, g);
			} else {
				animAtirandoParado.pintar(this, g);
			}
		} else if (!estaNoChao) { // Pulando
			if (carrinho) {
				animCarrinho.pintar(this, g);
			} else {
				animPulando.pintar(this, g);
			}

		} else if (carrinho) {

			animCarrinho.pintar(this, g);

		} else { // Correndo ou parado
			animCorrendo.pintar(this, g); // this: instância atual do robô
		}
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	public void setDirecao(int dir) {
		if (dir != 0) {
			this.ultimaDirecao = dir;
			this.direcao = dir;
		} else {
			this.direcao = dir;
		}
	}
	// Método responsável pela lógica do pulo do robo
	public void iniciaPulo() {
		System.out.println("Iniciando pulo");
		if (estaNoChao == true || pulouDenovo == false) {

			if (!estaNoChao) {
				pulouDenovo = true;
			}

			estaNoChao = false;
			posy0 = posy;
			tempoPulo = 0;
			pulou = true;
			animPulando.atualizar(this);
		}
	}
	// Método responsável pela lógica do carrinho do robo
	public void iniciaCarrinho() {
		System.out.println("Iniciando carrinho");
		carrinho = true;
		animCarrinho.atualizar(this);
	}
	// Método responsável pela lógica da morte do robo
	public void morra() {
		morreu = true;
		indiceImagemAtual = 0;
	}
	// Instância de tiro com base na ultima direção do robo e regras logicas pro tiro funcionar em concordancia com o personagem
	public Tiro atira() {

		atirando = true;
		indiceTiroAtual = 0;
		Tiro tiro;
		if (this.ultimaDirecao == 1) {
			// tiro = new Tiro(posx + largura - 20, posy + 40, ultimaDirecao);
			// tiro = new Tiro(posx + 100, posy + 40, ultimaDirecao);
			tiro = new Tiro(this.posx + 150, this.posy + 40, this.ultimaDirecao);
		} else {
			// tiro = new Tiro(posx, posy + 40, ultimaDirecao);
			tiro = new Tiro(this.posx, this.posy + 40, this.ultimaDirecao);
		}

		return tiro;
	}

	public boolean podeAtirar() {

		if (atirando == false) {
			return true;
		}
		return false;

	}

	public int getLargura() {
		return largura;
	}

	public int getAltura() {
		return altura;
	}

	public int getPosx() {
		return posx;
	}

	public int getPosy() {
		return posy;
	}

	public int getDirecao() {
		return direcao;
	}

	public int getUltimaDirecao() {
		return ultimaDirecao;
	}

	public void setPosy(int posy) {
		this.posy = posy;
	}

	public void setPosx(int posx) {
		this.posx = posx;
	}

	public int getPosy0() {
		return posy0;
	}

	public void setPosy0(int posy0) {
		this.posy0 = posy0;
	}
	// Método que controla as variaveis de colisão com o chão do robo
	public void encontrouChao() {

		if (tempoPulo >= 20) {
			quantoCaiu = 0;
			estaNoChao = true;
			pulou = false;
			pulouDenovo = false;
			posy0 = posy;
			tempoPulo = 0;
		}
	}

	public boolean naoEstaNoChao() {
		return !estaNoChao;
	}

	public boolean isEstaNoChao() {
		return estaNoChao;
	}

	public void setEstaNoChao(boolean estaNoChao) {
		this.estaNoChao = estaNoChao;
	}
	// Método responsável por reiniciar o robo na tela
	public void reiniciarRobo() {

		this.setPosx(50);
		this.setPosy(660);
		this.setEstaNoChao(true);
		this.pulou = false;
		this.pulouDenovo = false;
		this.quantoCaiu = 0;
		this.morreu = false;

	}
	
	public void reiniciarRobo2() {

		this.setPosx(50);
		this.setPosy(100);
		this.setEstaNoChao(true);
		this.pulou = false;
		this.pulouDenovo = false;
		this.quantoCaiu = 0;
		this.morreu = false;
	}

	public void setCarrinho(boolean carrinho) {
		this.carrinho = carrinho;
	}

}
