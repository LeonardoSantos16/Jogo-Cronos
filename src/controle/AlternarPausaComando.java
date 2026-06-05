package controle;

import prodjogo4.LevelConstrutor;
import prodjogo4.Robo.Robo;

public class AlternarPausaComando implements ComandoInterface{
    private final Robo robo;
    private final LevelConstrutor editor;

    public AlternarPausaComando (Robo robo, LevelConstrutor editor){
        this.robo = robo;
        this.editor = editor;
    }

    @Override
    public void executar() {
        robo.reiniciarRobo();
        editor.setPausado();
        editor.repaint();
    }
}
