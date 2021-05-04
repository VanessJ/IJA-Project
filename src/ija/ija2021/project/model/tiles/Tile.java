package ija.ija2021.project.model.tiles;


import ija.ija2021.project.model.Vehicle;

public class Tile {

    protected int x;
    protected int y;
    protected boolean occupied;
    protected Vehicle vehicle;
    protected TileType type;




    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
        this.type = TileType.PATH;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean hasVehicle(){
        if(this.vehicle != null){
            return true;
        }
        else {
            return false;
        }
    }
    public void setAsOccupied(){

        this.type = TileType.BLOCKAGE;
        this.occupied = true;
    }

    public void bindToVehicle(Vehicle vehicle){
        this.vehicle = vehicle;
    }

    public void unbindVehicle(Vehicle vehicle){
        this.vehicle = null;
    }

    public void clear (){
        if (this.type == TileType.BLOCKAGE){
            this.type = TileType.PATH;
            this.occupied = false;
        }
    }


    public boolean isOccupied() {
        return this.occupied;
    }

    public boolean isShelf() {
        if(this.type == TileType.SHELF){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isDP() {
        if(this.type == TileType.DELIVERY){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isBlockage() {
        if(this.type == TileType.BLOCKAGE){
            return true;
        }
        else {
            return false;
        }
    }


    public void setCoord(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }





}


