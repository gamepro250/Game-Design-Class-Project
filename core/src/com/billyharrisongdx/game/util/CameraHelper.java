/**
 * Author: Billy Harrison
 *
 * Date: 9/15/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.util ;

import com.badlogic.gdx.graphics.OrthographicCamera ;
import com.badlogic.gdx.math.MathUtils ;
import com.badlogic.gdx.math.Vector2 ;
import com.billyharrisongdx.game.game.objects.AbstractGameObject ;

public class CameraHelper
{
	private static final String TAG = CameraHelper.class.getName() ;

	/**
	 * Hold camera's max zoom, position, current zoom, and target object
	 */
	private final float MAX_ZOOM_IN = 0.25f ;
	private final float MAX_ZOOM_OUT = 10.0f ;
	private Vector2 position ;
	private float zoom ;
	private AbstractGameObject target ;

	/**
	 * Initializes position and starting zoom
	 */
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

		position.x = target.position.x + target.origin.x ;
		position.y = target.position.y + target.origin.y ;

		// Prevent camera from moving down too far
		position.y = Math.max(-1f, position.y) ;
	}

	/**
	 * Set the position of the camera
	 */
	public void setPosition(float x, float y)
	{
		this.position.set(x, y) ;
	}

	/**
	 * Get the current position of the camera
	 * @return position
	 */
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
	public void setTarget(AbstractGameObject target)
	{
		this.target = target ;
	}

	/**
	 * Returns cameras current target
	 */
	public AbstractGameObject getTarget()
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
	public boolean hasTarget(AbstractGameObject target)
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