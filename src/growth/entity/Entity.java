package growth.entity;

import growth.entity.module.Module;
import growth.render.Animation;
import growth.tilemap.TileMap;

import java.awt.*;
import java.util.ArrayList;

/**
 * Basic entity class.
 * This class is the mother class of all entity in game, including player, enemies or moving platform.
 */
public abstract class Entity {

	/**
	 * TileMap.
	 * This variable contains the tileMap to interact with it.
	 */
	TileMap tileMap;

	/**
	 * TileSize.
	 * This variable contains the tileSize to interact with it.
	 */
	int tileSize;

	/**
	 * Map position x.
	 * This variable contains the position x of the beginning of the rendering of the map.
	 */
	float xMap;

	/**
	 * Map position y.
	 * This variable contains the position y of the beginning of the rendering of the map.
	 */
	float yMap;

	/**
	 * Entity position x.
	 * This variable contains the position x of the entity.
	 */
	float posX;

	/**
	 * Entity position Y.
	 * This variable contains the position y of the entity.
	 */
	float posY;

	/**
	 * Entity size X.
	 * This variable contains the width of the entity.
	 */
	int sizeX;

	/**
	 * Entity size Y.
	 * This variable contains the height of the entity.
	 */
	int sizeY;

	/**
	 * Entity collision box size X.
	 * This variable contains the width of the collision's box entity.
	 */
	int cX;

	/**
	 * Entity collision box size Y.
	 * This variable contains the height of the collision's box entity.
	 */
	int cY;

	/**
	 * Temporary position x.
	 * This variable contains the temporary position in x.
	 */
	float xTemp;

	/**
	 * Temporary position y.
	 * This variable contains the temporary position in y.
	 */
	float yTemp;

	protected int priority;

	/**
	 * Animations table.
	 * This ArrayList contains all of the animations use by the entity.
	 */
	ArrayList<Animation> animations;


	protected int speed;

	/**
	 * Animations played.
	 * This variable contains the number of the animation played.
	 */
	int animationPlayed;

	ArrayList<Module> modules;

	/**
	 * Entity class constructor.
	 * Instance the class and set the tileMap.
	 *
	 * @param tileMap Add tileMap to the entity.
	 */
	Entity(TileMap tileMap) {
		this.tileMap = tileMap;
		this.tileSize = tileMap.getTileSize();
		priority = 0;
		speed = 1;
		posX = 0;
		posY = 0;
		sizeX = 0;
		sizeY = 0;
		cX = 0;
		cY = 0;
	}

	/**
	 * Test if th Entity meet another Entity.
	 *
	 * @param entity Another entity.
	 */
	public boolean intersects(Entity entity) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = entity.getRectangle();
		// Call the function intersects of Rectangle
		return r1.intersects(r2);
	}

	/*
	  Getters
	 */

	/**
	 * Return position x.
	 *
	 * @return posX
	 */
	public int getPosX() { return (int) posX; }

	/**
	 * Return position y.
	 *
	 * @return posY
	 */
	public int getPosY() { return (int) posY; }

	/**
	 * Return collision's box size x.
	 *
	 * @return cX
	 */
	public int getCX() { return cX; }

	/**
	 * Return collision's box size y.
	 *
	 * @return cY
	 */
	public int getCY() { return cY; }

	/**
	 * Return the rectangle of collision's box.
	 *
	 * @return rectangle
	 */
	private Rectangle getRectangle() {
		return new Rectangle((int) posX - cX, (int) posY - cY, cX, cY);
	}

	/*
	 * Setters
	 */

	/**
	 * Set the entity's position.
	 *
	 * @param posX New position x.
	 * @param posY New position y.
	 */
	public void setPosition(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
	}

	/**
	 * Set the map's position.
	 */
	void setMapPosition() {
		xMap = tileMap.getPosX();
		yMap = tileMap.getPosY();
	}

	/**
	 * Test if the entity isn't on screen.
	 */
	public boolean notOnScreen() {
		return posX + xMap + sizeX < 0 ||
				posX + xMap - sizeX > Window.WIDTH ||
				posY + yMap + sizeY < 0 ||
				posY + yMap - sizeY > Window.HEIGHT;
	}

	/**
	 * Delete the entity's textures contain in array list animations.
	 */
	public void unload() {
		for(Animation animation: animations) {
			animation.unload();
		}
	}

	public void setAnimations(int animationID, int priorityValue){
		if(priorityValue > priority){
			animationPlayed = animationID;
		}
	}

	public void setAnimationSpeed(int speed){ this.speed = speed;}


}