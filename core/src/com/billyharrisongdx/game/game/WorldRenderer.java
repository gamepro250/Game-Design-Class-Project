/**
 * Author: Billy Harrison
 *
 * Date: 9/15/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.game;

import com.badlogic.gdx.graphics.OrthographicCamera ;
import com.badlogic.gdx.graphics.g2d.SpriteBatch ;
import com.badlogic.gdx.utils.Disposable ;
import com.billyharrisongdx.game.util.Constants ;
import com.badlogic.gdx.graphics.g2d.Sprite ;

public class WorldRenderer {

	private OrthographicCamera camera ;
	private SpriteBatch batch ;
	private WorldController worldController ;

	public WorldRenderer(WorldController worldController)
	{
		this.worldController = worldController ;
		init() ;
	}
	private void init()
	{
		batch = new SpriteBatch() ;
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT) ; // Creates camera using the view port restraints
		camera.position.set(0, 0, 0) ; // Position camera at origin
		camera.update() ;
	}

	public void render()
	{
		renderTestObjects() ;
	}
	private void renderTestObjects()
	{
		worldController.cameraHelper.applyTo(camera) ;
		batch.setProjectionMatrix(camera.combined) ;
		batch.begin() ;
		for(Sprite sprite : worldController.testSprites)
		{
			sprite.draw(batch) ;
		}
		batch.end() ;
	}
	/**
	 * Alters size of the view port
	 */
	public void resize(int width, int height)
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width ;
		camera.update() ;
	}

	/**
	 * Acts as a clean up, releasing all resources
	 */
	public void dispose()
	{
		batch.dispose() ;
	}
}
