/**
 * Author: Billy Harrison
 *
 * Date: 9/15/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.game;

import com.badlogic.gdx.Gdx ;
import com.badlogic.gdx.assets.AssetDescriptor ;
import com.badlogic.gdx.assets.AssetErrorListener ;
import com.badlogic.gdx.assets.AssetManager ;
import com.badlogic.gdx.graphics.g2d.TextureAtlas ;
import com.badlogic.gdx.utils.Disposable ;
import com.billyharrisongdx.game.util.Constants ;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion ;
import com.badlogic.gdx.graphics.Texture ;
import com.badlogic.gdx.graphics.Texture.TextureFilter ;

public class Assets implements Disposable, AssetErrorListener
{
	public static final String TAG = Assets.class.getName() ;

	public static Assets instance = new Assets() ;

	private AssetManager assetManager ;

	public AssetCharacter character ;
	public AssetFire fire ;
	public AssetGround ground ;
	public AssetLava lava ;

	// singleton: prevent instantiation from other classes
	private Assets() {}

	public void init (AssetManager assetManager)
	{
		this.assetManager = assetManager ;

		// set asset manager error handler
		assetManager.setErrorListener(this) ;

		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,TextureAtlas.class) ;

		// start loading assets and wait until finished
		assetManager.finishLoading();

		Gdx.app.debug(TAG,  "# of assets loaded: " + assetManager.getAssetNames().size) ;

		for (String a: assetManager.getAssetNames())
		{
			Gdx.app.debug(TAG, "asset: " + a) ;
		}

		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS) ;

		// Enable texture filtering for pixel smoothing.
		for (Texture t : atlas.getTextures())
		{
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear) ;
		}

		// create game resource objects
		character = new AssetCharacter(atlas) ;
		fire = new AssetFire(atlas) ;
		ground = new AssetGround(atlas) ;
		lava = new AssetLava(atlas) ;
}

	/**
	 * Releases all assets when they are no longer needed
	 */
	@Override
	public void dispose()
	{
		assetManager.dispose() ;
	}

	/**
	 * Error handler
	 */
	@Override
	public void error(AssetDescriptor asset, Throwable throwable)
	{
		Gdx.app.error(TAG, "Wouldn't load asset '" + asset.fileName + "'", (Exception) throwable);
	}

	/**
	 * Asset classes that allow structuring and caching of game assets
	 */
	public class AssetCharacter
	{
		public final AtlasRegion character;

		public AssetCharacter (TextureAtlas atlas)
		{
			character = atlas.findRegion("Character") ;
		}
	}

	public class AssetFire
	{
		public final AtlasRegion fire ;

		public AssetFire (TextureAtlas atlas)
		{
			fire = atlas.findRegion("Fire") ;
		}
	}

	public class AssetGround
	{
		public final AtlasRegion ground ;

		public AssetGround (TextureAtlas atlas)
		{
			ground = atlas.findRegion("Ground") ;
		}
	}


	public class AssetLava
	{
		public final AtlasRegion lava ;

		public AssetLava (TextureAtlas atlas)
		{
			lava = atlas.findRegion("Lava") ;
		}
	}
}















