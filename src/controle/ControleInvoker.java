package controle;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class ControleInvoker implements KeyListener {
    private final Map<Integer, ComandoInterface> comandosPressionados = new HashMap<>();
    private final Map<Integer, ComandoInterface> comandosSoltos = new HashMap<>();
    private final Queue<ComandoInterface> filaComandos = new LinkedList<>();

    public void mapearTeclaPressionada(int keyCode, ComandoInterface comando){
        comandosPressionados.put(keyCode, comando);
    }

    public void mapearTeclaSolta(int keyCode, ComandoInterface comando){
        comandosSoltos.put(keyCode, comando);
    }

    public Queue<ComandoInterface> getFilaComandos(){
        return filaComandos;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        ComandoInterface comando = comandosPressionados.get(e.getKeyCode());
        if (comando != null){
            filaComandos.add(comando);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        ComandoInterface comando = comandosSoltos.get((e.getKeyCode()));
        if (comando != null){
            filaComandos.add(comando);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
