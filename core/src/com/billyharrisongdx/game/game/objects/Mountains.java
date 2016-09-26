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
import com.badlogic.gdx.math.MathUtils ;
import com.badlogic.gdx.math.Vector2 ;
import com.badlogic.gdx.utils.Array ;
import com.billyharrisongdx.game.game.Assets ;

/**
 * Class to handle the drawing of the
 * Mountain game object.
 */
public class Mountains extends AbstractGameObject
{
	private float length ;

	private Array<TextureRegion> regMountains ;
	private Array<Mountain> mountains ;

	/**
	 * Creates single mounatin to be added to array of mounatins
	 */
	private class Mountain extends AbstractGameObject
	{
		private TextureRegion regMountain ; // Holds the mounatins location on texture atlas

		public Mountain() {}

		public void setRegion(TextureRegion region)
		{
			regMountain = region ;
		}

		/**
		 * Implementation of Abstract render method
		 */
		@Override
		public void render(SpriteBatch batch)
		{
			TextureRegion reg = regMountain ;
			batch.draw(reg.getTexture(), position.x + origin.x, position.y - 1,
					origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
					reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false) ;
		}
	}

	public Mountains(float length)
	{
		this.length = length ;
		init() ;
	}

	private void init()
	{
		dimension.set(3.0f, 1.5f) ;

		// Store the texture atlas locations of the mounatin images in one array
		regMountains = new Array<TextureRegion>() ;
		regMountains.add(Assets.instance.levelDecoration.mountain) ;


		int distFac = 8 ; // Spacing between mounatins
		int numMountains = (int) (length / distFac) ; // # of mounatins based on length of game area
		mountains = new Array<Mountain>(2 * numMountains) ; // Initializes mounatins array to hold mounatins

		// Adds mountains, shifting each by the distance factor, to the mounatin array
		for(int i = 0; i < numMountains; i++)
		{
			Mountain mountain = spawnMountain() ;
			mountain.position.x = i * distFac ;
			mountains.add(mountain) ;
		}
	}

	/**
	 *
	 */
	private Mountain spawnMountain()
	{
		Mountain mountain = new Mountain() ;
		mountain.dimension.set(dimension) ;
		// Select random mounatin image
		mountain.setRegion(regMountains.random());
		// Position
		Vector2 pos = new Vector2() ;
		pos.x = length + 10 ; // Position after end of level
		pos.y += 0.75 ; // Base position
		pos.y += MathUtils.random(0.0f, 0.2f) * (MathUtils.randomBoolean() ? 1 : -1) ; // Random additional position
		mountain.position.set(pos) ;
		return mountain ;
	}

	/**
	 * Implementation of Abstract render method
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		for(Mountain mounatin : mountains)
			mounatin.render(batch) ;
	}
}