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
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.* ;
import com.badlogic.gdx.math.Interpolation ;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction ;
import com.badlogic.gdx.scenes.scene2d.Touchable ;

public class MenuScreen extends AbstractGameScreen
{
	private static final String TAG = MenuScreen.class.getName() ;

	private Stage stage ;
	private Skin skinMygame ;

	// Menu
	private Image imgBackground ;
	private Image imgIce ;
	private Image imgTitle ;
	private Image imgChar ;
	private Image imgPlatform ;
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
		skinMygame = new Skin(Gdx.files.internal(Constants.SKIN_MYGAME_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI)) ;

		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI)) ;
		// Build all layers
		Table layerBackground = buildBackgroundLayer() ;
		Table layerObjects = buildObjectsLayer() ;
		Table layerControls = buildControlsLayer() ;
		Table layerOptionsWindow = buildOptionsWindowLayer() ;

		// Assemble stage for menu screen
		stage.clear() ;
		Stack stack = new Stack() ;
		stage.addActor(stack) ;
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT) ;
		stack.add(layerBackground) ;
		stack.add(layerObjects) ;
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
		imgBackground = new Image(skinMygame, "Volcano") ;
		layer.add(imgBackground) ;
		return layer ;
	}

	/**
	 * Adds the coin and bunny images to the window
	 */
	private Table buildObjectsLayer()
	{
		Table layer = new Table() ;

		imgTitle = new Image(skinMygame, "Title") ;
		layer.addActor(imgTitle) ;
		imgTitle.setOrigin(imgTitle.getWidth() / 2, imgTitle.getHeight() / 2) ;
		imgTitle.addAction(sequence(

				parallel(moveBy(75, 300, 0.5f, Interpolation.swingOut),
					scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
					alpha(1.0f, 0.5f)))) ;

		imgPlatform = new Image(skinMygame, "Platform") ;
		layer.addActor(imgPlatform) ;
		imgPlatform.setOrigin(imgPlatform.getWidth() / 2, imgPlatform.getHeight() / 2) ;
		imgPlatform.addAction(sequence(

				parallel(moveBy(0, 0, 0.5f, Interpolation.swingOut),
					scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
					alpha(1.0f, 0.5f)))) ;

		imgPlatform = new Image(skinMygame, "Platform") ;
		layer.addActor(imgPlatform) ;
		imgPlatform.setOrigin(imgPlatform.getWidth() / 2, imgPlatform.getHeight() / 2) ;
		imgPlatform.addAction(sequence(

				parallel(moveBy(75.0f, 0, 0.5f, Interpolation.swingOut),
					scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
					alpha(1.0f, 0.5f)))) ;

		imgPlatform = new Image(skinMygame, "Platform") ;
		layer.addActor(imgPlatform) ;
		imgPlatform.setOrigin(imgPlatform.getWidth() / 2, imgPlatform.getHeight() / 2) ;
		imgPlatform.addAction(sequence(

				parallel(moveBy(150.0f, 100.0f, 0.5f, Interpolation.swingOut),
					scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
					alpha(1.0f, 0.5f)))) ;

		imgPlatform = new Image(skinMygame, "Platform") ;
		layer.addActor(imgPlatform) ;
		imgPlatform.setOrigin(imgPlatform.getWidth() / 2, imgPlatform.getHeight() / 2) ;
		imgPlatform.addAction(sequence(

				parallel(moveBy(225.0f, 100.0f, 0.5f, Interpolation.swingOut),
					scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
					alpha(1.0f, 0.5f)))) ;

		imgPlatform = new Image(skinMygame, "Platform") ;
		layer.addActor(imgPlatform) ;
		imgPlatform.setOrigin(imgPlatform.getWidth() / 2, imgPlatform.getHeight() / 2) ;
		imgPlatform.addAction(sequence(

				parallel(moveBy(300.0f, 100.0f, 0.5f, Interpolation.swingOut),
					scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
					alpha(1.0f, 0.5f)))) ;

		imgPlatform = new Image(skinMygame, "Platform") ;
		layer.addActor(imgPlatform) ;
		imgPlatform.setOrigin(imgPlatform.getWidth() / 2, imgPlatform.getHeight() / 2) ;
		imgPlatform.addAction(sequence(

				parallel(moveBy(400.0f, 200.0f, 0.5f, Interpolation.swingOut),
					scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
					alpha(1.0f, 0.5f)))) ;

		imgPlatform = new Image(skinMygame, "Platform") ;
		layer.addActor(imgPlatform) ;
		imgPlatform.setOrigin(imgPlatform.getWidth() / 2, imgPlatform.getHeight() / 2) ;
		imgPlatform.addAction(sequence(

				parallel(moveBy(475.0f, 200.0f, 0.5f, Interpolation.swingOut),
					scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
					alpha(1.0f, 0.5f)))) ;

		imgPlatform = new Image(skinMygame, "Platform") ;
		layer.addActor(imgPlatform) ;
		imgPlatform.setOrigin(imgPlatform.getWidth() / 2, imgPlatform.getHeight() / 2) ;
		imgPlatform.addAction(sequence(

				parallel(moveBy(575.0f, 300.0f, 0.5f, Interpolation.swingOut),
					scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
					alpha(1.0f, 0.5f)))) ;

		imgPlatform = new Image(skinMygame, "Platform") ;
		layer.addActor(imgPlatform) ;
		imgPlatform.setOrigin(imgPlatform.getWidth() / 2, imgPlatform.getHeight() / 2) ;
		imgPlatform.addAction(sequence(

				parallel(moveBy(650.0f, 300.0f, 0.5f, Interpolation.swingOut),
					scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
					alpha(1.0f, 0.5f)))) ;

		imgPlatform = new Image(skinMygame, "Platform") ;
		layer.addActor(imgPlatform) ;
		imgPlatform.setOrigin(imgPlatform.getWidth() / 2, imgPlatform.getHeight() / 2) ;
		imgPlatform.addAction(sequence(

				parallel(moveBy(725.0f, 300.0f, 0.5f, Interpolation.swingOut),
					scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
					alpha(1.0f, 0.5f)))) ;

		// + Coins
		imgIce = new Image(skinMygame, "Ice") ;
		layer.addActor(imgIce) ;
		imgIce.setOrigin(imgIce.getWidth() / 2, imgIce.getHeight() / 2) ;
		imgIce.addAction(sequence(
			moveTo(185, -20),
			scaleTo(0, 0),
			fadeOut(0),
			delay(2.5f),
			parallel(moveBy(0, 250, 0.5f, Interpolation.swingOut),
				scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
				alpha(1.0f, 0.5f)))) ;

		imgIce = new Image(skinMygame, "Ice") ;
		layer.addActor(imgIce) ;
		imgIce.setOrigin(imgIce.getWidth() / 2, imgIce.getHeight() / 2) ;
		imgIce.addAction(sequence(
			moveTo(260, -20),
			scaleTo(0, 0),
			fadeOut(0),
			delay(2.5f),
			parallel(moveBy(0, 250, 0.5f, Interpolation.swingOut),
				scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
				alpha(1.0f, 0.5f)))) ;

		imgIce = new Image(skinMygame, "Ice") ;
		layer.addActor(imgIce) ;
		imgIce.setOrigin(imgIce.getWidth() / 2, imgIce.getHeight() / 2) ;
		imgIce.addAction(sequence(
			moveTo(335, -20),
			scaleTo(0, 0),
			fadeOut(0),
			delay(2.5f),
			parallel(moveBy(0, 250, 0.5f, Interpolation.swingOut),
				scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
				alpha(1.0f, 0.5f)))) ;

		imgIce = new Image(skinMygame, "Ice") ;
		layer.addActor(imgIce) ;
		imgIce.setOrigin(imgIce.getWidth() / 2, imgIce.getHeight() / 2) ;
		imgIce.addAction(sequence(
			moveTo(435, -20),
			scaleTo(0, 0),
			fadeOut(0),
			delay(2.5f),
			parallel(moveBy(0, 350, 0.5f, Interpolation.swingOut),
				scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
				alpha(1.0f, 0.5f)))) ;

		imgIce = new Image(skinMygame, "Ice") ;
		layer.addActor(imgIce) ;
		imgIce.setOrigin(imgIce.getWidth() / 2, imgIce.getHeight() / 2) ;
		imgIce.addAction(sequence(
			moveTo(510, -20),
			scaleTo(0, 0),
			fadeOut(0),
			delay(2.5f),
			parallel(moveBy(0, 350, 0.5f, Interpolation.swingOut),
				scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
				alpha(1.0f, 0.5f)))) ;

		imgIce = new Image(skinMygame, "Ice") ;
		layer.addActor(imgIce) ;
		imgIce.setOrigin(imgIce.getWidth() / 2, imgIce.getHeight() / 2) ;
		imgIce.addAction(sequence(
			moveTo(610, -20),
			scaleTo(0, 0),
			fadeOut(0),
			delay(2.5f),
			parallel(moveBy(0, 450, 0.5f, Interpolation.swingOut),
				scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
				alpha(1.0f, 0.5f)))) ;

		imgIce = new Image(skinMygame, "Ice") ;
		layer.addActor(imgIce) ;
		imgIce.setOrigin(imgIce.getWidth() / 2, imgIce.getHeight() / 2) ;
		imgIce.addAction(sequence(
			moveTo(685, -20),
			scaleTo(0, 0),
			fadeOut(0),
			delay(2.5f),
			parallel(moveBy(0, 450, 0.5f, Interpolation.swingOut),
				scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
				alpha(1.0f, 0.5f)))) ;

		imgIce = new Image(skinMygame, "Ice") ;
		layer.addActor(imgIce) ;
		imgIce.setOrigin(imgIce.getWidth() / 2, imgIce.getHeight() / 2) ;
		imgIce.addAction(sequence(
			moveTo(760, -20),
			scaleTo(0, 0),
			fadeOut(0),
			delay(2.5f),
			parallel(moveBy(0, 450, 0.5f, Interpolation.swingOut),
				scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
				alpha(1.0f, 0.5f)))) ;

		// + Bunny
		imgChar = new Image(skinMygame, "Character") ;
		layer.addActor(imgChar) ;
		imgChar.addAction(sequence(
				moveTo(-100, -100),
				fadeOut(0),
				delay(4.0f),
				moveTo(20, 90),
				fadeIn(1)
				)) ;

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
		btnMenuPlay = new Button(skinMygame, "play") ;
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
		btnMenuOptions = new Button(skinMygame, "options") ;
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
		showOptionsWindow(false, false) ;
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
		game.setScreen(new GameScreen(game, Constants.LEVEL_01, 0, Constants.LIVES_START)) ;
	}

	/**
	 * Activates options window
	 */
	private void onOptionsClicked()
	{
		loadSettings() ;
		showMenuButtons(false) ;
		showOptionsWindow(true, true) ;
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
		showMenuButtons(true) ;
		showOptionsWindow(false, true) ;
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
		imgCharSkin = new Image(Assets.instance.character.characterHead) ;
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

	private void showMenuButtons(boolean visible)
	{
		float moveDuration = 1.0f ;
		Interpolation moveEasing = Interpolation.swing ;
		float delayOptionsButton = 0.25f ;

		float moveX = 300 * (visible ? -1 : 1) ;
		float moveY = 0 * (visible ? -1 : 1) ;
		final Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled ;
		btnMenuPlay.addAction(moveBy(moveX, moveY, moveDuration, moveEasing)) ;

		btnMenuOptions.addAction(sequence(
				delay(delayOptionsButton),
				moveBy(moveX, moveY, moveDuration, moveEasing))) ;

		SequenceAction seq = sequence() ;
		if(visible)
		{
			seq.addAction(delay(delayOptionsButton + moveDuration)) ;
		}
		seq.addAction(run(new Runnable()
			{
				public void run()
				{
					btnMenuPlay.setTouchable(touchEnabled) ;
					btnMenuOptions.setTouchable(touchEnabled) ;
				}
			})) ;
		stage.addAction(seq) ;
	}

	private void showOptionsWindow(boolean visible, boolean animated)
	{
		float alphaTo = visible ? 0.8f : 0.0f ;
		float duration = animated ? 1.0f : 0.0f ;
		Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled ;
		winOptions.addAction(sequence(
				touchable(touchEnabled),
				alpha(alphaTo, duration))) ;
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
		skinMygame.dispose() ;
	}

	@Override public void pause(){}
}