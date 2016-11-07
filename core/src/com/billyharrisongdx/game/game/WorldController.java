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
import com.billyharrisongdx.game.game.objects.Ground ;
import com.badlogic.gdx.math.Rectangle ;
import com.billyharrisongdx.game.game.objects.Ice ;
import com.billyharrisongdx.game.game.objects.Fire ;
import com.billyharrisongdx.game.game.objects.Character ;
import com.billyharrisongdx.game.game.objects.Character.JUMP_STATE;
import com.billyharrisongdx.game.screens.MenuScreen ;
import com.badlogic.gdx.Game ;
import com.billyharrisongdx.game.util.AudioManager ;
import com.badlogic.gdx.math.MathUtils ;
import com.badlogic.gdx.math.Vector2 ;
import com.badlogic.gdx.physics.box2d.Body ;
import com.badlogic.gdx.physics.box2d.BodyDef ;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType ;
import com.badlogic.gdx.physics.box2d.FixtureDef ;
import com.badlogic.gdx.physics.box2d.PolygonShape ;
import com.badlogic.gdx.physics.box2d.World ;
import com.badlogic.gdx.utils.Disposable ;

public class WorldController extends InputAdapter implements Disposable
{
	// Tags are required for all debug messages
	private static final String TAG = WorldController.class.getName() ;

	public CameraHelper cameraHelper ; // Instantiates instance of cameraHelper class

	public Level level ;
	public int lives ;
	public int score ;
	public float livesVisual ;
	public float scoreVisual ;

	public World world ;

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
		cameraHelper.setTarget(level.character) ;
		initPhysics() ;
	}

	private void initPhysics()
	{
		if(world != null) world.dispose() ;
		world = new World(new Vector2(0, -9.81f), true) ;
		Vector2 origin = new Vector2() ;
		for(Ground ground : level.grounds)
		{
			BodyDef bodyDef = new BodyDef() ;
			bodyDef.type = BodyType.KinematicBody ;
			bodyDef.position.set(ground.position) ;
				Body body = world.createBody(bodyDef) ;
			ground.body = body ;
			PolygonShape polygonShape = new PolygonShape() ;
			origin.x = ground.bounds.width / 2.0f ;
			origin.y = ground.bounds.height / 2.0f ;
			polygonShape.setAsBox(ground.bounds.width / 2.0f, ground.bounds.height / 2.0f, origin, 0) ;
			FixtureDef fixtureDef = new FixtureDef() ;
			fixtureDef.shape = polygonShape ;
			body.createFixture(fixtureDef) ;
			polygonShape.dispose() ;
		}


		// Create box2d body for character
		Character c = level.character ;

		BodyDef bodyDef = new BodyDef() ;
		bodyDef.type = BodyType.DynamicBody ;
		bodyDef.position.set(c.position) ;
		bodyDef.fixedRotation = true ;
			Body body = world.createBody(bodyDef) ;
		c.body = body ;
		PolygonShape polygonShape = new PolygonShape() ;

		origin.x = c.bounds.width / 2.0f ;
		origin.y = c.bounds.height / 2.0f ;

		polygonShape.setAsBox(c.bounds.width / 2.0f, c.bounds.height / 2.0f, origin, 0) ;
		// Set physics attributes
		FixtureDef fixtureDef = new FixtureDef() ;
		fixtureDef.shape = polygonShape ;
		fixtureDef.density = 50 ;

		fixtureDef.friction = c.friction.x ;
		body.createFixture(fixtureDef) ;
		polygonShape.dispose() ;

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
	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime) ;
		if(isGameOver()) // Returns to start menu when all lives lost
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
		if(!isGameOver() && isPlayerInLava())
		{
			AudioManager.instance.play(Assets.instance.sounds.lifeLost) ;
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
		if(livesVisual > lives)
		{
			livesVisual = Math.max(lives, livesVisual - 1 * deltaTime) ;
		}
		if(scoreVisual < score)
		{
			scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime) ;
		}

		world.step(1/60.0f, 8, 3);
	}

	private void handleDebugInput (float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop) return ;

		if(!cameraHelper.hasTarget(level.character))
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
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.character) ;
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
		if(cameraHelper.hasTarget(level.character))
		{
			// Player movement
			if(Gdx.input.isKeyPressed(Keys.LEFT))
			{
				level.character.velocity.x = -level.character.terminalVelocity.x ;
				level.character.body.setLinearVelocity(-level.character.terminalVelocity.x, level.character.velocity.y);
			}
			else if(Gdx.input.isKeyPressed(Keys.RIGHT))
			{
				level.character.velocity.x = level.character.terminalVelocity.x ;
				level.character.body.setLinearVelocity(level.character.terminalVelocity.x, level.character.velocity.y);
			}
			else
			{
				// Execute auto-forward movement on non-desktop platform
				if(Gdx.app.getType() != ApplicationType.Desktop)
				{
					level.character.velocity.x =
					level.character.terminalVelocity.x ;
				}
			}
			// Character Jump
			if(Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))
			{
				level.character.setJumping(true) ;
			}
			else
			{
				level.character.setJumping(false) ;
			}
		}
	}
/*
	private void onCollisionBunnyHeadWithGround(Ground ground)
	{
		Character character = level.character ;
		float heightDifference = Math.abs(character.position.y - (ground.position.y + ground.bounds.height)) ;
		if(heightDifference > 0.25f)
		{
			boolean hitRightEdge = character.position.x > (ground.position.x + ground.bounds.width / 2.0f) ;
			if(hitRightEdge)
			{
				character.position.x = ground.position.x + ground.bounds.width ;
			}
			else
			{
				character.position.x = ground.position.x - character.bounds.width ;
			}

			return ;
		}

		switch(character.jumpState)
		{
			case GROUNDED:
				break ;
			case FALLING:
			case JUMP_FALLING:
				character.position.y = ground.position.y + character.bounds.height + character.origin.y ;
				character.jumpState = JUMP_STATE.GROUNDED ;
				break ;
			case JUMP_RISING:
				character.position.y = ground.position.y + character.bounds.height + character.origin.y ;
				break ;
		}
	}
*/
	private void onCollisionBunnyWithIce(Ice ice)
	{
		ice.collected = true ;
		AudioManager.instance.play(Assets.instance.sounds.pickupIce) ;
		score += ice.getScore() ;
		Gdx.app.log(TAG, "Ice collected") ;
	}
	private void onCollisionBunnyWithFire(Fire fire)
	{
		fire.collected = true ;
		AudioManager.instance.play(Assets.instance.sounds.pickupFire) ;
		score += fire.getScore() ;
		level.character.setFirePowerup(true) ;
		Gdx.app.log(TAG, "Fire collected") ;
	}

	/**
	 * Use collision detection methods to see if character hits anything
	 */
	private void testCollisions()
	{
		r1.set(level.character.position.x, level.character.position.y,
				level.character.bounds.width, level.character.bounds.height) ;

		// Test for collision: Character <-> Ground
		for(Ground ground : level.grounds)
		{
			r2.set(ground.position.x, ground.position.y, ground.bounds.width,
					ground.bounds.height) ;
			if(!r1.overlaps(r2)) continue ;
			//onCollisionBunnyHeadWithGround(ground) ;
			// IMPORTANT: must do all collisions for valid
			// edge testing on ground
		}

		// Test collision: Character <-> Ice
		for(Ice ice : level.ice)
		{
			if(ice.collected) continue ;
			r2.set(ice.position.x, ice.position.y,
					ice.bounds.width, ice.bounds.height) ;
			if(!r1.overlaps(r2)) continue ;
			onCollisionBunnyWithIce(ice) ;
			break ;
		}

		// Test collision: Character <-> Fire
		for(Fire fire : level.fire)
		{
			if(fire.collected) continue ;
			r2.set(fire.position.x, fire.position.y,
					fire.bounds.width, fire.bounds.height) ;
			if(!r1.overlaps(r2)) continue ;
			onCollisionBunnyWithFire(fire) ;
			break ;
		}
	}

	/**
	 * Have all lives been lost?
	 */
	public boolean isGameOver()
	{
		return lives < 0 ;
	}

	/**
	 * Is player in lava (dead)
	 */
	public boolean isPlayerInLava()
	{
		return level.character.position.y < -5 ;
	}

	/**
	 * returns to start menu
	 */
	private void backToMenu()
	{
		// Switch to menu screen
		game.setScreen(new MenuScreen(game)) ;
	}

	@Override
	public void dispose()
	{
		if(world != null) world.dispose() ;
	}
}
