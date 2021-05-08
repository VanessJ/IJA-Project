package ija.ija2021.project.model;

import ija.ija2021.project.model.AStar.A_Star;
import ija.ija2021.project.model.tiles.Tile;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class Vehicle {
    /***
     * Classs representation of vehicle
     * authors: Vanessa Jóriová, Marián Zimmerman
     */
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


    /***
     *
     * @param capacity Capacity of new vehicle
     * @param grid Map of the grid on which the vehicle will be moving
     * @param number Number of new vehicle
     */
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

    /***
     *
     * @param item Item that will be added to order list of new vehicle
     */

    public void addToOrdered(Item item){
        this.ordered.add(item);
    }

    /***
     * Prints currently ordered items
     */
    public void printOrder(){
        Map<String, Long> counting = this.ordered.stream().collect(
                Collectors.groupingBy(Item::getType, Collectors.counting()));
        System.out.println("Nacitana objednavka: " + counting);
    }

    /***
     * Binds car and object for which the car is coming
     * @param comingFor object for which the vehicle is currently coming
     */
    public void setComingFor(Item comingFor) {
        this.comingFor = comingFor;
    }

    /***
     *
     * @return true if vehicle has no more orders, false if not
     */
    public boolean isFinished(){
        return this.finished;
    }

    /***
     *
     * @return true if vehicle has any path calculated, false if not
     */

    public boolean hasPath(){
        if (this.path != null){
            return true;
        }
        else {
            return false;
        }
    }

    /***
     *
     * @return calculated path between vehicle and its target
     */
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

    /***
     *
     * @return string representation of all orders grouped by type
     */
    public String getOrders(){
        String toWrite = "";
        Map<String, Long> counting = this.ordered.stream().collect(
                Collectors.groupingBy(Item::getType, Collectors.counting()));
        for (String getType : counting.keySet()){
            toWrite = toWrite.concat("\t" + getType + ": " + counting.get(getType) + "\n");
        }
        return toWrite;
    }

    /***
     *
     * @return string representation of currently held items grouped by type
     */

    public String getHeld(){
        String toWrite = "";
        Map<String, Long> counting = this.held.stream().collect(
                Collectors.groupingBy(Item::getType, Collectors.counting()));
        for (String getType : counting.keySet()){
            toWrite = toWrite.concat("\t" + getType + ": " + counting.get(getType) + "\n");
        }
        return toWrite;
    }

    /***
     *
     * @return true if full, false if vehicle has capacity for more items
     */
    public boolean isFull(){
        if (this.amount >= this.capacity){
            return true;
        }
        return false;
    }

    /***
     * Vehicle decides its next action:
     *      - calculate path to ordered item
     *      - calculate path to delivery point if full
     *      - calculate path to delivery point if there are no more ordered items to grab
     *      - move (change x,y coordinates)
     *      - recalculate path if current path is obstructed
     */
    public void nextMove(){

        if((this.finished)&& (this.ordered.size()!= 0)){
            this.finished = false;
        }

        if ((this.ordered.size() == 0) && (!this.active)){
            ArrayList<Tile> path = this.returnToDP();
            if (path == null){
                return;
            }
            this.path = path;
            this.active = true;
        }

        if(isFinished()){
            Tile tile = this.grid.getTile(this.x, this.y);
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
                    this.held.add(this.comingFor);
                    grid.findAndRemove(this.comingFor);
                    this.comingFor = null;
                    this.amount++;
                    this.active = false;
                }
                //ide do DP
                else {
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
            }
        }
    }

    /***
     * Vehicle moves to different location of grid by changing its coordinates
     * @param newX new x coordinate
     * @param newY new y coordinate
     */

    public void moveTo(int newX, int newY){
        Tile tile = this.grid.getTile(this.x, this.y);
        tile.unbindVehicle(this);
        this.x = newX;
        this.y = newY;

        tile = this.grid.getTile(newX, newY);
        tile.bindToVehicle(this);
        this.positionTile = tile;
    }

    /***
     *
     * @return calculated path between vehicle and its target (item)
     */
    public ArrayList<Tile> calculatePath(){

        Item item = this.ordered.get(0);
        Tile tile = this.grid.findAndReserve(this, item);
        if (tile == null){
            System.out.println("Item sa nenachadza v skladisku");
            System.exit(-1);
        }

        int start_x = this.x;
        int start_y = this.y;
        int end_x = tile.getX();
        int end_y = tile.getY() - 1;
        ArrayList<Tile> path = this.pathfinder.a_Star(this.grid, start_x, start_y, end_x, end_y);
        if (path == null){
            //skusit z druhej strany
            end_y = tile.getY() + 1;
            path = this.pathfinder.a_Star(this.grid, start_x, start_y, end_x, end_y);
            return null;
        }
        return path;
    }

    /***
     *
     * @return calculated path to delivery point
     */

    public ArrayList<Tile> returnToDP(){
        Tile tile = this.grid.getDP();
        int start_x = this.x;
        int start_y = this.y;
        int end_x = grid.getDeliveryX();
        int end_y = grid.getDeliveryY();
        ArrayList<Tile> path = this.pathfinder.a_Star(this.grid, start_x, start_y, end_x, end_y);
        return path;
    }





}


