package org.hjujgfg.dracer;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import org.hjujgfg.dracer.DracerGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new DracerGame(), config);
	}

	/*@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//GLTFDemo.AUTOLOAD_ENTRY = "BoomBox";
		//GLTFDemo.AUTOLOAD_VARIANT = "glTF";
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GLTFExample(), config);
	}*/
}
