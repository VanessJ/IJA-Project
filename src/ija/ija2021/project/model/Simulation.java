package ija.ija2021.project.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class Simulation {
    /***
     * Class responsible for loading the map, data, generating the cars and simulating movement
     * authors: Vanessa Jóriová, Marián Zimmerman
     */

    private Grid grid;
    private ArrayList<Item> order = new java.util.ArrayList<Item>();
    private ArrayList<Vehicle> vehicles = new java.util.ArrayList<>();


    /***
     * loads map internal representation from file
     */
    public void loadGrid(){
        this.grid = new Grid();
        grid.LoadLayout("data/layout.txt");
        //grid.printMap();
        grid.loadGoods("data/goods.txt");
        //grid.getStats();


    }

    /***
     *
     * @return current grid
     */

    public Grid getGrid() {
        return grid;
    }

    /***
     * starts simulation by generating cars and filling shelves with goods
     */
    public void simulate(){

        this.loadOrder("data/order.txt");

        this.generateCars();
        this.distributeOrder();


    }


    /***
     * every vehicle calculates it's next step
     */
    public void next_step(){


        for (Vehicle vehicle : this.vehicles){
            vehicle.nextMove();
        }

    }

    /***
     *
     * @param path path to file
     */
    public void loadOrder(String path){
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
                    order.add(item);
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
     * Prints all orders grouped by type
     */

    public void printOrder(){
        Map<String, Long> counting = this.order.stream().collect(
                Collectors.groupingBy(Item::getType, Collectors.counting()));
        System.out.println("Nacitana objednavka: " + counting);
    }

    /***
     * Prints all orders from vehicles
     */

    public void printOrders(){
        for (Vehicle car : this.vehicles){
            car.printOrder();
        }
    }

    /***
     * Distributes ordered items between cars
     */

    public void distributeOrder(){
        int avarage = this.order.size()/ this.vehicles.size();
        //looping through every car except the last one, which will get rest of the orders
        int n = this.vehicles.size() - 1;
        for (int i = 0; i < n; i++){
            Vehicle car = this.vehicles.get(i);
            for (int j = 0; j < avarage; j++){
                Item item = this.order.remove(0);
                car.addToOrdered(item);
            }
        }

        //last car
        Vehicle car = this.vehicles.get(this.vehicles.size() - 1);
        while(this.order.size() != 0){
            Item item = this.order.remove(0);
            car.addToOrdered(item);
        }

    }


    /***
     * Generates cars
     */
    public void generateCars(){
        Vehicle vehicle1 = new Vehicle(10, this.grid, 1);
        this.vehicles.add(vehicle1);
        Vehicle vehicle2 = new Vehicle(5, this.grid, 2);
        this.vehicles.add(vehicle2);
        Vehicle vehicle3 = new Vehicle(2, this.grid, 3);
        this.vehicles.add(vehicle3);
        Vehicle vehicle4 = new Vehicle(1, this.grid, 4);
        this.vehicles.add(vehicle4);
        Vehicle vehicle5 = new Vehicle(5, this.grid, 5);
        this.vehicles.add(vehicle5);
    }


}
