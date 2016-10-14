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
import com.billyharrisongdx.game.util.Constants ;
import com.billyharrisongdx.game.game.objects.Rock ;
import com.badlogic.gdx.math.Rectangle ;
import com.billyharrisongdx.game.game.objects.GoldCoin ;
import com.billyharrisongdx.game.game.objects.Feather ;
import com.billyharrisongdx.game.game.objects.BunnyHead ;
import com.billyharrisongdx.game.game.objects.BunnyHead.JUMP_STATE ;
import com.badlogic.gdx.Game ;
import com.billyharrisongdx.game.screens.MenuScreen ;

public class WorldController extends InputAdapter
{
	// Tags are required for all debug messages
	private static final String TAG = WorldController.class.getName() ;

	public CameraHelper cameraHelper ; // Instantiates instance of cameraHelper class

	/**
	 * Hold instance of level, remaining lives, and current score
	 */
	public Level level ;
	public int lives ;
	public int score ;
	public float livesVisual ;
	public float scoreVisual ;

	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle() ;
	private Rectangle r2 = new Rectangle() ;
	private Game game ;

	/**
	 * Delay between losing last life and game restarting
	 */
	private float timeLeftGameOverDelay ;


	public WorldController(Game game)
	{
		this.game = game ;
		init() ;
	}

	/**
	 * Loads level, starts score at 0 and has camera follow bunny
	 */
	private void initLevel()
	{
		score = 0 ; // Initiates score to 0
		scoreVisual = score ;
		level = new Level(Constants.LEVEL_01) ; // Initiates level using LEVEL_01 map
		cameraHelper.setTarget(level.bunnyHead) ;
	}

	/**
	 * Loads camera, sets initial game over delay, and starts with 3 extra lives
	 */
	private void init()
	{
		Gdx.input.setInputProcessor(this) ;
		cameraHelper = new CameraHelper() ;
		lives = Constants.LIVES_START ; // Starts level with 3 lives
		livesVisual = lives ;
		timeLeftGameOverDelay = 0 ;
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
	public void update(float deltaTime){
		handleDebugInput(deltaTime) ;
		if(isGameOver()) // Returns to start screen if all lives are lost
		{
			timeLeftGameOverDelay -= deltaTime ;
			if(timeLeftGameOverDelay < 0)
			{
				backToMenu() ;
			}
		}
		else
		{
			handleInputGame(deltaTime) ;
		}
		level.update(deltaTime) ;
		testCollisions() ;
		cameraHelper.update(deltaTime) ;
		if(!isGameOver() && isPlayerInWater())
		{
			lives-- ;
			if(isGameOver())
			{
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER ;
			}
			else
			{
				initLevel() ;
			}
		}
		level.mountains.updateScrollPosition(cameraHelper.getPosition()) ;

		if(livesVisual > lives)
		{
			livesVisual = Math.max(lives, livesVisual - 1 * deltaTime) ;
		}
		if(scoreVisual < score)
		{
			scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime) ;
		}
	}

	/**
	 * Controls used to move camera when not targeting bunny
	 */
	private void handleDebugInput (float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop) return ;

		if(!cameraHelper.hasTarget(level.bunnyHead))
		{
			// Camera Controls (move)
			float camMoveSpeed = 5 * deltaTime ;
			float camMoveSpeedAccelerationFactor = 5 ;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor ;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0) ;
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0) ;
			if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera (0, camMoveSpeed) ;
			if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed) ;
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0) ;
		}
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
		// Toggle camera follow
		else if(keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.bunnyHead) ;
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget()) ;
		}
		else if(keycode == Keys.ESCAPE || keycode == Keys.BACK)
		{
			backToMenu() ;
		}
		return false ;
	}

	private void handleInputGame(float deltaTime)
	{
		if(cameraHelper.hasTarget(level.bunnyHead))
		{
			// Player movement
			if(Gdx.input.isKeyPressed(Keys.LEFT))
			{
				level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x ;
			}
			else if(Gdx.input.isKeyPressed(Keys.RIGHT))
			{
				level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x ;
			}
			else
			{
				// Execute auto-forward movement on non-desktop platform
				if(Gdx.app.getType() != ApplicationType.Desktop)
				{
					level.bunnyHead.velocity.x =
					level.bunnyHead.terminalVelocity.x ;
				}
			}
			// Bunny Jump
			if(Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))
			{
				level.bunnyHead.setJumping(true) ;
			}
			else
			{
				level.bunnyHead.setJumping(false) ;
			}
		}
	}

	private void onCollisionBunnyHeadWithRock(Rock rock)
	{
		BunnyHead bunnyHead = level.bunnyHead ;
		float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height)) ;
		if(heightDifference > 0.25f)
		{
			boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f) ;
			if(hitRightEdge)
			{
				bunnyHead.position.x = rock.position.x + rock.bounds.width ;
			}
			else
			{
				bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width ;
			}

			return ;
		}

		switch(bunnyHead.jumpState)
		{
			case GROUNDED:
				break ;
			case FALLING:
			case JUMP_FALLING:
				bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y ;
				bunnyHead.jumpState = JUMP_STATE.GROUNDED ;
				break ;
			case JUMP_RISING:
				bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y ;
				break ;
		}
	}

	private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin)
	{
		goldcoin.collected = true ;
		score += goldcoin.getScore() ;
		Gdx.app.log(TAG, "Gold coin collected") ;
	}
	private void onCollisionBunnyWithFeather(Feather feather)
	{
		feather.collected = true ;
		score += feather.getScore() ;
		level.bunnyHead.setFeatherPowerup(true) ;
		Gdx.app.log(TAG, "Feather collected") ;
	}

	private void testCollisions()
	{
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y,
				level.bunnyHead.bounds.width, level.bunnyHead.bounds.height) ;

		// Test for collision: Bunny Head <-> Rocks
		for(Rock rock : level.rocks)
		{
			r2.set(rock.position.x, rock.position.y, rock.bounds.width,
					rock.bounds.height) ;
			if(!r1.overlaps(r2)) continue ;
			onCollisionBunnyHeadWithRock(rock) ;
			// IMPORTANT: must do all collisions for valid
			// edge testing on rocks
		}

		// Test collision: Bunny Head <-> Gold Coins
		for(GoldCoin goldcoin : level.goldcoins)
		{
			if(goldcoin.collected) continue ;
			r2.set(goldcoin.position.x, goldcoin.position.y,
					goldcoin.bounds.width, goldcoin.bounds.height) ;
			if(!r1.overlaps(r2)) continue ;
			onCollisionBunnyWithGoldCoin(goldcoin) ;
			break ;
		}

		// Test collision: Bunny Head <-> Feathers
		for(Feather feather : level.feathers)
		{
			if(feather.collected) continue ;
			r2.set(feather.position.x, feather.position.y,
					feather.bounds.width, feather.bounds.height) ;
			if(!r1.overlaps(r2)) continue ;
			onCollisionBunnyWithFeather(feather) ;
			break ;
		}
	}

	/**
	 * Checks if the player has lost all of their extra lives
	 */
	public boolean isGameOver()
	{
		return lives < 0 ;
	}

	/**
	 * Returns whether or not the bunny is below the water level (had died)
	 */
	public boolean isPlayerInWater()
	{
		return level.bunnyHead.position.y < -5 ;
	}

	/**
	 * Returns program to the start menu
	 */
	private void backToMenu()
	{
		// Switch to menu screen
		game.setScreen(new MenuScreen(game)) ;
	}
}