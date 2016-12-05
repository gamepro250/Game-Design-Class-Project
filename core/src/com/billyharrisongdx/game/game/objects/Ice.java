package com.billyharrisongdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch ;
import com.badlogic.gdx.graphics.g2d.TextureRegion ;
import com.billyharrisongdx.game.game.Assets ;
import com.badlogic.gdx.math.MathUtils ;
import com.badlogic.gdx.Gdx ;
import com.badlogic.gdx.graphics.g2d.ParticleEffect ;

public class Ice extends AbstractGameObject
{
	/**
	 * Variables holding the gold coin texture region and
	 * its current collection status
	 */
	private TextureRegion regGoldCoin ;
	public ParticleEffect iceParticle = new ParticleEffect() ;

	public boolean collected ;

	public Ice()
	{
		init() ;
	}

	/**
	 * Sets dimension for the coin and its bounds and sets
	 * its texture region and that it is not collected
	 */
	private void init()
	{
		dimension.set(0.5f, 0.5f) ;

		setAnimation(Assets.instance.ice.animIce) ;
		stateTime = MathUtils.random(0.0f, 1.0f) ;

		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y) ;

		collected = false ;

		// Particles
		iceParticle.load(Gdx.files.internal("../core/assets/particles/Ice.part"), Gdx.files.internal("particles")) ;
	}

	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime) ;

		iceParticle.update(deltaTime) ;
	}

	/**
	 * Draws the ice in the specified location
	 */
	public void render(SpriteBatch batch)
	{
		if(collected) return ;

		iceParticle.setPosition(body.getPosition().x + dimension.x / 2, body.getPosition().y + .1f) ;
		// Draw particle
		iceParticle.draw(batch) ;

		TextureRegion reg = null ;
		reg = animation.getKeyFrame(stateTime, true) ;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
				dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false) ;


	}

	/**
	 * Returns coin point value
	 */
	public static int getScore()
	{
		return 100 ;
	}
}