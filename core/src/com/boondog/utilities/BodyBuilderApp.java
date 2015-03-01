package com.boondog.utilities;

import com.boondog.imports.game.MyGame;
import com.boondog.imports.io.Assets;
import com.boondog.utilities.screens.FileChooserScreen;

public class BodyBuilderApp extends MyGame {
	public static String workingDir;
	
	
	@Override
	public void create() {
		init();
		Assets.setBaseDir("../core/assets/");
		printViewportDims();
		setScreen(new FileChooserScreen(this));		
//		setScreen(new BodyBuilderScreen(this, Gdx.files.absolute("/Users/george/Google Drive/Mote/Graphics/Mote save 1-01.png")));
	}

	@Override
	protected void initViewport() {
		setExtendViewport(200, 160, 300, 160);		
	}

}
