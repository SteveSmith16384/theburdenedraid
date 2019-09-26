package com.scs.billboardfps.modules;

public interface IModule {

	void update();
	
	void render();
	
	boolean isFinished();

	void destroy();
	
	void setFullScreen(boolean fullscreen);
}
