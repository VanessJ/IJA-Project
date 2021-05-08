package ija.ija2021.project.model.tiles;


import ija.ija2021.project.model.Vehicle;

public class Tile {
    /***
     * Representation of grid parts with x and y coordinates
     * authors: Vanessa Jóriová, Marián Zimmerman
     */

    protected int x;
    protected int y;
    protected boolean occupied;
    protected Vehicle vehicle;
    protected TileType type;


    /***
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
        this.type = TileType.PATH;
    }

    /***
     *
     * @return vehicle standing on this tile
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /***
     *
     * @return true if tile has vehicle standing on it, false if not
     */

    public boolean hasVehicle(){
        if(this.vehicle != null){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * converts to blockage
     */
    public void setAsOccupied(){

        this.type = TileType.BLOCKAGE;
        this.occupied = true;
    }

    /***
     *
     * @param vehicle vehicle standing on given tile
     */
    public void bindToVehicle(Vehicle vehicle){
        this.vehicle = vehicle;
    }

    /***
     *
     * @param vehicle vehicle that was standing on given tile
     */
    public void unbindVehicle(Vehicle vehicle){
        this.vehicle = null;
    }

    /***
     * clear blockage
     */
    public void clear (){
        if (this.type == TileType.BLOCKAGE){
            this.type = TileType.PATH;
            this.occupied = false;
        }
    }

    /***
     *
     * @return true if occupied, false if not
     */

    public boolean isOccupied() {
        return this.occupied;
    }

    /***
     *
     * @return true if tile is shelf, false if not
     */
    public boolean isShelf() {
        if(this.type == TileType.SHELF){
            return true;
        }
        else {
            return false;
        }
    }

    /***
     *
     * @return true if tile is delivery point, false if not
     */

    public boolean isDP() {
        if(this.type == TileType.DELIVERY){
            return true;
        }
        else {
            return false;
        }
    }

    /***
     *
     * @return true if tile is blockage, false if not
     */
    public boolean isBlockage() {
        if(this.type == TileType.BLOCKAGE){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @param x x coordinate
     * @param y y coordinate
     */

    public void setCoord(int x, int y){
        this.x = x;
        this.y = y;
    }

    /***
     *
     * @return x coordinate
     */
    public int getX() {
        return this.x;
    }

    /***
     *
     * @return y coordinate
     */
    public int getY() {
        return this.y;
    }





}


