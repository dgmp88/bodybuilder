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
import com.boondog.imports.physics.BodyBuilderBody;
import com.boondog.utilities.BodyBuilderApp;
import com.boondog.utilities.control.BodyBuilderControls;
import com.boondog.utilities.model.Triangulator;


public class BodyBuilderScreen extends MyScreen {
	Sprite body;	
	FileHandle imageFile;
	
	Vector2 pos, size;
	
	BodyBuilderControls controller;
	Array<Vector2> vertices = new Array<Vector2>();
	ShapeRenderer renderer = new ShapeRenderer();
	
	Skin skin, fileChooserSkin;
	Table menuTable;
	
	Triangulator triangulator = new Triangulator();
	boolean triangulating = false;
	
	public BodyBuilderScreen(MyGame app, FileHandle fh) {
		super(app);
		stage.clear();
		this.imageFile = fh;
		
		System.out.println(BodyBuilderApp.workingDir + "/" + imageFile.nameWithoutExtension() + ".json");
		
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
		
		
		// Triangulate file button
		TextButtonStyle bS2 = new TextButtonStyle();
		bS2.font = assets.getFont("fonts/helvetica.ttf", 50);
		bS2.font.setScale(0.2f);
		bS2.fontColor = Color.BLACK;
		bS2.up = skin.newDrawable("square", Color.GRAY);
		bS2.checked = skin.newDrawable("square", Color.MAROON);

		final TextButton triangulate = new TextButton("Triangulate", bS2);
		triangulate.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        		return true;
            }
            
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	if (triangulate.isChecked()) {
            		triangulating = true;
            	} else {
            		triangulating = false;
            	}
            	triangulate();
            }
        });
		menuTable.add(triangulate).pad(5);
		
		stage.addActor(menuTable);
	}
	
	protected void openImageFile() {
		changeScreen(new FileChooserScreen(app));
	}


	protected void writeFile() {
		Json json = new Json();			
		String ext = ".json";
		String name = imageFile.nameWithoutExtension();
		FileHandle fh = Gdx.files.absolute(BodyBuilderApp.workingDir + "/" + name + ext);
		
		BodyBuilderBody body = new BodyBuilderBody(vertices, triangulator.getTriangulation());
		
		json.toJson(body, fh);
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
		for (int i = 0; i < vertices.size; i++) {
			Vector2 v = vertices.get(i);
			renderer.circle(v.x, v.y, 1, 50);
			if (i >  0) {
				renderer.line(vertices.get(i), vertices.get(i-1));
			}
		}
		renderer.setColor(Color.LIGHT_GRAY);

		// Draw the lines
		for (int i = 0; i < vertices.size; i++) {
			if (i >  0) {
				renderer.line(vertices.get(i), vertices.get(i-1));
			}
		}
		
		if (vertices.size>1) {
			renderer.line(vertices.get(0), vertices.get(vertices.size-1));
		}
		
		if (triangulating) {
			triangulator.draw(renderer, vertices);
		}
		
		renderer.end();
	}

	
	@Override
	public void dispose() {
		
	}
	public void addVertex(Vector2 tmp) {
		if (inBounds(tmp)) {
			vertices.add(tmp);
			triangulate();
		}
	}
	
	private void triangulate() {
		if (triangulating) {
			triangulator.makeTriangulation(vertices);
		}
	}


	private boolean inBounds(Vector2 tmp) {
		if (tmp.x < pos.x - size.x/2 || tmp.y < pos.y - size.y/2) {
			System.out.println(tmp + " "+  pos);
			return false;
		} else if (tmp.x > pos.x + size.x/2 || tmp.y > pos.y + size.y/2) {
			return false;
		}

		return true;
	}


	public void undo() {
		if (vertices.size>0) {
			vertices.pop();
		}
	}

	public Array<Vector2> getVertices() {
		return vertices;
	}


	public void update() {
		if (triangulating) {
			triangulator.makeTriangulation(vertices);
		}
	}
	
	@Override
	public void reset() {
		changeScreen(new BodyBuilderScreen(app,imageFile));
	}
}
