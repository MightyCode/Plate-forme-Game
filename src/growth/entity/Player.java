package growth.entity;

import growth.entity.module.*;
import growth.entity.module.Player_Movement;
import growth.entity.module.Player_Sprint;
import growth.render.Animation;
import growth.render.Render;
import growth.screen.screens.GameScreen;
import growth.tilemap.TileMap;

import java.util.ArrayList;


/**
 * Player class.
 * This class is the class of player controlled by the played.
 *
 * @author MightyCode
 * @version 1.0
 */
public class Player extends MovingEntity{

	/**
	 * Player's states.
	 * These static final variable counting the different state of player.
	 */
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;

	/**
	 * Player class constructor.
	 * Firstly call the mother class constructor.
	 * Init all of player variable.
	 *
	 * @param tileMap Add tileMap to the player.
	 * @param sizeX Add sizeX to the player.
	 * @param sizeY Add sizeY to the player.
	 */
	public Player(TileMap tileMap, int sizeX, int sizeY) {
		// Call mother constructor
		super(tileMap);

		/* Init player's variables */
			// Size, and boxSize
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		cX = (int) (sizeX * 0.65);
		cY = sizeY;

			// Movement
		float walkSpeed = 2.5f;
		float runSpeed = 1.45f;
		float maxSpeed = 5.5f;
		float stopSpeed = 0.3f;
		float fallSpeed = 0.4f;
		float maxFallSpeed = GameScreen.TILESIZE - 2;
		float jumpStart = -13.5f;
		float stopJumpSpeed = 0.2f;


		// Add the modules of action to the player
		modules = new ArrayList<>();
		modules.add(new Player_Movement(this,walkSpeed, maxSpeed, stopSpeed));
		modules.add(new Player_Jump(this, jumpStart, fallSpeed, stopJumpSpeed, maxFallSpeed));
		modules.add(new Player_Sprint(this,(Player_Movement) modules.get(0), runSpeed));

			// Sprite and Animation
		facing = true;
		animationPlayed = 0;

			// Load animation and animationFrame
		animations = new ArrayList<>();
		animations.add(new Animation("/images/character/idle/", 1, 100));
		animations.add(new Animation("/images/character/walk/", 10, 4));
		animations.add(new Animation("/images/character/jump/", 1, 100));
		animations.add(new Animation("/images/character/fall/", 1, 100));
	}

	/**
	 * Update the player's position, states and this current animation.
	 */
	public void update() {
		for(Module module : modules){
			module.update();
		}

		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		// Direction
		if (speedX < 0) facing = false;
		else if (speedX > 0) facing = true;

		// Set the good animation
		if (speedY > 0) {
			if (animationPlayed != FALLING) {
				animationPlayed = FALLING;
			}
		} else if (speedY < 0) {
			if (animationPlayed != JUMPING) {
				animationPlayed = JUMPING;
			}
		} else if (left || right) {
			if (animationPlayed != WALKING) {
				animationPlayed = WALKING;
			}
		} else {
			if (animationPlayed != IDLE) {
				animationPlayed = IDLE;
			}
		}

		// And update chosen animation
		animations.get(animationPlayed).update();
	}

	/**
	 * Display the player with his good animation.
	 */
	public void display() {

		// Set the map position
		setMapPosition();
		// Draw animation left to right if the player go the the right and invert if the player go to the invert direction
		if (facing) {
			Render.image(
					(int) (posX + xMap - sizeX / 2),
					(int) (posY + yMap - sizeY / 2),
					sizeX, sizeY,
					animations.get(animationPlayed).getCurrentID(), 1);
		} else {
			Render.image(
					(int) (posX + xMap - sizeX / 2 + sizeX),
					(int) (posY + yMap - sizeY / 2),
					-sizeX, sizeY,
					animations.get(animationPlayed).getCurrentID(), 1);
		}
	}
}