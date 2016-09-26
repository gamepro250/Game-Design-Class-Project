/**
 * Author: Billy Harrison
 *
 * Date: 9/25/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.game.objects ;

import com.badlogic.gdx.graphics.g2d.SpriteBatch ;
import com.badlogic.gdx.math.Vector2 ;

/**
 * Contains methods that must be inherited by all classes that
 * intend to add objects to the game world
 */
public abstract class AbstractGameObject
{
	public Vector2 position ;
	public Vector2 dimension ;
	public Vector2 origin ;
	public Vector2 scale ;
	public float rotation ;

	public AbstractGameObject ()
	{
		position = new Vector2() ;
		dimension = new Vector2(1, 1) ;
		origin = new Vector2() ;
		scale = new Vector2(1, 1) ;
		rotation = 0 ;
	}

	public void update(float deltaTime)
	{

	}

	public abstract void render(SpriteBatch batch) ;

}