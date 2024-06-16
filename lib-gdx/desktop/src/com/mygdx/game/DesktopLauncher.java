package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
//		config.setResizable(false);
//		config.setDecorated(false);
//		config.setWindowedMode(1920, 1080);
		config.setTitle("Obsidian-Chronicles");
		new Lwjgl3Application(new MyGdxGame(), config);

	}
}
