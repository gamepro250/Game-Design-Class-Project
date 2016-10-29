/**
 * Author: Billy Harrison
 *
 * Date: 10/8/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.screens;

import com.badlogic.gdx.utils.viewport.StretchViewport ;
import com.badlogic.gdx.Game ;
import com.badlogic.gdx.Gdx ;
import com.badlogic.gdx.graphics.GL20 ;
import com.badlogic.gdx.graphics.Color ;
import com.badlogic.gdx.graphics.g2d.TextureAtlas ;
import com.badlogic.gdx.scenes.scene2d.Stage ;
import com.badlogic.gdx.scenes.scene2d.Actor ;
import com.badlogic.gdx.scenes.scene2d.ui.Button ;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox ;
import com.badlogic.gdx.scenes.scene2d.ui.Image ;
import com.badlogic.gdx.scenes.scene2d.ui.Label ;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle ;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox ;
import com.badlogic.gdx.scenes.scene2d.ui.Skin ;
import com.badlogic.gdx.scenes.scene2d.ui.Slider ;
import com.badlogic.gdx.scenes.scene2d.ui.Stack ;
import com.badlogic.gdx.scenes.scene2d.ui.Table ;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton ;
import com.badlogic.gdx.scenes.scene2d.ui.Window ;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener ;
import com.billyharrisongdx.game.game.Assets ;
import com.billyharrisongdx.game.util.Constants ;
import com.billyharrisongdx.game.util.CharacterSkin ;
import com.billyharrisongdx.game.util.GamePreferences ;
import com.billyharrisongdx.game.util.AudioManager ;

public class MenuScreen extends AbstractGameScreen
{
	private static final String TAG = MenuScreen.class.getName() ;

	private Stage stage ;
	private Skin skinCanyonBunny ;

	// Menu
	private Image imgBackground ;
	private Image imgLogo ;
	private Image imgInfo ;
	private Image imgCoins ;
	private Image imgBunny ;
	private Button btnMenuPlay ;
	private Button btnMenuOptions ;

	// Options
	private Window winOptions ;
	private TextButton btnWinOptSave ;
	private TextButton btnWinOptCancel ;
	private CheckBox chkSound ;
	private Slider sldSound ;
	private CheckBox chkMusic ;
	private Slider sldMusic ;
	private SelectBox<CharacterSkin> selCharSkin ;
	private Image imgCharSkin ;
	private CheckBox chkShowFpsCounter ;
	private Skin skinLibgdx ;

	// Debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f ;
	private boolean debugEnabled = false ;
	private float debugRebuildStage ;

	public MenuScreen(Game game)
	{
		super(game) ;
	}

	private void rebuildStage()
	{
		skinCanyonBunny = new Skin(Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI)) ;

		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI)) ;
		// Build all layers
		Table layerBackground = buildBackgroundLayer() ;
		Table layerObjects = buildObjectsLayer() ;
		Table layerLogos = buildLogosLayer() ;
		Table layerControls = buildControlsLayer() ;
		Table layerOptionsWindow = buildOptionsWindowLayer() ;

		// Assemble stage for menu screen
		stage.clear() ;
		Stack stack = new Stack() ;
		stage.addActor(stack) ;
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT) ;
		stack.add(layerBackground) ;
		stack.add(layerObjects) ;
		stack.add(layerLogos) ;
		stack.add(layerControls) ;
		stage.addActor(layerOptionsWindow) ;
	}

	/**
	 * Adds the background image to the window
	 */
	private Table buildBackgroundLayer()
	{
		Table layer = new Table() ;
		// + Background
		imgBackground = new Image(skinCanyonBunny, "background") ;
		layer.add(imgBackground) ;
		return layer ;
	}

	/**
	 * Adds the coin and bunny images to the window
	 */
	private Table buildObjectsLayer()
	{
		Table layer = new Table() ;
		// + Coins
		imgCoins = new Image(skinCanyonBunny, "coins") ;
		layer.addActor(imgCoins) ;
		imgCoins.setPosition(135, 80) ;
		// + Bunny
		imgBunny = new Image(skinCanyonBunny, "bunny") ;
		layer.addActor(imgBunny) ;
		imgBunny.setPosition(355, 40) ;
		return layer ;
	}

	/**
	 * Adds the logo images to the window
	 */
	private Table buildLogosLayer()
	{
		Table layer = new Table() ;
		layer.left().top() ;
		// + Game Logo
		imgLogo = new Image(skinCanyonBunny, "logo") ;
		layer.add(imgLogo) ;
		layer.row().expandY() ;
		// + Info Logos
		imgInfo = new Image(skinCanyonBunny, "info") ;
		layer.add(imgInfo).bottom() ;
		if(debugEnabled)
		{
			layer.debug() ;
		}
		return layer ;
	}

	/**
	 * Adds the play and option buttons to the window
	 */
	private Table buildControlsLayer()
	{
		Table layer = new Table() ;
		layer.right().bottom() ;
		// + Play Button
		btnMenuPlay = new Button(skinCanyonBunny, "play") ;
		layer.add(btnMenuPlay) ;
		btnMenuPlay.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				onPlayClicked() ;
			}
		}) ;
		layer.row() ;
		//+ Options Button
		btnMenuOptions = new Button(skinCanyonBunny, "options") ;
		layer.add(btnMenuOptions) ;
		btnMenuOptions.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				onOptionsClicked() ;
			}
		}) ;
		if(debugEnabled) layer.debug() ;
		return layer ;
	}

	/**
	 * Creates the window that appears when options button is clicked
	 */
	private Table buildOptionsWindowLayer()
	{
		winOptions = new Window("Options", skinLibgdx) ;
		// + Audio Settings: Sound/Music CheckBox and Volume Slider
		winOptions.add(buildOptWinAudioSettings()).row() ;
		// + Character Skin: Selection Box (White, Gray, Brown)
		winOptions.add(buildOptWinSkinSelection()).row() ;
		// + Debug: Show FPS Counter
		winOptions.add(buildOptWinDebug()).row() ;
		// + Sepatator and Buttons (Save, Cancel)
		winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 10) ;

		// Make options window slightly transparent
		winOptions.setColor(1, 1, 1, 0.8f) ;
		// Hide options window by default
		winOptions.setVisible(false) ;
		if(debugEnabled)
		{
			winOptions.debug() ;
		}
		// Let TableLayout recalculate widget sizes and positions
		winOptions.pack() ;
		// Move options window to bottom right corner
		winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50) ;
		return winOptions ;
	}

	/**
	 * Switchs to game screen
	 */
	private void onPlayClicked()
	{
		game.setScreen(new GameScreen(game)) ;
	}

	/**
	 * Activates options window
	 */
	private void onOptionsClicked()
	{
		loadSettings() ;
		btnMenuPlay.setVisible(false) ;
		btnMenuOptions.setVisible(false) ;
		winOptions.setVisible(true) ;
	}

	@Override
	public void render(float deltaTime)
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f) ;
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT) ;

		if(debugEnabled)
		{
			debugRebuildStage -= deltaTime ;
			if(debugRebuildStage <= 0)
			{
				debugRebuildStage = DEBUG_REBUILD_INTERVAL ;
				rebuildStage() ;
			}
		}
		stage.act(deltaTime) ;
		stage.draw() ;
		//stage.setDebugAll(true) ;
	}

	/**
	 * Loads options settings previously saved
	 */
	private void loadSettings()
	{
		GamePreferences prefs = GamePreferences.instance ;
		prefs.load() ;
		chkSound.setChecked(prefs.sound) ;
		sldSound.setValue(prefs.volSound) ;
		chkMusic.setChecked(prefs.music) ;
		sldMusic.setValue(prefs.volMusic) ;
		selCharSkin.setSelectedIndex(prefs.charSkin) ;
		onCharSkinSelected(prefs.charSkin) ;
		chkShowFpsCounter.setChecked(prefs.showFpsCounter) ;
	}


	/**
	 * Saves options settings to be loaded in at another time
	 */
	private void saveSettings()
	{
		GamePreferences prefs = GamePreferences.instance ;
		prefs.sound = chkSound.isChecked() ;
		prefs.volSound = sldSound.getValue() ;
		prefs.music = chkMusic.isChecked() ;
		prefs.volMusic = sldMusic.getValue() ;
		prefs.charSkin = selCharSkin.getSelectedIndex() ;
		prefs.showFpsCounter = chkShowFpsCounter.isChecked() ;
		prefs.save() ;
	}

	/**
	 * Sets the characters skin via the options menu
	 */
	private void onCharSkinSelected(int index)
	{
		CharacterSkin skin = CharacterSkin.values()[index] ;
		imgCharSkin.setColor(skin.getColor()) ;
	}

	/**
	 * Saves settings
	 */
	private void onSaveClicked()
	{
		saveSettings() ;
		onCancelClicked() ;
		AudioManager.instance.onSettingsUpdated() ;
	}

	/**
	 * Closes menu without saving
	 */
	private void onCancelClicked()
	{
		btnMenuPlay.setVisible(true) ;
		btnMenuOptions.setVisible(true) ;
		winOptions.setVisible(false) ;
		AudioManager.instance.onSettingsUpdated() ;
	}

	/**
	 * Construct audio portion of menu
	 */
	private Table buildOptWinAudioSettings()
	{
		Table tbl = new Table() ;
		// + Title: "Audio"
		tbl.pad(10, 10, 0, 10) ;
		tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE)).colspan(3) ;
		tbl.row() ;
		tbl.columnDefaults(0).padRight(10) ;
		tbl.columnDefaults(1).padRight(10) ;
		// + Checkbox, "Sound" label, sound volume slider
		chkSound = new CheckBox("", skinLibgdx) ;
		tbl.add(chkSound) ;
		tbl.add(new Label("Sound", skinLibgdx)) ;
		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx) ;
		tbl.add(sldSound) ;
		tbl.row() ;
		// + Checkbox, "Music" label, music volume slider
		chkMusic = new CheckBox("", skinLibgdx) ;
		tbl.add(chkMusic) ;
		tbl.add(new Label("Music", skinLibgdx)) ;
		sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx) ;
		tbl.add(sldMusic) ;
		tbl.row() ;
		return tbl ;
	}

	/**
	 * Construct skin selection portion of menu
	 */
	private Table buildOptWinSkinSelection()
	{
		Table tbl = new Table() ;
		// + Title: "Character Skin"
		tbl.pad(10, 10, 0, 10) ;
		tbl.add(new Label("Character Skin", skinLibgdx, "default-font", Color.ORANGE)).colspan(2) ;
		tbl.row() ;
		// + Drop down box filled with skin items
		selCharSkin = new SelectBox<CharacterSkin>(skinLibgdx) ;

		selCharSkin.setItems(CharacterSkin.values()) ;

		selCharSkin.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				onCharSkinSelected(((SelectBox<CharacterSkin>)actor).getSelectedIndex()) ;
			}
		}) ;
		tbl.add(selCharSkin).width(120).padRight(20) ;
		// + Skin preview image
		imgCharSkin = new Image(Assets.instance.character.character) ;
		tbl.add(imgCharSkin).width(50).height(50) ;
		return tbl ;
	}

	/**
	 * Construct debug portion of menu
	 */
	private Table buildOptWinDebug()
	{
		Table tbl = new Table() ;
		// + Title: "Debug"
		tbl.pad(10, 10, 0, 10) ;
		tbl.add(new Label("Debug", skinLibgdx, "default-font", Color.RED)).colspan(3) ;
		tbl.row() ;
		tbl.columnDefaults(0).padRight(10) ;
		tbl.columnDefaults(1).padRight(10) ;
		// + Checkbox, "Show FPS Counter" label
		chkShowFpsCounter = new CheckBox("", skinLibgdx) ;
		tbl.add(new Label("Show FPS Counter", skinLibgdx)) ;
		tbl.add(chkShowFpsCounter) ;
		tbl.row() ;
		return tbl ;
	}

	/**
	 * Construct buttons portion of menu
	 */
	private Table buildOptWinButtons()
	{
		Table tbl = new Table() ;
		// + Separator
		Label lbl = null ;
		lbl = new Label("", skinLibgdx) ;
		lbl.setColor(0.75f, 0.75f, 0.75f, 1) ;
		lbl.setStyle(new LabelStyle(lbl.getStyle())) ;
		lbl.getStyle().background = skinLibgdx.newDrawable("white") ;
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1) ;
		tbl.row() ;
		lbl = new Label("", skinLibgdx) ;
		lbl.setColor(0.5f, 0.5f, 0.5f, 1) ;
		lbl.setStyle(new LabelStyle(lbl.getStyle())) ;
		lbl.getStyle().background = skinLibgdx.newDrawable("white") ;
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0) ;
		tbl.row() ;
		// + Save Button with event handler
		btnWinOptSave = new TextButton("Save", skinLibgdx) ;
		tbl.add(btnWinOptSave).padRight(30) ;
		btnWinOptSave.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				onSaveClicked() ;
			}
		}) ;
		// + Cancel Button with event handler
		btnWinOptCancel = new TextButton("Cancel", skinLibgdx) ;
		tbl.add(btnWinOptCancel) ;
		btnWinOptCancel.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				onCancelClicked() ;
			}
		}) ;
		return tbl ;
	}

	@Override public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true) ;
	}

	@Override public void show()
	{
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT)) ;
		Gdx.input.setInputProcessor(stage) ;
		rebuildStage() ;
	}

	@Override public void hide()
	{
		stage.dispose() ;
		skinCanyonBunny.dispose() ;
	}

	@Override public void pause(){}
}