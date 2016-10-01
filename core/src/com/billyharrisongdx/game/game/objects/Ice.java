package com.billyharrisongdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch ;
import com.badlogic.gdx.graphics.g2d.TextureRegion ;
import com.billyharrisongdx.game.game.Assets ;

public class Ice extends AbstractGameObject
{
	/**
	 * Variables holding the ice texture region and
	 * its current collection status
	 */
	private TextureRegion regIce ;

	public boolean collected ;

	public Ice()
	{
		init() ;
	}

	/**
	 * Sets dimension for the ice and its bounds and sets
	 * its texture region and that it is not collected
	 */
	private void init()
	{
		dimension.set(0.5f, 0.5f) ;

		regIce = Assets.instance.ice.ice ;

		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y) ;

		collected = false ;
	}

	/**
	 * Draws the ice in the specified location
	 */
	public void render(SpriteBatch batch)
	{
		if(collected) return ;

		TextureRegion reg = null ;
		reg = regIce ;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
				dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false) ;
	}

	/**
	 * Returns ice point value
	 */
	public int getScore()
	{
		return 100 ;
	}
}
