package armazenamentoStrategy;

import cenarios.Cenario;
import prodjogo4.LevelConstrutor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public interface IArmazenamento {
    public void salvar(List<Cenario> cenario, String nomeArquivo);

    public List<Cenario> carregar(ArrayList<BufferedImage> imagensTiles, LevelConstrutor editor, String origemArquivo);
}
