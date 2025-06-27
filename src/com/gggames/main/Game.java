package com.gggames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import com.gggames.entities.Entity;
import com.gggames.entities.Player;
import com.gggames.graficos.Spritesheet;
import com.gggames.graficos.UI;
import com.gggames.world.World;

public class Game extends Canvas implements Runnable,KeyListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 500;
	public static final int SCALE = 1;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	
	public UI ui;
	
	public static int pontos_atual = 0;
	public static int pontos_contagem = 0;
	
	public Game() {
		Sound.music.loop();
		addKeyListener(this);
		//setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		
		//Inicializando Objetos.
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0,0,16,16,1.2,spritesheet.getSprite(32,0,16,16));
		entities = new ArrayList<Entity>();
		world = new World("/level1.png");
		ui = new UI();
		
		entities.add(player);
		
	}
	
	public void initFrame() {
		frame = new JFrame("Pac-Man");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		isRunning = true;
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();			
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		/*Renderização do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		/***/
		world.render(g);
		Collections.sort(entities,Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
	
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		//Aqui renderizamos o jogo!
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		ui.render(g);
		bs.show();
		
		if(player.victory == true) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,16));
			g.drawString("> Parabéns! Você venceu! Pressione R para reiniciar o jogo. <", WIDTH*SCALE, HEIGHT*SCALE);
		}
	}
	
	public void run() {
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
		}
		
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_R) {
			World.restartGame();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
			e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || 
			e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
				player.right = false;
			}else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
					e.getKeyCode() == KeyEvent.VK_A) {
				player.left = false;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_UP || 
				e.getKeyCode() == KeyEvent.VK_W) {
				player.up = false;
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
					e.getKeyCode() == KeyEvent.VK_S) {
				player.down = false;
			}
	}
}

