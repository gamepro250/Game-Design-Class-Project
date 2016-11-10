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
import com.badlogic.gdx.math.MathUtils ;

public class GoldCoin extends AbstractGameObject
{
	/**
	 * Variables holding the gold coin texture region and
	 * its current collection status
	 */
	private TextureRegion regGoldCoin ;

	public boolean collected ;

	public GoldCoin()
	{
		init() ;
	}

	/**
	 * Sets dimension for the coin and its bounds and sets
	 * its texture region and that it is not collected
	 */
	private void init()
	{
		dimension.set(0.5f, 0.5f) ;

		setAnimation(Assets.instance.goldCoin.animGoldCoin) ;
		stateTime = MathUtils.random(0.0f, 1.0f) ;

		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y) ;

		collected = false ;
	}

	/**
	 * Draws the gold coin in the specified location
	 */
	public void render(SpriteBatch batch)
	{
		if(collected) return ;

		TextureRegion reg = null ;
		reg = animation.getKeyFrame(stateTime, true) ;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
				dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false) ;
	}

	/**
	 * Returns coin point value
	 */
	public int getScore()
	{
		return 100 ;
	}
}