package growth.render;

import growth.render.texture.Texture;

/**
 * Animation class.
 * This class is use by entity to store textures.
 *
 * @author MightyCode
 * @version 1.0
 */
public class Animation {

	/**
	 * Current texture displayed.
	 * This variable contains the number of the displayed texture.
	 */
	private int current;

	/**
	 * Delay.
	 * This variable contains the delay (number of frame) between two frame.
	 */
	private final int delay;

	/**
	 * Counter.
	 * This variable is a counter to update animation and the texture's delay.
	 */
	private float count;

	/**
	 * Textures.
	 * This variable contains all of the texture use by animation.
	 */
	private final Texture[] textures;

	/**
	 * Animation class constructor.
	 *
	 * @param path Init the textures from the path.
	 * @param numbAnimation Number of textures that will be played.
	 * @param delay Temp before moving on to the next texture.
	 */
	public Animation(String path, int numbAnimation, int delay) {
		textures = new Texture[numbAnimation];
		for (int i = 0; i < numbAnimation; i++) {
			textures[i] = new Texture((path + Integer.toString(i) + ".png"));
			// Example: load(\IAmAFile\1.png)
		}
		this.delay = delay;
	}

	/**
	 * Update the animation's state.
	 *
	 * @param speed Speed application to the animation.
	 */
	public void update(float speed) {
		count+= speed;
		if (count > delay) {
			count = 0;
			current++;
			if (current >= textures.length) {
				current = 0;
			}
		}
	}

	/**
	 * Delete the texture.
	 */
	public void unload(){
		for(Texture tex : textures){
			tex.unload();
		}
	}

	/**
	 * Bind the current texture
	 */
	public void bind() {
		 textures[current].bind();
	}
}