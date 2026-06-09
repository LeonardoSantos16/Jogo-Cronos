package cenarios;

import java.awt.image.BufferedImage;

public class Plataforma extends Cenario{
    public Plataforma(BufferedImage imagem, int posx, int posy, int tamx, int tamy, int indice) {
        super(imagem, posx, posy, tamx, tamy, indice);
    }

    @Override
    public boolean solido() {
        return true;
    }
}
