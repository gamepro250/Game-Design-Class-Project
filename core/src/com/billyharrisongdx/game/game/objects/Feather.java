/**
 * Author: Billy Harrison
 *
 * Date: 10/1/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch ;
import com.badlogic.gdx.graphics.g2d.TextureRegion ;
import com.billyharrisongdx.game.game.Assets ;

public class Feather extends AbstractGameObject
{
	/**
	 * Holds feather texture region and collected status
	 */
	private TextureRegion regFeather ;

	public boolean collected ;

	public Feather()
	{
		init() ;
	}

	/**
	 * Sets feather size, region, bounds, and collected status
	 */
	private void init()
	{

		dimension.set(0.5f, 0.5f) ;

		regFeather = Assets.instance.feather.feather ;

		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y) ;

		collected = false ;
	}

	/**
	 * Draws feather in specified location
	 */
	public void render(SpriteBatch batch)
	{
		if(collected) return ;

		TextureRegion reg = null ;
		reg = regFeather ;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
				dimension.x, dimension.y,scale.x, scale.y,rotation,reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(),reg.getRegionHeight(), false, false) ;
	}

	/**
	 * Returns Feather point value
	 */
	public int getScore()
	{
		return 250 ;
	}
}