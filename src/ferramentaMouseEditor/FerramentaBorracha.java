package ferramentaMouseEditor;

import cenarios.Cenario;

import java.awt.event.MouseEvent;
import java.util.List;

public class FerramentaBorracha implements FerramentaEditor {
    private final List<Cenario> cenarios;

    public FerramentaBorracha (List<Cenario> cenarios) {
        this.cenarios = cenarios;
    }

    @Override
    public void aoClicar(MouseEvent e, int translacao) {
        int x = e.getX() - 10;
        int y = e.getY() - 30;

        x = x - x % 25;
        y = y - y % 25;

        for (int i = cenarios.size() - 1; i >= 0; i--) {
            Cenario cenario = cenarios.get(i);
            if (cenario.getPosx() - translacao <= x && cenario.getPosx() + cenario.getTamx() - translacao > x
                    && cenario.getPosy() <= y && cenario.getPosy() + cenario.getTamy() > y) {
                cenarios.remove(i);
            }
        }
    }
}
