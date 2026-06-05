package ferramentaMouseEditor;

import cenarios.*;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class FerramentaPincel implements FerramentaEditor{
    private final int indiceSelecionado;
    private final int maiorIndiceFundo;
    private final int maiorIndiceInimigos;
    private final int maiorIndicePlataforma;
    private final int maiorIndiceUtilidades;
    private final BufferedImage imagemCenario;
    private final List<Cenario> cenarios;

    public FerramentaPincel (int indiceSelecionado, int maiorIndiceFundo, int maiorIndiceInimigos, int maiorIndicePlataforma,
                             BufferedImage imagemCenario, int maiorIndiceUtilidades, List<Cenario> cenarios){
        this.indiceSelecionado = indiceSelecionado;
        this.maiorIndiceFundo = maiorIndiceFundo;
        this.maiorIndiceInimigos = maiorIndiceInimigos;
        this.maiorIndicePlataforma = maiorIndicePlataforma;
        this.maiorIndiceUtilidades = maiorIndiceUtilidades;
        this.imagemCenario = imagemCenario;
        this.cenarios = cenarios;
    }

    @Override
    public void aoClicar(MouseEvent e, int translacao) {
        int x = e.getX() - 10;
        int y = e.getY() - 30;

        x = x - x % 25;
        y = y - y % 25;

        if (imagemCenario != null) {
            Cenario novoTile = null;
            if (indiceSelecionado < maiorIndiceFundo) {
                novoTile = new CenarioFundo(imagemCenario, x + translacao, y, 75, 75, indiceSelecionado);
            } else if (indiceSelecionado < maiorIndicePlataforma) {
                novoTile = new Plataforma(imagemCenario, x + translacao, y, 75, 75, indiceSelecionado);
            } else if (indiceSelecionado < maiorIndiceInimigos) {
                int tamanho = indiceSelecionado == 14 ? 150 : 75;
                novoTile = new CenarioInimigo(imagemCenario, x + translacao, y, tamanho, tamanho, indiceSelecionado);
            } else if (indiceSelecionado < maiorIndiceUtilidades) {
                int tamanhoY = (indiceSelecionado == 16 || indiceSelecionado == 17) ? 150 : 75;
                int tamanhoX = (indiceSelecionado == 16 || indiceSelecionado == 17) ? 100 : 75;
                novoTile = new CenarioUtilidades(imagemCenario, x + translacao, y, tamanhoX, tamanhoY, indiceSelecionado);
            }

            if (novoTile != null) {
                cenarios.add(novoTile);
            }
        }
    }
}
