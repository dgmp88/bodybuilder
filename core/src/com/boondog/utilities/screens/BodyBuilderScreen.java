package com.boondog.utilities.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import com.boondog.imports.game.MyGame;
import com.boondog.imports.game.MyScreen;
import com.boondog.utilities.control.BodyBuilderControls;
import com.boondog.utilities.model.BodyVertices;


public class BodyBuilderScreen extends MyScreen {
	Sprite body;	
	FileHandle imageFile;
	
	Vector2 pos, size;
	
	BodyBuilderControls controller;
	private BodyVertices vertices = new BodyVertices();
	ShapeRenderer renderer = new ShapeRenderer();
	Array<Object> lastCommands = new Array<Object>();

	
	Skin skin, fileChooserSkin;
	
	Table menuTable;
	
	public BodyBuilderScreen(MyGame app, FileHandle fh) {
		super(app);
		this.imageFile = fh;
		
		System.out.println(imageFile.parent().path() + "/" + imageFile.nameWithoutExtension() + ".json");
		
		pos = new Vector2(worldWidth * 0.5f, worldHeight * 0.45f);
		
		size = new Vector2(worldWidth * 0.8f, worldHeight * 0.8f);
		
		Vector2 fit = size.cpy().scl(0.9f);
		
		body = new Sprite(new Texture(imageFile));
		Vector2 tmp = Scaling.fit.apply(body.getWidth(), body.getHeight(), fit.x,fit.y);
		
		body.setSize(tmp.x, tmp.y);
		body.setCenter(pos.x, pos.y);
		setDebugMode();

		controller = new BodyBuilderControls(this);
		inputs = new InputMultiplexer(stage);
		inputs.addProcessor(controller);

		setupSkins();
		makeMenu();
	}
		
	
	private void setupSkins() {
		skin = new Skin();
		skin.add("square", assets.getTexture("square.png"));
		
		fileChooserSkin = new Skin();
		fileChooserSkin.add("square", assets.getTexture("square.png"));
		BitmapFont font = assets.getFont("fonts/helvetica.ttf", 50);
		font.setScale(0.2f);
		font.setColor(Color.BLACK);
		fileChooserSkin.add("font",font);
	}


	private void makeMenu() {
		menuTable = new Table();
		menuTable.setSize(worldWidth, worldHeight * 0.1f);
		menuTable.setPosition(worldWidth * 0.5f, worldHeight, Align.top);
		
		TextButtonStyle bS = new TextButtonStyle();
		bS.font = assets.getFont("fonts/helvetica.ttf", 50);
		bS.font.setScale(0.2f);
		bS.fontColor = Color.BLACK;
		bS.up = skin.newDrawable("square", Color.MAROON);

		// Open file button
		TextButton open = new TextButton("Open", bS);
		open.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        		return true;
            }
            
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        		openImageFile();
            }
        });
		menuTable.add(open).pad(5);
		
		// Open file button
		TextButton write = new TextButton("Write", bS);
		write.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        		return true;
            }
            
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	writeFile();
            }
        });
		menuTable.add(write).pad(5);
		
		
		stage.addActor(menuTable);
	}
	
	protected void openImageFile() {
		stage.getRoot().removeActor(menuTable);
		
		FileChooserTable fct = new FileChooserTable(this,worldWidth * 0.5f,worldHeight, fileChooserSkin);
		fct.setPosition(worldWidth * 0.5f, worldHeight * 0.5f, Align.center);
		stage.addActor(fct);
		System.out.println("added");
		stage.setDebugAll(true);
	}


	protected void writeFile() {
		Json json = new Json();			
		String ext = ".json";
		String name = imageFile.parent().path() + "/" + imageFile.nameWithoutExtension();

		if (Gdx.files.local(name + ext).exists()) {
			int num = 1;
			while (Gdx.files.local(name + num + ext).exists()) {
				num ++;
			}
			name = name + num + ext;
		} else {
			name = name + ext;
		}
		
		FileHandle fh = Gdx.files.local(name);
		json.toJson(getVertices().getVertices(), fh);
	}
	
	
	
	@Override
	public void render(float delta) {
		clearColor();
		batch.setProjectionMatrix(MyGame.getViewport().getCamera().combined);
		renderer.setProjectionMatrix(MyGame.getViewport().getCamera().combined);
		stage.act(delta);
		stage.draw();

		batch.begin();
		body.draw(batch);
		batch.end();
		
		renderer.setAutoShapeType(true);
		renderer.begin();

		renderer.setColor(Color.BLACK);
		renderer.set(ShapeType.Line);
		renderer.rect(pos.x - size.x/2, pos.y - size.y/2, size.x, size.y);
		
		renderer.set(ShapeType.Filled);
		
		// Draw the circles
		for (int i = 0; i < getVertices().getVertices().size; i++) {
			Vector2 v = getVertices().getVertex(i);
			renderer.circle(v.x, v.y, BodyVertices.vertSize, 50);
			
			if (i >  0) {
				renderer.line(getVertices().getVertex(i), getVertices().getVertex(i-1));
			}
		}
		renderer.setColor(Color.WHITE);

		// Draw the lines
		for (int i = 0; i < getVertices().getVertices().size; i++) {
			if (i >  0) {
				renderer.line(getVertices().getVertex(i), getVertices().getVertex(i-1));
			}
		}
		
		if (getVertices().getVertices().size>0) {
			renderer.line(getVertices().getVertex(0), getVertices().getVertex(getVertices().getVertices().size-1));
		}
		
		renderer.end();
		
		
	}

	
	@Override
	public void dispose() {
		
	}
	public void addStar(Vector2 tmp) {
		getVertices().addVertex(tmp);
		lastCommands.add(tmp);
	}
	
	public void undo() {
		if (lastCommands.size>0) {
			Object o = lastCommands.pop();
			
			if (o instanceof Vector2) {
				getVertices().removeVertex((Vector2) o);
			}
						
		}
	}

	public BodyVertices getVertices() {
		return vertices;
	}


	public void setVertices(BodyVertices vertices) {
		this.vertices = vertices;
	}	
	
	
	@Override
	public void reset() {
		changeScreen(new BodyBuilderScreen(app,imageFile));
	}
}
