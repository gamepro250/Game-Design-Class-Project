/**
 * Author: Billy Harrison
 *
 * Date: 9/25/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch ;
import com.badlogic.gdx.graphics.g2d.TextureRegion ;
import com.billyharrisongdx.game.game.Assets ;
import com.badlogic.gdx.math.MathUtils ;
import com.badlogic.gdx.math.Vector2 ;

/**
 * Class that contains information on how to draw the
 * Rock game object. Includes the outer edges and the middle
 */
public class Rock extends AbstractGameObject
{
	private TextureRegion regEdge ; // Location of the edge of the rock in the texture atlas
	private TextureRegion regMiddle ; // Location of the middle of the rock in the texture atlas

	private int length ; // # of times to repeat the middle of the rock
	private final float FLOAT_CYCLE_TIME = 2.0f ;
	private final float FLOAT_AMPLITUDE = 0.25f ;
	private float floatCycleTimeLeft ;
	private boolean floatingDownwards ;
	private Vector2 floatTargetPosition ;

	public Rock()
	{
		init() ;
	}

	private void init()
	{
		dimension.set(1, 1.5f) ;

		regEdge = Assets.instance.rock.edge ;
		regMiddle = Assets.instance.rock.middle ;

		//Start length of this rock
		setLength(1) ;

		floatingDownwards = false ;
		floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME / 2) ;
		floatTargetPosition = null ;
	}

	/**
	 * Sets the length of the rock object
	 * @param length
	 */
	public void setLength(int length)
	{
		this.length = length ;
		// Update bounding box for collision detection
		bounds.set(0, 0, dimension.x * length, dimension.y) ;
	}

	/**
	 * Increases length of rock object by amount
	 * @param amount
	 */
	public void increaseLength(int amount)
	{
		setLength(length + amount) ;
	}

	/**
	 * Implementation of AbstractGameObject render method
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null ;

		float relX = 0 ;
		float relY = 0 ;

		// Draw left edge
		reg = regEdge ;
		relX -= dimension.x / 4 ;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
				origin.x, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false) ;

		// Draw middle
		relX = 0 ;
		reg = regMiddle ;
		for(int i = 0; i < length; i++)
		{
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
					origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
					rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
					reg.getRegionHeight(), false, false) ;
			relX += dimension.x ;
		}

		// Draw right edge
		reg = regEdge ;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
				origin.x + dimension.x / 8, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), true, false) ;

	}

	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime) ;

		floatCycleTimeLeft -= deltaTime ;
		if(floatTargetPosition == null)
		{
			floatTargetPosition = new Vector2(position) ;
		}

		if(floatCycleTimeLeft <= 0)
		{
			floatCycleTimeLeft = FLOAT_CYCLE_TIME ;
			floatingDownwards = !floatingDownwards ;
			floatTargetPosition.y += FLOAT_AMPLITUDE * (floatingDownwards ? -1 : 1) ;
		}
		position.lerp(floatTargetPosition, deltaTime) ;
	}
}








