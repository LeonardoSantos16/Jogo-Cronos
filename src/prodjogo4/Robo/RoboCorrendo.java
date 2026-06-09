package prodjogo4.Robo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RoboCorrendo implements Animacao {

	private BufferedImage imgParado[];
	private BufferedImage imgCorrendo[];

	private int timer;
	private int indiceAtual;
	private int indiceIdle;

	public RoboCorrendo() {
		imgParado = new BufferedImage[10];
		imgCorrendo = new BufferedImage[8];

		try {
			for (int i = 0; i < 10; i++) {
				String imagem = "imagensRobo/Idle (" + (i + 1) + ").png";
				imgParado[i] = ImageIO.read(new File(imagem));
			}

			for (int i = 0; i < 8; i++) {
				String imagem = "imagensRobo/Run (" + (i + 1) + ").png";
				imgCorrendo[i] = ImageIO.read(new File(imagem));
			}
		} catch (IOException e) {
			System.out.println("Não foi possível carregar as imagens do robô correndo");
		}

		indiceAtual = 0;
		indiceIdle = 0;
		timer = 0;
	}

	@Override
	public void atualizar(Robo robo) {
		if (!robo.isEstaNoChao()) {
			robo.setEstadoAtual(new RoboPulando());
			return;
		}

		if (robo.getDirecao() == 1) {
			robo.setPosx(robo.getPosx() + robo.getVelocidade());
		} else if (robo.getDirecao() == -1) {
			robo.setPosx(robo.getPosx() - robo.getVelocidade());
		}

		timer++;
		if (timer >= 4) {
			indiceAtual++;
			indiceIdle++;
			if (indiceAtual >= 8) {
				indiceAtual = 0;
			}
			if (indiceIdle >= 10) {
				indiceIdle = 0;
			}
			timer = 0;
		}
	}

	@Override
	public void pintar(Robo robo, Graphics2D g) {
		if (robo.getUltimaDirecao() == 1) {
			if (robo.getDirecao() == 0) {
				g.drawImage(imgParado[indiceIdle], robo.getPosx(), robo.getPosy(), robo.getPosx() + robo.getLargura(), robo.getPosy() + robo.getAltura(), 0, 0,
						imgParado[indiceIdle].getWidth(), imgParado[indiceIdle].getHeight(), null);
			} else {
				g.drawImage(imgCorrendo[indiceAtual], robo.getPosx(), robo.getPosy(), robo.getPosx() + robo.getLargura(), robo.getPosy() + robo.getAltura(), 0, 0,
						imgCorrendo[indiceAtual].getWidth(), imgCorrendo[indiceAtual].getHeight(), null);
			}

		} else if (robo.getUltimaDirecao() == -1) {
			if (robo.getDirecao() == 0) {
				g.drawImage(imgParado[indiceIdle], robo.getPosx(), robo.getPosy(), robo.getPosx() + robo.getLargura(), robo.getPosy() + robo.getAltura(),
						imgParado[indiceIdle].getWidth(), 0, 0, imgParado[indiceIdle].getHeight(), null);
			} else {
				g.drawImage(imgCorrendo[indiceAtual], robo.getPosx(), robo.getPosy(), robo.getPosx() + robo.getLargura(), robo.getPosy() + robo.getAltura(),
						imgCorrendo[indiceAtual].getWidth(), 0, 0, imgCorrendo[indiceAtual].getHeight(), null);
			}
		}
	}

	@Override
	public void mudarEstado(Robo robo, String acao) {
		if (acao.equals("CARRINHO")) {
			robo.setEstadoAtual(new RoboEscorregando());
		}
		else if (acao.equals("PULAR")) {
			robo.iniciaPulo();
			robo.setEstadoAtual(new RoboPulando());
		}
	}
}
