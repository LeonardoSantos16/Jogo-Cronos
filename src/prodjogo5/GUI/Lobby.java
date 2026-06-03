package prodjogo5.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import prodjogo5.Janela5;

public class Lobby extends JPanel implements MouseListener, Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton btnNovoJogo = new JButton("Novo Jogo");
	private JButton btnCarregar = new JButton("Carregar");
	private JButton btnExit = new JButton("Exit");
	
	private BufferedImage imgRobo[];
	
	private int timer, indice = 0;
	// Construtor do jpanel lobby recebe um Jframe para manipulação e uma instância da classe janela
	public Lobby(JFrame janela, Janela5 j5) {
		
		imgRobo = new BufferedImage[10];
		
		this.setLayout(null);
		
		try {
			for (int i = 0; i < 10; i++) {
				String imagem = "imagensRobo/Idle (" + (i + 1) + ").png";
				imgRobo[i] = ImageIO.read(new File(imagem));
			}
		} catch (Exception e) {
			System.out.println();
		}
		
		this.add(btnNovoJogo);
		btnNovoJogo.setBounds(450, 320, 100, 30);
		btnNovoJogo.addActionListener(action -> {
			j5.carregar();
		});
		
		this.add(btnCarregar);
		btnCarregar.setBounds(450, 370, 100, 30);
		btnCarregar.addActionListener(action -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				String caminhoLoad = System.getProperty("user.dir");
				JFileChooser escolherArquivo = new JFileChooser(caminhoLoad + "/saves");
				escolherArquivo.showOpenDialog(this);
				File arquivoSelecionado = escolherArquivo.getSelectedFile();
				BufferedReader leitorDoArquivo = new BufferedReader(new FileReader(arquivoSelecionado));
			
				String linha = leitorDoArquivo.readLine();
				String cache[] = linha.split(" ");
				
				for (int i = 0; i < cache.length; i++) {
					System.out.println(cache[i]);
				}
				
				j5.setCache(true, cache);
				j5.carregar();
			} catch (Exception e) {
				System.out.println(e);
				System.out.println("Arquivo inapropriado");
			}
			
		});
		
		btnExit.setBounds(450, 420, 100, 30);
		btnExit.addActionListener(action -> {
			System.exit(0);
		});
		this.add(btnExit);
		
		
		Thread t = new Thread(this);
		t.start();
	}
	// Este método é, assim como em outras classes, responsável por atualizar a tela e tornar a 
	//classe "runnable"
	@Override
	public void run() {
        
        while (true) {
            
            atualiza();
            
            repaint();
           
            dorme();
        }
    }
	// Método atualiza é responsável por atualizar variáveis de controle como timer e indice
	// (funciona de mesma forma em outras classes porém com outras variaveis)
	private void atualiza() {

		timer++;
		if (timer >= 4) {
			
			indice++;
			if (indice == 10)
				indice = 0;
			
			timer = 0;
		}

	}
	// Responsável por reduzir o ritmo de atualização da Thread
	private void dorme() {
        
        try { 
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	
	// Método responsável por pintar imagens na tela, nesse caso é o gif do robo no lobby
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		
		Graphics2D g2d = (Graphics2D) g.create();
		
		g.setColor(Color.gray);
		g.fillRect(0, 0, 1000, 700);
		
		pintar(g2d);

	}
	// Método que atualiza o indice do gif e utiliza o componente g2d para pintar no paint component
	public void pintar(Graphics2D g2d) {
		g2d.drawImage(imgRobo[indice], 430, 120, 430 + 150, 120 + 150, 0, 0,
				imgRobo[indice].getWidth(), imgRobo[indice].getHeight(), null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
