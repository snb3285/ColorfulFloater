package com.jim28915.colorfulfloater;

import java.util.ArrayList;

public class Floater {
    private ArrayList<TileInfo> mFloater = null;;
    private int nSize = 0;
    private int nFillSize = 0;

    public Floater(int size) {
        initialize( size );
    }

    public void setSize(int size) {
        cleanUp();
        initialize( size );
    }

    protected void initialize(int size) {
        nSize = size;
        if (nSize > 0) {
            mFloater = new ArrayList<TileInfo>();
            for (int floaterIndex = 0; floaterIndex < nSize; floaterIndex++) {
                mFloater.add(new TileInfo(0, 0,  0,
                        0, 0));
            }
        }
    }
    //release all the object created
    public void cleanUp() {
        for (int floaterIndex = 0; floaterIndex < nSize; floaterIndex++) {
            TileInfo ti = mFloater.get(floaterIndex);
            ti = null;
        }
        mFloater.clear();
        nSize = 0;
        nFillSize = 0;
    }

    public void moveLeft() {
        for (int floaterIndex = 0; floaterIndex < nSize; floaterIndex++) {
            mFloater.get(floaterIndex).moveLeft();
        }
    }

    public void moveRight() {
        for (int floaterIndex = 0; floaterIndex < nSize; floaterIndex++) {
            mFloater.get(floaterIndex).moveRight();
        }
    }

    public void moveDown() {
        for (int floaterIndex = 0; floaterIndex < nSize; floaterIndex++) {
            mFloater.get(floaterIndex).moveDown();
        }
    }

    public boolean addToFloater(Floater floatFactory) {
        TileInfo head = mFloater.get(0);
        if (nFillSize < nSize) {
            TileInfo newTile = floatFactory.getTile(nFillSize);
            TileInfo ti = mFloater.get(nFillSize);
            ti.copyTile(newTile);
            nFillSize++;
            if( nFillSize > 0 ){
                ti.setX(head.getX());
            }
            return true;
        }
        else
            return false;
    }

    public int getFillSize(){
        return nFillSize;
    }

    public void setFillSize(int size){
        nFillSize = size;
    }

    public int getSize(){
        return nSize;
    }

    public TileInfo getTile( int index ){
        TileInfo ti = null;
        if( index >= 0 && index < nSize ){
            ti = mFloater.get(index);
        }
        return ti;
    }

    public void setTilesStatus(int status) {
        for (TileInfo t : mFloater) {
            t.setStatus( status );
        }
    }
}
