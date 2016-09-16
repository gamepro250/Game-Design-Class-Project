/**
 * Author: Billy Harrison
 *
 * Date: 9/15/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.billyharrisongdx.game.util.CameraHelper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class WorldController extends InputAdapter
{
	// Tags are required for all debug messages
	private static final String TAG = WorldController.class.getName() ;

	public Sprite[] testSprites ; // Array to hold sprites to be displayed
	public int selectedSprite ; // Current active sprite

	public CameraHelper cameraHelper ; // Instantiates instance of cameraHelper class

	public WorldController()
	{
		init() ;
	}

	private void init()
	{
		Gdx.input.setInputProcessor(this) ;
		cameraHelper = new CameraHelper() ;
		initTestObjects() ;
	}

	private void initTestObjects()
	{
		// Create new array for 5 sprites
		testSprites = new Sprite[5] ;

//		// Create empty POT-sized Pixmap with 8 bit RGBA pixel data
//		int width = 32 ;
//		int height = 32 ;
//		Pixmap pixmap = createProceduralPixmap(width, height) ;
//		// Create a new texture from pixmap data
//		Texture texture = new Texture(pixmap) ;

		// Create a list of Texture Regions
		Array<TextureRegion> regions = new Array<TextureRegion>() ;
		regions.add(Assets.instance.bunny.head) ;
		regions.add(Assets.instance.feather.feather) ;
		regions.add(Assets.instance.goldCoin.goldCoin) ;

		// Create new sprites using the just created texture
		for (int i = 0; i < testSprites.length; i++)
		{
			Sprite spr = new Sprite(regions.random()) ;
			// Define sprite size to be 1m x 1m in game world.
			spr.setSize(1, 1) ;

			// Set origin to sprite's center
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f) ;

			// Calculate random position for sprite
			float randomX = MathUtils.random(-2.0f, 2.0f) ;
			float randomY = MathUtils.random(-2.0f, 2.0f) ;
			spr.setPosition(randomX, randomY) ;

			// Put new sprite into array
			testSprites[i] = spr ;
		}
		//Set first sprite as selected one
		selectedSprite = 0 ;
	}

	/**
	 * Creates assest used to test rendering
	 */
	private Pixmap createProceduralPixmap (int width, int height)
	{
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888) ;
		// Fill square with red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f) ;
		pixmap.fill() ;
		// Draw a yellow-colored X shape on the square
		pixmap.setColor(1, 1, 0, 1) ;
		pixmap.drawLine(0, 0, width, height) ;
		pixmap.drawLine(width, 0, 0, height) ;
		// Draw a cyan-colored border around square
		pixmap.setColor(0, 1, 1, 1) ;
		pixmap.drawRectangle(0,  0, width, height) ;
		return pixmap ;
	}

	/**
	 * Updates the games state based on the deltaTime
	 */
	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime) ;
		updateTestObjects(deltaTime) ;
		cameraHelper.update(deltaTime) ;
	}

	private void handleDebugInput (float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop) return ;

		// Selected Sprite Controls
		float sprMoveSpeed = 5 * deltaTime ;
		if (Gdx.input.isKeyPressed(Keys.A)) moveSelectedSprite(-sprMoveSpeed, 0) ;
		if (Gdx.input.isKeyPressed(Keys.D)) moveSelectedSprite(sprMoveSpeed, 0) ;
		if (Gdx.input.isKeyPressed(Keys.W)) moveSelectedSprite(0, sprMoveSpeed) ;
		if (Gdx.input.isKeyPressed(Keys.S)) moveSelectedSprite(0, -sprMoveSpeed) ;

		// Camera Controls (move)
		float camMoveSpeed = 5 * deltaTime ;
		float camMoveSpeedAccelerationFactor = 5 ;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor ;
		if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0) ;
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0) ;
		if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera (0, camMoveSpeed) ;
		if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed) ;
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0) ;

		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime ;
		float camZoomSpeedAccelerationFactor = 5 ;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor ;
		if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed) ;
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed) ;
		if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1) ;
	}

	/**
	 * Move active sprite to location specified
	 */
	private void moveSelectedSprite (float x, float y)
	{
		testSprites[selectedSprite].translate(x, y) ;
	}

	/**
	 * Move camera to location specified
	 */
	private void moveCamera (float x, float y)
	{
		x += cameraHelper.getPosition().x ;
		y += cameraHelper.getPosition().y ;
		cameraHelper.setPosition(x, y) ;
	}

	/**
	 * Rotates active sprite 360 degrees every 4 seconds
	 */
	public void updateTestObjects(float deltaTime)
	{
		// Get current rotation from selected sprite
		float rotation = testSprites[selectedSprite].getRotation() ;
		// Rotate sprite by 90 degrees per second
		rotation += 90 * deltaTime ;
		// Wrap around at 360 degrees
		rotation %= 360 ;
		// Set new rotation value to selected sprite
		testSprites[selectedSprite].setRotation(rotation) ;
	}

	@Override
	public boolean keyUp (int keycode)
	{
		//Reset game world
		if (keycode == Keys.R)
		{
			init() ;
			Gdx.app.debug(TAG, "Game world was reset") ; // "Resetted" isn't a word, so I changed the log message a bit.
		}
		// Select next sprite
		else if (keycode == Keys.SPACE)
		{
			selectedSprite = (selectedSprite + 1) % testSprites.length ;
			// Update camera's target to follow the currently selected sprite
			if (cameraHelper.hasTarget())
			{
				cameraHelper.setTarget(testSprites[selectedSprite]) ;
			}
			Gdx.app.debug(TAG, "Sprite #" + selectedSprite + " selected") ;
		}
		// Toggle Camera follow
		else if (keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : testSprites[selectedSprite]) ; // I don't understand this syntax.
			// So I looked it up and basically this means if it has a target, it'll set it to null,
			// and if it doesn't have a target, it'll set it to the selected sprite.
			Gdx.app.debug (TAG, "Camera follow enabled: " + cameraHelper.hasTarget()) ;
		}
		return false ;
	}
}