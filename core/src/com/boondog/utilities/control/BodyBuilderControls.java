package com.boondog.utilities.control;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.boondog.imports.game.MyGame;
import com.boondog.imports.math.MyVectors;
import com.boondog.utilities.screens.BodyBuilderScreen;

public class BodyBuilderControls implements InputProcessor {
	private BodyBuilderScreen screen;
	private Vector2 tmp = new Vector2();
		
	public BodyBuilderControls(BodyBuilderScreen screen) {
		this.screen = screen;
	}
	
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Keys.S:
			tmp.set(Gdx.input.getX(),Gdx.input.getY());
			screenToWorld(tmp);
			screen.addVertex(tmp.cpy());
			break;
		case Keys.R:
			screen.reset();
			break;
		case Keys.U:
			screen.undo();
			break;
		case Keys.LEFT:
			tmp.set(Gdx.input.getX(),Gdx.input.getY());
			screenToWorld(tmp);
			MyVectors.rotate(screen.getVertices(), screen.getVertices().first(), 3);
			break;
		case Keys.RIGHT:
			tmp.set(Gdx.input.getX(),Gdx.input.getY());
			screenToWorld(tmp);
			MyVectors.rotate(screen.getVertices(), screen.getVertices().first(), -3);
			break;
		}
		screen.update();
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		tmp.set(screenX,screenY);
		screenToWorld(tmp);
		screen.addVertex(tmp.cpy());
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	private Vector2 screenToWorld(Vector2 v){
		return MyGame.getViewport().unproject(v);
	}

}
