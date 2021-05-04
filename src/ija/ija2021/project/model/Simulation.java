package ija.ija2021.project.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Simulation {

    private Grid grid;
    private ArrayList<Item> order = new java.util.ArrayList<Item>();
    private ArrayList<Vehicle> vehicles = new java.util.ArrayList<>();


    public void loadGrid(){
        this.grid = new Grid();
        grid.LoadLayout("data/layout.txt");
        //grid.printMap();
        grid.loadGoods("data/goods.txt");
        //grid.getStats();


    }

    public Grid getGrid() {
        return grid;
    }

    public void simulate(){

        this.loadOrder("data/order.txt");

        this.generateCars();
        this.distributeOrder();

        Vehicle vehicle = this.vehicles.get(2);
        Vehicle vehicle2 = this.vehicles.get(1);

        while(true){
            //Thread.sleep(500);
            vehicle.nextMove();
            vehicle2.nextMove();
        }
    }


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

    public void printOrder(){
        Map<String, Long> counting = this.order.stream().collect(
                Collectors.groupingBy(Item::getType, Collectors.counting()));
        System.out.println("Nacitana objednavka: " + counting);
    }

    public void printOrders(){
        for (Vehicle car : this.vehicles){
            car.printOrder();
        }
    }

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


    public void generateCars(){
        Vehicle vehicle1 = new Vehicle(10, this.grid);
        this.vehicles.add(vehicle1);
        Vehicle vehicle2 = new Vehicle(5, this.grid);
        this.vehicles.add(vehicle2);
        Vehicle vehicle3 = new Vehicle(2, this.grid);
        this.vehicles.add(vehicle3);
        Vehicle vehicle4 = new Vehicle(1, this.grid);
        this.vehicles.add(vehicle4);
        Vehicle vehicle5 = new Vehicle(5, this.grid);
        this.vehicles.add(vehicle5);
    }


}
