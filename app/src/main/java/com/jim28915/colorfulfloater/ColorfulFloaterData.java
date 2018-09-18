/*
 * Copyright (C) 2018

  */

package com.jim28915.colorfulfloater;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import java.util.Random;

/**
 * ColorfulFloaterData: a data object for floater.
 * 
 */
public class ColorfulFloaterData {

    /**
     * Labels for the drawables that will be loaded into the TileView class
     */
    public static final int RED_STAR = 1;
    public static final int YELLOW_STAR = 2;
    public static final int GREEN_STAR = 3;
    public static final int BLUE_STAR = 4;
    public static final int BROWN_STAR = 5;
    public static final int CYAN_STAR = 6;
    public static final int ORANGE_STAR = 7;
    public static final int MAGENTA_STAR = 8;
    public static final int WHITE_STAR = 9;
    public static final int GRAY_STAR = 10;

    /**
     * mScore: Used to track the number of apples captured mMoveDelay: number of milliseconds
     * between floater movements. This will decrease as apples are captured.
     */
    private long mScore = 0;
    private  long mHighestScore = 0;
    private long mMoveDelay = 1000;
    private static final long moveDelay = 1000;
    private long mLastMove = 0;

    /**
     * Parameters controlling the size of the tiles and their range within view. Width/Height are in
     * pixels, and Drawables will be scaled to fit to these dimensions. X/Y Tile Counts are the
     * number of tiles that will be drawn.
     */

    private static int mTileSize = 11;

    private static int mXTileCount;
    private static int mYTileCount;

    private static int mXOffset;
    private static int mYOffset;
    private static int mFloaterSize = 3;
    private static int mLevel = 1;

    private final Paint mPaint = new Paint();

    /**
     * Current mode of application: READY to run, RUNNING, or you have already lost. static final
     * ints are used instead of an enum for performance reasons.
     */
    private int mMode = READY;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;

    /**
     * These strings indicate the status of the tiles.
     * mFrozen: Used to indicate that a tile is frozen and should not be removed by ClearTiles. Frozen
     * tiles should accumulate near the bottom and be checked for matches.
     * mLanded: Used to indicate that a tile has landed and will freeze on the next tick if
     * no action is taken.
     * mFalling: This means that the tile is part of a faller that is still falling and has not
     * landed yet.
     */
    public static final int mFalling = 0;
    public static final int mLanded = 1;
    public static final int mFrozen = 2;

    /**
     * These strings indicate whether a tile is being matched or not. When a tile is marked for
     * matching, it will be destroyed on the next tick and all tiles above it will fall to fill
     * the space left behind.
     */
    public static final int mUnmatched = 0;
    public static final int mMatched = 1;

    /**
     * Current direction the floater is headed.
     */
    private int mDirection = SOUTH;
    private int mNextDirection = SOUTH;
    public static final int SOUTH = 2;


    /**
     * A hash that maps integer handles specified by the subclasser to the drawable that will be
     * used for that reference
     */
    private Bitmap[] mTileArray;

    /**
     * mFloaterFactory: A list of TileInfos used for mFloater.
     * mFloater: A list of TileInfos .
     */
    private Floater mFloater;
    private Floater mFloaterFactory;

    /**
     * Everyone needs a little randomness in their life
     */
    private static final Random RNG = new Random();
    protected static final int mTileCount = 11;

    /**
     * mMatch: Used to indicate whether or not there are matching tiles that will be removed on the
     * next tick.
     */
    private boolean mMatch = false;

    /**
     * A two-dimensional array of TileInfo objects
     */
    private TileInfo[][] mTileGrid;
    private TileInfo[][] mTileGridCopy;

    public ColorfulFloaterData() {
        mXTileCount = 0;
        mYTileCount = 0;
        mTileArray = null;
        mFloater = new Floater(mFloaterSize);
        mFloaterFactory = new Floater(mFloaterSize);
    }

    public void setFloatSize(int s) {
        mFloaterSize = s;
        mFloater = null;
        mFloaterFactory = null;
        mFloater = new Floater(mFloaterSize);
        mFloaterFactory = new Floater(mFloaterSize);
    }

    public int getFloatSize() {
        return mFloaterSize;
    }

    public void setLevel(int s) {
        if( s > 0 )
            mLevel = s;
        mMoveDelay = moveDelay/mLevel;
    }

    public TileInfo getFloater(int index) {
        return mFloater.getTile(index);
    }

    public int getFloatFillSize() {
        return mFloater.getFillSize();
    }

    public void setTileSize(int s) {
        mTileSize = s;
    }

    public void setNextDirection(int d) {
        mNextDirection = d;
    }

    public void setDirection(int d) {
        mDirection = d;
    }

    public int getNextDirection() {
        return mNextDirection;
    }

    public int getDirection() {
        return mDirection;
    }

    public void setScore(int s) {
        mScore = s;
    }

    public void incScore(int s) { mScore += s;}

    public void setMoveDelay(long d) {
        mMoveDelay = d;
    }

    public void setScore(long d) {
        mScore = d;
    }

    public long getScore() {
        return mScore;
    }

    public long getMoveDelay() {
        return mMoveDelay;
    }

    public void setHighestScore(int s) {
        mHighestScore = s;
    }

    public long getHighestScore() {
        if( mScore > mHighestScore)
            mHighestScore = mScore;
        return mHighestScore;
    }

    public void setLastMove(long d) {
        mLastMove = d;
    }

    public long getLastMove() {
        return mLastMove;
    }
    /**
     * @return the Game state as Running, Ready, Paused, Lose
     */
    public int getGameState() {
        return mMode;
    }

    public void setGameState(int m) {
        mMode = m;
    }

    public boolean getMatch() {
        return mMatch;
    }

    public void setMatch(boolean match) {
        mMatch = match;
    }

    /**
     * Release all the object created.
     */
    public void cleanUp() {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                mTileGrid[x][y] = null;
                mTileGridCopy[x][y] = null;
            }
        }
        mFloater.cleanUp();
        mFloaterFactory.cleanUp();
    }

    public void drawTiles() {
        int size = mFloater.getFillSize();
        for (int index = 0; index < size; index++) {
            TileInfo c = mFloater.getTile(index);
            if (c != null)
                setTile(c.getX(), c.getY(), c.getColor(), c.getStatus(), c.getMatch());
        }
    }

    /**
     * Given a ArrayList of TileInfos, we need to flatten them into an array of ints before we can
     * stuff them into a map for flattening and storage.
     *
     * @return : a simple array containing the x/y values of the TileInfos as
     * [x1,y1,x2,y2,x3,y3...]
     */
    public int[] coordArrayListToArray() {
        int size = mFloater.getFillSize();
        if (size == 0) return null;
        int[] rawArray = new int[size * 5];

        int i = 0;
        for (int k = 0; k < size; k++) {
            TileInfo c = mFloater.getTile(k);
            if (c != null) {
                rawArray[i++] = c.getX();
                rawArray[i++] = c.getY();
                rawArray[i++] = c.getColor();
                rawArray[i++] = c.getStatus();
                rawArray[i++] = c.getMatch();
            }
        }

        return rawArray;
    }

    public int[] mTileGridCopyToArray() {
        int[] rawArray = new int[getLengthOfMTileGrid() * 5];

        int i = 0;
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                rawArray[i++] = mTileGridCopy[x][y].getX();
                rawArray[i++] = mTileGridCopy[x][y].getY();
                rawArray[i++] = mTileGridCopy[x][y].getColor();
                rawArray[i++] = mTileGridCopy[x][y].getStatus();
                rawArray[i++] = mTileGridCopy[x][y].getMatch();
            }
        }
        return rawArray;
    }

    /**
     * Given a flattened array of ordinate pairs, we reconstitute them into a ArrayList of
     * TileInfo objects
     *
     * @param rawArray : [x1,y1,x2,y2,...]
     * @return a ArrayList of TileInfos
     */
    public void coordArrayToArrayList(int[] rawArray) {
        int coordCount = rawArray.length;
        int count = 0;
        for (int index = 0; index < coordCount; index += 5) {
            TileInfo c = mFloater.getTile(index);
            c.set(rawArray[index], rawArray[index + 1], rawArray[index + 2],
                    rawArray[index + 3], rawArray[index + 4]);
            count++;
        }
        mFloater.setFillSize(count);
    }

    public void ArrayToMTileGrid(int[] rawArray) {

        int coordCount = rawArray.length;
        for (int index = 0; index < coordCount; index += 5) {
            int x = rawArray[index];
            int y = rawArray[index + 1];
            mTileGrid[x][y].set(x, y, rawArray[index + 2],
                    rawArray[index + 3], rawArray[index + 4]);
        }
    }

    /**
     * Resets all tiles that have not landed or are frozen to 0 (empty)
     */
    public void clearTiles() {
        if (getLengthOfMTileGrid() == (mXTileCount * mYTileCount)) {
            for (int x = 0; x < mXTileCount; x++) {
                for (int y = 0; y < mYTileCount; y++) {
                    if (mTileGrid[x][y] != null && mTileGrid[x][y].getStatus() != mFrozen) {
                        setTile(x, y, 0, 0, 0);
                    }
                }
            }
        } else {
            initializeTiles(mTileGrid);
        }
    }

    public void initializeTiles(TileInfo[][] tileGrid) {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                tileGrid[x][y] = new TileInfo(x, y, 0, 0, 0);
            }
        }
    }

    public void makeAllTilesInvisible() {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                setTile(x, y, 0, 0, 0);
            }
        }
    }

    public int getLengthOfMTileGrid() {
        int totalLength = 0;
        for (TileInfo[] c : mTileGrid) {
            totalLength += c.length;
        }
        return totalLength;
    }

    public boolean matchTiles() {
        boolean matches = false;
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                boolean tileHasMatch = checkForMatchingTiles(y, x);
                if (tileHasMatch) {
                    matches = true;
                }
            }
        }
        return matches;
    }

    private boolean checkForMatchingTiles(int row, int col) {
        boolean match = false;
        if (threeOrMoreInARow(row, col, 0, 1) || threeOrMoreInARow(row, col, 1, 1)
                || threeOrMoreInARow(row, col, 1, 0) || threeOrMoreInARow(row, col, 0, -1)
                || threeOrMoreInARow(row, col, 1, -1) || threeOrMoreInARow(row, col, -1, 1)
                || threeOrMoreInARow(row, col, -1, 0) || threeOrMoreInARow(row, col, -1, -1)) {
            match = true;
        }
        return match;
    }

    private boolean threeOrMoreInARow(int row, int col, int rowdelta, int coldelta) {
        boolean threeOrMore = false;
        TileInfo startTile = mTileGrid[col][row];
        if (startTile.getColor() > 0 && startTile.getColor() < 12) {
            int numMatchedTiles = 0;
            for (int factor = 1; factor < Math.max(mXTileCount, mYTileCount); factor++) {
                if (!(isValidRowNumber(row + rowdelta * factor) && isValidColumnNumber(col + coldelta * factor)
                        && mTileGrid[col + coldelta * factor][row + rowdelta * factor].getColor() == startTile.getColor())) {
                    numMatchedTiles = factor;
                    break;
                }
            }
            if (numMatchedTiles >= getFloatSize()) {
                threeOrMore = true;
                for (int delta = 0; delta < numMatchedTiles; delta++) {
                    if (isValidRowNumber(row + rowdelta * delta) && isValidColumnNumber(col + coldelta * delta)
                            && mTileGrid[col + coldelta * delta][row + rowdelta * delta].getColor() == startTile.getColor()) {
                        if( mTileGrid[col + coldelta * delta][row + rowdelta * delta].getMatch() == mUnmatched ) {
                            mTileGrid[col + coldelta * delta][row + rowdelta * delta].setMatch(mMatched);
                        }
                    }
                }
            }
        }
        return threeOrMore;
    }

    protected void removeAllMatchedTiles() {
        for (int y = 0; y < mYTileCount; y++) {
            for (int x = 0; x < mXTileCount; x++) {
                if (mTileGrid[x][y].getMatch() == mMatched) {
                    mTileGrid[x][y].setStatus(0);
                    mTileGrid[x][y].setColor(0);
                    incScore(100);
                }
            }
        }
    }

    public void tileGravity() {
        for (int y = mYTileCount - 1; y >= 0; y--) {
            for (int x = 0; x < mXTileCount; x++) {
                TileInfo currentTile = mTileGrid[x][y];
                if (currentTile.getMatch() == mMatched) {
                    //we have match, move all the tile above down
                    //Find first unmatch tile above
                    int unmatched = 0;
                    for (int k = y - 1; k >= 0; k--) {
                        TileInfo tile = mTileGrid[x][k];
                        if (tile.getMatch() != mMatched) {
                            unmatched = k;
                            break;
                        }
                    }
                    TileInfo tile = mTileGrid[x][unmatched];
                    currentTile.copyTileContent(tile);
                    int gap = y - unmatched;
                    //then move down all the tiles above
                    for (int k = y; k >= gap; k--) {
                        TileInfo downTile = mTileGrid[x][k];
                        TileInfo upTile = mTileGrid[x][k - gap];
                        downTile.copyTileContent(upTile);
                    }
                    for (int k = gap; k >= 0; k--) {
                        TileInfo toptile = mTileGrid[x][k];
                        toptile.set(x, k, 0, 0, 0);
                    }
                }
            }
        }
    }

    public boolean isValidRowNumber(int row) {
        return (0 <= row && row < mYTileCount);
    }

    public boolean isValidColumnNumber(int col) {
        return (0 <= col && col < mXTileCount);
    }

    public void initNewFloater() {
        setScore(0);
        mFloaterFactory.setSize(mFloaterSize);
        mFloater.setSize(mFloaterSize);
    }

    public void createNewFloater() {
        mFloater.setFillSize(0);
        int randX = RNG.nextInt(mXTileCount);
        for (int floaterIndex = 0; floaterIndex < mFloaterSize; floaterIndex++) {
            TileInfo ti = mFloaterFactory.getTile(floaterIndex);
            if( ti != null){
                ti.set(randX, 0, RNG.nextInt(mTileCount - 1) + 1,
                        mFalling, mUnmatched);
            }
        }
    }

    //If game is over, return false, otherwise return true.
    public boolean addToFloater() {
        mFloater.addToFloater(mFloaterFactory);
        return true;
    }

    public boolean isFloaterFreeze() {
        TileInfo ti = mFloater.getTile(0);
        if (ti != null) {
            if (ti.getStatus() == mFrozen)
                return true;
        }
        return false;
    }

    public boolean canMoveFloaterLeft() {
        TileInfo ti = mFloater.getTile(0);
        if (ti != null) {
            if( ti.getStatus() == mFrozen )
                return false;
            int x = ti.getX();
            //hit the left wall
            if (x <= 0)
                return false;
            //left has a float
            if (mTileGrid[x - 1][ti.getY()].getColor() != 0)
                return false;
            else
                return true;
        } else
            return false;
    }

    public boolean canMoveFloaterRight() {
        TileInfo ti = mFloater.getTile(0);
        if (ti != null) {
            if( ti.getStatus() == mFrozen )
                return false;
            int x = ti.getX();
            //hit the right wall
            if (x >= mXTileCount - 1)
                return false;
            //right has a float
            if (mTileGrid[x + 1][ti.getY()].getColor() != 0)
                return false;
            else
                return true;
        } else
            return false;
    }

    public boolean canMoveFloaterDown() {
        if( mFloater.getFillSize() == 0 )
            return true;
        TileInfo ti = mFloater.getTile(0);
        if (ti != null) {
            if( ti.getStatus() == mFrozen )
                return false;
            int y = ti.getY();
            //hit the right wall
            if (y >= mYTileCount - 1)
                return false;
            //right has a float
            if (mTileGrid[ti.getX()][y + 1].getColor() != 0)
                return false;
            else
                return true;
        } else
            return false;
    }

    public boolean isGameOver() {
        boolean bOver = ( getFloatFillSize() > 0 ) && (getFloatFillSize() < getFloatSize()) && !canMoveFloaterDown();
        if( bOver && mScore > mHighestScore)
            mHighestScore = mScore;
        return bOver;
    }

    public void shiftFloatersLeft() {
        if (canMoveFloaterLeft())
            mFloater.moveLeft();
    }

    public void shiftFloatersRight() {
        if (canMoveFloaterRight())
            mFloater.moveRight();
    }

    public void shiftFloatersDown() {
        if (canMoveFloaterDown())
            mFloater.moveDown();
    }

    public void rotateFactory() {
        int size = mFloaterFactory.getSize();
        if (size < 2)
            return;
        int temp = mFloaterFactory.getTile(0).getColor();
        for (int i = 0; i < size - 1; i++)
            mFloaterFactory.getTile(i).setColor(mFloaterFactory.getTile(i + 1).getColor());
        mFloaterFactory.getTile(size - 1).setColor(temp);
        copyMFloaterCopyToMFloater();
    }

    public void copyMFloaterCopyToMFloater() {
        int size = mFloater.getFillSize();
        for (int floaterIndex = 0; floaterIndex < size; floaterIndex++) {
            TileInfo ti = mFloater.getTile(floaterIndex);
            ti.setColor(mFloaterFactory.getTile(floaterIndex).getColor());
        }
    }

    /**
     * Function to set the specified Drawable as the tile for a particular integer key.
     *
     * @param key
     * @param tile
     */
    public void loadTile(int key, Drawable tile) {
        Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, mTileSize, mTileSize);
        tile.draw(canvas);

        mTileArray[key] = bitmap;
    }


    /**
     * Rests the internal array of Bitmaps used for drawing tiles, and sets the maximum index of
     * tiles to be inserted
     *
     * @param size
     */

    public void resetTiles(int size) {
        if(mTileArray != null) {
            for (int i = 0; i < mTileCount; i++)
                mTileArray[i] = null;
        }
        mTileSize = size;
        mTileArray = new Bitmap[mTileCount];
    }

    /**
     * Used to indicate that a particular tile (set with loadTile and referenced by an integer)
     * should be drawn at the given x/y TileInfos during the next invalidate/draw cycle.
     *
     * @param x
     * @param y
     * @param tileColor
     * @param tileStatus
     * @param tileMatch
     */
    public void setTile(int x, int y, int tileColor, int tileStatus, int tileMatch) {
        if (x < 0 || x >= mXTileCount) {
            return;
        }
        if (y < 0 || y >= mYTileCount) {
            return;
        }
        TileInfo ti = mTileGrid[x][y];
        ti.set(x, y, tileColor, tileStatus, tileMatch);
    }

    public void setLandedTiles() {
        mFloater.setTilesStatus(mLanded);
    }

    public void copyMTileGridtoMTileGridCopy() {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                mTileGridCopy[x][y].copyTileContent(mTileGrid[x][y]);
            }
        }
    }

    public void copyMTileGridCopytoMTileGrid() {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                mTileGrid[x][y].copyTileContent(mTileGridCopy[x][y]);
            }
        }
    }

    public void setFrozenTiles() {
        mFloater.setTilesStatus(mFrozen);
    }

    public TileInfo getTile(int x, int y) {
        return mTileGrid[x][y];
    }


    public TileInfo[][] getGrid() {
        return mTileGrid;
    }

    public void Draw(Canvas canvas) {
        if (mTileGrid.length > 0) {
            for (int x = 0; x < mXTileCount; x += 1) {
                for (int y = 0; y < mYTileCount; y += 1) {
                    if (mTileGrid[x][y] instanceof TileInfo && mTileGrid[x][y].getColor() > 0) {
                        canvas.drawBitmap(mTileArray[mTileGrid[x][y].getColor()], mXOffset + x * mTileSize,
                                mYOffset + y * mTileSize, mPaint);
                    }
                }
            }
        }
    }

    public void changeSize(int w, int h, int offw, int offh) {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                mTileGrid[x][y] = null;
                mTileGridCopy[x][y] = null;
            }
        }
        mXTileCount = (int) Math.floor(w / mTileSize);
        mYTileCount = (int) Math.floor(h / mTileSize);

        mXOffset = ((w - (mTileSize * mXTileCount)) / 2);
        mYOffset = ((h - (mTileSize * mYTileCount)) / 2);

        mXOffset = offh;
        mYOffset = offw;

        mTileGrid = new TileInfo[mXTileCount][mYTileCount];
        mTileGridCopy = new TileInfo[mXTileCount][mYTileCount];
        initializeTiles(mTileGrid);
        initializeTiles(mTileGridCopy);
    }
}
