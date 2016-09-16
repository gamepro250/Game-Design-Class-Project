/**
 * Author: Billy Harrison
 *
 * Date: 9/15/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.util ;

import com.badlogic.gdx.graphics.OrthographicCamera ;
import com.badlogic.gdx.graphics.g2d.Sprite ;
import com.badlogic.gdx.math.MathUtils ;
import com.badlogic.gdx.math.Vector2 ;

public class CameraHelper
{
	private static final String TAG = CameraHelper.class.getName() ;

	private final float MAX_ZOOM_IN = 0.25f ;
	private final float MAX_ZOOM_OUT = 10.0f ;

	private Vector2 position ;
	private float zoom ;
	private Sprite target ;

	public CameraHelper()
	{
		position = new Vector2() ;
		zoom = 1.0f ;
	}

	/**
	 * Updates the games state based on the deltaTime
	 */
	public void update(float deltaTime)
	{
		if(!hasTarget())
			return ;

		position.x = target.getX() + target.getOriginX() ;
		position.y = target.getY() + target.getOriginY() ;
	}

	/**
	 * Set the position of the camera
	 */
	public void setPosition(float x, float y)
	{
		this.position.set(x, y) ;
	}


	public Vector2 getPosition()
	{
		return position ;
	}

	/**
	 * Adjust the zoom of the camera
	 */
	public void addZoom(float amount)
	{
		setZoom(zoom + amount) ;
	}

	/**
	 * Sets the zoom to input value
	 */
	public void setZoom(float zoom)
	{
		this.zoom = MathUtils.clamp(zoom,  MAX_ZOOM_IN, MAX_ZOOM_OUT) ;
	}

	/**
	 * Get cameras current zoom level
	 */
	public float getZoom()
	{
		return zoom ;
	}

	/**
	 * Gives camera a target to follow
	 */
	public void setTarget(Sprite target)
	{
		this.target = target ;
	}

	/**
	 * Returns cameras current target
	 */
	public Sprite getTarget()
	{
		return target ;
	}

	/**
	 * Inquires if camera has target
	 */
	public boolean hasTarget()
	{
		return target != null ;
	}

	/**
	 * Inquires if argument is cameras current target
	 */
	public boolean hasTarget(Sprite target)
	{
		return hasTarget() && this.target.equals(target) ;
	}

	/**
	 * Applies settings to camera
	 */
	public void applyTo(OrthographicCamera camera)
	{
		camera.position.x = position.x ;
		camera.position.y = position.y ;
		camera.zoom = zoom ;
		camera.update();
	}
}













