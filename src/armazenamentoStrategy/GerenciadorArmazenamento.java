package armazenamentoStrategy;

import cenarios.Cenario;
import prodjogo4.LevelConstrutor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArmazenamento {
    private IArmazenamento estrategia;

    public GerenciadorArmazenamento (IArmazenamento armazenamentoInical) {
        this.estrategia = armazenamentoInical;
    }

    public void setEstrategia (IArmazenamento novoArmazenamento){
        this.estrategia = novoArmazenamento;
    }

    public void executarSalvamento (List<Cenario> cenarios, String nomeArquivo) {
        estrategia.salvar(cenarios, nomeArquivo);
    }

    public List<Cenario> executarCarregamento (ArrayList<BufferedImage> imagensTiles, LevelConstrutor editor,
                                      String origemArquivo) {
        return estrategia.carregar(imagensTiles, editor, origemArquivo);
    }
}
