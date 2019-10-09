package com.scs.billboardfps.game.player.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.game.player.CameraController;
import com.scs.billboardfps.game.player.Player;

public interface IPlayersWeapon {

	void update(CameraController cameraController);
	
	void render(SpriteBatch batch);
	
	void attackPressed(Vector3 position, Vector3 direction);
	
	//boolean IsAttackMade(Player player);

}
