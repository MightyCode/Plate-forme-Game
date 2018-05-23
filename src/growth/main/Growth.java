package growth.main;

/**
 * Main class of the game.
 *
 * @author MightyCode
 * @version of the current game developed: 0.2.3
 */
public class Growth {

    /**
     * Window.
     * This global variable contains all of the main game structure.
     */
    public static final Window WINDOW = new Window();

    /**
     * Run the game.
     */
    public static void main(String[] args) {
        WINDOW.run();
    }
}
