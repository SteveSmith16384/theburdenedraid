package com.scs.billboardfps.game.interfaces;

import com.scs.billboardfps.game.player.Player;

public interface IInteractable {

	boolean isInteractable();
	
	void interact(Player player);
	
	String getInteractText(Player player);
}
