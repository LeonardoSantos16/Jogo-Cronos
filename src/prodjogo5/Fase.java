package prodjogo5;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import prodjogo4.Tile;

public class Fase {

	ArrayList<Tile> camadaFundo;
	ArrayList<Tile> plataformas;
	ArrayList<Tile> inimigos;
	ArrayList<Tile> utilidades;
	// A classe fase tem sua maior importância no construtor, onde ela irá ler um arquivo txt com as coordenadas dos Tiles e dos inimigos e adicionara em objetos, arrays e pintará na tela
	public Fase(String nomeArquivo, ArrayList<BufferedImage> tiles) {
		
		camadaFundo = new ArrayList<Tile>();
		plataformas = new ArrayList<Tile>();
		inimigos = new ArrayList<Tile>();
		utilidades = new ArrayList<Tile>();
		
		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			String caminhoLoad = System.getProperty("user.dir");
//			JFileChooser escolherArquivo = new JFileChooser(caminhoLoad);
//			escolherArquivo.showOpenDialog(this);
			File arquivoSelecionado = new File(nomeArquivo);
			BufferedReader leitorDoArquivo = new BufferedReader(new FileReader(arquivoSelecionado));

			String linhaLida;
			while ((linhaLida = leitorDoArquivo.readLine()) != null) {
				String partes[] = linhaLida.split(" ");

				int indice = Integer.parseInt(partes[1]);
				int posx = Integer.parseInt(partes[2]);
				int posy = Integer.parseInt(partes[3]);
				int tamx = Integer.parseInt(partes[4]);
				int tamy = Integer.parseInt(partes[5]);

				if (partes[0].equals("#FUNDO")) {
					camadaFundo.add(new Tile(tiles.get(indice), posx, posy, tamx, tamy, indice));
				}

				if (partes[0].equals("#PLATAFORMA")) {
					plataformas.add(new Tile(tiles.get(indice), posx, posy, tamx, tamy, indice));
				}

				if (partes[0].equals("#INIMIGO")) {
					inimigos.add(new Tile(tiles.get(indice), posx, posy, tamx, tamy, indice));
				}
				
				if (partes[0].equals("#UTILIDADE")) {
					utilidades.add(new Tile(tiles.get(indice), posx, posy, tamx, tamy, indice));
				}

			}
			leitorDoArquivo.close();

		} catch (Exception e) {
			System.out.println("Arquivo inapropriado");
		}
		
	}
	// Responsável por pintar os tiles e inimigos
	public void pintar(Graphics g) {
		
		for (int i = 0; i < camadaFundo.size(); i++) {
//			if (camadaFundo.get(i).getPosx() < 1550)
				camadaFundo.get(i).pintar(g, 0);
		}

		for (int i = 0; i < plataformas.size(); i++) {
//			if (plataformas.get(i).getPosx() < 1550)
				plataformas.get(i).pintar(g, 0);
		}

		for (int i = 0; i < inimigos.size(); i++) {
//			if (inimigos.get(i).getPosx() < 1550)
				inimigos.get(i).pintar(g, 0);
		}

		for (int i = 0; i < utilidades.size(); i++) {
//			if (utilidades.get(i).getPosx()  < 1550)
				utilidades.get(i).pintar(g, 0);
		}
		
	}
	
	public ArrayList<Tile> getInimigos() {
		return inimigos;
	}
	
	public ArrayList<Tile> getPlataformas() {
		return plataformas;
	}
	// Método importante para identificar o tile "porta" e adicionar um controle a ela 
	public ArrayList<Tile> getPortas() {
		
		ArrayList<Tile> portas = new ArrayList<Tile>();
		
		for (int i = 0; i < utilidades.size(); i++) {
			if (utilidades.get(i).getIndice() == 17) {
				portas.add(utilidades.get(i));
			}
		}
		
		return portas;
	}
	
}
