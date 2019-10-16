package com.scs.lostinthegame.game.interfaces;

import com.scs.lostinthegame.game.player.Player;

public interface IInteractable {

	boolean isInteractable();
	
	void interact(Player player);
	
	String getInteractText(Player player);
}
