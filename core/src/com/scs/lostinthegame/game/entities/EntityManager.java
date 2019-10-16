package com.scs.lostinthegame.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.decals.DecalManager;
import com.scs.lostinthegame.game.entities.burdenlair.LootEntity;

public class EntityManager {

	private DecalManager decalManager;
	private ArrayList<Entity> entities;

	public EntityManager(DecalManager decal) {
		decalManager = decal;

		entities = new ArrayList<Entity>();
	}


	public ArrayList<Entity> getEntities() {
		return entities;
	}


	public void add(Entity ent) {
		entities.add(ent);
		if (ent.decalEntity != null) {
			decalManager.add(ent.decalEntity);
		}
	}


	private void remove(Entity ent){
		entities.remove(ent);
		decalManager.remove(ent.decalEntity);
	}


	public void update(World world) {
		for (int i = 0; i < entities.size(); i++) {
			Entity ent = entities.get(i);
			if (ent.remove) {
				remove(ent);
				i--;
				continue;
			}
			
			ent.update(world);
			if (ent.decalEntity != null) {
				ent.decalEntity.setPosition(ent.position.x, ent.position.y, ent.position.z);
			}
			
		}
	}


	public void dropLoot(Vector3 position) {
		add(new LootEntity(Game.art.items, position.x, position.z));
	}


}

