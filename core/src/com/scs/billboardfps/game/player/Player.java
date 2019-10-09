package com.scs.billboardfps.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.Settings;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.entity.Entity;
import com.scs.billboardfps.game.interfaces.IAttackable;
import com.scs.billboardfps.game.interfaces.IDamagable;
import com.scs.billboardfps.game.interfaces.IHarmsPlayer;
import com.scs.billboardfps.game.interfaces.IInteractable;
import com.scs.billboardfps.game.player.weapons.IPlayersWeapon;
import com.scs.billboardfps.game.player.weapons.PlayersSword;

public class Player implements IDamagable {

	private static final float moveSpeed = 2f * Game.UNIT;
	private static final float gravityScale = 25 * Game.UNIT;
	public static final float playerHeight = Game.UNIT * 0.4f;
	private static final float colliderSize = .2f * Game.UNIT;
	private static final float jumpScale = 4f * Game.UNIT;
	private static final float hurtDistanceSquared = Game.UNIT * .5f * Game.UNIT * .5f;

	public Camera camera;
	private World world;
	public Inventory inventory;
	public CameraController cameraController;
	public Vector3 position;
	private Vector3 moveVector;
	private Vector3 tmpVector;
	private boolean onGround = false;
	private float gravity = 0f;
	private boolean mouseReleased = false;
	private float footstepTimer;
	private int health, max_health;
	private float hurtTimer = 0f;
	private Texture hurtTexture; // Screen goes red when hit
	private Texture heart;
	public IInteractable interactTarget;
	private IPlayersWeapon weapon;

	public Player(Camera cam, World wrld, Inventory inv, int lookSens, int maxHealth, IPlayersWeapon _weapon) {
		inventory = inv;
		camera = cam;
		world = wrld;
		this.max_health = maxHealth;
		this.health = this.max_health;

		cameraController = new CameraController(camera, lookSens);

		position = new Vector3();//world.playersStartMapX * Game.UNIT, 0f, world.playerStartMapY * Game.UNIT);
		moveVector = new Vector3();
		tmpVector = new Vector3();

		weapon = _weapon;//new SwordWeapon(Settings.USE_WAND);

		hurtTexture = new Texture(Gdx.files.internal("red.png"));
		heart = new Texture(Gdx.files.internal("heart.png"));
	}


	public Vector3 getPosition() {
		return position;
	}


	public void update() {
		move();
		gravity();
		if (weapon != null) {
			checkForAttack();
		}
		interact();

		cameraController.update();

		if (weapon != null) {
			weapon.update(cameraController);
		}

		if (hurtTimer>0) {
			hurtTimer -= Gdx.graphics.getDeltaTime();
		} else {
			// Check if any enemies are harming us
			//float hurtDistance = Game.UNIT * .5f;
			for (Entity ent : Game.entityManager.getEntities()) {
				if (ent instanceof IHarmsPlayer) {
					IHarmsPlayer hp = (IHarmsPlayer)ent;
					if (hp.harmsPlayer()) {
						// For efficiency, we use a simple dist2 and check against hurtDistance2
						if (ent.getPosition().dst2(position) < hurtDistanceSquared) {
							this.damaged(1, new Vector3()); // todo - dir
						}
					}
				}
			}
		}
	}


	private void interact() {
		interactTarget = null;

		float dist = 0f;
		float d = 0;

		Vector3 hitPos = new Vector3().set(position).mulAdd(camera.direction, Game.UNIT/2f);

		for(Entity ent : Game.entityManager.getEntities()) {
			if (ent instanceof IInteractable) {
				IInteractable ii = (IInteractable)ent;
				if (ii.isInteractable()) {
					d = ent.getPosition().dst2(position);
					if(Game.collision.hitCircle(hitPos, ent.getPosition(), Game.UNIT/2f) && (dist==0 || d<dist)) {
						interactTarget = (IInteractable)ent;
						dist = d;
					}
				}
			}
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.E) && interactTarget!=null) {
			interactTarget.interact(this);
		}

	}


	private void checkForAttack() {
		/* if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && attackAnimation <= 0){
			attackAnimation = 1.0f;
			didAttack = false;
			didPlayAudio = false;
		}
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			if (mouseReleased && Gdx.input.isCursorCatched() && attackAnimation <= 0) {
				attackAnimation = 1.0f;
				didAttack = false;
				didPlayAudio = false;
			}
			mouseReleased = false;
		} else {
			mouseReleased = true;
		}
		 */
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			weapon.attackPressed(this.position, this.camera.direction);
		} else if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			mouseReleased = true;
			if (mouseReleased && Gdx.input.isCursorCatched()) {
				mouseReleased = false;
				weapon.attackPressed(this.position, this.camera.direction);
			}
		}

		//weapon.checkForAttack();

		/*if (weapon.IsAttackMade(this)) {// attackAnimation < 0.3f && !didAttack) {
			//didAttack = true;
			checkAttackHit();
		}*/
	}


	private void gravity() {
		gravity -= gravityScale*Gdx.graphics.getDeltaTime();
		position.y += gravity*Gdx.graphics.getDeltaTime();

		position.y = Math.max(0, position.y);
		position.y = Math.min(0.8f*Game.UNIT-playerHeight, position.y);

		onGround = (position.y == 0);

		if (onGround) {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				gravity = jumpScale;
			} else {
				gravity = 0f;
			}
		}
	}


	private void move() {
		//showPosition();
		float dt = Gdx.graphics.getDeltaTime();

		moveVector.setZero();

		//Movement
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			tmpVector.set(camera.direction);
			tmpVector.y = 0;
			moveVector.add(tmpVector.nor().scl(dt * moveSpeed));
		} else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			tmpVector.set(camera.direction);
			tmpVector.y = 0;
			moveVector.add(tmpVector.nor().scl(dt * -moveSpeed));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)){
			tmpVector.set(camera.direction).crs(camera.up);
			tmpVector.y = 0;
			moveVector.add(tmpVector.nor().scl(dt * -moveSpeed));
		} else if (Gdx.input.isKeyPressed(Input.Keys.D)){
			tmpVector.set(camera.direction).crs(camera.up);
			tmpVector.y = 0;
			moveVector.add(tmpVector.nor().scl(dt * moveSpeed));
		}

		if (moveVector.len2() > 0) {
			float colX = moveVector.x==0 ? 0 : (moveVector.x>0 ? 1 : -1);
			float colZ = moveVector.z==0 ? 0 : (moveVector.z>0 ? 1 : -1);

			if (world.getMapSquareAt(position.x + moveVector.x + colX * colliderSize, position.z) == World.NOTHING) {
				position.add(moveVector.x, 0, 0);
			}
			if (world.getMapSquareAt(position.x, position.z + moveVector.z + colZ * colliderSize) == World.NOTHING) {
				position.add(0, 0, moveVector.z);
			}
		}

		camera.position.set(position.x, position.y + playerHeight, position.z);

		if (moveVector.len2() > 0) {
			footstepTimer += Gdx.graphics.getDeltaTime();

			if (footstepTimer > 0.45f) {
				footstepTimer -= 0.45f;
				Game.audio.play("step");
			}
		}
		//showPosition();
	}

	/*
	private void showPosition() {
		Settings.p("Player pos: " + this.position);
	}
	 */

	public void render(SpriteBatch batch) {
		if (weapon != null) {
			weapon.render(batch);
		}
	}


	public void renderUI(SpriteBatch batch, BitmapFont font) {
		if (interactTarget != null) {
			String str = interactTarget.getInteractText(this);
			int w2 = str.length() * 8;
			font.setColor(1,1,1,1);
			font.draw(batch, str, Gdx.graphics.getWidth() / 2 - w2, Gdx.graphics.getHeight() / 2 + 50/8);
		}

		int sx = Gdx.graphics.getWidth()/2 - health*18;
		for (int i = 0; i < inventory.keys; i++) {
			batch.draw(Game.art.items[0][0], 10 + i*50, Gdx.graphics.getHeight()-40, 48, 48);
		}


		for (int i = 0; i < health; i++) {
			batch.draw(heart, sx + i*36, Gdx.graphics.getHeight()-40, 32, 32);
		}

		if (hurtTimer > 0 && (int)(hurtTimer*5)%2 == 0) {
			batch.setColor(1,1,1,.25f);
			batch.draw(hurtTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.setColor(1,1,1,1);
		}

	}


	@Override
	public int getHealth() {
		return health;
	}


	@Override
	public void damaged(int amt, Vector3 dir) {
		health -= amt;

		hurtTimer = 1.5f;
		Game.audio.play("player_hurt");
	}


	public void resetHealth() {
		this.health = this.max_health;
	}

}

