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
public class Ground extends AbstractGameObject
{
	private TextureRegion regLeft ; // Location of the left edge of the ground in the texture atlas
	private TextureRegion regCenter ; // Location of the middle of the ground in the texture atlas
	private TextureRegion regRight ; // Location of the right edge of the ground in the texture atlas
	private int length ; // # of times to repeat the middle of the ground

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
	}

	/**
	 * Sets the length of the rock object
	 * @param length
	 */
	public void setLength(int length)
	{
		this.length = length ;
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






