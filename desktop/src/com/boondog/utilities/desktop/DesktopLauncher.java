package com.boondog.utilities.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.boondog.utilities.BodyBuilderApp;

public class DesktopLauncher {
	static boolean repack = true;
	
	public static void main (String[] arg) {
		
		if (repack) {
			packTextures();
		}
		
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.samples = 8;

		//// This is iPad retina resolution, only half the size.
		config.width = 2048/2;
		config.height = 1536/2;
	
		new LwjglApplication(new BodyBuilderApp(), config);
	}
	
	

	private static void packTextures() {
		Settings settings = new Settings();
		settings.maxWidth = 1024;
		settings.maxHeight = 1024;
		settings.paddingX = 10;
		settings.paddingY = 10;
		settings.edgePadding = false;
        settings.filterMin = TextureFilter.MipMapLinearNearest;
        settings.filterMag = TextureFilter.Nearest;
        
  		TexturePacker.process(settings, "../assets_general/menu", "../core/assets/atlas", "menu");								
	}
}
