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


/**
 * Class that handles the drawing of the mountain
 * game object. Includes Left and Right pieces
 */
public class Volcano extends AbstractGameObject
{
	private TextureRegion regVolcano ; // Location of the left mountain in the texture atlas


	private int length ; // length of mountain range on screen

	public Volcano(int length)
	{
		this.length = length ;
		init() ;
	}

	private void init()
	{
		dimension.set(10, 2) ;

		regVolcano = Assets.instance.levelDecoration.volcano ;

		// Shift mountain and extend length
		origin.x = -dimension.x * 2 ;
		length += dimension.x * 2 ;
	}

	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null ;

		// mountains span the whole level
		int mountainLength = 0 ;
		mountainLength += MathUtils.ceil(length) ;
		for(int i = 0; i < mountainLength; i++)
		{

			reg = regVolcano ;
			batch.draw(reg.getTexture(), origin.x + dimension.x * i, position.y - 1,
					origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y * 3, rotation,
					reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false) ;

		}
	}
}
