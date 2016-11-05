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
import com.billyharrisongdx.game.game.objects.Rock ;
import com.billyharrisongdx.game.game.objects.Mountains ;
import com.billyharrisongdx.game.game.objects.WaterOverlay ;
import com.billyharrisongdx.game.game.objects.Clouds ;
import com.billyharrisongdx.game.game.objects.GoldCoin ;
import com.billyharrisongdx.game.game.objects.Feather ;
import com.billyharrisongdx.game.game.objects.BunnyHead ;
import com.billyharrisongdx.game.game.objects.Carrot ;
import com.billyharrisongdx.game.game.objects.Goal ;

public class Level
{
	public static final String TAG = Level.class.getName() ;
	public Array<Carrot> carrots ;
	public Goal goal ;

	/**
	 * Color data used to compare from the
	 * level map to determine what block
	 * is to be drawn
	 */
	public enum BLOCK_TYPE
	{
		EMPTY(0, 0, 0), // Black
		ROCK(0, 255, 0), // Green
		PLAYER_SPAWNPOINT(255, 255, 255), // White
		ITEM_FEATHER(255, 0, 255), // Purple
		ITEM_GOLD_COIN(255, 255, 0),  // Yellow
		GOAL(255, 0, 0) ; // Red

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
		public Array<Rock> rocks ;
		public BunnyHead bunnyHead ;
		public Array<GoldCoin> goldcoins ;
		public Array<Feather> feathers ;

		// Decoration
		public Clouds clouds ;
		public Mountains mountains ;
		public WaterOverlay waterOverlay ;

		/**
		 * Initiates a level using the input filename
		 * @param filename
		 */
		public Level(String filename)
		{
			init(filename) ;
		}

		/**
		 * Draws level map based on input file. Uses colors
		 * to position items on the map and draws background
		 * images
		 */
		private void init(String filename)
		{
			// Player character
			bunnyHead = null ;
			// Objects
			rocks = new Array<Rock>() ;
			goldcoins = new Array<GoldCoin>() ;
			feathers = new Array<Feather>() ;
			carrots = new Array<Carrot>() ;

			// Load image file that represents the level data
			Pixmap pixmap = new Pixmap(Gdx.files.internal(filename)) ;
			// Scan pixels from top-left to bottom-right
			int lastPixel = -1 ;
			for(int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
			{
				for(int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
				{
					AbstractGameObject obj = null ;
					float offsetHeight = 0 ;
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
					// Rock
					else if(BLOCK_TYPE.ROCK.sameColor(currentPixel))
					{
						if(lastPixel != currentPixel)
						{
							obj = new Rock() ;
							float heightIncreaseFactor = 0.25f ;
							offsetHeight = -2.5f ;
							obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight) ;
							rocks.add((Rock) obj) ;
						}
						else
						{
							rocks.get(rocks.size - 1).increaseLength(1) ;
						}
					}
					// Player spawn point
					else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
					{
						obj = new BunnyHead() ;
						offsetHeight = -3.0f ;
						obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight) ;
						bunnyHead = (BunnyHead) obj ;
					}
					// Feather
					else if(BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel))
					{
						obj = new Feather() ;
						offsetHeight = -1.5f ;
						obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight) ;
						feathers.add((Feather) obj);
					}
					// Gold coin
					else if(BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel))
					{
						obj = new GoldCoin() ;
						offsetHeight = -1.5f ;
						obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight) ;
						goldcoins.add((GoldCoin) obj) ;
					}
					// Goal
					else if(BLOCK_TYPE.GOAL.sameColor(currentPixel))
					{
						obj = new Goal() ;
						offsetHeight = -7.0f ;
						obj.position.set(pixelX, baseHeight + offsetHeight) ;
						goal = (Goal)obj ;
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
			clouds = new Clouds(pixmap.getWidth()) ;
			clouds.position.set(0, 2) ;
			mountains = new Mountains(pixmap.getWidth()) ;
			mountains.position.set(-1, -1) ;
			waterOverlay = new WaterOverlay(pixmap.getWidth()) ;
			waterOverlay.position.set(0, -3.75f) ;

			// Free memory
			pixmap.dispose() ;
			Gdx.app.debug(TAG, "level'" + filename + "' loaded") ;
		}

		/**
		 * Called to draw all background images as well as all
		 * coins, feathers, rocks and the player character
		 */
		public void render(SpriteBatch batch)
		{
			// Draw Mountains
			mountains.render(batch) ;

			// Draw Goal
			goal.render(batch) ;

			// Draw Rocks
			for (Rock rock : rocks)
			{
				rock.render(batch) ;
			}
			// Draw Gold Coins
			for(GoldCoin goldCoin : goldcoins)
			{
				goldCoin.render(batch) ;
			}
			// Draw Feathers
			for(Feather feather : feathers)
			{
				feather.render(batch) ;
			}
			// Draw Carrots
			for(Carrot carrot: carrots)
			{
				carrot.render(batch) ;
			}
			// Draw player character
			bunnyHead.render(batch) ;

			// Draw Water Overlay
			waterOverlay.render(batch) ;

			// Draw Clouds
			clouds.render(batch) ;
		}

		/**
		 * Updates moving objects and stops rendering of collected items
		 */
		public void update(float deltaTime)
		{
			bunnyHead.update(deltaTime) ;

			for(Rock rock : rocks)
			{
				rock.update(deltaTime) ;
			}

			for(GoldCoin goldCoin : goldcoins)
			{
				goldCoin.update(deltaTime) ;
			}

			for(Feather feather : feathers)
			{
				feather.update(deltaTime) ;
			}

			for(Carrot carrot : carrots)
			{
				carrot.update(deltaTime) ;
			}
			clouds.update(deltaTime) ;
		}
}