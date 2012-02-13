package isoj2me;

import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * <p>isoj2me: Item</p>
 *
 * <p>This class manages Items.</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>License: Lesser GPL (http://www.gnu.org)</p>
 *
 * <p>Company: <a href=http://www.mondonerd.com
 * target="_blank">Mondonerd.com</a></p>
 *
 * @author Massimo Maria Avvisati
 * @version 0.5b
 */
public class Item {

    public boolean isMoving = false;
    public String name = "";
    public int x = 0;
    public int y = 0;
    public int z = 0;
    public boolean isWalkable = false;
    public int modifierX = 0; //This changes the painting coordinates in the draw function
    public int modifierY = 0; //This changes the painting coordinates in the draw function
    private int frame = 0;
    private Hashtable actions = new Hashtable(6);
    private String currentAction = null;
    private Hashtable frames = new Hashtable();
    private long startTime = System.currentTimeMillis();
    private long endTime = 0;
    public int speed = 500;

    public Item() {
    }

    /**
     * Create a new Item.
     *
     * @param name Name/code of the Item
     */
    public Item(String name) {
        this.name = name;
    }

    /**
     * Create a new Item
     *
     * @param name Name or code of the Item
     * @param x x on the map
     * @param y y on the map
     * @param z layer on the map
     */
    public Item(String name, int x, int y, int z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * This draw the Item at given coordinates on given Graphics. This method
     * also change the current frame (if more than one) for the current action.
     *
     * @param x x
     * @param y y
     * @param g given Graphics where to paint the Item
     */
    public void draw(int x, int y, Graphics g) {
        endTime = System.currentTimeMillis();
        long deltaTime = 0;
        if (currentAction != null && actions.containsKey(currentAction)) {
            String tempFrame = "";

            if (frame > Integer.parseInt(actions.get(currentAction).toString())) {
                frame = 0;
            }
            if (frame > 0) {
                tempFrame = frame + "";
            }
            deltaTime = endTime - startTime;

            Image temp_image = Utility.loadImage(currentAction + tempFrame, frames);
            g.drawImage(temp_image, x + modifierX,
                    y - temp_image.getHeight() + modifierY,
                    Graphics.TOP | Graphics.LEFT);
            if (deltaTime >= speed) {
                changeFrame();
                startTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * Store an action into this Item action list.
     *
     * @param action basic string used to load frames
     * @param frames number of frames for this action
     */
    public void putAction(String action, int frames) {
        actions.put(action, frames + "");
    }

    /**
     * This return the name that identify the action that is performed in this
     * moment by the Item
     *
     * @return basic string for this action
     */
    public String getCurrentAction() {
        return currentAction;
    }

    /**
     * Calculate the current frame and return the associated Image object
     *
     * @return current Image used
     */
    public Image getCurrentImage() {
        Image temp_image = null;
        if (currentAction != null && actions.containsKey(currentAction)) {
            String tempFrame = "";

            if (frame > Integer.parseInt(actions.get(currentAction).toString())) {
                frame = 0;
            }
            if (frame > 0) {
                tempFrame = frame + "";
            }
            temp_image = Utility.loadImage(currentAction + tempFrame, frames);
        }

        return temp_image;
    }

    /**
     * Set the current action for this Item. The action have to be already in
     * the actions list
     *
     * @param action name of the action to perform
     */
    public void setCurrentAction(String action) {
        if (actions.containsKey(action)) {
            currentAction = action;
        }
    }

    private void changeFrame() {
        if (actions.containsKey(currentAction)) {
            int numberOfFrames = Integer.parseInt(actions.get(currentAction).toString());
            frame++;
            if (frame > numberOfFrames) {
                frame = 0;
            }
        } else {
            frame = 0;
        }
    }

    /**
     * Return the name of the Item
     *
     * @return name of the Item
     */
    public String toString() {
        return this.name;
    }
}