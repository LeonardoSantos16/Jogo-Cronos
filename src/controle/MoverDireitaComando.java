package controle;

import prodjogo4.Robo.Robo;

public class MoverDireitaComando implements ComandoInterface{
    private final Robo robo;

    public MoverDireitaComando (Robo robo) {
        this.robo = robo;
    }

    @Override
    public void executar() {
        robo.setDirecao(1);
    }
}
