package net.evgiz.ld40.game.components;

import net.evgiz.ld40.game.player.Player;

public interface IInteractable {

	boolean isInteractable();
	
	void interact(Player player);
	
	String getInteractText(Player player);
}
