/**
 * Author: Billy Harrison
 *
 * Date: 9/25/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.game.objects ;

import com.badlogic.gdx.graphics.g2d.SpriteBatch ;
import com.badlogic.gdx.math.Vector2 ;
import com.badlogic.gdx.math.Rectangle ;
import com.badlogic.gdx.math.MathUtils ;
import com.badlogic.gdx.physics.box2d.Body ;
import com.badlogic.gdx.graphics.g2d.Animation ;

/**
 * Contains methods that must be inherited by all classes that
 * intend to add objects to the game world
 */
public abstract class AbstractGameObject
{

	/**
	 * This first set of variables holds the basic position and
	 * sizing information for our game objects
	 */
	public Vector2 position ;
	public Vector2 dimension ;
	public Vector2 origin ;
	public Vector2 scale ;
	public float rotation ;
	public float stateTime ;
	public Animation animation ;

	/**
	 * This set of variables holds the movement information and the
	 * collision information for our game objects
	 */
	public Vector2 velocity ;
	public Vector2 terminalVelocity ;
	public Vector2 friction ;

	public Vector2 acceleration ;
	public Rectangle bounds ;

	public Body body ;

	/**
	 * Initializes all object variables
	 */
	public AbstractGameObject ()
	{
		position = new Vector2() ;
		dimension = new Vector2(1, 1) ;
		origin = new Vector2() ;
		scale = new Vector2(1, 1) ;
		rotation = 0 ;
		velocity = new Vector2() ;
		terminalVelocity = new Vector2(1,1) ;
		friction = new Vector2() ;
		acceleration = new Vector2() ;
		bounds = new Rectangle() ;
	}

	public void setAnimation(Animation animation)
	{
		this.animation = animation ;
		stateTime = 0 ;
	}

	/**
	 * Updates and applies any movement that was taken
	 */
	public void update(float deltaTime)
	{
		stateTime += deltaTime ;

		updateMotionX(deltaTime) ;
		updateMotionY(deltaTime) ;

		if(body == null)
		{
			// Move to new position
			position.x += velocity.x * deltaTime ;
			position.y += velocity.y * deltaTime ;
		}
		else
		{
			position.set(body.getPosition()) ;
			rotation = body.getAngle() * MathUtils.radiansToDegrees ;
		}
	}

	/**
	 * Calculates left and right movement to be applied by update
	 * keeping it within the terminal velocity
	 */
	protected void updateMotionX(float deltaTime)
	{
		if(velocity.x != 0)
		{
			// Apply friction
			if(velocity.x > 0)
			{
				velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0) ;
			}
			else
			{
				velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0) ;
			}
		}

		// Apply acceleration
		velocity.x += acceleration.x * deltaTime ;
		// Make sure the object's velocity does not exceed the
		// positive or negative terminal velocity
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x) ;
	}

	/**
	 * Calculates up and down movement to be applied by update
	 * keeping it within the terminal velocity
	 */
	protected void updateMotionY(float deltaTime)
	{
		if(velocity.y != 0)
		{
			// Apply friction
			if(velocity.y > 0)
			{
				velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0) ;
			}
			else
			{
				velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0) ;
			}
		}

		// Apply acceleration
		velocity.y += acceleration.y * deltaTime ;
		// Make sure the object's velocity does not exceed the
		// Positive or negative terminal velocity
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y) ;
	}

	public abstract void render(SpriteBatch batch) ;

	public void render(SpriteBatch batch, boolean slow) {
		// TODO Auto-generated method stub

	}

}


















