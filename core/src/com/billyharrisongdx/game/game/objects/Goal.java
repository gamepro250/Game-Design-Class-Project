/**
 * Author: Billy Harrison
 *
 * Date: 10/1/16
 *
 * Class: Game Design
 */
package com.billyharrisongdx.game.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch ;
import com.badlogic.gdx.graphics.g2d.TextureRegion ;
import com.billyharrisongdx.game.game.Assets ;
import com.badlogic.gdx.math.MathUtils ;

public class Goal extends AbstractGameObject
{
	/**
	 * Holds Fire texture region and collected status
	 */
	private TextureRegion regGoal ;

	public int score ;

	public Goal()
	{
		init() ;
	}

	/**
	 * Sets fire size, region, bounds, and collected status
	 */
	private void init()
	{

		dimension.set(2.0f, 2.0f) ;

		regGoal = Assets.instance.boat.boat ;

		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y * 10) ;
	}

	/**
	 * Draws feather in specified location
	 */
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null ;
		reg = regGoal ;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
				dimension.x, dimension.y,scale.x, scale.y,rotation,reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(),reg.getRegionHeight(), true, false) ;
	}
}
