package prodjogo4.Robo;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RoboMorrendo implements Animacao {

    private int tick = 0;
    private int frame = 0;

    @Override
    public void atualizar(Robo robo) {
        tick++;
        if (tick >= 4) {
            frame++;
            if (frame >= 10) {
                frame = 9;
                robo.reiniciarRobo();
                robo.setEstadoAtual(new RoboCorrendo());
                return;
            }
            tick = 0;
        }
        robo.setIndiceImagemAtual(frame);
    }

    @Override
    public void pintar(Robo robo, Graphics2D g) {
        BufferedImage[] imgMorrendo = robo.getImgMorrendo();
        BufferedImage spriteAtual = imgMorrendo[frame];

        if (robo.getUltimaDirecao() == 1) {
            g.drawImage(spriteAtual, robo.getPosx(), robo.getPosy(),
                    robo.getPosx() + robo.getLargura(), robo.getPosy() + robo.getAltura(),
                    0, 0, spriteAtual.getWidth(), spriteAtual.getHeight(), null);
        } else if (robo.getUltimaDirecao() == -1) {
            g.drawImage(spriteAtual, robo.getPosx(), robo.getPosy(),
                    robo.getPosx() + robo.getLargura(), robo.getPosy() + robo.getAltura(),
                    spriteAtual.getWidth(), 0, 0, spriteAtual.getHeight(), null);
        }
    }

    @Override
    public void mudarEstado(Robo robo, String acao) {
    }
}
