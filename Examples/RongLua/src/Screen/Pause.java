/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Screen;


import ClassFrame.Button;
import ClassFrame.Constant;
import ClassFrame.InputKey;
import ClassFrame.Resource;
import GamePlay.CanvasGame;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author QuyetNM1
 */

public class Pause extends IScreen {
    private int scrRealWidth = 0;
//    private Button buttContinue;
//    private Button buttExit;
    public Pause(CanvasGame _canvas) {
        super(_canvas);
//        buttContinue = new Button("Chơi tiếp", Resource.IMG_BUTTONLT, Resource.IMG_TXT_BUTTONLT, 5, Constant.buttFrameLeft_x, Constant.buttFrameLeft_y, Constant.buttonlt_w, Constant.buttonlt_h, Button.ENABLE);
//        buttExit = new Button("Menu", Resource.IMG_BUTTONLT, Resource.IMG_TXT_BUTTONLT, 7, Constant.buttFrameRight_x, Constant.buttFrameRight_y, Constant.buttonlt_w, Constant.buttonlt_h, Button.ENABLE);
    }


    public void load_screen() {
        canvas.getResource().loadArray(Resource.pauseArrayImages);
        canvas.getResource().loadArray(Resource.gamePlayArrayImages);
    }

    public void un_load_screen() {
        canvas.getResource().unLoadArray(Resource.pauseArrayImages);
        canvas.getResource().unLoadArray(Resource.gamePlayArrayImages);
    }
    public void keyEvent(int keycode, int event){
       if(event == InputKey.KEY_EVENT_DOWN){
            if(keycode == InputKey.KEY_RIGHT_SOFTKEY){
                canvas.setScreen( new Menu(canvas));
                canvas.setContinuePlay(CanvasGame.SCR_GAME);
                canvas.getResource().unLoadSound(Resource.SOUND_PLAY);
                canvas.getResource().playSound(Resource.SOUND_MENU, - 1);
            }else if(keycode == InputKey.KEY_LEFT_SOFTKEY){
                canvas.setScreen( new Game(canvas));
                
//                ((Game) canvas.getScreen()).initGame();
//                ((Game) canvas.getScreen()).initLevel();
                
                CanvasGame.isPlaying = true;
            }
       }

    }

    public void pointerEvent(int x, int y, int event) {
//        switch (event) {
//            case InputKey.POINTER_EVENT_DOWN:
//                if (canvas.checkButton(x, y, buttContinue)) {
//                    buttContinue.setState(Button.HOLD_CLICK);
//                } else if (canvas.checkButton(x, y, buttExit)) {
//                    buttExit.setState(Button.HOLD_CLICK);
//                }
//                break;
//            case InputKey.POINTER_EVENT_UP:
//                if (canvas.checkButton(x, y, buttContinue) && buttContinue.getState() == Button.HOLD_CLICK) {
//                    canvas.setScreen( new Game(canvas));
//                } else if (canvas.checkButton(x, y, buttExit) && buttExit.getState() == Button.HOLD_CLICK) {
//                    canvas.setScreen( new Menu(canvas));
//                    canvas.setContinuePlay(CanvasGame.SCR_GAME);
//                }
//                buttContinue.setState(Button.ENABLE);
//                buttExit.setState(Button.ENABLE);
//                break;
//        }
    }

    public void update(){
        
    }
    public void paint(Graphics g){
//        scrRealWidth += 3;
//        canvas.getImage().drawImagePartNormal(g, canvas, Resource.IMG_BG_FRAME, 0, 0, scrRealWidth % Constant.SCR_W, 0, Constant.SCR_W - scrRealWidth % Constant.SCR_W, Constant.SCR_H);
//        canvas.getImage().drawImagePartNormal(g, canvas, Resource.IMG_BG_FRAME, Constant.SCR_W - scrRealWidth % Constant.SCR_W, 0, 0, 0, scrRealWidth % Constant.SCR_W, Constant.SCR_H);

//        canvas.getImage().drawImagePartNormal(g, canvas, Resource.IMG_BG_FRAME, 0, 0, 0, scrRealWidth % Constant.SCR_W, Constant.SCR_H, Constant.SCR_W - scrRealWidth % Constant.SCR_W);
//        canvas.getImage().drawImagePartNormal(g, canvas, Resource.IMG_BG_FRAME, 0, Constant.SCR_W - scrRealWidth % Constant.SCR_W, 0, 0, Constant.SCR_H, scrRealWidth % Constant.SCR_W);

//        canvas.getImage().drawImage(g, canvas, Resource.IMG_FRAME, Constant.frame_x, Constant.frame_y);
        canvas.getImage().drawImage(g, canvas, Resource.IMG_PAUSE, Constant.txt_frame_x, Constant.txt_frame_y);
//        buttContinue.drawButton(g, canvas);
//        buttExit.drawButton(g, canvas);
        
    }

}
