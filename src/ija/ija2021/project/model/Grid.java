package ija.ija2021.project.model;

import ija.ija2021.project.model.tiles.DeliveryPoint;
import ija.ija2021.project.model.tiles.Shelf;
import ija.ija2021.project.model.tiles.Tile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Grid {
    /***
     * Internal representation of warehouse map
     * authors: Vanessa Jóriová, Marián Zimmerman
     */

    public Tile[][] layout;
    public int dimension;
    private ArrayList<Shelf> shelves = new ArrayList<>();
    private int deliveryX;
    private int deliveryY;

    /***
     *
     * @param path path to file from which the layout will be loaded
     */
    public void LoadLayout(String path) {

        int shelfNumber = 1;
        File file = new File(path);

        try {
            Scanner in = new Scanner(file);

            String line = in.nextLine();
            this.dimension = Integer.parseInt(line);
            this.layout = new Tile[dimension][dimension];

            for(int i=0; i<this.dimension; i++){
                line = in.nextLine();
                line.replaceAll("\\s+","");
                for(int j=0; j<this.dimension; j++){
                    char typeSymbol = line.charAt(j);
                    if(typeSymbol == '*'){
                        this.layout[i][j] = new Tile(i,j);
                    }
                    else if(typeSymbol == 'X'){
                        Shelf shelf = new Shelf(i,j, shelfNumber);
                        this.layout[i][j] = shelf;
                        this.shelves.add(shelf);
                        shelfNumber++;

                    }
                    else if(typeSymbol == 'D'){
                        this.layout[i][j] = new DeliveryPoint(i,j);
                        this.deliveryX = i;
                        this.deliveryY = j;
                    }

                    else{
                        System.out.println("Error loading layout");
                        System.exit(-1);
                    }
                }
            }


            in.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /***
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return tile on that position
     */

    public Tile getTile(int x, int y){
        return this.layout[x][y];
    }


    /*public void blockAndClear (int x, int y){
        Tile tile = this.layout[x][y];
        if (!tile.isOccupied() && !tile.isDP()){
            tile.setAsOccupied();
        }
        else if (tile.isBlockage()){
            tile.clear();
        }

    }*/

    /***
     *
     * @param item item to be added
     * @return true if added succesfully, false if warehouse is full
     */
    public boolean addItem(Item item){
        for (Shelf shelf : this.shelves){
            if (!shelf.isFull()){
                shelf.addItem(item);
                return true;
            }
        }
        System.out.println("Sklad je plny\n");
        return false;
    }


    /***
     *
     * @param vehicle vehicle which wants to reserve given item
     * @param toRemove item that will be reserved and later removed by vehicle
     * @return tile with given item if found, null if not
     */
    public Tile findAndReserve (Vehicle vehicle, Item toRemove){
        Tile tile;
        for (Shelf shelf: this.shelves){
            tile = shelf.findAndReserve(vehicle, toRemove);
            if (tile != null){
                return tile;
            }
        }
        return null;
    }

    /***
     *
     * @param toRemove item that will be removed
     */
    public void findAndRemove (Item toRemove){
        for (Shelf shelf: this.shelves){
            boolean removed = shelf.findAndRemove(toRemove);
            if (removed){
                return;
            }
        }
        System.out.println("Tu by sa to dostat nemalo");
    }


    /***
     *
     * @param path path to file containing goods in warehouse
     */
    public void loadGoods(String path){
        try {
            File file = new File(path);
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                String data = in.nextLine();
                String[] split = data.split("\\s+");
                String itemType = split[0];
                int amount = Integer.parseInt(split[1]);
                for (int i = 0; i < amount; i++){
                    Item item = new Item(itemType);
                    if (!this.addItem(item)){
                        System.out.println("Sklad je plny");
                        return;
                    }
                }

            }

            in.close();
        }
        catch (FileNotFoundException e)  {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /***
     * prints stats of all shelves
     */
    public void getStats(){
        for (Shelf shelf : this.shelves){
            if (!shelf.isEmpty()){
                shelf.printStats();
            }

        }
    }


    /***
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return string representation of shelf stats
     */

    public String getShelfStats(int x, int y){
        Shelf shelf = (Shelf)this.layout[x][y];
        String toWrite = shelf.getStats();
        return toWrite;
    }

    /***
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return shelf on given coordinates
     */
    public Shelf getShelf(int x, int y){
        Shelf shelf = (Shelf)this.layout[x][y];
        return shelf;
    }

    /***
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return vehicle on given coordinate
     */

    public Vehicle getVehicle(int x, int y){
        Tile tile = this.layout[x][y];
        return tile.getVehicle();
    }


    /***
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return string form of stats of vehicle on given position
     */
    public String getSVehicleStats(int x, int y){
        Tile tile = this.layout[x][y];
        String toWrite = tile.getVehicle().printStats();
        return toWrite;
    }

    /***
     *
     * @return delivery point of this grid
     */
    public Tile getDP(){
        Tile DP = this.layout[this.deliveryX][this.deliveryY];
        return DP;
    }

    /***
     * Prints console representation of map
     */

    public void printMap(){

        for (Tile [] tile_row : this.layout)
        {
            for (Tile tile : tile_row)
            {
                if (tile.isShelf()){
                    System.out.print(" R ");
                }
                else if (tile.isDP()){
                    System.out.print(" D ");
                }

                else {
                    System.out.print(" * ");
                }

            }
            System.out.println();
        }

    }


    /***
     *
     * @return x coordinate of delivery point
     */
    public int getDeliveryX() {
        return deliveryX;
    }

    /***
     *
     * @return y coordinate of delivery point
     */
    public int getDeliveryY() {
        return this.deliveryY;
    }
}
