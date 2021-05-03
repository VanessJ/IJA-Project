package ija.ija2021.project.model.tiles;


public class Tile {

    protected int x;
    protected int y;
    protected boolean occupied;
    protected TileType type;




    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
        this.type = TileType.PATH;

    }

    public void setAsOccupied(){
        this.occupied = true;
    }

    public void clear (){
        if (this.occupied = true){
            this.occupied = false;
        }
    }

    public boolean isOccupied() {
        if (this.occupied == true) {
            return true;
        } else {
            return false;
        }
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


