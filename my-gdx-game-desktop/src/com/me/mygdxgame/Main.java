package com.me.mygdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "my-gdx-game";
		cfg.useGL20 = false;

//		cfg.width = 1024;
		cfg.width = 1920;
//		cfg.height = 768;
		cfg.height = 1080;
		cfg.vSyncEnabled = true;
		cfg.useCPUSynch = false;			
//		cfg.fullscreen = true;
		
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
