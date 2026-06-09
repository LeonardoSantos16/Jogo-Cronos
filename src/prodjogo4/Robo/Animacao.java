package prodjogo4.Robo;

import java.awt.Graphics2D;

interface Animacao {

	public void atualizar(Robo robo);
	
	public void pintar(Robo robo, Graphics2D g);

	public void mudarEstado(Robo robo, String acao);
}
