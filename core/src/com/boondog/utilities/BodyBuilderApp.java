package com.boondog.utilities;

import com.badlogic.gdx.Gdx;
import com.boondog.imports.game.MyGame;
import com.boondog.imports.io.Assets;
import com.boondog.utilities.screens.BodyBuilderScreen;
import com.boondog.utilities.screens.FileChooserScreen;

public class BodyBuilderApp extends MyGame {
	@Override
	public void create() {
		init();
		Assets.setBaseDir("../core/assets/");
		printViewportDims();
		//setScreen(new FileChooserScreen(this));		
		setScreen(new BodyBuilderScreen(this, Gdx.files.local("../core/assets/rocket.png")));
	}

	@Override
	protected void initViewport() {
		setExtendViewport(200, 160, 300, 160);		
	}

}
