package com.gggames.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.gggames.main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,18));
		g.drawString("Pontos: "+(Game.pontos_atual+"/"+Game.pontos_contagem), 30, 30);
	}
	
}
