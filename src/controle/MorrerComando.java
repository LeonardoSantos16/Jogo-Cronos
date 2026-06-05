package controle;

import prodjogo4.Robo.Robo;

public class MorrerComando implements ComandoInterface{
    private final Robo robo;

    public MorrerComando (Robo robo) {
        this.robo = robo;
    }

    @Override
    public void executar() {
        robo.morra();
    }
}
