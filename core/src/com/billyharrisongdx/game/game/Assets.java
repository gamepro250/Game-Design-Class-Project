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
import com.badlogic.gdx.graphics.g2d.BitmapFont ;
import com.badlogic.gdx.audio.Music ;
import com.badlogic.gdx.audio.Sound ;
import com.badlogic.gdx.graphics.g2d.Animation ;
import com.badlogic.gdx.utils.Array ;

public class Assets implements Disposable, AssetErrorListener
{
	public static final String TAG = Assets.class.getName() ;

	public static Assets instance = new Assets() ;

	private AssetManager assetManager ;

	public AssetCharacter character ;
	public AssetFire fire ;
	public AssetGround ground ;
	public AssetIce ice ;
	public AssetLevelDecoration levelDecoration ;
	public AssetBoat boat ;
	public AssetFonts fonts ;
	public AssetVolcano volcano ;
	public AssetSounds sounds ;
	public AssetMusic music ;


	// singleton: prevent instantiation from other classes
	private Assets() {}

	public class AssetFonts
	{
		public final BitmapFont defaultSmall ;
		public final BitmapFont defaultNormal ;
		public final BitmapFont defaultBig ;

		public AssetFonts()
		{
			// Create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(Gdx.files.internal("../core/assets/images/arial-15.fnt"), true) ;
			defaultNormal = new BitmapFont(Gdx.files.internal("../core/assets/images/arial-15.fnt"), true) ;
			defaultBig = new BitmapFont(Gdx.files.internal("../core/assets/images/arial-15.fnt"), true) ;

			// Set font sizes
			defaultSmall.getData().setScale(0.75f) ;
			defaultNormal.getData().setScale(1.0f) ;
			defaultBig.getData().setScale(2.0f) ;
			// Enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	public void init (AssetManager assetManager)
	{
		this.assetManager = assetManager ;

		// set asset manager error handler
		assetManager.setErrorListener(this) ;

		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,TextureAtlas.class) ;

		// load sounds
		assetManager.load("sounds/jump.wav", Sound.class) ;
		assetManager.load("sounds/jump_powerup.wav", Sound.class) ;
		assetManager.load("sounds/ice.wav", Sound.class) ;
		assetManager.load("sounds/fire.wav", Sound.class) ;
		assetManager.load("sounds/life_lost.wav", Sound.class) ;

		// load music

		assetManager.load("music/williams_when_it_falls.mp3", Music.class) ;


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
		ice = new AssetIce(atlas) ;
		levelDecoration = new AssetLevelDecoration(atlas) ;
		boat = new AssetBoat(atlas) ;
		volcano = new AssetVolcano(atlas) ;
		fonts = new AssetFonts() ;
		sounds = new AssetSounds(assetManager) ;
		music = new AssetMusic(assetManager) ;
}

	/**
	 * Releases all assets when they are no longer needed
	 */
	@Override
	public void dispose()
	{
		assetManager.dispose() ;
		fonts.defaultSmall.dispose() ;
		fonts.defaultNormal.dispose() ;
		fonts.defaultBig.dispose() ;
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
		public final AtlasRegion character ;
		public final AtlasRegion characterHead ;
		public final Animation runAnim ;

		public AssetCharacter (TextureAtlas atlas)
		{
			character = atlas.findRegion("run") ;
			characterHead = atlas.findRegion("Character_Head") ;

			Array<AtlasRegion> regions = null ;

			// Run Animation
			regions = atlas.findRegions("run") ;
			runAnim = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.NORMAL) ;
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

	public class AssetIce
	{
		public final AtlasRegion ice ;

		public AssetIce (TextureAtlas atlas)
		{
			ice = atlas.findRegion("Ice") ;
		}
	}

	public class AssetBoat
	{
		public final AtlasRegion boat ;

		public AssetBoat (TextureAtlas atlas)
		{
			boat = atlas.findRegion("Boat") ;
		}
	}

	public class AssetGround
	{
		public final AtlasRegion groundLeft ;
		public final AtlasRegion groundRight ;
		public final AtlasRegion groundCenter ;

		public AssetGround (TextureAtlas atlas)
		{
			groundCenter = atlas.findRegion("Ground_Center") ;
			groundLeft = atlas.findRegion("Ground_Left") ;
			groundRight = atlas.findRegion("Ground_Right") ;
		}
	}

	public class AssetVolcano
	{
		public final AtlasRegion volcano ;

		public AssetVolcano (TextureAtlas atlas)
		{
			volcano = atlas.findRegion("Volcano") ;
		}
	}

	public class AssetLevelDecoration
	{
		public final AtlasRegion gas ;
		public final AtlasRegion lavaOverlay ;
		public final AtlasRegion mountain ;
		public final AtlasRegion tree ;
		public final AtlasRegion volcano ;

		public AssetLevelDecoration (TextureAtlas atlas)
		{
			gas = atlas.findRegion("Gas") ;
			lavaOverlay = atlas.findRegion("Lava") ;
			mountain = atlas.findRegion("Mountain") ;
			tree = atlas.findRegion("Tree") ;
			volcano = atlas.findRegion("Volcano") ;
		}
	}

	public class AssetSounds
	{
		public final Sound jump ;
		public final Sound jumpPowerup ;
		public final Sound pickupIce ;
		public final Sound pickupFire ;
		public final Sound lifeLost ;

		public AssetSounds(AssetManager am)
		{
			jump = am.get("sounds/jump.wav", Sound.class) ;
			jumpPowerup = am.get("sounds/jump_powerup.wav", Sound.class) ;
			pickupIce = am.get("sounds/ice.wav", Sound.class) ;
			pickupFire = am.get("sounds/fire.wav", Sound.class) ;
			lifeLost = am.get("sounds/life_lost.wav", Sound.class) ;
		}
	}

	public class AssetMusic
	{
		public final Music song01 ;

		public AssetMusic(AssetManager am)
		{
			song01 = am.get("music/williams_when_it_falls.mp3", Music.class) ;
		}
	}
}















