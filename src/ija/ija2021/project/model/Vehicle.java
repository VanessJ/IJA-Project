package ija.ija2021.project.model;

import ija.ija2021.project.model.AStar.A_Star;
import ija.ija2021.project.model.tiles.Tile;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class Vehicle {
    private int capacity;
    private int amount;
    private boolean ready;
    private Grid grid;
    private Tile positionTile;
    private int number;
    private int x;
    private int y;
    private A_Star pathfinder = new A_Star();
    private Item comingFor;


    private ArrayList<Item> ordered = new ArrayList<Item>();
    private ArrayList<Item> held = new ArrayList<Item>();
    private ArrayList<Tile> path = new ArrayList<>();

    public Vehicle(int capacity, Grid grid, int number) {
        this.number = number;
        this.capacity = capacity;
        this.grid = grid;
        this.amount = 0;
        this.ready = true;
        int x = this.grid.getDeliveryX();
        int y = this.grid.getDeliveryY();
        this.x = x;
        this.y = y;
        this.positionTile = this.grid.getTile(x, y);
        this.positionTile.bindToVehicle(this);
    }

    public void addToOrdered(Item item){
        this.ordered.add(item);
    }

    public void printOrder(){
        Map<String, Long> counting = this.ordered.stream().collect(
                Collectors.groupingBy(Item::getType, Collectors.counting()));
        System.out.println("Nacitana objednavka: " + counting);
    }


    public void setComingFor(Item comingFor) {
        this.comingFor = comingFor;
    }



    public boolean hasPath(){
        if (this.path != null){
            return true;
        }
        else {
            return false;
        }
    }

    public ArrayList<Tile> getPath() {
        return path;
    }

    public String printStats(){
        String toWrite = "";
        toWrite = toWrite.concat("Vozik cislo " + this.number + "\n\n");
        toWrite = toWrite.concat("Objednavky: \n");
        if (this.ordered.size() == 0){
            toWrite = toWrite.concat("\t Zoznam objednavok je prazdny");
            return toWrite;
        }
        else {
            toWrite = toWrite.concat(this.getOrders());

        }
        toWrite = toWrite.concat("Aktualny naklad: \n");
        if (this.held.size() == 0){
            toWrite = toWrite.concat("\t Vozik je prazdny");
            return toWrite;
        }
        else {
            toWrite = toWrite.concat(this.getOrders());
        }
        return toWrite;
    }

    public String getOrders(){
        String toWrite = "";
        Map<String, Long> counting = this.ordered.stream().collect(
                Collectors.groupingBy(Item::getType, Collectors.counting()));
        for (String getType : counting.keySet()){
            toWrite = toWrite.concat("\t" + getType + ": " + counting.get(getType) + "\n");
        }
        return toWrite;
    }

    public String getHeld(){
        String toWrite = "";
        Map<String, Long> counting = this.held.stream().collect(
                Collectors.groupingBy(Item::getType, Collectors.counting()));
        for (String getType : counting.keySet()){
            toWrite = toWrite.concat("\t" + getType + ": " + counting.get(getType) + "\n");
        }
        return toWrite;
    }



    public void nextMove(){

        if (this.ordered.size() == 0){
            System.out.println("Vozik splnil svoje poslanie a moze spokojne umriet");
            System.exit(0);
        }


        if (this.path.size() == 0){
            //nema ziadnu cestu naplanovanu


            if (this.amount == this.capacity){
                if (path != null){
                    this.path = path;
                }
            }


            if (this.ordered.size() > 0){
                if (this.amount < this.capacity){
                    ArrayList<Tile> path = this.calculatePath();
                    if (path == null){
                        //nenasla sa, prida sa na koniec listu
                        System.out.println("Cesta sa nenasla");
                        Item item = this.ordered.remove(0);
                        this.ordered.add(item);
                    }
                    this.setComingFor(this.ordered.get(0));
                    this.path = path;

                }

                else {
                    //uz nema kapacitu (asi)
                    ArrayList<Tile> path = this.returnToDP();
                    System.out.println("Vozik je plny, rozhodol sa ist k DP");
                    this.path = path;


                }

            }

        }

        //ma cestu
        else {
            //TODO check, ci netreba prepocitat
            if (this.path.size() == 1){

                if (this.comingFor != null){
                    //ide za nejakym objektom
                    this.ordered.remove(0); //removne to z objednavky
                    System.out.println("Vozik nabral objekt " + this.comingFor.getType());
                    this.setComingFor(null);
                    grid.findAndRemove(this.comingFor);
                    this.held.add(this.comingFor);
                    this.comingFor = null;
                    Tile tile = this.path.remove(0);
                    this.amount++;
                }

                else {
                    //ide k DP
                    System.out.println("Vozik priniesol naklad do DP");
                    this.amount = 0;
                    while (this.held.size() > 0){
                        this.held.remove(0);
                    }
                    Tile tile = this.path.remove(0);

                }
            }

            else{
                Tile tile = this.path.remove(0);
                int newX = tile.getX();
                int newY = tile.getY();
                this.moveTo(newX, newY);
                System.out.format("Vozik stoji na suradnici %d %d\n", newX, newY);
            }
        }



    }

    public void setCoords(int newX, int newY){
        this.x = newX;
        this.y = newY;
    }

    public void moveTo(int newX, int newY){
        Tile tile = this.grid.getTile(this.x, this.y);
        tile.unbindVehicle(this);
        this.x = newX;
        this.y = newY;

        tile = this.grid.getTile(newX, newY);
        tile.bindToVehicle(this);
        this.positionTile = tile;
    }

    public ArrayList<Tile> calculatePath(){

        //TODO vymyslieť neretardovaný spôsob
        Item item = this.ordered.get(0);
        Tile tile = this.grid.findAndReserve(this, item);
        if (tile == null){
            System.out.println("Tento error");
            System.exit(-1);
        }


        System.out.format("Vozik pocita cestu pre novu objednavku %s\n", item.getType());
        int start_x = this.x;
        int start_y = this.y;
        int end_x = tile.getX();
        int end_y = tile.getY() - 1; //PLACEHOLDER - kým nevymyslím lepší spôsob
        ArrayList<Tile> path = this.pathfinder.a_Star(this.grid, start_x, start_y, end_x, end_y);
        if (path == null){
            //skusit z druhej strany
            end_y = tile.getY() + 1;
            path = this.pathfinder.a_Star(this.grid, start_x, start_y, end_x, end_y);
            return null;
        }
        return path;
    }


    public ArrayList<Tile> returnToDP(){
        //TODO vymyslieť neretardovaný spôsob
        Tile tile = this.grid.getDP();
        int start_x = this.x;
        int start_y = this.y;
        int end_x = tile.getX();
        int end_y = tile.getY();
        ArrayList<Tile> path = this.pathfinder.a_Star(this.grid, start_x, start_y, end_x, end_y);
        return path;
    }





}


