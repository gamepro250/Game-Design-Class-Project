package com.billyharrisongdx.game.game.objects;

import com.badlogic.gdx.Gdx ;
import com.badlogic.gdx.graphics.g2d.SpriteBatch ;
import com.badlogic.gdx.graphics.g2d.TextureRegion ;
import com.billyharrisongdx.game.game.Assets ;
import com.billyharrisongdx.game.util.Constants ;

public class Character extends AbstractGameObject
{
	public static final String TAG = Character.class.getName() ;

	/**
	 * Set the minimun and maximum jumping times with and
	 * without the feather power-up
	 */
	private final float JUMP_TIME_MAX = 0.3f ;
	private final float JUMP_TIME_MIN = 0.1f ;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f ;

	/**
	 * enum for the possible movement directions
	 */
	public enum VIEW_DIRECTION
	{
		LEFT, RIGHT
	}

	/**
	 * enum for used in controlling the characters state
	 */
	public enum JUMP_STATE
	{
		GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
	}

	/**
	 * Information on the character
	 * direction, power-up remaining, etc.
	 */
	private TextureRegion regHead ;

	public VIEW_DIRECTION viewDirection ;
	public float timeJumping ;
	public JUMP_STATE jumpState ;
	public boolean hasFirePowerup ;
	public float timeLeftFirePowerup ;

	public Character()
	{
		init() ;
	}

	/**
	 * Initializes all variables to starting points
	 * starting direction, power-up disabled, starting speed, etc.
	 */
	public void init()
	{
		dimension.set(1, 1) ;
		regHead = Assets.instance.character.character ;
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2) ;
		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y) ;
		// Set physics values
		terminalVelocity.set(3.0f, 4.0f) ;
		friction.set(12.0f, 0.0f) ;
		acceleration.set(0.0f, -25.0f) ;
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT ;
		// Jump state
		jumpState = JUMP_STATE.FALLING ;
		timeJumping = 0 ;
		// Power-ups
		hasFirePowerup = false ;
		timeLeftFirePowerup = 0 ;
	}

	/**
	 * Controls what occurs during each version of jumpState
	 */
	public void setJumping(boolean jumpKeyPressed)
	{
		switch(jumpState)
		{
			case GROUNDED: // Character is standing on a platform
				if(jumpKeyPressed)
				{
					// Start counting jump time from the beginning
					timeJumping = 0 ;
					jumpState = JUMP_STATE.JUMP_RISING ;
				}
				break ;
			case JUMP_RISING: // Rising in the air
				if(!jumpKeyPressed)
				{
					jumpState = JUMP_STATE.JUMP_FALLING ;
				}
				break ;
			case FALLING: // Falling down
			case JUMP_FALLING: // Falling down after jump
				if(jumpKeyPressed && hasFirePowerup)
				{
					timeJumping = JUMP_TIME_OFFSET_FLYING ;
					jumpState = JUMP_STATE.JUMP_RISING ;
				}
				break ;
		}
	}

	/**
	 * Used to activate feather power-up
	 */
	public void setFirePowerup(boolean pickedUp)
	{
		hasFirePowerup = pickedUp ;
		if(pickedUp)
		{
			timeLeftFirePowerup = Constants.ITEM_FIRE_POWERUP_DURATION ;
		}
	}

	/**
	 * Returns if power-up is active
	 */
	public boolean hasFirePowerup()
	{
		return hasFirePowerup && timeLeftFirePowerup > 0 ;
	}

	/**
	 * Alter view direction based on x velocity and
	 * tracks time remaining on power-up
	 */
    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        if (velocity.x != 0)
        {
            viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
        }
        if (timeLeftFirePowerup > 0)
        {
            timeLeftFirePowerup -= deltaTime;
            if (timeLeftFirePowerup < 0)
            {
                // disable power-up
                timeLeftFirePowerup = 0;
                setFirePowerup(false);
            }
        }
    }

	/**
	 * Controls y movement based on current jumpState
	 * of bunny
	 */
	@Override
	protected void updateMotionY(float deltaTime)
	{
		switch(jumpState)
		{
			case GROUNDED:
				jumpState = JUMP_STATE.FALLING ;
				break ;
			case JUMP_RISING:
				// Keep track of jump time
				timeJumping += deltaTime ;
				// Jump time left?
				if(timeJumping <= JUMP_TIME_MAX)
				{
					// Still jumping
					velocity.y = terminalVelocity.y ;
				}
				break ;
			case FALLING:
				break ;
			case JUMP_FALLING:
				// Add delta times to track jump time
				timeJumping += deltaTime ;
				// Jump to minimal height if jump key was pressed too short
				if(timeJumping > 0 && timeJumping <= JUMP_TIME_MIN)
				{
					// Still jumping
					velocity.y = terminalVelocity.y ;
				}
		}
		if(jumpState != JUMP_STATE.GROUNDED)
		{
			super.updateMotionY(deltaTime) ;
		}
	}

	/**
	 * Changes color of bunny when power-up is active and flip
	 * if bunny is moving left
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null ;

		// Set special color when game object has a feather power-up
		if(hasFirePowerup)
		{
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f) ;
		}

		// Draw image
		reg = regHead ;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
				dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(),reg.getRegionWidth(), reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT, false) ;

		// Reset color to white
		batch.setColor(1, 1, 1, 1) ;
	}
}