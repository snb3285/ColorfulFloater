package com.jim28915.colorfulfloater;

public class TileInfo {
    private int x;
    private int y;
    private int color;
    private int status;
    private int match;


    public TileInfo(int newX, int newY, int newColor, int newStatus, int newMatch) {
        x = newX;
        y = newY;
        color = newColor;
        status = newStatus;
        match = newMatch;
    }

    public boolean equals(TileInfo other) {
        return x == other.x && y == other.y && color == other.color && status == other.status && match == other.match;
    }

    @Override
    public String toString() {
        return "TileInfo: [" + x + "," + y + "]" + " color: " + color;
    }

    public void copyTileContent(TileInfo other){
        color = other.color;
        status = other.status;
        match = other.match;
    }

    public void copyTile(TileInfo other){
        x = other.x;
        y = other.y;
        color = other.color;
        status = other.status;
        match = other.match;
    }
    public void set(int newX, int newY, int newColor, int newStatus, int newMatch) {
        x = newX;
        y = newY;
        color = newColor;
        status = newStatus;
        match = newMatch;
    }

    public void moveLeft() {
        x -= 1;
    }

    public void moveRight() {
        x += 1;
    }

    public void moveDown() {
        y += 1;
    }

    public void setX( int newX){
        x = newX;
    }

    int getX( ){
        return x;
    }


    public void setY( int newY){
        y = newY;
    }

    int getY( ){
        return y;
    }

    public void setColor( int newColor){
        color = newColor;
    }

    int getColor( ){
        return color;
    }

    public void setStatus( int newStatus){
        status = newStatus;
    }

    int getStatus( ){
        return status;
    }

    public void setMatch( int newMatch){
        match = newMatch;
    }

    int getMatch( ){
        return match;
    }
}
