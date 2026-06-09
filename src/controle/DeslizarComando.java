package controle;

import prodjogo4.Robo.Robo;

public class DeslizarComando implements ComandoInterface{
    private final Robo robo;

    public DeslizarComando (Robo robo){
        this.robo = robo;
    }
    @Override
    public void executar() {
        robo.dispararAcao("CARRINHO");
    }
}
