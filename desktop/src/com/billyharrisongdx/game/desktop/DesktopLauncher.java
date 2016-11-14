/**
 * Author: Billy Harrison
 *
 * Date: 9/15/16
 *
 * Class: Game Design
 */

package com.billyharrisongdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.billyharrisongdx.game.CanyonBunnyMain;


public class DesktopLauncher {
	private static boolean rebuildAtlas = true ; // Tells whether or not to build atlas
	private static boolean drawDebugOutline = false ; // Draw box around displayed images

	public static void main (String[] arg)
	{
		if (rebuildAtlas)
		{
			Settings settings = new Settings() ; // Create a set of settings for the texture packer
			// Sets height and width of texture atlas
			settings.maxWidth = 1024 ;
			settings.maxHeight = 1024 ;
			//settings.duplicatePadding = false ;
			settings.debug = drawDebugOutline ;
			TexturePacker.process(settings, "assets-raw/images", "../core/assets/images", "canyonbunny.pack") ; // Creates atlas in desired location
			TexturePacker.process(settings, "assets-raw/images-ui", "../core/assets/images", "canyonbunny-ui.pack") ; // Creates start screen atlas in desired location
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new CanyonBunnyMain(), config);
		config.width = 800;
		config.height = 480;
	}
}