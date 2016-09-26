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
 * Trees game object.
 */
public class Trees extends AbstractGameObject
{
	private float length ; // Length of screen

	private Array<TextureRegion> regTrees ;
	private Array<Tree> trees ;

	/**
	 * Creates single tree to be added to array of trees
	 */
	private class Tree extends AbstractGameObject
	{
		private TextureRegion regTree ; // Holds the trees location on texture atlas

		public Tree() {}

		public void setRegion(TextureRegion region)
		{
			regTree = region ;
		}

		/**
		 * Implementation of Abstract render method
		 */
		@Override
		public void render(SpriteBatch batch)
		{
			TextureRegion reg = regTree ;
			batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y,
					origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
					reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false) ;
		}
	}

	public Trees(float length)
	{
		this.length = length ;
		init() ;
	}

	private void init()
	{
		dimension.set(3.0f, 1.5f) ;

		// Store the texture atlas locations of the tree images in one array
		regTrees = new Array<TextureRegion>() ;
		regTrees.add(Assets.instance.levelDecoration.tree) ;

		int distFac = 5 ; // Spacing between trees
		int numTrees = (int) (length / distFac) ; // # of trees based on length of game area
		trees = new Array<Tree>(2 * numTrees) ; // Initializes trees array to hold trees

		// Adds trees, shifting each by the distance factor, to the tree array
		for(int i = 0; i < numTrees; i++)
		{
			Tree tree = spawnTree() ;
			tree.position.x = i * distFac ;
			trees.add(tree) ;
		}
	}

	private Tree spawnTree()
	{
		Tree tree = new Tree() ;
		tree.dimension.set(dimension) ;
		// Select tree image
		tree.setRegion(regTrees.random());
		// Position
		Vector2 pos = new Vector2() ;
		pos.x = length + 10 ; // Position after end of level
		pos.y += 1.25 ; // Base position
		pos.y += MathUtils.random(0.0f, 0.2f) * (MathUtils.randomBoolean() ? 1 : -1) ; // Random additional position
		tree.position.set(pos) ;
		return tree ;
	}

	/**
	 * Implementation of Abstract render method
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		for(Tree tree : trees)
			tree.render(batch) ;
	}
}