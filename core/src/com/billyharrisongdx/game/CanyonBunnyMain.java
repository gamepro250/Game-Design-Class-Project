/**
 * Author: Billy Harrison
 *
 * Date: 9/15/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game;

import com.badlogic.gdx.Application ;
import com.badlogic.gdx.Game ;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager ;
import com.billyharrisongdx.game.game.Assets ;
import com.billyharrisongdx.game.screens.MenuScreen ;
import com.billyharrisongdx.game.util.AudioManager ;
import com.billyharrisongdx.game.util.GamePreferences ;

public class CanyonBunnyMain extends Game
{
	@Override
	public void create()
	{
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG) ;
		
		// Load assets
		Assets.instance.init(new AssetManager()) ;
		
		// Load preferences for audio settings and start playing music
		GamePreferences.instance.load() ;
		AudioManager.instance.play(Assets.instance.music.song01) ;
		
		// Start game at menu screen
		setScreen(new MenuScreen(this)) ;
	}
}