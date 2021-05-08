package ija.ija2021.project.model.tiles;

public class DeliveryPoint extends Tile{

    /***
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public DeliveryPoint(int x, int y) {
        super(x, y);
        this.type = TileType.DELIVERY;
        this.occupied = false;
    }

    /***
     *
     * @return always false (vehicle will not be displayed on delivery point)
     */
    @Override
    public boolean hasVehicle(){
       return false;
    }


}
