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
import com.billyharrisongdx.game.game.Assets ;
import com.badlogic.gdx.math.Vector2 ;


/**
 * Class that handles the drawing of the mountain
 * game object. Includes Left and Right pieces
 */
public class Mountains extends AbstractGameObject
{
	private TextureRegion regMountainLeft ; // Location of the left mountain in the texture atlas
	private TextureRegion regMountainRight ; // Location of the right mountain in the texture atlas

	private int length ; // length of mountain range on screen

	public Mountains(int length)
	{
		this.length = length ;
		init() ;
	}

	private void init()
	{
		dimension.set(10, 2) ;

		regMountainLeft = Assets.instance.levelDecoration.mountainLeft ;
		regMountainRight = Assets.instance.levelDecoration.mountainRight ;

		// Shift mountain and extend length
		origin.x = -dimension.x * 2 ;
		length += dimension.x * 2 ;
	}

	/**
	 * Draws mountains by alternating the left and right mountain images
	 * to stretch across the screen
	 * @param batch
	 * @param offsetX
	 * @param offsetY
	 * @param tintColor
	 */
	private void drawMountain(SpriteBatch batch, float offsetX, float offsetY, float tintColor, float parallaxSpeedX)
	{
		TextureRegion reg = null ;
		batch.setColor(tintColor, tintColor, tintColor, 1) ;
		float xRel = dimension.x * offsetX ;
		float yRel = dimension.y * offsetY ;

		// mountains span the whole level
		int mountainLength = 0 ;


		mountainLength += MathUtils.ceil(length / (2 * dimension.x) * (1 - parallaxSpeedX)) ;
		mountainLength += MathUtils.ceil(0.5f + offsetX) ;
		for(int i = 0; i < mountainLength; i++)
		{
			// Mountain left
			reg = regMountainLeft ;
			batch.draw(reg.getTexture(),
			origin.x + xRel + position.x * parallaxSpeedX,
			position.y + origin.y + yRel,
			origin.x, origin.y,
			dimension.x, dimension.y,
			scale.x, scale.y,
			rotation,
			reg.getRegionX(), reg.getRegionY(),
			reg.getRegionWidth(), reg.getRegionHeight(),
			false, false) ;
			xRel += dimension.x ;

			// Mountain right
			reg = regMountainRight ;
			batch.draw(reg.getTexture(),
			origin.x + xRel + position.x * parallaxSpeedX,
			position.y + origin.y + yRel,
			origin.x, origin.y,
			dimension.x, dimension.y,
			scale.x, scale.y,
			rotation,
			reg.getRegionX(), reg.getRegionY(),
			reg.getRegionWidth(), reg.getRegionHeight(),
			false, false) ;
			xRel += dimension.x ;
		}
		// Reset color to white
		batch.setColor(1, 1, 1, 1) ;
	}

	@Override
	public void render(SpriteBatch batch)
	{
		// 80% Distant mountains (dark gray)
		drawMountain(batch, 0.5f, 0.5f, 0.5f, 0.8f) ;
		// 50% Distant mountains (gray)
		drawMountain(batch, 0.25f, 0.25f, 0.7f, 0.5f) ;
		// 30% Distant mountains (light gray)
		drawMountain(batch, 0.0f, 0.0f, 0.9f, 0.3f) ;
	}

	public void updateScrollPosition(Vector2 camPosition)
	{
		position.set(camPosition.x, position.y) ;
	}
}


