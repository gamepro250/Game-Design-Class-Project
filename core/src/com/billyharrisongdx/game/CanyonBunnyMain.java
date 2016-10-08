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

public class CanyonBunnyMain extends Game
{
	@Override
	public void create()
	{
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG) ;
		// Load assets
		Assets.instance.init(new AssetManager()) ;
		// Start game at menu screen
		setScreen(new MenuScreen(this)) ;
	}
}