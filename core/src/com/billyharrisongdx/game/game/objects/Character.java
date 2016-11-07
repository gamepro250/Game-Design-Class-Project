/**
 * Author: Billy Harrison
 *
 * Date: 10/1/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.game.objects;

import com.badlogic.gdx.Gdx ;
import com.badlogic.gdx.graphics.g2d.SpriteBatch ;
import com.badlogic.gdx.graphics.g2d.TextureRegion ;
import com.billyharrisongdx.game.game.Assets ;
import com.billyharrisongdx.game.util.Constants ;
import com.billyharrisongdx.game.util.GamePreferences ;
import com.billyharrisongdx.game.util.CharacterSkin ;
import com.badlogic.gdx.graphics.g2d.ParticleEffect ;

public class Character extends AbstractGameObject
{
	public static final String TAG = Character.class.getName() ;

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
	private ParticleEffect lavaDust = new ParticleEffect() ;

	public VIEW_DIRECTION viewDirection ;
	public float timeJumping ;
	public JUMP_STATE jumpState ;
	public static boolean hasFirePowerup ;
	public static float timeLeftFirePowerup ;
	public boolean grounded ;
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
        terminalVelocity.set(300.0f, 1.5f);
        friction.set(0.4f, 0.0f);
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT ;
		timeJumping = 0 ;
		// Power-ups
		hasFirePowerup = false ;
		timeLeftFirePowerup = 0 ;

		// Particle
		lavaDust.load(Gdx.files.internal("../core/assets/particles/lavaDust.part"), Gdx.files.internal("particles")) ;
	}

	/**
	 * Controls what occurs during each version of jumpState
	 */

	/**
	 * Used to activate fire power-up
	 */
	public static void setFirePowerup(boolean pickedUp)
	{
		hasFirePowerup = pickedUp ;
		if(pickedUp)
		{
			timeLeftFirePowerup = Constants.ITEM_FIRE_POWERUP_DURATION ;
			//terminalVelocity.x = 6 ;
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

        if(grounded)
        {
            lavaDust.setPosition(body.getPosition().x + dimension.x / 2, body.getPosition().y) ;

        	lavaDust.start() ;
        }
        else
        {
        	lavaDust.allowCompletion() ;
        }
        lavaDust.update(deltaTime) ;
    }

	/**
	 * Changes color of bunny when power-up is active and flip
	 * if bunny is moving left
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null ;

		// Draw Particles
		lavaDust.draw(batch) ;

		// Apply Skin Color
		batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor()) ;

		// Set special color when game object has a fire power-up
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