package prodjogo4.Robo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RoboEscorregando implements Animacao {

	int timer, indiceAtual = 0;
	int altRobo, largRobo;

	private BufferedImage imgCarrinho[];

	public RoboEscorregando() {
		imgCarrinho = new BufferedImage[10];

		try {
			// Carregando imagens do robô pulando
			for (int i = 0; i < 10; i++) {
				String imagem = "imagensRobo/Slide (" + (i + 1) + ").png";
				imgCarrinho[i] = ImageIO.read(new File(imagem));
			}
			System.out.println("Imagens carregadas");
		} catch (IOException e) {
			System.out.println("Não consegui carregar as imagens do robô dando carrinho");
		}
	}

	@Override
	public void atualizar(Robo robo) {
		altRobo = robo.getAltura();
		
		timer++;
		
		if (timer >= 4) {
			indiceAtual++;
			if (indiceAtual >= 10) {
				indiceAtual = 0;
			}
			timer = 0;
		}

//		robo.setAltura(110);
//		robo.setLargura(170);
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

}
