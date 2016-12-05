/**
 * Author: Billy Harrison
 *
 * Date: 9/15/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.billyharrisongdx.game.util.CameraHelper;
import com.billyharrisongdx.game.util.CollisionHandler;
import com.badlogic.gdx.utils.Array;
import com.billyharrisongdx.game.util.Constants ;
import com.billyharrisongdx.game.util.GamePreferences;
import com.billyharrisongdx.game.game.objects.Ground ;
import com.billyharrisongdx.game.game.objects.Ice ;
import com.billyharrisongdx.game.game.objects.Fire ;
import com.billyharrisongdx.game.game.objects.Goal;
import com.billyharrisongdx.game.game.objects.AbstractGameObject;
import com.billyharrisongdx.game.game.objects.Character ;
import com.billyharrisongdx.game.screens.MenuScreen ;
import com.billyharrisongdx.game.screens.GameScreen ;
import com.badlogic.gdx.Game ;
import com.billyharrisongdx.game.util.AudioManager ;
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
	public int lastLevelScore ;
	public float livesVisual ;
	public float scoreVisual ;
	public float timeHeld ;
	public boolean playJump = true ;
	public World world ;
	public Array<AbstractGameObject> objectsToRemove ;
	public String levelNum ;
	public boolean showScores = false ;
	GamePreferences prefs = GamePreferences.instance ;

	private Game game ;
	private float timeLeftGameOverDelay ;	// Delay between losing last life and game restarting
	private float levelEndDelay ;
	public WorldController(Game game, String levelNum, int score, int lives)
	{
		this.game = game ;
		this.levelNum = levelNum ;
		init(levelNum, score, lives) ;
	}

	/**
	 * Loads level, starts score at 0 and has camera follow bunny
	 */
	private void initLevel(String levelNum)
	{
		scoreVisual = score ;
		level = new Level(levelNum) ; // Initiates level using LEVEL_01 map
		cameraHelper.setTarget(level.character) ;
		initPhysics() ; // Initializes the Box2D physics world
	}

	/**
	 * Sets up bodies for all Box2D objects
	 */
	private void initPhysics()
	{
		if(world != null)
		{
			world.dispose() ;
		}
		world = new World(new Vector2(0, -9.81f), true) ;
		world.setContactListener(new CollisionHandler(this));

		Vector2 origin = new Vector2() ;

		// Create box2d body for ground pieces
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
			origin.y = ground.bounds.height / 2.0f - 0.25f ;
			polygonShape.setAsBox(ground.bounds.width / 2.0f, ground.bounds.height / 2.0f, origin, 0) ;
			FixtureDef fixtureDef = new FixtureDef() ;
			fixtureDef.shape = polygonShape ;
			fixtureDef.friction = 0.5f ;
			body.createFixture(fixtureDef) ;
			polygonShape.dispose() ;
		}

		for(Ground ground : level.slowGrounds)
		{
			BodyDef bodyDef = new BodyDef() ;
			bodyDef.type = BodyType.KinematicBody ;
			bodyDef.position.set(ground.position) ;
				Body body = world.createBody(bodyDef) ;
			body.setUserData(ground);
			ground.body = body ;
			PolygonShape polygonShape = new PolygonShape() ;
			origin.x = ground.bounds.width / 2.0f ;
			origin.y = ground.bounds.height / 2.0f - 0.25f ;
			polygonShape.setAsBox(ground.bounds.width / 2.0f, ground.bounds.height / 2.0f, origin, 0) ;
			FixtureDef fixtureDef = new FixtureDef() ;
			fixtureDef.shape = polygonShape ;
			fixtureDef.friction = 0.75f ;
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
		fixtureDef.density = 5 ;

		fixtureDef.friction = c.friction.x ;
		body.createFixture(fixtureDef) ;
		polygonShape.dispose() ;

		// Create box2d body for ice collectibles
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
			polygonShape2.setAsBox(ice.bounds.width / 2.0f, ice.bounds.height / 2.0f, origin, 0) ;
			FixtureDef fixtureDef2 = new FixtureDef() ;
			fixtureDef2.shape = polygonShape2 ;
			fixtureDef2.density = 5 ;
			fixtureDef2.isSensor = true ;
			body2.createFixture(fixtureDef2) ;
			polygonShape2.dispose() ;
		}

		Goal boat = level.boat ;
		BodyDef bodyDef4 = new BodyDef() ;
		bodyDef4.type = BodyType.KinematicBody ;
		bodyDef4.position.set(boat.position) ;
			Body body4 = world.createBody(bodyDef4) ;
		body4.setUserData(boat) ;
		boat.body = body4 ;
		PolygonShape polygonShape4 = new PolygonShape() ;
		origin.x = boat.bounds.width / 2.0f ;
		origin.y = boat.bounds.height / 2.0f ;
		polygonShape4.setAsBox(boat.bounds.width / 2.0f, boat.bounds.height / 2.0f, origin, 0) ;
		FixtureDef fixtureDef4 = new FixtureDef() ;
		fixtureDef4.shape = polygonShape4 ;
		body4.createFixture(fixtureDef4) ;
		polygonShape4.dispose() ;


		// Create box2d body for fire collectibles
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
			fixtureDef3.isSensor = true ;
			fixtureDef3.shape = polygonShape3 ;
			body3.createFixture(fixtureDef3) ;
			polygonShape3.dispose() ;
		}

	}

	/**
	 * Loads camera, sets initial game over delay, and starts with 3 extra lives
	 */
	private void init(String levelNum, int score, int lives)
	{
		objectsToRemove = new Array<AbstractGameObject>();
		Gdx.input.setInputProcessor(this) ;
		cameraHelper = new CameraHelper() ;
		this.score = score ;
		this.lastLevelScore = score ;
		this.scoreVisual = score ;
		this.lives = lives ; // Starts level with 3 lives
		this.livesVisual = lives ;
		timeLeftGameOverDelay = 0 ;
		levelEndDelay = Constants.LEVEL_END_DELAY ;
		initLevel(levelNum) ;
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
		else if(level.goalReached)
		{
			levelEndDelay -= deltaTime ;
			if(levelEndDelay < 0)
			{
				if(levelNum.equals(Constants.LEVEL_01))
				{
					changeLevel(Constants.LEVEL_02) ;
				}
				else if(levelNum.equals(Constants.LEVEL_02))
				{
					for(int i=0;i<10;i++)
					{
						int prevScore = 0 ;

						if(this.score > prefs.getHighScores(i))
						{

							prevScore = prefs.getHighScores(i) ;
							prefs.setHighScore(this.score, i);

							score = prevScore ;
						}
					}

					backToMenu() ;
				}

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
				score = lastLevelScore ;
				initLevel(levelNum) ;
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

	/**
	 * When camera is not locked on player, this is used to move the camera
	 * around the game world
	 */
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

	/**
	 * Handles what happens when a button is released
	 */
	@Override
	public boolean keyUp (int keycode)
	{
		//Reset game world
		if (keycode == Keys.R)
		{
			init(Constants.LEVEL_01, 0, Constants.LIVES_START) ;
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
		else if(keycode == Keys.LEFT || keycode == Keys.RIGHT)
		{
			level.character.running = false ;
		}
		else if(keycode == Keys.SPACE)
		{
			level.character.airborne = true ;
		}
		return false ;
	}

	/**
	 * Handles player input
	 */
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
				level.character.running = true ;
			}
			else if(Gdx.input.isKeyPressed(Keys.RIGHT))
			{
				level.character.viewDirection = Character.VIEW_DIRECTION.RIGHT ;
				Vector2 vel = level.character.body.getLinearVelocity() ;
				//level.character.body.setLinearVelocity(level.character.terminalVelocity.x, vel.y);
				level.character.body.applyForceToCenter(level.character.terminalVelocity.x, vel.y, true);
				level.character.running = true ;
			}
			// Character Jump

			if((Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) && level.character.airborne == false)
			{
				Vector2 vel = level.character.body.getLinearVelocity() ;

				if(level.character.hasFirePowerup() && timeHeld < 0.75)
				{
					level.character.body.setLinearVelocity(vel.x, level.character.terminalVelocity.y) ;

					timeHeld += deltaTime ;
				}
				else if(timeHeld < .5)
				{
					level.character.grounded = false ;

					if(playJump)
					{
						AudioManager.instance.play(Assets.instance.sounds.jump) ;
						playJump = false ;
					}

					level.character.body.setLinearVelocity(vel.x, level.character.terminalVelocity.y) ;

					level.character.position.set(level.character.body.getPosition()) ;

					timeHeld += deltaTime ;
				}
			}
			if(Gdx.input.isKeyPressed(Keys.H))
			{
				showScores = true ;
			}
			if(Gdx.input.isKeyPressed(Keys.J))
			{
				showScores = false ;
			}
		}
	}

	/**
	 * Adds object to array for future removal
	 */
	public void flagForRemoval(AbstractGameObject obj)
	{
		objectsToRemove.add(obj) ;
	}

	/**
	 * Allows the character to jump again
	 */
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

	private void changeLevel(String levelNum)
	{
		// Advance to next level
		game.setScreen(new GameScreen(game, levelNum, score, lives)) ;
	}

	@Override
	public void dispose()
	{
		if(world != null) world.dispose() ;
	}

	public void setScore(int score)
	{
		this.score = score ;
		this.scoreVisual = score ;
	}

	public int getScore()
	{
		return this.score ;
	}
}
