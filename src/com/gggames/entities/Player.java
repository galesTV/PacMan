package com.gggames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gggames.main.Game;
import com.gggames.main.Sound;
import com.gggames.world.Camera;
import com.gggames.world.World;

public class Player extends Entity{

	public boolean right,left,up,down;
	
	private int frames = 0,maxFrames = 5,index = 0,maxIndex = 2;
	private boolean moved = false;
	
	private BufferedImage sprite_left[];
	private BufferedImage sprite_right[];
	
	public boolean victory = false;

	public int lastDir = 1;
	
	public static int life = 1;
	
	//public boolean sp = false;
	//public int speedTime = 0, timeCur = 60;
	
	public Player(int x, int y, int width, int height,double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		sprite_left = new BufferedImage[3];
		sprite_right = new BufferedImage[3];
		for(int i = 0; i < 3; i++) {
			sprite_left[i] = Game.spritesheet.getSprite(80+(i*16), 0, 16, 16);
		}
		for(int i = 0; i < 3; i++) {
			sprite_right[i] = Game.spritesheet.getSprite(32+(i*16), 0, 16, 16);
		}
		
	}
	
	public void tick() {
		depth = 1;
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY(),z)){
			moved = true;
			x+=speed;
			lastDir = 1;
		}	
		else if(left && World.isFree((int)(x-speed),this.getY(),z)){
			moved = true;
			x-=speed;
			lastDir = -1;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed),z)){
			moved = true;
			y-=speed;
		}	
		else if(down && World.isFree(this.getX(),(int)(y+speed),z)){
			moved = true;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
		}
		
		/*if(speedTime == timeCur) {
			speed = 1;
			speedTime = 0;
		}*/
		
		updateCamera();
		
		checkCollisionPoint();
		//checkCollisionSuperPoint();
		
		if(Game.pontos_contagem == Game.pontos_atual) {
			victory = true;
			World.restartGame();
			//System.out.println("Ganhamos o jogo!");
			return;
			
		}
	}

	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkCollisionPoint() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if(current instanceof Dot) {
				if(Entity.isColliding(this, current)) {
					Game.pontos_atual++;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}
	
	/*public void checkCollisionSuperPoint(){
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if(current instanceof SuperPoint) {
				if(Entity.isColliding(this, current)) {
					Game.pontos_atual++;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}*/
	
	public void render(Graphics g) {
		if(lastDir == 1) {
			g.drawImage(sprite_right[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else{
			g.drawImage(sprite_left[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
		}
	}
	
}
