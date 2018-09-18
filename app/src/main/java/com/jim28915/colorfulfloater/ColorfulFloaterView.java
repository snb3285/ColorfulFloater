package com.jim28915.colorfulfloater;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class ColorfulFloaterView extends View {
    private static final String TAG = "ColorfulFloaterView";
    private ColorfulFloaterData floater = null;

    public ColorfulFloaterView(Context context) {
        super(context);
        init(context,null, 0);
    }

    public ColorfulFloaterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ColorfulFloaterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

     /**
     * mStatusText: Text shows to the user in some run states
     */
    private TextView mStatusText;

    /**
     * mScoreText: Displays the current score
     */
    private TextView mScoreText;

    /**
     * mArrowsView: View which shows 2 arrows to signify 2 directions in which the floater can move
     */
    private View mArrowsView;

    /**
     * mBackgroundView: Background View which shows 4 different colored triangles pressing which
     * moves the floater
     */
    private View mBackgroundView;

    /**
     * Create a simple handler that we can use to cause animation to happen. We set ourselves as a
     * target and we can use the sleep() function to cause an update/invalidate to occur at a later
     * date.
     */

    private Handler mRedrawHandlerContinuous = new Handler();
    private Handler mRedrawHandlerStatic = new Handler();

    public void startRedrawing() {
        mRedrawHandlerContinuous.post(mUIRedrawRunnableContinous);
    }

    private Runnable mUIRedrawRunnableContinous = new Runnable() {
        @Override
        public void run() {
            floater.clearTiles();
            updateFloater();
            ColorfulFloaterView.this.invalidate();
            mRedrawHandlerContinuous.postDelayed(this, floater.getMoveDelay());
        }
    };

    public void redrawOnce() {
        mRedrawHandlerStatic.post(mUIRedrawRunnableStatic);
    }

    private Runnable mUIRedrawRunnableStatic = new Runnable() {
        @Override
        public void run() {
            floater.clearTiles();
            floater.drawTiles();
            ColorfulFloaterView.this.invalidate();
        }
    };

    public void stopRedrawing() {
        mRedrawHandlerContinuous.removeCallbacks(mUIRedrawRunnableContinous);
        floater.copyMTileGridtoMTileGridCopy();
        floater.makeAllTilesInvisible();
        ColorfulFloaterView.this.invalidate();
    }

    public void stopRunnable() {
        mRedrawHandlerContinuous.removeCallbacks(mUIRedrawRunnableContinous);
        floater.makeAllTilesInvisible();
        ColorfulFloaterView.this.invalidate();
    }



    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TileView);
        int tileSize = a.getDimensionPixelSize(R.styleable.TileView_tileSize, 60);

        a.recycle();
        Resources r = this.getContext().getResources();
        floater = null;
        floater = new ColorfulFloaterData();
        floater.resetTiles(tileSize);
        floater.loadTile(ColorfulFloaterData.RED_STAR, r.getDrawable(R.drawable.redstar));
        floater.loadTile(ColorfulFloaterData.YELLOW_STAR, r.getDrawable(R.drawable.yellowstar));
        floater.loadTile(ColorfulFloaterData.GREEN_STAR, r.getDrawable(R.drawable.greenstar));
        floater.loadTile(ColorfulFloaterData.BLUE_STAR, r.getDrawable(R.drawable.bluestar));
        floater.loadTile(ColorfulFloaterData.BROWN_STAR, r.getDrawable(R.drawable.brownstar));
        floater.loadTile(ColorfulFloaterData.CYAN_STAR, r.getDrawable(R.drawable.cyanstar));
        floater.loadTile(ColorfulFloaterData.ORANGE_STAR, r.getDrawable(R.drawable.orangestar));
        floater.loadTile(ColorfulFloaterData.MAGENTA_STAR, r.getDrawable(R.drawable.magentastar));
        floater.loadTile(ColorfulFloaterData.WHITE_STAR, r.getDrawable(R.drawable.whitestar));
        floater.loadTile(ColorfulFloaterData.GRAY_STAR, r.getDrawable(R.drawable.graystar));
    }

    private void initNewGame() {
        if( floater == null )
            floater = new ColorfulFloaterData();
        stopRunnable();
        floater.initNewFloater();
        floater.createNewFloater();
        floater.setNextDirection( ColorfulFloaterData.SOUTH );
        startRedrawing();
    }

    /**
     * Save game state so that the user does not lose anything if the game process is killed while
     * we are in the background.
     *
     * @return a Bundle with this view's state
     */
    public Bundle saveState() {
        Bundle map = new Bundle();

        map.putInt("mFloaterSize", Integer.valueOf(floater.getFloatSize()));
        map.putInt("mDirection", Integer.valueOf(floater.getDirection()));
        map.putInt("mNextDirection", Integer.valueOf(floater.getNextDirection()));
        map.putLong("mMoveDelay", Long.valueOf(floater.getMoveDelay()));
        map.putLong("mScore", Long.valueOf(floater.getScore()));
        map.putLong("mHighestScore", Long.valueOf(floater.getHighestScore()));
        map.putIntArray("mFloater", floater.coordArrayListToArray());
        map.putIntArray("mTileGrid", floater.mTileGridCopyToArray());

        return map;
    }

    /**
     * Restore game state if our process is being relaunched
     *
     * @param icicle a Bundle containing the game state
     */
    public void restoreState(Bundle icicle) {
        setMode(ColorfulFloaterData.PAUSE);

        floater.setFloatSize( icicle.getInt("mFloaterSize") );
        floater.setDirection( icicle.getInt("mDirection") );
        floater.setNextDirection( icicle.getInt("mNextDirection") );
        floater.setMoveDelay(icicle.getInt("mMoveDelay") );
        floater.setScore( icicle.getLong("mScore") );
        floater.setHighestScore( icicle.getInt("mHighestScore") );
        floater.coordArrayToArrayList(icicle.getIntArray("mFloater"));
        floater.ArrayToMTileGrid(icicle.getIntArray("mTileGrid"));
    }

    public void rotateFloaters() {
        if (!floater.getMatch()) {
            floater.rotateFactory();
            redrawOnce();
        }
    }

    public int getGameState(){
        return floater.getGameState();
    }

    /**
     * Handles floater movement triggers from floater Activity and moves the floater accordingly.
     *
     * @param direction The desired direction of movement
     */
    public void moveColorfulFloater(int direction) {
        int mMode = floater.getGameState();
        if (direction == ColorfulFloater.MOVE_UP) {
            if (mMode == ColorfulFloaterData.READY | mMode == ColorfulFloaterData.LOSE) {
                /*
                 * At the beginning of the game, or the end of a previous one,
                 * we should start a new game if UP key is clicked.
                 */
                setMode(ColorfulFloaterData.RUNNING);
                initNewGame();
                return;
            }

            if (mMode == ColorfulFloaterData.PAUSE) {
                /*
                 * If the game is merely paused, we should just continue where we left off.
                 */
                floater.copyMTileGridCopytoMTileGrid();
                setMode(ColorfulFloaterData.RUNNING);
                startRedrawing();
                return;
            }

        }

        if (direction == ColorfulFloater.MOVE_LEFT) {
            floater.shiftFloatersLeft();
            redrawOnce();
        }

        else if (direction == ColorfulFloater.MOVE_RIGHT) {
            floater.shiftFloatersRight();
            redrawOnce();
       }
    }

    /**
     * Sets the Dependent views that will be used to give information (such as "Game Over" to the
     * user and also to handle touch events for making movements
     *
     *
     */
    public void setDependentViews(TextView msgView, TextView scoreView, View arrowView, View backgroundView) {
        mStatusText = msgView;
        mScoreText = scoreView;
        mArrowsView = arrowView;
        mBackgroundView = backgroundView;
    }

    public void showScore(){
        Resources res = getContext().getResources();
        CharSequence str = "";
        str = res.getString(R.string.mode_score, floater.getScore());
        mScoreText.setText(str);
    }
    /**
     * Updates the current mode of the application (RUNNING or PAUSED or the like) as well as sets
     * the visibility of textview for notification
     *
     * @param newMode
     */
    public void setMode(int newMode) {
        int oldMode = floater.getGameState();
        floater.setGameState(newMode);

 
        if (newMode == ColorfulFloaterData.RUNNING && oldMode != ColorfulFloaterData.RUNNING) {
            // hide the game instructions
            mStatusText.setVisibility(View.INVISIBLE);
            mScoreText.setVisibility(View.VISIBLE);
            mArrowsView.setVisibility(View.VISIBLE);
            mBackgroundView.setVisibility(View.VISIBLE);
            return;
        }

        Resources res = getContext().getResources();
        CharSequence str = "";
        if (newMode == ColorfulFloaterData.PAUSE) {
            stopRedrawing();
            mArrowsView.setVisibility(View.GONE);
            mBackgroundView.setVisibility(View.GONE);
            str = res.getText(R.string.mode_pause);
        }
        if (newMode == ColorfulFloaterData.READY) {
            mScoreText.setVisibility(View.GONE);
            mArrowsView.setVisibility(View.GONE);
            mBackgroundView.setVisibility(View.GONE);
            str = res.getText(R.string.mode_ready);
        }
        if (newMode == ColorfulFloaterData.LOSE) {
            stopRedrawing();
            mScoreText.setVisibility(View.GONE);
            mArrowsView.setVisibility(View.GONE);
            mBackgroundView.setVisibility(View.GONE);
            str = res.getString(R.string.mode_lose, floater.getScore());
        }

        mStatusText.setText(str);
        mStatusText.setVisibility(View.VISIBLE);
    }

    /**
     * Figure out which way the floater is going.
     */
    private void updateFloater() {
        floater.setDirection( floater.getNextDirection() );

        // Collision detection
        // For now we have a 1-square wall around the entire arena
        boolean match;
        TileInfo head = floater.getFloater(0);
        if (head != null) {
            if (floater.canMoveFloaterDown()) {
                floater.shiftFloatersDown();
                floater.addToFloater();
            } else {
                if (head.getStatus() == ColorfulFloaterData.mFalling) {
                    floater.setLandedTiles();
                } else if (head.getStatus() == ColorfulFloaterData.mLanded) {
                    floater.setFrozenTiles();
                    floater.setNextDirection(ColorfulFloaterData.SOUTH);
                } else if (head.getStatus() == ColorfulFloaterData.mFrozen) {
                    do {
                        match = floater.matchTiles();
                        floater.setMatch(match);
                        if (match) {
                            floater.removeAllMatchedTiles();
                            floater.tileGravity();
                            redrawOnce();
                        } else {
                            if (floater.isGameOver()) {
                                setMode(ColorfulFloaterData.LOSE);
                                return;
                            }
                            floater.createNewFloater();
                            break;
                        }
                    } while (match);
                }
            }
        }
        floater.drawTiles();
        showScore();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        floater.Draw( canvas );
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int sh = (int)mStatusText.getTextSize();
        floater.changeSize(w, h - sh, oldw, oldh );
    }

    public void setLevel( int l){
        floater.setLevel(l);
    }

    public void setNumberOfBlock( int b ){
        if( b < 3 || b > 7 || b == floater.getFloatSize() )
            return;
        floater.setFloatSize(b);
        initNewGame();
    }
}
