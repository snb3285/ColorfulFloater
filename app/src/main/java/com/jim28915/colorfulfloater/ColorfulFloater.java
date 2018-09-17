package com.jim28915.colorfulfloater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class ColorfulFloater extends AppCompatActivity {

    public static int MOVE_LEFT = 0;
    public static int MOVE_UP = 1;
    public static int MOVE_DOWN = 2;
    public static int MOVE_RIGHT = 3;

    private static String ICICLE_KEY = "colorfulFloater-view";

    private com.jim28915.colorfulfloater.ColorfulFloaterView mColorfulFloaterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_colorful_floater);
        mColorfulFloaterView = (com.jim28915.colorfulfloater.ColorfulFloaterView) findViewById(R.id.colorful_floater);
        mColorfulFloaterView.setDependentViews((TextView) findViewById(R.id.text),
                findViewById(R.id.arrowContainer), findViewById(R.id.background));
        mColorfulFloaterView.setOnTouchListener(handleTouch );

        if (savedInstanceState == null) {
            // We were just launched -- set up a new game
            mColorfulFloaterView.setMode(com.jim28915.colorfulfloater.ColorfulFloaterData.READY);
        } else {
            // We are being restored
            Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
            if (map != null) {
                mColorfulFloaterView.restoreState(map);
            } else {
                mColorfulFloaterView.setMode(com.jim28915.colorfulfloater.ColorfulFloaterData.PAUSE);
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_colorful_floater, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_number_of_block:
                //
                mColorfulFloaterView.setNumberOfBlock(5);
                return true;
            case R.id.action_select_level:
                // do something
                mColorfulFloaterView.setLevel(2);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Called when the user touches the button
     */
    public void moveLeft(View view) {
        // Do something in response to button click
        if (mColorfulFloaterView.getGameState() == com.jim28915.colorfulfloater.ColorfulFloaterData.RUNNING) {
            mColorfulFloaterView.moveColorfulFloater(MOVE_LEFT);
        }
    }

    /**
     * Called when the user touches the button
     */
    public void moveRight(View view) {
        // Do something in response to button click
        if (mColorfulFloaterView.getGameState() == com.jim28915.colorfulfloater.ColorfulFloaterData.RUNNING) {
            mColorfulFloaterView.moveColorfulFloater(MOVE_RIGHT);
        }
    }

    private View.OnTouchListener handleTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch (View v, MotionEvent event){
            if (mColorfulFloaterView.getGameState() == com.jim28915.colorfulfloater.ColorfulFloaterData.RUNNING) {
                mColorfulFloaterView.rotateFloaters();

            } else {
                // If the game is not running then on touching any part of the screen
                // we start the game by sending MOVE_UP signal to ColorfulFloaterView
                mColorfulFloaterView.moveColorfulFloater(MOVE_UP);
            }
            return false;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        // Pause the game along with the activity
        mColorfulFloaterView.setMode(com.jim28915.colorfulfloater.ColorfulFloaterData.PAUSE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Store the game state
        super.onSaveInstanceState(outState);
        outState.putBundle(ICICLE_KEY, mColorfulFloaterView.saveState());
    }


    /**
     * Handles key events in the game. Update the direction our ColorfulFloater is traveling based on the
     * DPAD.
     *
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                mColorfulFloaterView.moveColorfulFloater(MOVE_UP);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mColorfulFloaterView.moveColorfulFloater(MOVE_RIGHT);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                mColorfulFloaterView.moveColorfulFloater(MOVE_DOWN);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mColorfulFloaterView.moveColorfulFloater(MOVE_LEFT);
                break;
        }

        return super.onKeyDown(keyCode, msg);
    }

}



