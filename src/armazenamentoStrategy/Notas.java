package armazenamentoStrategy;

import cenarios.*;
import prodjogo4.LevelConstrutor;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Notas implements IArmazenamento{
    @Override
    public void salvar(List<Cenario> cenarios, String nomeArquivo) {
        try {
            File arquivo = new File(nomeArquivo + ".txt");
            FileWriter escreveArquivo = new FileWriter(arquivo);

            for (int i = 0; i < cenarios.size(); i++) {
                Cenario cenario = cenarios.get(i);
                if (cenario instanceof CenarioFundo) {
                    escreveArquivo.write("#FUNDO " + cenario.toString() + "\n");
                } else if (cenario instanceof Plataforma) {
                    escreveArquivo.write("#PLATAFORMA " + cenario.toString() + "\n");
                } else if (cenario instanceof CenarioInimigo) {
                    escreveArquivo.write("#INIMIGO " + cenario.toString() + "\n");
                } else if (cenario instanceof CenarioUtilidades) {
                    escreveArquivo.write("#UTILIDADE " + cenario.toString() + "\n");
                }
            }

            escreveArquivo.flush();
            escreveArquivo.close();

        } catch (Exception e) {
            System.out.println("Não foi possível trabalhar com o arquivo");
        }
    };


    @Override
    public List<Cenario> carregar(ArrayList<BufferedImage> imagensTiles, LevelConstrutor editor, String origemArquivo) {
        List<Cenario> cenarios = new ArrayList<>();

        try {
            File arquivoSelecionado = new File(origemArquivo);
            BufferedReader leitorDoArquivo = new BufferedReader(new FileReader(arquivoSelecionado));

            String linhaLida;
            while ((linhaLida = leitorDoArquivo.readLine()) != null) {
                String[] partes = linhaLida.split(" ");

                int indice = Integer.parseInt(partes[1]);
                int posx = Integer.parseInt(partes[2]);
                int posy = Integer.parseInt(partes[3]);
                int tamx = Integer.parseInt(partes[4]);
                int tamy = Integer.parseInt(partes[5]);

                if (partes[0].equals("#FUNDO")) {
                    cenarios.add(new CenarioFundo(imagensTiles.get(indice), posx, posy, tamx, tamy, indice));
                }

                if (partes[0].equals("#PLATAFORMA")) {
                    cenarios.add(new Plataforma(imagensTiles.get(indice), posx, posy, tamx, tamy, indice));
                }

                if (partes[0].equals("#INIMIGO")) {
                    cenarios.add(new CenarioInimigo(imagensTiles.get(indice), posx, posy, tamx, tamy, indice));
                }

                if (partes[0].equals("#UTILIDADE")) {
                    cenarios.add(new CenarioUtilidades(imagensTiles.get(indice), posx, posy, tamx, tamy, indice));
                }

            }
            leitorDoArquivo.close();

        } catch (Exception e) {
            System.out.println("Arquivo inapropriado");
        }
        return cenarios;
    }
}
