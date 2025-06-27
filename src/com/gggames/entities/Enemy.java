package com.gggames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.gggames.main.Game;
import com.gggames.main.Sound;
import com.gggames.world.AStar;
import com.gggames.world.Vector2i;
import com.gggames.world.World;


public class Enemy extends Entity{
	
	public static boolean ghostMode = false;
	public int ghostFrames = 0;
	public int nextTime = Entity.rand.nextInt(60*5 - 60*3) + 60*3;
	
	private int maskx,masky,mwidth,mheight;
	

	public Enemy(int x, int y, int width, int height, int speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}

	public void tick() {
		depth = 0;
		if(ghostMode == false) {
			if(path == null || path.size() == 0) {
				Vector2i start = new Vector2i(((int)(x/16)),((int)(y/16)));
				Vector2i end = new Vector2i(((int)(Game.player.x/16)),((int)(Game.player.y/16)));
				path = AStar.findPath(Game.world, start, end);
			}
			
			if(new Random().nextInt(100) < 80)
				followPath(path);
			if(x % 16 == 0 && y % 16 == 0) {
				if(new Random().nextInt(100) < 10) {
					Vector2i start = new Vector2i((int)(x/16),(int)(y/16));
					Vector2i end = new Vector2i((int)(Game.player.x/16),(int)(Game.player.y/16));
					path = AStar.findPath(Game.world, start, end);
				}
			}
		}
			
		if(collidingPlayer()) {
			Player.life--;
			if(Player.life == 0) {
				Sound.deathEffect.play();
				World.restartGame();
			}
		}
		
			ghostFrames++;
			if(ghostFrames == nextTime) {
				nextTime = Entity.rand.nextInt(60*5 - 60*3) + 60*3;
				ghostFrames = 0;
				if(ghostMode == false) {
					System.out.println("Está no modo fantasma");
					ghostMode = true;
				}else {
					ghostMode = false;
				}
			}
	}
	
	public boolean collidingPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, mwidth, mheight);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
		
		return enemyCurrent.intersects(player);
	}
	
	public void render(Graphics g) {
		super.render(g);
	}
	
}
