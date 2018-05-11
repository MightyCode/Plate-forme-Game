package growth.game.render;

import growth.game.screen.ScreenManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glRotatef;

public class GameFont {
    private Texture[] charactersp, characterso;
    private HashMap<String, IntObject> charlistp = new HashMap<String, IntObject>();
    private HashMap<String, IntObject> charlisto = new HashMap<String, IntObject>();
    private int kerneling;
    private int fontsize = 32;
    private Font font;

    /*
     * Need a special class to hold character information in the hasmaps
     */
    private class IntObject {
        public int charnum;
        IntObject(int charnumpass) {
            charnum = charnumpass;
        }
    }

    /*
     * Pass in the preloaded truetype font, the resolution at which
     * you wish the initial texture to be rendered at, and any extra
     * kerneling you want inbetween characters
     */
    public GameFont(String path, int fontResolution, int extrakerneling) {
        try {
            this.font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(path));
            fontsize = fontResolution;
            createPlainSet();
            createOutlineSet();
            this.kerneling = extrakerneling;
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error of initialisation of GameFont !!");
        }
    }

    /*
     * Create and store the plain (non-outlined) set of the given fonts
     */
    private void createPlainSet() {
        charactersp = new Texture[256];

        for(int i = 0; i < 256; i++) {
            char ch = (char)i;

            BufferedImage image = getFontImage(ch);

            String temptexname = "Char." + i;
            charactersp[i] = new Texture(image);

            charlistp.put(String.valueOf(ch), new IntObject(i));

        }
    }

    /*
     * creates and stores the outlined set for the font
     */
    private void createOutlineSet() {
        characterso = new Texture[256];

        for(int i=0;i<256;i++) {
            char ch = (char)i;

            BufferedImage image = getOutlineFontImage(ch);

            String temptexname = "Charo." + i;
            characterso[i] = new Texture(image);

            charlisto.put(String.valueOf(ch), new IntObject(i));
        }
    }

    /*
     * Create a standard Java2D bufferedimage to later be transferred into a texture
     */
    private BufferedImage getFontImage(char ch) {
        Font tempfont;
        tempfont = font.deriveFont((float)fontsize);
        //Create a temporary image to extract font size
        BufferedImage tempfontImage = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)tempfontImage.getGraphics();
        //// Add AntiAliasing /////
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        ///////////////////////////
        g.setFont(tempfont);
        FontMetrics fm = g.getFontMetrics();
        int charwidth = fm.charWidth(ch);

        if (charwidth <= 0) {
            charwidth = 1;
        }
        int charheight = fm.getHeight();
        if (charheight <= 0) {
            charheight = fontsize;
        }

        //Create another image for texture creation
        BufferedImage fontImage;
        fontImage = new BufferedImage(charwidth,charheight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gt = (Graphics2D)fontImage.getGraphics();
        //// Add AntiAliasing /////
        gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        ///////////////////////////
        gt.setFont(tempfont);

        //// Uncomment these to fill in the texture with a background color
        //// (used for debugging)
        //gt.setColor(Color.RED);
        //gt.fillRect(0, 0, charwidth, fontsize);

        gt.setColor(new java.awt.Color(1.f,1.f,1.f,1.f));
        int charx = 0;
        int chary = 0;
        gt.drawString(String.valueOf(ch), (charx), (chary) + fm.getAscent());

        return fontImage;
    }

    /*
     * Create a standard Java2D bufferedimage for the font outline to later be
     * converted into a texture
     */
    private BufferedImage getOutlineFontImage(char ch) {
        Font tempfont;
        tempfont = font.deriveFont((float)fontsize);

        //Create a temporary image to extract font size
        BufferedImage tempfontImage = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)tempfontImage.getGraphics();
        //// Add AntiAliasing /////
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        ///////////////////////////
        g.setFont(tempfont);
        FontMetrics fm = g.getFontMetrics();
        int charwidth = fm.charWidth(ch);

        if (charwidth <= 0) {
            charwidth = 1;
        }
        int charheight = fm.getHeight();
        if (charheight <= 0) {
            charheight = fontsize;
        }

        //Create another image for texture creation
        int ot = (int)((float)fontsize/24f);

        BufferedImage fontImage;
        fontImage = new BufferedImage(charwidth + 4*ot,charheight + 4*ot, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gt = (Graphics2D)fontImage.getGraphics();
        //// Add AntiAliasing /////
        gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        ///////////////////////////
        gt.setFont(tempfont);

        //// Uncomment these to fill in the texture with a background color
        //// (used for debugging)
        //gt.setColor(Color.RED);
        //gt.fillRect(0, 0, charwidth, fontsize);

        //// Create Outline by painting the character in multiple positions and blurring it
        gt.setColor(new java.awt.Color(1.f,1.f,1.f,1.f));
        int charx = -fm.getLeading() + 2*ot;
        int chary = 2*ot;
        gt.drawString(String.valueOf(ch), (charx) + ot, (chary) + fm.getAscent());
        gt.drawString(String.valueOf(ch), (charx) - ot, (chary) + fm.getAscent());
        gt.drawString(String.valueOf(ch), (charx), (chary) + ot + fm.getAscent());
        gt.drawString(String.valueOf(ch), (charx), (chary) - ot + fm.getAscent());
        gt.drawString(String.valueOf(ch), (charx) + ot, (chary) + ot + fm.getAscent());
        gt.drawString(String.valueOf(ch), (charx) + ot, (chary) - ot + fm.getAscent());
        gt.drawString(String.valueOf(ch), (charx) - ot, (chary) + ot + fm.getAscent());
        gt.drawString(String.valueOf(ch), (charx) - ot, (chary) - ot + fm.getAscent());

        float ninth = 1.0f / 9.0f;
        float[] blurKernel = {
                ninth, ninth, ninth,
                ninth, ninth, ninth,
                ninth, ninth, ninth
        };

        BufferedImageOp blur = new ConvolveOp(new Kernel(3, 3, blurKernel));

        BufferedImage returnimage = blur.filter(fontImage, null);

        return returnimage;

    }

    /*
     * Draws the given characters to the screen
     * size = size of the font (does not change resolution)
     * x,y,z = position to draw at
     * color = color of font to draw
     * rotx, roty, rotz = how much to rotate the font on each axis
     * centered = center the font at the given location, or left justify
     *
     */
    public void drawText(String whatchars , float size, float x, float y, float z, int color, float rotxpass, float rotypass, float rotzpass, boolean centered) {
        float fontsizeratio = size/(float)fontsize;

        int tempkerneling = kerneling;

        int k = 0;
        float realwidth = getWidth(whatchars,size,false);
        glPushMatrix();
        boolean islightingon = glIsEnabled(GL_LIGHTING);

        if (islightingon) {
            glDisable(GL_LIGHTING);
        }

        glTranslatef(x, y, z);
        glRotatef(rotxpass,1,0,0);
        glRotatef(rotypass,0,1,0);
        glRotatef(rotzpass,0,0,1);
        float totalwidth = 0;
        if (centered) {
            totalwidth = -realwidth/2f;
        }
        for (int i = 0; i < whatchars.length(); i++) {
            String tempstr = whatchars.substring(i,i+1);
            k = ((charlistp.get(tempstr))).charnum;
            drawtexture(charactersp[k],fontsizeratio,totalwidth, 0, color, rotxpass, rotypass, rotzpass);
            totalwidth += (charactersp[k].getWidth()*fontsizeratio + tempkerneling);
        }
        if (islightingon) {
            glEnable(GL_LIGHTING);
        }
        glPopMatrix();
    }


    /*
     * Draws the given characters to the screen with a drop shadow
     * size = size of the font (does not change resolution)
     * x,y,z = position to draw at
     * color = color of font to draw
     * shadowcolor = color of the drop shadow
     * rotx, roty, rotz = how much to rotate the font on each axis
     * centered = center the font at the given location, or left justify
     *
     */
   /* public void drawText(String whatchars, float size, float x, float y, float z, Color4f color, Color4f shadowcolor, float rotxpass, float rotypass, float rotzpass, boolean centered) {
        drawText(whatchars,size,x+1f,y-1f,z,shadowcolor,rotxpass,rotypass,rotzpass,centered);
        drawText(whatchars,size,x,y,z,color,rotxpass,rotypass,rotzpass,centered);
    }*/


    /*
     * Draws the given characters to the screen
     * size = size of the font (does not change resolution)
     * x,y,z = position to draw at
     * color = color of font to draw
     * outlinecolor = color of the font's outline
     * rotx, roty, rotz = how much to rotate the font on each axis
     * centered = center the font at the given location, or left justify
     *
     */
    public void drawOutlinedText(String whatchars, float size, float x, float y, float z, int color, int outlinecolor, float rotxpass, float rotypass, float rotzpass, boolean centered) {
        float fontsizeratio = size/(float)fontsize;

        float tempkerneling = kerneling;

        int k = 0;
        int ko = 0;
        float realwidth = getWidth(whatchars,size,true);
        glPushMatrix();
        boolean islightingon = glIsEnabled(GL_LIGHTING);

        if (islightingon) {
            glDisable(GL_LIGHTING);
        }

        glTranslatef(x, y, z);
        glRotatef(rotxpass,1,0,0);
        glRotatef(rotypass,0,1,0);
        glRotatef(rotzpass,0,0,1);
        float xoffset,yoffset;
        float totalwidth = 0;
        if (centered) {
            totalwidth = -realwidth/2f;
        }
        for (int i=0; i < whatchars.length(); i++) {
            String tempstr = whatchars.substring(i,i+1);
            ko = ((charlisto.get(tempstr))).charnum;
            drawtexture(characterso[ko],fontsizeratio,totalwidth,0,outlinecolor, rotxpass, rotypass, rotzpass);

            k = ((charlistp.get(tempstr))).charnum;
            xoffset = (characterso[k].getWidth() - charactersp[k].getWidth())*fontsizeratio/2f;
            yoffset = (characterso[k].getHeight() - charactersp[k].getHeight())*fontsizeratio/2f;
            drawtexture(charactersp[k],fontsizeratio,totalwidth + xoffset,yoffset,color, rotxpass, rotypass, rotzpass);
            totalwidth += ((characterso[k].getWidth()*fontsizeratio) + tempkerneling);
        }
        if (islightingon) {
            glEnable(GL_LIGHTING);
        }
       glPopMatrix();
    }

    /*
     * Draw the actual quad with character texture
     */
    private void drawtexture(Texture texture, float ratio, float x, float y, int color, float rotx, float roty, float rotz) {
        // Get the appropriate measurements from the texture itself
        float imgwidth = texture.getWidth() * ratio;
        float imgheight = -texture.getHeight() * ratio;
        float texwidth = texture.getWidth();
        float texheight = texture.getHeight();

        // Bind the texture
        texture.bind();

        // translate to the right location
        glColor4f(color,color,color, 1);

        // draw a quad with to place the character onto
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex2f(0 + x, 0 - y);

            glTexCoord2f(0, texheight);
            glVertex2f(0 + x, imgheight - y);

            glTexCoord2f(texwidth, texheight);
            glVertex2f(imgwidth + x,imgheight - y);

            glTexCoord2f(texwidth, 0);
            glVertex2f(imgwidth + x,0 - y);
        }
        glEnd();

    }

    /*
     * Returns the width in pixels of the given string, size, outlined or not
     * used for determining how to position the string, either for the user
     * or for this object
     *
     */
    public float getWidth(String whatchars, float size, boolean outlined) {
        float fontsizeratio = size/(float)fontsize;

        float tempkerneling = ((float)kerneling*fontsizeratio);
        float totalwidth = 0;
        int k = 0;
        for (int i=0; i < whatchars.length(); i++) {
            String tempstr = whatchars.substring(i,i+1);
            if (outlined) {
                k = ((charlisto.get(tempstr))).charnum;
                totalwidth += (characterso[k].getWidth()*fontsizeratio) + tempkerneling;
            } else {
                k = ((charlistp.get(tempstr))).charnum;
                totalwidth += (charactersp[k].getWidth()*fontsizeratio) + tempkerneling;
            }
        }
        return totalwidth;
    }

    public void unload(){
        for(Texture tex : charactersp) {
            tex.unload();
        }

        for(Texture tex : characterso) {
            tex.unload();
        }
    }
}