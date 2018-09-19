package growth.game.tilemap;

import growth.entity.Eobject.Echaracter;
import growth.entity.Eobject.Edrawable;
import growth.entity.Eobject.Emoveable;
import growth.entity.Eobject.Eobject;
import growth.main.Config;
import growth.screen.screens.GameScreen;

/**
 * Map class.
 * This class is use to store a map with the layers and entities.
 *
 * @author MightyCode
 * @version 1.0
 */
public class Map {

	/**
	 * Map id.
	 * This variable contains the Id of the map.
	 */
	private final int mapID;

	/**
	 * Map's beginning tile.
	 * This variable contains the different spawns points in the map.
	 */
	private final float[][] spawn;

	/**
	 * Map's exit interval.
	 * This variable contains the different exit interval for each side of map.
	 */
	private final float[][][] exit;

	/**
	 * Layers.
	 * This variable contains the different layer using in the map.
	 */
	private final Layer[] layer;

	/**
	 * Maps width.
	 * This variable contains the width of the map.
	 */
	private int width;

	/**
	 * Maps height.
	 * This variable contains the height of the map.
	 */
	private int height;

	private String zone, location;

	private String[][] entities;

	/**
	 * Map class constructor.
	 * Instance the class and set the map id.
	 *
	 * @param mapID Map id.
	 */
	public Map(int mapID, int numberSpawns) {
		this.mapID = mapID;
		layer = new Layer[3];
		spawn = new float[numberSpawns][3];
		exit = new float[4][1][4];
	}

	/**
	 * Set the beginning of map in relation to the arrival side.
	 *
	 * @param number The spawn point number.
	 * @param beginX The tile x to begin.
	 * @param beginY The tile y to begin.
	 */
	public void setSpawnTile(int number, float beginX, float beginY, int facing) {
		spawn[number][0] = beginX;
		spawn[number][1] = beginY;
		spawn[number][2] = facing;
	}

	/**
	 * Set a layer on the map.
	 *
	 * @param layerID The number of the layer to initialize.
	 * @param map The map associate to the map.
	 *
	 */
	public void setLayer(int layerID, int[][] map){
		layer[layerID] = new Layer(map);
		width = layer[layerID].getWidth();
		height = layer[layerID].getHeight();
	}

	/**
	 * Set a exit interval for one side
	 *
	 * @param side The side where the exit is.
	 * @param pointName The name/ID of the exit interval.
	 * @param mapID Where the exit go.
	 * @param beg The first number of the exit interval.
	 * @param end The last number of the exit interval.
	 */
	public void setExit(int side, int pointName, int mapID, float beg, float end){
		float[][] old = exit[side];
		exit[side] = new float[exit[side].length+1][4];
		for(int i = 0; i < old.length; i++){
			exit[side][i][0] = old[i][0];
			exit[side][i][1] = old[i][1];
			exit[side][i][2] = old[i][2];
			exit[side][i][3] = old[i][3];
		}

		exit[side][exit[side].length-1][0] = mapID;
		exit[side][exit[side].length-1][1] = pointName;
		exit[side][exit[side].length-1][2] = beg;
		exit[side][exit[side].length-1][3] = end;
	}

	public void setEntities(String[][] entities){
		this.entities = entities;
	}

	public void loadEntity(){
		for (String[] entity : entities) {
			try {
				GameScreen.entityManager.addEntity(
						(Eobject) Class.forName(Config.ENTITY_PATH + entity[0]).getConstructor(String[].class).newInstance((Object) entity));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Getters methods
	 */

	/**
	 * Get the map's tile id.
	 *
	 * @return Map's tile id for the the current layer.
	 */
	int[][] getMap(int currentLayer) {
		try{
			return layer[currentLayer].getMap();
		} catch(Exception e){
			return new int[height][width];
		}
	}

	float[] saveValues(int point){
		return spawn[point];
	}

	/**
	 * Get the data for each exit point for one side.
	 *
	 * @return The data of a sidee.
	 */
	float[][] getExitPoints(int side){
		return exit[side];
	}

	public void setLocation(String newLocation){location = newLocation;}

	public void setZone(String newZone){zone = newZone;}

	public String getLocation(){return location;}

	public String getZone(){return zone;}
}