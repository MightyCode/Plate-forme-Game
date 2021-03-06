package growth.game.tilemap;

import growth.game.entity.type.Player;
import growth.main.Config;
import growth.main.Window;
import growth.render.texture.Texture;
import growth.render.texture.TextureRenderer;
import growth.screen.GameManager;
import growth.screen.screens.GameScreen;
import growth.util.XmlReader;
import growth.util.math.Vec2;

import java.util.ArrayList;

/**
 * TileMap class.
 * This class is use to store the game structure.
 *
 * @author MightyCode
 * @version 1.1
 */
@SuppressWarnings("SameReturnValue")
public class TileMap {

	/**
	 * Current.
	 * This variable contains the number of the current map.
	 */
	private int currentMap;

	/**
	 * Map.
	 * This variable contains the current map.
	 */
	private int[][] map;

	/**
	 * Array list of map.
	 * This variable contains the every maps.
	 */
	private final ArrayList<Map> maps = new ArrayList<>();

	/**
	 * Number of row.
	 * This variable contains the number of row in the current map.
	 */
	private int numRows;

	/**
	 * Number of column.
	 * This variable contains the number of column in the current map.
	 */
	private int numCols;

	/**
	 * Map size x.
	 * This variable contains the map's size x.
	 */
	private int sizeX;

	/**
	 * Map size y.
	 * This variable contains the map's size y.
	 */
	private int sizeY;

	/**
	 * Map tile set.
	 * This variable contains tile set.
	 */
	private final Tile[] tileSet;

	private final Texture tileSetT;

	/**
	 * Row off set.
	 * This variable contains the number of row where the player is.
	 */
	private int rowOffset;

	/**
	 * Column off set.
	 * This variable contains the number of column where the player is.
	 */
	private int colOffset;

	/**
	 * Number rows to draw.
	 * This variable contains the number of row to draw.
	 */
	private final int numRowsToDraw;

	/**
	 * Number columns to draw.
	 * This variable contains the number of column to draw.
	 */
	private final int numColsToDraw;

	/**
	 * Current layer.
	 * This variable contains the layer where the player is.
	 * This variable is using to do the collision with the good layer.
	 */
	private int currentLayer;

	/**
	 * Number of map.
	 * This variable contains the number of variable charged on a xml file.
	 */
	private final int nbMap;

	private Player player;

	private float givePosX, givePosY;

	private int newMapId;

	/**
	 * Tilemap class constructor.
	 * Instance the class and set the tile's textures of tile set with the path.
	 *
	 * @param path Path to file's xml to load.
	 */
	public TileMap(String path) {
		// Init variables;

		// Init tileSet
		tileSetT = new Texture("/textures/game/tiles/Tileset.png");
		tileSet = XmlReader.createTileSet(path);

		// Init map
		nbMap = Integer.parseInt(XmlReader.getValueInJar(Config.MAP_OPTION_PATH,"number", "number"))+1;

		for(int i = 1; i < nbMap; i++){
			maps.add(XmlReader.createMap("map"+i+".xml"));
		}
		currentMap = 0;

		currentLayer = 1;
		chargeMap();

		// Init current map variables
		numCols = map[0].length;
		numRows = map.length;
		numRowsToDraw = Window.height / GameScreen.tileSize + 2;
		numColsToDraw = Window.width / GameScreen.tileSize + 2;
		sizeX = numCols * GameScreen.tileSize;
		sizeY = numRows * GameScreen.tileSize;

		// Init camera
		GameManager.CAMERA.setBoundMax(Window.width - sizeX, Window.height  - sizeY);
		GameManager.CAMERA.setBoundMin(0, 0);
	}

	/**
	 * The player associated to the tilemap.
	 * @param player The player.
	 */
	public void setEntity(Player player){
		this.player = player;
	}

	/**
	 * Display the current map.
	 */
	public void display(boolean pos) {
		tileSetT.bind();

		colOffset = -GameManager.CAMERA.getPosX() / GameScreen.tileSize;
		rowOffset = -GameManager.CAMERA.getPosY() / GameScreen.tileSize;

		int begin = (pos)? 0: currentLayer+1;
		int end = (pos)? currentLayer+1 : 4;

		int maxRow = (rowOffset + numRowsToDraw > numRows)? numRows : rowOffset + numRowsToDraw;
		int maxCol = (colOffset + numColsToDraw > numCols)? numCols : colOffset + numColsToDraw;

		// For each layer
		for(int i  =  begin; i < end ; i++){

			int[][] map = maps.get(currentMap).getMap(i);

			// For each row
			for (int row = rowOffset; row < maxRow; row++) {

				//For each col
				for (int col = colOffset; col < maxCol; col++) {

					if(map[row][col]==0)continue;
					TextureRenderer.image(
							new Vec2(col * GameScreen.tileSize, row * GameScreen.tileSize),
							new Vec2(GameScreen.tileSize, GameScreen.tileSize),
							tileSet[map[row][col]].getFrom(),
							tileSet[map[row][col]].getTo()
					);
				}
			}
		}
	}

	/**
	 * Change the map with side.
	 * @param point The side of the window.
	 * @param posX Player's position x.
	 * @param posY Player's position y.
	 *
	 * @return isMap or not
	 */
	public boolean changeMap(int point, float posX, float posY){
		int[] result = isMap( Math.abs(point-2), posX, posY);
		if(result[0] == 1){
			changeMap(result[1], Math.abs(point-2));
			return true;
		}
		return false;
	}

	/**
	 * Change the map with mapID.
	 * @param mapID The new map.
	 * @param point The point to come.
	 */
	public void changeMap(int mapID, int point){
		GameScreen.setState(GameScreen.STATE_TRANSITION);
		newMapId = mapID;
		givePosX = maps.get(mapID).getTileToComeX(point) * GameScreen.tileSize;
		givePosY = maps.get(mapID).getTileToComeY(point) * GameScreen.tileSize - player.getSizeY()/2;
	}

	/**
	 * Check if there are a next map for the player's position.
	 *
	 * @param side The side to change the map.
	 * @param x The player position x.
	 * @param y The player position y.
	 *
	 * @return The new map id and the spawn point.
	 */
	private int[] isMap(int side, float x, float y) {
		float posX = x / GameScreen.tileSize;
		float posY = y / GameScreen.tileSize;

		float[][] neighbour = maps.get(currentMap).getExitPoints(Math.abs(side-2));

		for (float[] aNeighbour : neighbour) {
			if (side == 0 || side == 2) {
				if (aNeighbour[2] < posY && posY < aNeighbour[3]) {
					return new int[]{1, (int) aNeighbour[0] - 1};
				}
			} else if (side == 1 || side == 3) {
				if (aNeighbour[2] < posX && posX < aNeighbour[3]) {
					return new int[]{1, (int) aNeighbour[0] - 1};
				}
			}
		}
		return new int[]{0};
	}

	/**
	 * Set the new map and give the position to the player.
	 */
	public void doTransition(){
		player.setPosition(givePosX, givePosY);
		currentMap = newMapId;
		chargeMap();
		numCols = map[0].length;
		numRows = map.length;

		sizeX = numCols * GameScreen.tileSize;
		sizeY = numRows * GameScreen.tileSize;

		GameManager.CAMERA.setBoundMax(Window.width - sizeX, Window.height  - sizeY);
		GameManager.CAMERA.setBoundMin(0, 0);
		GameManager.CAMERA.setPosition(false);
		System.out.println("New map, id: " + currentMap);
	}


	/**
	 * Charge the current layer for collision and another features.
	 */
	private void chargeMap(){
		map = maps.get(currentMap).getMap(currentLayer);
	}

	/**
	 * Change the layer to a higher layer.
	 */
	public void setLayer(int numberToAdd){
		if(currentLayer + numberToAdd > 0 || currentLayer + numberToAdd < 2) {
			currentLayer+= numberToAdd;
			System.out.println("Change the current layer to " + currentLayer);
			chargeMap();
		}
	}

	/**
	 * Return the number of map'row.
	 *
	 * @return the number of row
	 */
	public int getNumRows() {
		return numRows;
	}

	/**
	 * Return the number of map'column.
	 *
	 * @return the number of column
	 */
	public int getNumCols() {
		return numCols;
	}

	/**
	 * Return the map size x.
	 *
	 * @return size x
	 */
	public int getSizeX() {
		return sizeX;
	}

	/**
	 * Return the map size y.
	 *
	 * @return size y
	 */
	public int getSizeY() {
		return sizeY;
	}

	/**
	 * Return type of block.
	 *
	 * @param row Number of row to choose the tile's number.
	 * @param col Number of col to choose the tile's number.
	 *
	 * @return tile's type
	 */
	public int getType(int row, int col) {
		int rc = map[row][col];
		return tileSet[rc].getType();
	}

	/**
	 * Unload the tile's texture to free memory.
	 */
	public void unload() {
		tileSetT.unload();
	}
}