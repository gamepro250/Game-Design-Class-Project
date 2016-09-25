/**
 * Author: Billy Harrison
 *
 * Date: 9/15/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.game;

//import com.badlogic.gdx.graphics.Pixmap;
//import com.badlogic.gdx.graphics.Pixmap.Format;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.billyharrisongdx.game.util.CameraHelper;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.utils.Array;
import com.billyharrisongdx.game.util.Constants ;
//import com.billyharrisongdx.game.game.objects.Rock ;


public class WorldController extends InputAdapter
{
	// Tags are required for all debug messages
	private static final String TAG = WorldController.class.getName() ;

	public CameraHelper cameraHelper ; // Instantiates instance of cameraHelper class

	public Level level ;
	public int lives ;
	public int score ;

	public WorldController()
	{
		init() ;
	}

	private void initLevel()
	{
		score = 0 ; // Initiates score to 0
		level = new Level(Constants.LEVEL_01) ; // Initiates level using LEVEL_01 map
	}

	private void init()
	{
		Gdx.input.setInputProcessor(this) ;
		cameraHelper = new CameraHelper() ;
		lives = Constants.LIVES_START ; // Starts level with 3 lives
		initLevel() ;
	}



//	/**
//	 * Creates assest used to test rendering
//	 */
//	private Pixmap createProceduralPixmap (int width, int height)
//	{
//		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888) ;
//		// Fill square with red color at 50% opacity
//		pixmap.setColor(1, 0, 0, 0.5f) ;
//		pixmap.fill() ;
//		// Draw a yellow-colored X shape on the square
//		pixmap.setColor(1, 1, 0, 1) ;
//		pixmap.drawLine(0, 0, width, height) ;
//		pixmap.drawLine(width, 0, 0, height) ;
//		// Draw a cyan-colored border around square
//		pixmap.setColor(0, 1, 1, 1) ;
//		pixmap.drawRectangle(0,  0, width, height) ;
//		return pixmap ;
//	}

	/**
	 * Updates the games state based on the deltaTime
	 */
	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime) ;
		cameraHelper.update(deltaTime) ;
	}

	private void handleDebugInput (float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop) return ;

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
	 * Move camera to location specified
	 */
	private void moveCamera (float x, float y)
	{
		x += cameraHelper.getPosition().x ;
		y += cameraHelper.getPosition().y ;
		cameraHelper.setPosition(x, y) ;
	}


	@Override
	public boolean keyUp (int keycode)
	{
		//Reset game world
		if (keycode == Keys.R)
		{
			init() ;
			Gdx.app.debug(TAG, "Game world was reset") ;
		}
		return false ;
	}
}