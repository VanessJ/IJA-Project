package ija.ija2021.project.model.tiles;

import com.sun.org.apache.xpath.internal.objects.XNumber;
import ija.ija2021.project.model.Item;
import ija.ija2021.project.model.Vehicle;


import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class Shelf extends Tile{

    private ArrayList<Item> items = new ArrayList<>();
    private int maxCapacity = 20;
    private int amount = 0; //current amount of goods
    private int number;

    public Shelf(int x, int y, int number){
        super(x, y);
        this.number = number;
        this.type = TileType.SHELF;
        this.occupied = true;
    }

    public int getNumber() {
        return number;
    }

    /***
     *
     * @param item Item to be added to rack
     */
    public void addItem(Item item){
        this.items.add(item);
        this.amount++;
    }

    /***
     *
     * @return True if full, False if not
     */

    public boolean isFull(){
        if (this.amount < this.maxCapacity){
            return false;
        }
        else {
            return true;
        }
    }

    /***
     *
     * @return true if empty, false if not
     */
    public boolean isEmpty(){
        if (this.amount == 0){
            return true;
        }
        else {
            return false;
        }
    }

    /***
     * prints stats of items shelved on this tile
     */

    public void printStats(){
        if (this.isEmpty()){
            System.out.println("Regal je prazdny");
            return;
        }
        Map<String, Long> counting = items.stream().collect(
                Collectors.groupingBy(Item::getType, Collectors.counting()));
        //System.out.format("%d (%d, %d): ", this.number, this.x, this.y);
        //System.out.print(": ");
        System.out.println("Regal cislo " + this.number);
        counting.forEach((item,getType)->System.out.println(item + ": " + getType));
        //System.out.println(counting);
    }

    /***
     *
     * @return string representation of item shelved on given tile grouped by type
     */

    public String getStats(){
        String toWrite = "";
        toWrite = toWrite.concat("Regal cislo " + this.number);
        if (this.isEmpty()){
            toWrite = toWrite.concat(" je prazdny");
            return toWrite;
        }
        toWrite = toWrite.concat(":\n");
        Map<String, Long> counting = items.stream().collect(
                Collectors.groupingBy(Item::getType, Collectors.counting()));
        for (String getType : counting.keySet()){
            toWrite = toWrite.concat("\t" + getType + ": " + counting.get(getType) + "\n");
        }

        return toWrite;

    }

    /**
     *
     * @param vehicle vehicle that is looking for given item
     * @param toReserve item to be reserved
     * @return Tile with given item or null if item was not found
     */

    public Tile findAndReserve (Vehicle vehicle, Item toReserve){
        for (Item item: items){
            if (item.getType().equals(toReserve.getType())){
                if (!item.isReserved()){
                    item.reserveItem();
                    vehicle.setComingFor(item);
                    return this;
                }
            }
        }
        return null;
    }

    /***
     *
     * @param toRemove item to be removed
     * @return true if item was found, false if not
     */

    public boolean findAndRemove (Item toRemove){
        for (Item item: items){
            if (item == toRemove){
                items.remove(item);
                return true;
            }
        }
        return false;
    }


    /***
     *
     * @return Capacity of given rack-grid
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }




}
