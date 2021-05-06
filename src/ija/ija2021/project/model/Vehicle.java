package ija.ija2021.project.model;

import ija.ija2021.project.model.AStar.A_Star;
import ija.ija2021.project.model.tiles.Tile;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class Vehicle {
    private int capacity;
    private int amount;
    private boolean active;
    private Grid grid;
    private Tile positionTile;
    private int number;
    private int x;
    private int y;
    private boolean finished;
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
        this.active = false;
        this.finished = false;
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


    public boolean isFinished(){
        return this.finished;
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
        toWrite = toWrite.concat("Kapacita: " + this.capacity + "\n\n");
        toWrite = toWrite.concat("Objednavky: \n");
        if (this.ordered.size() == 0){
            toWrite = toWrite.concat("\tZiadne objednavky\n");
        }
        else {
            toWrite = toWrite.concat(this.getOrders());

        }
        toWrite = toWrite.concat("Aktualny naklad: \n");
        if (this.held.size() == 0){
            toWrite = toWrite.concat("\t Vozik je prazdny");
        }
        else {
            toWrite = toWrite.concat(this.getHeld());
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

    public boolean isFull(){
        if (this.amount >= this.capacity){
            return true;
        }
        return false;
    }


    public void nextMove(){

        if((this.finished)&& (this.ordered.size()!= 0)){
            //System.out.println("Lenive hovna");
            this.finished = false;
        }

        if ((this.ordered.size() == 0) && (!this.active)){
            ArrayList<Tile> path = this.returnToDP();
            if (path == null){
                return;
            }
            this.path = path;
            //System.out.println(this.path.size());
            this.active = true;
        }

        if(isFinished()){
            Tile tile = this.grid.getTile(this.x, this.y);
            //System.out.println("Auto skoncilo");
            tile.unbindVehicle(this);
            return;
        }
        //nacita objednavku
        if (!active){
            if (this.isFull()){
                ArrayList<Tile> path = this.returnToDP();
                if (path == null){
                    return;
                }
                //System.out.println("Vozik je plny, rozhodol sa ist k DP");
                this.path = path;
                this.active = true;
            }
            else{
                ArrayList<Tile> path = this.calculatePath();
                if (path == null){ //cesta sa nenasla, aktualna objednavka sa prida na koniec listu
                    Item item = this.ordered.remove(0);
                    this.ordered.add(item);
                    return;
                }
                this.path = path;
                this.active = true;
            }
        }
        else {
            if (this.path.size() == 0){
                //vyklada item
                if (this.comingFor != null){
                    this.ordered.remove(0); //removne to z objednavky
                    //System.out.println("Vozik nabral objekt " + this.comingFor.getType());
                    this.held.add(this.comingFor);
                    grid.findAndRemove(this.comingFor);
                    this.comingFor = null;
                    this.amount++;
                    this.active = false;
                }
                //ide do DP
                else {
                    //System.out.println("Vozik priniesol naklad do DP");
                    this.amount = 0;
                    while (this.held.size() != 0){
                        this.held.remove(0);
                    }
                    this.active = false;
                    if (this.ordered.size() == 0){
                        this.finished = true;
                    }
                }
            }

            //pohyb
            else {
                for (Tile path : this.path){
                    if (path.isOccupied() || path.hasVehicle()){
                        if (this.comingFor != null){
                            ArrayList<Tile> path2 = this.calculatePath();
                            if (path2 == null){
                                //nenasla sa, prida sa na koniec listu
                                Item item = this.ordered.remove(0);
                                this.ordered.add(item);
                                this.comingFor = null;
                                this.active = false;
                                return;
                            }
                            this.path = path2;
                        }
                        else {
                            ArrayList<Tile> path2 = this.returnToDP();
                            //System.out.println("Vozik je plny, rozhodol sa ist k DP");
                            this.path = path2;
                            if (path2 == null){
                                this.active = false;
                                return;
                            }
                        }
                    }
                }
                Tile tile = this.path.remove(0);
                int newX = tile.getX();
                int newY = tile.getY();
                this.moveTo(newX, newY);
                //System.out.format("Vozik stoji na suradnici %d %d\n", newX, newY);
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
            System.out.println("Item sa nenachadza v skladisku");
            System.exit(-1);
        }

        //System.out.format("Vozik pocita cestu pre novu objednavku %s\n", item.getType());
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
        int end_x = grid.getDeliveryX();
        int end_y = grid.getDeliveryY();
        ArrayList<Tile> path = this.pathfinder.a_Star(this.grid, start_x, start_y, end_x, end_y);
        return path;
    }





}


