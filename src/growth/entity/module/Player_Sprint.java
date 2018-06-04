package growth.entity.module;

import growth.entity.Player;
import growth.screen.ScreenManager;

/**
 * Player speed module class.
 * This class is the module use by the player to go faster.
 *
 * Important : This module need other player module :
 *  - Movement module.
 *
 * @author MightyCode
 * @version 1.0
 */
public class Player_Sprint extends Module{

    /**
     * Player.
     * This variable contains the reference to the player who use this module.
     */
    private Player player;

    /**
     * Movement module.
     * This variable contains the reference to the movement module using by the player.
     */
    private Player_Movement movement;

    /**
     * Run speed coefficient.
     * This variable contains the multiplicative coefficient of the player speed x.
     */
    private float runSpeed;

    /**
     * Jump module class constructor.
     * Instance the class and set the module variables.
     *
     * @param player Player using the module.
     * @param movement The movement right.
     * @param runSpeed The multiplicative coefficient of speed x.
     */
    public Player_Sprint(Player player, Player_Movement movement, float runSpeed){
        super(player);
        this.player = player;
        this.movement = movement;
        this.runSpeed = runSpeed;
    }

    /**
     * Update the module and the player.
     */
    public void update(){
        float speedX = player.getSpeedX();

        if (ScreenManager.KEY.key(5) ) {
            if (speedX > 0 && movement.getRight() && !movement.getLeft()) {
                speedX = movement.getMaxSpeed() * runSpeed;
            } else if (speedX < 0 && movement.getLeft() && !movement.getRight()) {
                speedX = -movement.getMaxSpeed() * runSpeed;
            }
        }

        player.setSpeedX(speedX);
    }
}
