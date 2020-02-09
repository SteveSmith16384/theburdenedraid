package com.scs.lostinthegame.game.player.weapons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.scs.lostinthegame.game.player.CameraController;

public interface IPlayersWeapon {

	void update(CameraController cameraController);
	
	void render(Batch batch);
	
	void attackPressed(Vector3 position, Vector3 direction);
	
}
