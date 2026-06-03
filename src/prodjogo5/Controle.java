package prodjogo5;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controle implements KeyListener {

	private boolean movDireita = false;
	private boolean movEsquerda = false;
	private boolean pulo = false;
	private boolean carrinho = false;
	private boolean tiro = false;
	private static boolean entrar = false;
	static int acelHorizontal = 1;
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	// Método responsável por "ouvir" quando uma tecla é pressionada e executar uma ação
	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_D) {
			movDireita = true;
		} 
		
		if (e.getKeyCode() == KeyEvent.VK_A) {
			movEsquerda = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_W) {
			pulo = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			acelHorizontal = 2;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_S) {
			entrar = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_B) {
			carrinho = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			tiro = true;
		}
		
	}
	// Método responsável por "ouvir" quando uma tecla é solta e executar uma ação
	@Override
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_D) {
			movDireita = false;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_A) {
			movEsquerda = false;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_W) {
			pulo = false;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			acelHorizontal = 1;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_S) {
			entrar = false;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_B) {
			carrinho = false;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			tiro = false;
		}

	}

	public int getAcelHorizontal() {
		return acelHorizontal;
	}

	public boolean isMovDireita() {
		return movDireita;
	}

	public boolean isMovEsquerda() {
		return movEsquerda;
	}

	public boolean isPulo() {
		if (pulo == true) {
			pulo = false;
			return true;
		}
		return pulo;
	}

	public boolean getEntrar() {
		return entrar;
	}

	public boolean isCarrinho() {
		return carrinho;
	}

	public boolean isTiro() {
		return tiro;
	}

	public void setTiro(boolean tiro) {
		this.tiro = tiro;
	}
	
	
}
