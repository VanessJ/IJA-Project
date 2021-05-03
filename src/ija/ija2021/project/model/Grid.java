package ija.ija2021.project.model;

import ija.ija2021.project.model.tiles.DeliveryPoint;
import ija.ija2021.project.model.tiles.Shelf;
import ija.ija2021.project.model.tiles.Tile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Grid {

    public Tile[][] layout;
    public int dimension;
    private ArrayList<Shelf> shelves = new ArrayList<>();
    private int deliveryX;
    private int deliveryY;

    public void LoadLayout(String path) {

        int shelfNumber = 1;
        File file = new File(path);

        try {
            Scanner in = new Scanner(file);

            String line = in.nextLine();
            this.dimension = Integer.parseInt(line);
            System.out.println(this.dimension);
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


    public void findAndRemove (Item toRemove){
        Tile tile;
        for (Shelf shelf: this.shelves){
            boolean removed = shelf.findAndRemove(toRemove);
            if (removed){
                return;
            }
        }
    }


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


    public void getStats(){
        for (Shelf shelf : this.shelves){
            if (!shelf.isEmpty()){
                shelf.getStats();
            }

        }
    }

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




    public int getDeliveryX() {
        return deliveryX;
    }

    public int getDeliveryY() {
        return this.deliveryY;
    }
}
