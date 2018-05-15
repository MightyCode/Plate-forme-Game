package growth.game.utils.button;

import growth.game.render.Render;
import growth.game.render.texture.Texture;
import growth.game.screen.Screen;
import growth.main.Growth;
import org.lwjgl.BufferUtils;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

/**
 * ClickButton class.
 * This class is the structure of the click button.
 * The button have two state, one for mouse's hover and one for not.
 *
 * @author MightyCode
 * @version 1.0
 */
public class ClickButton extends AbstractInput {

    /**
     * Mouse position x.
     * This variable contains the detection of mouse's position x.
     */
    private double mouseX;

    /**
     * Mouse position y.
     * This variable contains the detection of mouse's position y.
     */
    private double mouseY;

    /**
     * Over size x.
     * This variable contains button's size x after mouse'hovering.
     */
    private int overSizeX;

    /**
     * Over size y
     * This variable contains button's size y after mouse'hovering.
     */
    private int overSizeY;

    /**
     * Texture idle.
     * This variable contains the texture of the idle's button.
     */
    private Texture texIdle;

    /**
     * Texture Over.
     * This variable contains the texture of the Over's button.
     */
    private Texture texOver;

    /**
     * Button class constructor.
     * Instance the class and set the basic variables.
     *
     * @param posX Position x of the button.
     * @param posY Position y of the button.
     * @param sizeX Size x of the button.
     * @param sizeY Size y of the button.
     * @param name Name of the button to find the file and charge her textures.
     */
    public ClickButton(int posX, int posY, int sizeX, int sizeY, String name, Screen screen) {
        // Call the mother class
        super(posX, posY, sizeX, sizeY, screen);

        // Set the button's variables
            // Size
        overSizeX = (int)(sizeX*1.1);
        overSizeY = (int)( sizeY*1.1);
            // Textures
        texIdle = new Texture("/images/menu/" + name + ".png");
        texOver= new Texture("/images/menu/" + name + "-hover.png");
            // Mouse interaction class
    }

    /**
     * Test if the mouse is over the button.
     */
    private boolean mouseOver() {
        return (mouseX > posX - sizeX/2 && mouseX < posX + sizeX/2) && (mouseY > posY - sizeY/2 && mouseY < posY + sizeY/2);
    }

    /**
     * Update the button.
     */
    public void update() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(Growth.WINDOWID, x, y);

        mouseX = x.get(0);
        mouseY = y.get(0);
        System.out.println(mouseX + " ," + mouseY);
        if(mouseOver()){
            action();
        }
    }

    /**
     * Display the button.
     */
    public void display() {
        if (!mouseOver()) {
            Render.image(posX - (sizeX/2), posY - (sizeY/2), sizeX, sizeY, texIdle.getID(),1);
        } else {
            Render.image(posX - overSizeX/2, posY - overSizeY/2, overSizeX, overSizeY, texOver.getID(),1);
        }
    }

    /**
     * Override class for what does the button.
     */
    public void action(){ }

    /**
     * Free the memory.
     */
    public void unload(){
        texOver.unload();
        texIdle.unload();
    }
}