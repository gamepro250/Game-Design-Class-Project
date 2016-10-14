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
 * Clouds game object. Uses three different
 * cloud images
 */
public class Clouds extends AbstractGameObject
{
	private float length ;

	private Array<TextureRegion> regClouds ;
	private Array<Cloud> clouds ;

	/**
	 * Creates single cloud to be added to array of clouds
	 */
	private class Cloud extends AbstractGameObject
	{
		private TextureRegion regCloud ; // Holds the clouds location on texture atlas

		public Cloud() {}

		public void setRegion(TextureRegion region)
		{
			regCloud = region ;
		}

		/**
		 * Implementation of Abstract render method
		 */
		@Override
		public void render(SpriteBatch batch)
		{
			TextureRegion reg = regCloud ;
			batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y,
					origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
					reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false) ;
		}
	}

	public Clouds(float length)
	{
		this.length = length ;
		init() ;
	}

	private void init()
	{
		dimension.set(3.0f, 1.5f) ;

		// Store the texture atlas locations of the cloud images in one array
		regClouds = new Array<TextureRegion>() ;
		regClouds.add(Assets.instance.levelDecoration.cloud01) ;
		regClouds.add(Assets.instance.levelDecoration.cloud02) ;
		regClouds.add(Assets.instance.levelDecoration.cloud03) ;

		int distFac = 5 ; // Spacing between clouds
		int numClouds = (int) (length / distFac) ; // # of clouds based on length of game area
		clouds = new Array<Cloud>(2 * numClouds) ; // Initializes clouds array to hold clouds

		// Adds clouds, shifting each by the distance factor, to the cloud array
		for(int i = 0; i < numClouds; i++)
		{
			Cloud cloud = spawnCloud() ;
			cloud.position.x = i * distFac ;
			clouds.add(cloud) ;
		}
	}

	/**
	 * Chooses a random cloud of the three to draw
	 * and determines the location to draw it
	 * @return cloud
	 */
	private Cloud spawnCloud()
	{
		Cloud cloud = new Cloud() ;
		cloud.dimension.set(dimension) ;
		// Select random cloud image
		cloud.setRegion(regClouds.random());
		// Position
		Vector2 pos = new Vector2() ;
		pos.x = length + 10 ; // Position after end of level
		pos.y += 1.75 ; // Base position
		pos.y += MathUtils.random(0.0f, 0.2f) * (MathUtils.randomBoolean() ? 1 : -1) ; // Random additional position
		cloud.position.set(pos) ;

		// Speed
		Vector2 speed = new Vector2() ;
		speed.x += 0.5f ; // Base speed
		// Random additional speed
		speed.x += MathUtils.random(0.0f, 0.75f) ;
		cloud.terminalVelocity.set(speed) ;
		speed.x *= -1 ; // Move left
		cloud.velocity.set(speed) ;

		return cloud ;
	}

	/**
	 * Implementation of Abstract render method
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		for(Cloud cloud : clouds)
			cloud.render(batch) ;
	}

	@Override
	public void update(float deltaTime)
	{
		for(int i = clouds.size - 1; i >= 0; i--)
		{
			Cloud cloud = clouds.get(i) ;
			cloud.update(deltaTime) ;
			if(cloud.position.x < - 10)
			{
				// Cloud moved outside the world.
				// Destroy and spawn new cloud at end of level.
				clouds.removeIndex(i) ;
				clouds.add(spawnCloud()) ;
			}
		}
	}
}