package controle;

import prodjogo4.LevelConstrutor;

public class MoverCameraDireitaComando  implements ComandoInterface{
    private final LevelConstrutor editor;

    public MoverCameraDireitaComando (LevelConstrutor editor){
        this.editor = editor;
    }
    @Override
    public void executar() {
        editor.setTranslacao(25);
    }
}
