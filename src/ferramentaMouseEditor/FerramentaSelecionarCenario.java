package ferramentaMouseEditor;

import prodjogo4.LevelConstrutor;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class FerramentaSelecionarCenario implements FerramentaEditor {
    private final List<BufferedImage> imagensCenario;
    private final LevelConstrutor editor; // Canal de comunicação com o contexto

    public FerramentaSelecionarCenario(List<BufferedImage> imagensCenario, LevelConstrutor editor) {
        this.imagensCenario = imagensCenario;
        this.editor = editor;
    }

    @Override
    public void aoClicar(MouseEvent e, int translacao) {
        System.out.println("Mouse: " + e.getX() + ", " + e.getY());

        int x = e.getX();
        int y = e.getY();

        x -= 1608;
        x /= 106;

        y -= 35;
        y /= 75;

        int indice = y * 3 + x;

        System.out.println("Imagem: " + indice + " X: " + x + " Y: " + y);

        if (indice < imagensCenario.size() && indice >= 0) {
            editor.tileSelecionado = imagensCenario.get(indice);
            editor.indiceSelecionado = indice;
        } else {
            editor.tileSelecionado = null;
            editor.indiceSelecionado = -1;
        }
    }
}