/**
 * Author: Billy Harrison
 *
 * Date: 10/1/16
 *
 * Class: Game Design
 */
package com.billyharrisongdx.game.game;

import com.badlogic.gdx.Gdx ;
import com.badlogic.gdx.graphics.Pixmap ;
import com.badlogic.gdx.graphics.g2d.SpriteBatch ;
import com.badlogic.gdx.utils.Array ;
import com.billyharrisongdx.game.game.objects.AbstractGameObject ;
import com.billyharrisongdx.game.game.objects.Ground ;
import com.billyharrisongdx.game.game.objects.Mountains ;
import com.billyharrisongdx.game.game.objects.LavaOverlay ;
import com.billyharrisongdx.game.game.objects.Trees ;
import com.billyharrisongdx.game.game.objects.Volcano ;
import com.billyharrisongdx.game.game.objects.Ice ;
import com.billyharrisongdx.game.game.objects.Fire ;
import com.billyharrisongdx.game.game.objects.Goal;
import com.billyharrisongdx.game.game.objects.Character ;

public class Level
{
	public static final String TAG = Level.class.getName() ;

	/**
	 * Color data used to compare from the
	 * level map to determine what block
	 * is to be drawn
	 */
	public enum BLOCK_TYPE
	{
		EMPTY(0, 0, 0), // Black
		GROUND(0, 255, 0), // Green
		SLOW_GROUND(0, 255, 255) ,
		PLAYER_SPAWNPOINT(255, 255, 255), // White
		FIRE(255, 0, 255), // Purple
		ICE(255, 255, 0), // Yellow
		GOAL(0, 0, 255) ; // Blue

		private int color ;

		/**
		 * Stores color values into one int for easy comparison
		 * @param r
		 * @param g
		 * @param b
		 */
		private BLOCK_TYPE(int r, int g, int b)
		{
			color = r << 24 | g << 16 | b << 8 | 0xff ;
		}

		/**
		 * Compares to colors
		 * @param Color
		 * @return true if colors are the same
		 */
		public boolean sameColor(int Color)
		{
			return this.color == Color ;
		}

		/**
		 * @return the color of a block
		 */
		public int getColor()
		{
			return color ;
		}
	}
		// Objects
		public Array<Ground> grounds ;
		public Array<Ground> slowGrounds ;
		public Character character ;
		public Goal boat ;
		public Array<Ice> ice ;
		public Array<Fire> fire ;

		// Decoration
		public Trees trees ;
		public Mountains mountains ;
		public LavaOverlay lavaOverlay ;
		public Volcano volcano ;

		public boolean goalReached = false ;

		/**
		 * Initiates a level using the input filename
		 * @param filename
		 */
		public Level(String filename)
		{
			init(filename) ;
		}

		private void init(String filename)
		{
			// Player character
			character = null ;
			boat = null ;

			// Objects
			grounds = new Array<Ground>() ;
			slowGrounds = new Array<Ground>() ;
			ice = new Array<Ice>() ;
			fire = new Array<Fire>() ;

			// Load image file that represents the level data
			Pixmap pixmap = new Pixmap(Gdx.files.internal(filename)) ;
			// Scan pixels from top-left to bottom-right
			int lastPixel = -1 ;
			for(int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
			{
				for(int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
				{
					AbstractGameObject obj = null ;
					float offsetHeight = -3f ;
					// Height grows from bottom to top
					float baseHeight = pixmap.getHeight() - pixelY ;
					// Get color of current pixel as 32-bit RGBA value
					int currentPixel = pixmap.getPixel(pixelX, pixelY) ;
					// Find matching color value to identify block type at (x, y)
					// point and create the corresponding game object if there is
					// a match

					// Empty space
					if(BLOCK_TYPE.EMPTY.sameColor(currentPixel))
					{
						// Do nothing
					}
					// Goal
					else if(BLOCK_TYPE.GOAL.sameColor(currentPixel))
					{
						obj = new Goal() ;
						obj.position.set(pixelX, baseHeight + offsetHeight) ;
						boat = (Goal) obj ;
					}
					// Ground
					else if(BLOCK_TYPE.GROUND.sameColor(currentPixel))
					{
						if(lastPixel != currentPixel)
						{
							obj = new Ground() ;
							obj.position.set(pixelX, baseHeight + offsetHeight) ;
							grounds.add((Ground) obj) ;
						}
						else
						{
							grounds.get(grounds.size - 1).increaseLength(1) ;
						}
					}
					else if(BLOCK_TYPE.SLOW_GROUND.sameColor(currentPixel))
					{
						if(lastPixel != currentPixel)
						{
							obj = new Ground() ;
							obj.position.set(pixelX, baseHeight + offsetHeight) ;
							slowGrounds.add((Ground) obj) ;
						}
						else
						{
							slowGrounds.get(slowGrounds.size - 1).increaseLength(1) ;
						}
					}
					// Player spawn point
					else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
					{
						obj = new Character() ;
						obj.position.set(pixelX, baseHeight + offsetHeight); //baseHeight * obj.dimension.y + offsetHeight) ;
						character = (Character) obj ;
					}
					// Fire
					else if(BLOCK_TYPE.FIRE.sameColor(currentPixel))
					{
						obj = new Fire() ;
						obj.position.set(pixelX, baseHeight + offsetHeight) ;//baseHeight * obj.dimension.y + offsetHeight) ;
						fire.add((Fire) obj);
					}
					// Ice
					else if(BLOCK_TYPE.ICE.sameColor(currentPixel))
					{
						obj = new Ice() ;
						obj.position.set(pixelX, baseHeight + offsetHeight) ;// baseHeight * obj.dimension.y + offsetHeight) ;
						ice.add((Ice) obj) ;
					}
					// Unknown object/pixel color
					else
					{
						int r = 0xff & (currentPixel >>> 24) ; // Red color channel
						int g = 0xff & (currentPixel >>> 16) ; // Green color channel
						int b = 0xff & (currentPixel >>> 8) ; // Blue collor channel
						int a = 0xff & currentPixel ; // Alpha channel
						Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">") ;
					}
					lastPixel = currentPixel ;
				}
			}

			// Decoration
			trees = new Trees(pixmap.getWidth()) ;
			trees.position.set(0, 2) ;
			mountains = new Mountains(pixmap.getWidth()) ;
			mountains.position.set(-1, -1) ;
			lavaOverlay = new LavaOverlay(pixmap.getWidth()) ;
			lavaOverlay.position.set(0, -3.75f) ;
			volcano = new Volcano(pixmap.getWidth()) ;

			// Free memory
			pixmap.dispose() ;
			Gdx.app.debug(TAG, "level'" + filename + "' loaded") ;
		}
		public void render(SpriteBatch batch)
		{
			// Draw Volcano background
			volcano.render(batch);

			// Draw Mountains
			mountains.render(batch) ;

			// Draw Trees
//			trees.render(batch) ;

			// Draw Ground
			for (Ground ground : grounds)
			{
				ground.render(batch, false) ;
			}

			for (Ground ground : slowGrounds)
			{
				ground.render(batch, true) ;
			}

			// Draw Ice
			for(Ice ice : ice)
			{
				ice.render(batch) ;
			}
			// Draw Fire
			for(Fire fire : fire)
			{
				fire.render(batch) ;
			}

			if(!goalReached)
			{
				//Draw player character
				character.render(batch) ;
			}
			// Draw Lava Overlay
			lavaOverlay.render(batch) ;

			boat.render(batch) ;
		}

		/**
		 * Updates moving objects and stops rendering of collected items
		 */
		public void update(float deltaTime)
		{
			character.update(deltaTime) ;
			boat.update(deltaTime) ;
			for(Ground ground: grounds)
			{
				ground.update(deltaTime) ;
			}

			for(Ground ground: slowGrounds)
			{
				ground.update(deltaTime) ;
			}

			for(Ice ice : ice)
			{
				ice.update(deltaTime) ;
			}

			for(Fire fire : fire)
			{
				fire.update(deltaTime) ;
			}

			mountains.update(deltaTime) ;

		}
}