package com.boondog.utilities.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.boondog.imports.game.MyGame;
import com.boondog.imports.game.MyScreen;


public class FileChooserScreen extends MyScreen {
	Skin skin, fileChooserSkin;
	
	public FileChooserScreen(MyGame app) {
		super(app);
		stage.clear();
		inputs = new InputMultiplexer(stage);
		setDebugMode();

		setupSkins();
		
		openImageFile();
	}
		
	
	private void setupSkins() {
		skin = new Skin();
		skin.add("square", assets.getTexture("square.png"));
		
		fileChooserSkin = new Skin();
		fileChooserSkin.add("square", assets.getTexture("square.png"));
		BitmapFont font = assets.getFont("fonts/helvetica.ttf", 50);
		font.setScale(0.15f);
		font.setColor(Color.BLACK);
		fileChooserSkin.add("font",font);
	}


	protected void openImageFile() {
		FileChooserTable fct = new FileChooserTable(this,worldWidth * 0.5f,worldHeight, fileChooserSkin);
		fct.setPosition(worldWidth * 0.5f, worldHeight * 0.5f, Align.center);
		stage.addActor(fct);
	}
	
	
	@Override
	public void render(float delta) {
		clearColor();
		batch.setProjectionMatrix(MyGame.getViewport().getCamera().combined);		
		stage.act(delta);
		stage.draw();
	}

	
	@Override
	public void dispose() {
		
	}
	
	@Override
	public void reset() {
		changeScreen(new FileChooserScreen(app));
	}
}
