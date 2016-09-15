package com.billyharrisongdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.billyharrisongdx.game.CanyonBunnyMain;
import com.billyharrisongdx.game.billyHarrisonGdxGame;
import com.badlogic.gdx.tools.texturepacker.TexturePacker ;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings ;

public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		boolean rebuildAtlas = false ;
		boolean drawDebugOutline = true ;

		if(rebuildAtlas)
		{
			Settings settings = new Settings() ; // A group of settings defined for TexturePacker
			settings.maxWidth = 1024 ; // Width of atlas
			settings.maxHeight = 1024 ; // Height of atlas
			settings.duplicatePadding = false ;
			settings.debug = drawDebugOutline ; // draws square outline around each image in atlas
			TexturePacker.process(settings, "assets-raw/images","../core/assets/images", "canyonbunny.pack") ; // packs images from source into destination
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new CanyonBunnyMain(), config);
	}
}
