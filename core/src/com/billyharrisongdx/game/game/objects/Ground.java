/**
 * Author: Billy Harrison
 *
 * Date: 9/25/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch ;
import com.badlogic.gdx.graphics.g2d.TextureRegion ;
import com.billyharrisongdx.game.game.Assets ;
import com.badlogic.gdx.math.MathUtils ;

public class Ground extends AbstractGameObject
{
	private TextureRegion regLeft ; // Location of the left edge of the ground in the texture atlas
	private TextureRegion regCenter ; // Location of the middle of the ground in the texture atlas
	private TextureRegion regRight ; // Location of the right edge of the ground in the texture atlas
	private int length ; // # of times to repeat the middle of the ground
	private final float FLOAT_CYCLE_TIME = 2.0f ;
	private final float FLOAT_AMPLITUDE = 0.25f ;
	private float floatCycleTimeLeft ;
	private boolean floatingDownwards ;


	public Ground()
	{
		init() ;
	}

	private void init()
	{
		dimension.set(1, 1.5f) ;

		regLeft = Assets.instance.ground.groundLeft ;
		regCenter = Assets.instance.ground.groundCenter ;
		regRight = Assets.instance.ground.groundRight ;

		//Start length of this rock
		setLength(1) ;

		floatingDownwards = false ;
		floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME / 2) ;
	}

	/**
	 * Sets the length of the rock object
	 * @param length
	 */
	public void setLength(int length)
	{
		this.length = length ;
		// Update bounding box for collision detection
		bounds.set(0, 0, dimension.x * length, dimension.y/2) ;
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
	public void render(SpriteBatch batch, boolean slow)
	{
		if(slow)
		{
			TextureRegion reg = null ;
			float relX = 0 ;
			float relY = -0.75f ;

			batch.setColor(.5f, .5f, .5f, 1.0f);

			// Draw left edge
			reg = regLeft ;
			relX -= dimension.x / 4 ;
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
					origin.x, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y,
					rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
					reg.getRegionHeight(), false, false) ;

			// Draw middle
			relX = 0 ;
			reg = regCenter ;
			for(int i = 0; i < length; i++)
			{
				batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
						origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
						rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
						reg.getRegionHeight(), false, false) ;
				relX += dimension.x ;
			}

			// Draw right edge
			reg = regRight ;
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
					origin.x + dimension.x / 8, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y,
					rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
					reg.getRegionHeight(), false, false) ;

			batch.setColor(Color.WHITE) ;
		}
		else
		{
			TextureRegion reg = null ;

			float relX = 0 ;
			float relY = -0.75f ;

			// Draw left edge
			reg = regLeft ;
			relX -= dimension.x / 4 ;
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
					origin.x, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y,
					rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
					reg.getRegionHeight(), false, false) ;

			// Draw middle
			relX = 0 ;
			reg = regCenter ;
			for(int i = 0; i < length; i++)
			{

				batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
						origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
						rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
						reg.getRegionHeight(), false, false) ;
				relX += dimension.x ;
			}

			// Draw right edge
			reg = regRight ;
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
					origin.x + dimension.x / 8, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y,
					rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
					reg.getRegionHeight(), false, false) ;
		}
	}

	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime) ;

		floatCycleTimeLeft -= deltaTime ;

		if(floatCycleTimeLeft <= 0)
		{
			floatCycleTimeLeft = FLOAT_CYCLE_TIME ;
			floatingDownwards = !floatingDownwards ;
			body.setLinearVelocity(0, FLOAT_AMPLITUDE * (floatingDownwards ? -1 : 1)) ;
		}
		else
		{
			body.setLinearVelocity(body.getLinearVelocity().scl(0.98f));
		}
	}

	@Override
	public void render(SpriteBatch batch)
	{
	}
}






