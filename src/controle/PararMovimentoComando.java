package controle;

import prodjogo4.Robo.Robo;

public class PararMovimentoComando implements ComandoInterface{
    private final Robo robo;

    public PararMovimentoComando (Robo robo) {
        this.robo = robo;
    }

    @Override
    public void executar() {
        robo.setDirecao(0);
    }
}
