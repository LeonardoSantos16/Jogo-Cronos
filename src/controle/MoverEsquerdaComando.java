package controle;

import prodjogo4.Robo.Robo;

public class MoverEsquerdaComando implements ComandoInterface{
    private final Robo robo;

    public MoverEsquerdaComando (Robo robo) {
        this.robo = robo;
    }

    @Override
    public void executar(){
        robo.setDirecao(-1);
    }
}
