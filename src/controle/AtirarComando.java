package controle;

import prodjogo4.Robo.Robo;

public class AtirarComando implements ComandoInterface{
    private final Robo robo;

    public AtirarComando (Robo robo) {
        this.robo = robo;
    }

    @Override
    public void executar() {
        robo.podeAtirar();
    }
}
