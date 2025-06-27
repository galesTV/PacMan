package com.gggames.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	private AudioClip clip;
	
	public static final Sound music = new Sound("/music.wav");
	public static final Sound deathEffect = new Sound("/explosion.wav");
	public static final Sound wakawakaSound = new Sound("/wakawaka.wav");
	
	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		}catch(Throwable e){}
	}
	
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
				
			}.start();
		}catch(Throwable e) {}
	}
	
	public void loop() {
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
				
			}.start();
		}catch(Throwable e) {}
	}
	
}
