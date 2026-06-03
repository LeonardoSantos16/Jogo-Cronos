package prodjogo4.Robo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RoboPulando implements Animacao {

	int timer;
	int indiceAtual;
	
	float aceleracao = -1;
	float v0 = 19;

	private BufferedImage imgPulando[];

	public RoboPulando() {

		imgPulando = new BufferedImage[10];

		try {
			// Carregando imagens do robô pulando
			for (int i = 0; i < 10; i++) {
				String imagem = "imagensRobo/Jump (" + (i + 1) + ").png";
				imgPulando[i] = ImageIO.read(new File(imagem));
			}
		} catch (IOException e) {
			System.out.println("Não consegui carregar as imagens do robô pulando");
		}
	}

	// Física de lançamento de objetos
	// y = y0 + v0t - (at^2)/2
	
	@Override
	public void atualizar(Robo robo) {

		indiceAtual = 3;
		
		robo.quantoCaiu = robo.getPosy();
		
		if (robo.pulou) { // Queda pulo (calculo)
			robo.setPosy((int) (robo.getPosy0() - (v0 * robo.tempoPulo + (aceleracao * robo.tempoPulo * robo.tempoPulo) / 2)));
		} else { // Queda sem pular (calculo)
			robo.setPosy((int) (robo.getPosy0() - (aceleracao * robo.tempoPulo * robo.tempoPulo / 2)));
		}
		
		robo.quantoCaiu -= robo.getPosy();
		
		robo.tempoPulo++;
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

}
