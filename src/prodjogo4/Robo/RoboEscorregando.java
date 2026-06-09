package prodjogo4.Robo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RoboEscorregando implements Animacao {

	private int timer;
	private int indiceAtual;

	private BufferedImage imgCarrinho[];

	public RoboEscorregando() {
		imgCarrinho = new BufferedImage[10];

		try {
			for (int i = 0; i < 10; i++) {
				String imagem = "imagensRobo/Slide (" + (i + 1) + ").png";
				imgCarrinho[i] = ImageIO.read(new File(imagem));
			}
		} catch (IOException e) {
			System.out.println("Não consegui carregar as imagens do robô dando carrinho");
		}

		indiceAtual = 0;
		timer = 0;
	}

	@Override
	public void atualizar(Robo robo) {
		if (!robo.isEstaNoChao()) {
			robo.setEstadoAtual(new RoboPulando());
			return;
		}

		if (robo.getUltimaDirecao() == 1) {
			robo.setPosx(robo.getPosx() + (robo.getVelocidade() + 2));
		} else if (robo.getUltimaDirecao() == -1) {
			robo.setPosx(robo.getPosx() - (robo.getVelocidade() + 2));
		}

		timer++;
		if (timer >= 4) {
			indiceAtual++;

			if (indiceAtual >= 10) {
				robo.setEstadoAtual(new RoboCorrendo());
				return;
			}
			timer = 0;
		}
	}

	@Override
	public void pintar(Robo robo, Graphics2D g) {
		if (robo.getUltimaDirecao() == 1) {
			g.drawImage(imgCarrinho[indiceAtual], robo.getPosx(), robo.getPosy(), robo.getPosx() + robo.getLargura(),
					robo.getPosy() + robo.getAltura(), 0, 0, imgCarrinho[indiceAtual].getWidth(),
					imgCarrinho[indiceAtual].getHeight(), null);
		} else if (robo.getUltimaDirecao() == -1) {
			g.drawImage(imgCarrinho[indiceAtual], robo.getPosx(), robo.getPosy(), robo.getPosx() + robo.getLargura(),
					robo.getPosy() + robo.getAltura(), imgCarrinho[indiceAtual].getWidth(), 0, 0,
					imgCarrinho[indiceAtual].getHeight(), null);
		}
	}

	@Override
	public void mudarEstado(Robo robo, String acao) {
		if (acao.equals("PULAR")) {
			robo.iniciaPulo();
			robo.setEstadoAtual(new RoboPulando());
		}
	}
}
