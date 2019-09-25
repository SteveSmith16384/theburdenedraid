package net.evgiz.ld40.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.evgiz.ld40.game.Game;
import net.evgiz.ld40.game.World;
import net.evgiz.ld40.game.decals.DecalEntity;
import net.evgiz.ld40.game.player.Player;

public class Entity {

	protected Vector3 position;
	protected boolean remove = false;
	protected DecalEntity decalEntity;
	protected TextureRegion texture[][];
	protected int world_x, world_y;

	public Entity() {
	}
	

	public Entity(TextureRegion tex[][], int map_x, int map_y) {
		this(tex, map_x, map_y, 0, 0);
		/*texture = tex;

		decalEntity = new DecalEntity(tex[0][0]);
		position = new Vector3(Game.UNIT*x, 0,Game.UNIT*y);*/

	}

	public Entity(TextureRegion tex[][], int map_x, int map_y, int tx, int ty) {
		texture = tex;

		decalEntity = new DecalEntity(tex[tx][ty]);
		position = new Vector3(Game.UNIT*map_x, 0,Game.UNIT*map_y);

	}

	public void bindWorldTile(World wrld, int tx, int ty) {
		world_x = tx;
		world_y = ty;
		wrld.world[tx + ty*wrld.width] = 2;
	}

	/*
	public void interact(Player player) {
	}

	public String getInteractText(Player player){
		return "Interact";
	}
*/
	public Vector3 getPosition(){
		return position;
	}

	public void update(World world) {
	}

	public void render(DecalBatch batch) {
	}
/*
	public void damaged(Vector3 direction) {
	}
*/

	
	// todo - remove this method and use vector3 for all
	protected boolean tryMove(World world, Vector2 moveVec, boolean doFine) {
		return this.tryMove(world, new Vector3(moveVec.x, 0, moveVec.y), doFine);
	}


	/**
	 * Returns false if entity fails to move on any axis.
	 */
	protected boolean tryMove(World world, Vector3 moveVec, boolean doFine) {
		boolean resultX = false;
		if(world.rectangleFree(position.x+moveVec.x, position.z, .75f, .75f)) {
			position.x += moveVec.x;
			resultX = true;
		} else if (doFine) {
			for (int i = 0; i < 10; i++) {
				if (world.rectangleFree(position.x+moveVec.x/10f, position.z, .75f, .75f)) {
					position.x += moveVec.x/10f;
					resultX = true;
				} else {
					break;
				}
			}
		}

		boolean resultZ = false;
		if(world.rectangleFree(position.x, position.z+moveVec.z, .75f, .75f)) {
			position.z += moveVec.z;
			resultZ = true;
		} else if (doFine){
			for (int i = 0; i < 10; i++) {
				if(world.rectangleFree(position.x, position.z+moveVec.z/10f, .75f, .75f)) {
					position.z += moveVec.z/10f;
					resultZ = true;
				} else {
					break;
				}
			}
		}
		
		if (moveVec.y != 0) {
			position.z += moveVec.z;
			if (position.z < 0 || position.z > Game.UNIT) {
				return false;
			}
		}
		
		return resultX && resultZ;
	}

}
