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
import com.billyharrisongdx.game.util.CollisionHandler;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.billyharrisongdx.game.util.Constants ;
import com.billyharrisongdx.game.game.objects.Ground ;
import com.badlogic.gdx.math.Rectangle ;
import com.billyharrisongdx.game.game.objects.Ice ;
import com.billyharrisongdx.game.game.objects.Fire ;
import com.billyharrisongdx.game.game.objects.AbstractGameObject;
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

	public float timeHeld ;
	public boolean playJump = true ;

	public World world ;

	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle() ;
	private Rectangle r2 = new Rectangle() ;
	private Game game ;

	public Array<AbstractGameObject> objectsToRemove ;

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
		world.setContactListener(new CollisionHandler(this));
		Vector2 origin = new Vector2() ;
		for(Ground ground : level.grounds)
		{
			BodyDef bodyDef = new BodyDef() ;
			bodyDef.type = BodyType.KinematicBody ;
			bodyDef.position.set(ground.position) ;
				Body body = world.createBody(bodyDef) ;
			body.setUserData(ground);
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
		body.setUserData(c) ;
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

		for(Ice ice : level.ice)
		{
			BodyDef bodyDef2 = new BodyDef() ;
			bodyDef2.type = BodyType.KinematicBody ;
			bodyDef2.position.set(ice.position) ;
				Body body2 = world.createBody(bodyDef2) ;
			body2.setUserData(ice);
			ice.body = body2 ;
			PolygonShape polygonShape2 = new PolygonShape() ;
			origin.x = ice.bounds.width / 2.0f ;
			origin.y = ice.bounds.height / 2.0f ;
			polygonShape.setAsBox(ice.bounds.width / 2.0f, ice.bounds.height / 2.0f, origin, 0) ;
			FixtureDef fixtureDef2 = new FixtureDef() ;
			fixtureDef2.shape = polygonShape2 ;
			body2.createFixture(fixtureDef2) ;
			polygonShape2.dispose() ;
		}

		for(Fire fire: level.fire)
		{
			BodyDef bodyDef3 = new BodyDef() ;
			bodyDef3.type = BodyType.KinematicBody ;
			bodyDef3.position.set(fire.position) ;
				Body body3 = world.createBody(bodyDef3) ;
			body3.setUserData(fire);
			fire.body = body3 ;
			PolygonShape polygonShape3 = new PolygonShape() ;
			origin.x = fire.bounds.width / 2.0f ;
			origin.y = fire.bounds.height / 2.0f ;
			polygonShape3.setAsBox(fire.bounds.width / 2.0f, fire.bounds.height / 2.0f, origin, 0) ;
			FixtureDef fixtureDef3 = new FixtureDef() ;
			fixtureDef3.shape = polygonShape3 ;
			body3.createFixture(fixtureDef3) ;
			polygonShape3.dispose() ;
		}
	}

	/**
	 * Loads camera, sets initial game over delay, and starts with 3 extra lives
	 */
	private void init()
	{
		objectsToRemove = new Array<AbstractGameObject>();
		Gdx.input.setInputProcessor(this) ;
		cameraHelper = new CameraHelper() ;
		lives = Constants.LIVES_START ; // Starts level with 3 lives
		livesVisual = lives ;
		timeLeftGameOverDelay = 0 ;
		initLevel() ;
	}
	/**
	 * Updates the games state based on the deltaTime
	 */
	public void update(float deltaTime)
	{

		if (objectsToRemove.size > 0)
		{
			for (AbstractGameObject obj : objectsToRemove)
			{
				if (obj instanceof Ice)
				{
					int index = level.ice.indexOf((Ice) obj, true);
					if (index != -1)
					{
					    level.ice.removeIndex(index);
					    world.destroyBody(obj.body);
					}
				}
				else if (obj instanceof Fire)
				{
					int index = level.fire.indexOf((Fire) obj, true);
					if (index != -1)
					{
					    level.fire.removeIndex(index);
					    world.destroyBody(obj.body);
					}
				}
			}
			objectsToRemove.removeRange(0, objectsToRemove.size - 1);
		}

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
				level.character.viewDirection = Character.VIEW_DIRECTION.LEFT ;
				Vector2 vel = level.character.body.getLinearVelocity() ;
				//level.character.body.setLinearVelocity(-level.character.terminalVelocity.x, vel.y);
				level.character.body.applyForceToCenter(-level.character.terminalVelocity.x, vel.y, true);
			}
			else if(Gdx.input.isKeyPressed(Keys.RIGHT))
			{

				level.character.viewDirection = Character.VIEW_DIRECTION.RIGHT ;
				Vector2 vel = level.character.body.getLinearVelocity() ;
				//level.character.body.setLinearVelocity(level.character.terminalVelocity.x, vel.y);
				level.character.body.applyForceToCenter(level.character.terminalVelocity.x, vel.y, true);
			}
			// Character Jump
			if(Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))
			{
				if(timeHeld < 1)
				{
					level.character.grounded = false ;
					if(playJump == true)
					{
						AudioManager.instance.play(Assets.instance.sounds.jump) ;
						playJump = false ;
					}
					Vector2 vel = level.character.body.getLinearVelocity() ;

					if(level.character.hasFirePowerup())
					{
						level.character.body.setLinearVelocity(vel.x, level.character.terminalVelocity.y * 2) ;
					}
					else
					{
						level.character.body.setLinearVelocity(vel.x, level.character.terminalVelocity.y) ;
					}
					level.character.position.set(level.character.body.getPosition()) ;

					timeHeld += deltaTime ;
				}
			}
		}
	}

	public void flagForRemoval(AbstractGameObject obj)
	{
		objectsToRemove.add(obj) ;
	}

	public void resetJump()
	{
		playJump = true ;
		timeHeld = 0 ;
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
