package com.boondog.utilities.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.boondog.imports.game.MyGame;
import com.boondog.imports.game.MyScreen;
import com.boondog.utilities.BodyBuilderApp;

public class FileChooserTable extends Table {
	MyScreen screen;
	
	Skin skin;
	ScrollPane fileListPane;
	Table fileListTable;
	TextButtonStyle fileStyle, buttonStyle;
	
	float buttonHeights;
	
	FileHandle currentDirectory, selectedFile;
	
	
	float buttonProp = 0.08f;
	
	
	public FileChooserTable(MyScreen screen, float width, float height, Skin skin) {
		super();
		this.screen = screen;
		this.skin = skin;
		setSize(width, height);
		setBackground(skin.newDrawable("square",Color.WHITE));
		
		setupStyle();
		screen.getApp();
		BodyBuilderApp.workingDir = MyGame.getPrefs().getString("lastDir",""); 
		
		if (BodyBuilderApp.workingDir.equals("")) {
			BodyBuilderApp.workingDir = Gdx.files.getLocalStoragePath();
		}
		
		currentDirectory = Gdx.files.absolute(BodyBuilderApp.workingDir);
		
		setupTopButtons();
		setupFileListTable(currentDirectory);
		setupBottomButtons();
	}

	private void setupStyle() {
		fileStyle = new TextButtonStyle();
		fileStyle.font = skin.getFont("font");
		fileStyle.fontColor = Color.DARK_GRAY;
		fileStyle.checked = skin.newDrawable("square",Color.DARK_GRAY);
		fileStyle.checkedFontColor = Color.WHITE;
		
		buttonStyle = new TextButtonStyle();
		buttonStyle.font = skin.getFont("font");
		buttonStyle.fontColor = Color.WHITE;
		buttonStyle.up = skin.newDrawable("square",Color.BLACK);

		
	}

	private void setupTopButtons() {
		TextButton button = new TextButton("Back", buttonStyle);
		button.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        		return true;
            }
			
		 	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		 		setupFileListTable(currentDirectory.parent());
	 		}
		});
		
		add(button).height(getHeight()*buttonProp);
		row();
	}


	private void setupBottomButtons() {
		TextButton button = new TextButton("Select", buttonStyle);
		button.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        		return true;
            }
			
		 	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		 		
	 		}
		});
		
		add(button).height(getHeight()*buttonProp);
		row();		
	}
	
	private void setupFileListTable(FileHandle dir) {
		if (!dir.isDirectory()) {
			System.out.println("Not a directory");
			return;
		}
		
		this.currentDirectory = dir;
		
		if (fileListTable != null) {
			fileListTable.clear();
		}  else {
			fileListTable = new Table();
			fileListPane = new ScrollPane(fileListTable);
			add(fileListPane).width(getWidth()).height(getHeight()*(1-2*buttonProp)).align(Align.left);
			row();
		}
	
		
		ButtonGroup<FileTextButton> bg = new ButtonGroup<FileTextButton>();
		bg.setMaxCheckCount(1);
		for (FileHandle fh : dir.list()) {
			FileTextButton button = makeButton(fh);
			fileListTable.add(button).align(Align.left);
			bg.add(button);
			fileListTable.row();
		}
	}
	
	private FileTextButton makeButton(FileHandle fh) {		
		return new FileTextButton(fileStyle, fh);
	}
	
	private void selectFile() {
		if (selectedFile == null) {
			return;
		}
		Texture t;
		try {
			t = new Texture(selectedFile);
		} catch (Exception e) {
			System.out.println("Not an image file");
			return;
		}
		t.dispose();
		screen.changeScreen(new BodyBuilderScreen(screen.getApp(), selectedFile));
	}
	
	
	public class FileTextButton extends TextButton {
		FileHandle fh;
		long lastClicked;
		
		public FileTextButton(TextButtonStyle style, final FileHandle fh) {
			super(fh.name(), style);
			this.fh = fh;
			
			this.addListener(new InputListener() {
	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            	selectedFile = fh;
	        		return true;
	            }
	            
	            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	            	if (TimeUtils.millis() - lastClicked < 500) {
	            		if (fh.isDirectory()) {
	            			MyGame.getPrefs().putString("lastDir",fh.path());
	            			MyGame.getPrefs().flush();
	            			BodyBuilderApp.workingDir = fh.path();
	            			setupFileListTable(fh);
	            		} else {
	            			selectFile();
	            		}
	            	}
	            	lastClicked = TimeUtils.millis();
	            }
	        });
		}
	}
}
