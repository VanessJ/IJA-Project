package ija.ija2021.project.model.tiles;

public class DeliveryPoint extends Tile{


    public DeliveryPoint(int x, int y) {
        super(x, y);
        this.type = TileType.DELIVERY;
        this.occupied = false;
    }

    @Override
    public boolean hasVehicle(){
       return false;
    }


}
