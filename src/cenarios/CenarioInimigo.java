package cenarios;

import java.awt.image.BufferedImage;

public class CenarioInimigo extends Cenario{
    public CenarioInimigo(BufferedImage imagem, int posx, int posy, int tamx, int tamy, int indice) {
        super(imagem, posx, posy, tamx, tamy, indice);
    }

    @Override
    public boolean solido(){
        return true;
    }

    @Override
    public boolean causaDano(){
        return true;
    }
}
