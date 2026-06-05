package controle;

import prodjogo4.LevelConstrutor;

public class MoverCameraEsquerdaComando implements ComandoInterface{
    private final LevelConstrutor editor;

    public MoverCameraEsquerdaComando (LevelConstrutor editor) {
        this.editor = editor;
    }
    @Override
    public void executar() {
        editor.setTranslacao(-25);
    }
}
