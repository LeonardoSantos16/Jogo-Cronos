package controle;

import prodjogo4.Robo.Robo;

public class CorrerComando implements ComandoInterface{
    private final Robo robo;

    public CorrerComando (Robo robo) {
        this.robo = robo;
    }
    @Override
    public void executar() {
        robo.dispararAcao("CORRENDO");
    }
}
