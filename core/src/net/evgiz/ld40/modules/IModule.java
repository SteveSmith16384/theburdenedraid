package net.evgiz.ld40.modules;

public interface IModule {

	void update();
	
	void render();
	
	boolean isFinished();

	void destroy();
	
	void setFullScreen(boolean fullscreen);
}
