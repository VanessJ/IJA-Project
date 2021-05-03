package ija.ija2021.project.model;

import ija.ija2021.project.model.tiles.Shelf;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import ija.ija2021.project.model.Grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Simulation extends Application {
    private ArrayList<Item> order = new java.util.ArrayList<Item>();
    private ArrayList<Vehicle> vehicles = new java.util.ArrayList<>();
    private Grid grid;
    private final int GRID_WIDTH = 600;
    private final int GRID_HEIGHT = 460;


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.grid = new Grid();
        grid.LoadLayout("data/layout.txt");
        grid.printMap();
        grid.loadGoods("data/goods.txt");
        grid.getStats();
        this.loadOrder("data/order.txt");
        //this.printOrder();

        this.generateCars();
        this.distributeOrder();
        //this.printOrders();

        //test

        Vehicle vehicle = this.vehicles.get(2);
        Vehicle vehicle2 = this.vehicles.get(1);



        Parent root = FXMLLoader.load(getClass().getResource("iny.fxml"));

        primaryStage.setTitle("Hello World");
        Scene mainScene = new Scene(root, 800, 600);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        vehicle.printOrder();


        //TEST
        GridPane mainPane = (GridPane) mainScene.lookup("#maingrid");

        int tileNum = grid.dimension;
        double tileWidth = GRID_WIDTH / tileNum;
        double tileHeight = GRID_HEIGHT / tileNum;

        for (int i = 0; i < tileNum; i++) {
            for (int j = 0; j < tileNum; j++) {
                UITile tile = new UITile(i, j, tileWidth, tileHeight);
                // Set each 'Tile' the width and height
                tile.setPrefSize(tileWidth, tileHeight);
                // Add node on j column and i row
                mainPane.add(tile, j, i);
            }
        }

        this.placeholder_load(grid, mainPane);

        /*while(true){
            Thread.sleep(250);
            vehicle.nextMove();
            vehicle2.nextMove();
        }*/
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


    public void placeholder_load(Grid grid, GridPane gridpane){
        for (int i = 0; i < grid.dimension; i++) {
            for (int j = 0; j < grid.dimension; j++) {
                if (grid.layout[i][j].isShelf()){
                    this.setAsShelf(gridpane, i, j);
                }
                else if (grid.layout[i][j].isDP()){
                    this.setAsDP(gridpane, i, j);
                }
                else {
                    this.setAsPath(gridpane, i, j);
                }
            }

        }
    }

    private void setAsShelf(GridPane grid, int row, int col){
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                UITile tile = (UITile) node;
                tile.rectangle.setFill(Color.BLACK);
            }
        }

    }

    private void setAsPath(GridPane grid, int row, int col){
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                UITile tile = (UITile) node;
                tile.rectangle.setFill(Color.WHITE);
                tile.rectangle.setStroke(Color.BLACK);
            }
        }

    }

    private void setAsDP(GridPane grid, int row, int col){
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                UITile tile = (UITile) node;
                tile.rectangle.setFill(Color.GREEN);
            }
        }

    }

    private void changeTile(GridPane grid, int col, int row){
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                UITile tile = (UITile) node;
                tile.rectangle.setFill(Color.BLUE);
            }
        }

    }

    /***
     * Prints order
     */

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

    public static void main(String[] args) {
        launch(args);
    }


    class UITile extends Pane {
        private int positionX;
        private int positionY;
        private double width;
        private double height;
        private Rectangle rectangle;

        public UITile(int x, int y, double tileWidth, double tileHeight) {
            positionX = x;
            positionY = y;
            String color = "red";
            rectangle = new Rectangle(tileWidth,tileHeight, Color.WHITE);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeType(StrokeType.INSIDE);
            //this.setStyle("-fx-background-color: black;");
            //this.setStyle("-fx-border-color: " + color);
            setOnMouseClicked(e -> {
                System.out.println(positionX + " " + positionY);
            });
            this.getChildren().addAll(rectangle);
        }
    }

}
