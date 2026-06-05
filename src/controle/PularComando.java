package controle;

import prodjogo4.Robo.Robo;

public class PularComando  implements ComandoInterface{
    private final Robo robo;

    public PularComando (Robo robo){
        this.robo = robo;
    }
    @Override
    public void executar(){
        robo.iniciaPulo();
    }
}
