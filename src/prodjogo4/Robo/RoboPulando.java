package prodjogo4.Robo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

public class RoboPulando implements Animacao {

	private int timer;
	private int indiceAtual;

	private float aceleracao = -1;
	private float v0 = 19;

	private BufferedImage imgPulando[];

	public RoboPulando() {
		imgPulando = new BufferedImage[10];
		try {
			for (int i = 0; i < 10; i++) {
				String imagem = "imagensRobo/Jump (" + (i + 1) + ").png";
				imgPulando[i] = ImageIO.read(new File(imagem));
			}
		} catch (IOException e) {
			System.out.println("Não consegui carregar as imagens do robô pulando");
		}

		indiceAtual = 0;
		timer = 0;
	}

	@Override
	public void atualizar(Robo robo) {
		if (robo.isEstaNoChao()) {
			robo.setEstadoAtual(new RoboCorrendo());
			return;
		}

		timer++;
		if (timer >= 3) {
			if (indiceAtual < 9) {
				indiceAtual++;
			}
			timer = 0;
		}

		robo.quantoCaiu = robo.getPosy();

		if (robo.pulou) {
			robo.setPosy((int) (robo.getPosy0() - (v0 * robo.tempoPulo + (aceleracao * robo.tempoPulo * robo.tempoPulo) / 2)));
		} else {
			robo.setPosy((int) (robo.getPosy0() - (aceleracao * robo.tempoPulo * robo.tempoPulo / 2)));
		}

		robo.quantoCaiu -= robo.getPosy();
		robo.tempoPulo++;

		if (robo.getDirecao() == 1) {
			robo.setPosx(robo.getPosx() + robo.getVelocidade());
		} else if (robo.getDirecao() == -1) {
			robo.setPosx(robo.getPosx() - robo.getVelocidade());
		}
	}

	@Override
	public void pintar(Robo robo, Graphics2D g) {
		if (robo.getUltimaDirecao() == 1) {
			g.drawImage(imgPulando[indiceAtual], robo.getPosx(), robo.getPosy(), robo.getPosx() + robo.getLargura(),
					robo.getPosy() + robo.getAltura(), 0, 0, imgPulando[indiceAtual].getWidth(),
					imgPulando[indiceAtual].getHeight(), null);
		} else if (robo.getUltimaDirecao() == -1) {
			g.drawImage(imgPulando[indiceAtual], robo.getPosx(), robo.getPosy(), robo.getPosx() + robo.getLargura(),
					robo.getPosy() + robo.getAltura(), imgPulando[indiceAtual].getWidth(), 0, 0,
					imgPulando[indiceAtual].getHeight(), null);
		}
	}

	@Override
	public void mudarEstado(Robo robo, String acao) {
		if (acao.equals("CARRINHO")){
			robo.setEstadoAtual(new RoboEscorregando());
		}
		else if (acao.equals("CORRENDO")){
			robo.setEstadoAtual(new RoboCorrendo());
		}
	}
}
