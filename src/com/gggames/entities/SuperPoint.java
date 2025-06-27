package com.gggames.entities;

import java.awt.image.BufferedImage;

public class SuperPoint extends Entity{

	public SuperPoint(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		depth = 0;
	}

}
