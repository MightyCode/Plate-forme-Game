package growth.render.gui;

import growth.math.Color4;
import growth.math.Vec2;
import growth.render.text.FontFace;
import growth.render.text.FontRenderer;
import growth.render.texture.Texture;
import growth.render.texture.TextureRenderer;
import growth.screen.ScreenManager;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class GUICheckBox extends GUIComponents{

    private Texture uncheck, uncheck_hover, check, check_hover;
    private Color4 textColor;
    private Color4 hoverTextColor;

    private FontRenderer fontRenderer;

    private boolean mouseOver;

    private String text;
    private FontFace font;

    public GUICheckBox(Vec2 pos, Vec2 size, String text, FontFace font, Color4 textColor, Color4 hoverTextColor){
        super(new Vec2(pos.getX() - (size.getX() / 2), pos.getY() - (size.getY() / 2)),size);
        // Loading texture
        uncheck = new Texture("/textures/menu/GUICheckBox-uncheck.png");
        uncheck_hover = new Texture("/textures/menu/GUICheckBox-uncheckHover.png");
        check = new Texture("/textures/menu/GUICheckBox-check.png");
        check_hover = new Texture("/textures/menu/GUICheckBox-checkHover.png");

        this.text = text;
        this.font = font;

        this.textColor = textColor;
        this.hoverTextColor = hoverTextColor;

        fontRenderer = new FontRenderer(text, font, size.getY()*0.4f, pos, textColor);
        fontRenderer.setPos(new Vec2(this.pos.getX() + size.getX()/2, this.pos.getY()));
    }

    /**
     * Update the button.
     */
    public void update() {
        mouseOver = mouseOver();

        if(mouseOver){
            fontRenderer.setColor(hoverTextColor);
            if(ScreenManager.mouseManager.mousePressed(GLFW_MOUSE_BUTTON_LEFT)) {
                state = (state == 1)? 0 : 1;
                action();
            }
        } else{
            fontRenderer.setColor(textColor);
        }
    }

    /**
     * Display the button.
     */
    public void display() {
        if(state == 1){
            if (mouseOver) {
                check_hover.bind();
            } else {
                check.bind();
            }
        } else{
            if (mouseOver) {
                uncheck_hover.bind();
            } else {
                uncheck.bind();
            }
        }
        TextureRenderer.imageC(pos,size,1f);
        fontRenderer.render();
    }

    /**
     * Free the memory.
     */
    public void unload(){
        uncheck.unload();
        uncheck_hover.unload();
        check.unload();
        check_hover.unload();
    }
}
