/**
 * Author: Billy Harrison
 *
 * Date: 10/8/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.util;

import com.badlogic.gdx.graphics.Color ;

public enum CharacterSkin
{
	// Diffent color choices for skin
	WHITE("White", 1.0f, 1.0f, 1.0f),
	GRAY("Gray", 0.7f, 0.7f, 0.7f),
	BROWN("Brown", 0.7f, 0.5f, 0.3f) ;

	private String name ;
	private Color color = new Color() ;
	private CharacterSkin(String name, float r, float g, float b)
	{
		this.name = name ;
		color.set(r, g, b, 1.0f) ;
	}

	/**
	 * Returns color name
	 */
	@Override
	public String toString()
	{
		return name ;
	}
	/**
	 * Returns color
	 */
	public Color getColor()
	{
		return color ;
	}
}