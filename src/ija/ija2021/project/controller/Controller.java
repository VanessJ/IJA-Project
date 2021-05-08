package ija.ija2021.project.controller;

import ija.ija2021.project.model.Grid;
import ija.ija2021.project.model.Item;
import ija.ija2021.project.model.Simulation;
import ija.ija2021.project.model.Vehicle;
import ija.ija2021.project.model.tiles.Shelf;
import ija.ija2021.project.model.tiles.Tile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Controller {

    private int speedLevel;
    private int speed;
    private long simTime;
    private Simulation simulation;
    private final int GRID_WIDTH = 600;
    private final int GRID_HEIGHT = 460;
    final Timer t = new Timer();

    private Shelf focusShelf;
    private Vehicle focusVehicle;
    private Lock lock = new ReentrantLock();


    @FXML
    public TextArea objednavka;
    public TextField zadat;
    public TextField skuska;
    public GridPane maingrid;
    public Button replay;
    public Label speedSetting;
    public Button faster;
    public Button slower;




    public void init(){
         this.speedLevel = 5;
         this.setSpeed();
         this.objednavka.setEditable(false);
         this.skuska.setEditable(false);
         this.simulation = new Simulation();
         simulation.loadGrid();
         Grid grid = simulation.getGrid();
         int tileNum = grid.dimension;
         double tileWidth = GRID_WIDTH / tileNum;
         double tileHeight = GRID_HEIGHT / tileNum;

         for (int i = 0; i < tileNum; i++) {
             for (int j = 0; j < tileNum; j++) {
                 UITile tile = new UITile(i, j, tileWidth, tileHeight);
                 // Set each 'Tile' the width and height
                 tile.setPrefSize(tileWidth, tileHeight);
                 // Add node on j column and i row
                 this.maingrid.add(tile, j, i);
             }
         }

         simulation.simulate();


         t.schedule(new toSchedule(), this.speed);

    }

    public void restart(){
        this.simulation = new Simulation();
        this.simulation.loadGrid();
        this.simulation.simulate();
        this.simTime = 0;
        this.focusVehicle = null;
        this.focusShelf = null;
        objednavka.setText("");
    }

    public void lowerSpeed(){
        if (this.speedLevel > 0){
            this.speedLevel--;
            setSpeed();
        }

    }

    public void fasterSpeed(){
        if (this.speedLevel < 10){
            this.speedLevel++;
            setSpeed();
        }
    }

    public void setSpeed(){
        switch(this.speedLevel){
            case 0:
                this.speed = 5000;
                break;
            case 1:
                this.speed = 3000;
                break;
            case 2:
                this.speed = 2000;
                break;
            case 3:
                this.speed = 1000;
                break;
            case 4:
                this.speed = 800;
                break;
            case 5:
                this.speed = 600;
                break;
            case 6:
                this.speed = 400;
                break;
            case 7:
                this.speed = 200;
                break;
            case 8:
                this.speed = 100;
                break;
            case 9:
                this.speed = 50;
                break;
            case 10:
                this.speed = 20;
                break;
            default:
                this.speed = 500;
                break;
        }
        String s = String.format("    Speed: %d/10", this.speedLevel);
        speedSetting.setText(s);

    }

    public void manageFocus(){
        skuska.setText(String.format("%02d:%02d:%02d", (simTime/3600000)%24, (simTime/60000)%60, (simTime/1000)%60));
        if (focusVehicle != null){
            String toWrite = this.focusVehicle.printStats();
            objednavka.setText(toWrite);

        }
        if (focusShelf != null){
            //this.focusShelf.printStats();
            String toWrite = this.focusShelf.getStats();
            objednavka.setText(toWrite);
        }

    }

    private void onClick(int x, int y){
        Grid grid = simulation.getGrid();
        Tile tile = grid.layout[x][y];

        //setnutie blokády
        if (!tile.isOccupied() && !tile.isDP() && !tile.hasVehicle()){
            tile.setAsOccupied();
        }

        //clear focusu a shelf do focusu
        else if (tile.isShelf()){
            this.focusVehicle = null;
            this.focusShelf = grid.getShelf(x, y);
        }

        //vehicle do focusu
        else if(tile.hasVehicle()){
            this.focusShelf = null;
            this.focusVehicle = grid.getVehicle(x, y);

        }

        //vycistenie blokády
        else if (tile.isBlockage()){
            tile.clear();
        }

        redraw(grid);

    }


    public void redraw(Grid grid){
        for (int i = 0; i < grid.dimension; i++) {
            for (int j = 0; j < grid.dimension; j++) {
                if (grid.layout[i][j].isShelf()){
                    this.setAsShelf(this.maingrid, i, j);
                }
                else if (grid.layout[i][j].isDP()){
                    this.setAsDP(this.maingrid, i, j);
                }

                else if (grid.layout[i][j].hasVehicle()){
                    this.setAsVehicle(this.maingrid, i, j);
                }

                else if (!grid.layout[i][j].isOccupied()){
                    this.setAsPath(this.maingrid, i, j);
                }
                if (grid.layout[i][j].isBlockage()){
                    this.setAsBlockage(this.maingrid, i, j);
                }
            }

        }
        if(this.focusVehicle != null){
            if (this.focusVehicle.hasPath()){
                ArrayList<Tile> path = this.focusVehicle.getPath();
                for (Tile tileGrid : path){
                    if (!tileGrid.hasVehicle() && !tileGrid.isDP()){
                        setAsVehiclePath(this.maingrid, tileGrid.getX(), tileGrid.getY());
                    }

                }

            }
        }

        for (int i = 0; i < grid.dimension; i++) {
            for (int j = 0; j < grid.dimension; j++) {
                if (grid.layout[i][j].isBlockage()){
                    this.setAsBlockage(this.maingrid, i, j);
                }
            }

        }
        manageFocus();

    }

    private void setAsShelf(GridPane grid, int row, int col){
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                UITile tile = (UITile) node;
                tile.rectangle.setFill(Color.BLACK);
            }
        }

    }

    private void setAsVehicle(GridPane grid, int row, int col){
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                UITile tile = (UITile) node;
                tile.rectangle.setFill(Color.STEELBLUE);
            }
        }

    }


    private void setAsBlockage(GridPane grid, int row, int col){
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                UITile tile = (UITile) node;
                tile.rectangle.setFill(Color.INDIANRED);
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
                tile.rectangle.setFill(Color.DARKSEAGREEN);
            }
        }

    }

    private void setAsVehiclePath(GridPane grid, int row, int col){
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                UITile tile = (UITile) node;
                tile.rectangle.setFill(Color.BLANCHEDALMOND);
            }
        }

    }

    @FXML
    private void onZoom(ScrollEvent event){
        event.consume();
        double zoom = event.getDeltaY() > 0? 1.1 : 0.9;
        maingrid.setScaleX(zoom * maingrid.getScaleX());
        maingrid.setScaleY(zoom * maingrid.getScaleY());



    }

    public void testPrint(){
        if (this.focusVehicle != null){
            String input = zadat.getText();
            System.out.println(input);
            String[] split = input.split("\\s+");
            String itemType = split[0];
            int amount = Integer.parseInt(split[1]);
            for (int i = 0; i < amount; i++){
                Item item = new Item(itemType);
                if (this.focusVehicle != null){
                    this.focusVehicle.addToOrdered(item);
                }
            }
        }
        redraw(this.simulation.getGrid());
    }

    public void nextStep(){
        this.simulation.next_step();
        Platform.runLater(new Runnable() {
            @Override public void run() {
                redraw(simulation.getGrid());
            }
        });

    }


    class UITile extends Pane {
        private int positionX;
        private int positionY;
        private Rectangle rectangle;

        public UITile(int x, int y, double tileWidth, double tileHeight) {
            positionX = x;
            positionY = y;
            String color = "red";
            rectangle = new Rectangle(tileWidth,tileHeight, Color.WHITE);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeType(StrokeType.INSIDE);
            setOnMouseClicked(e -> {
                onClick(positionX, positionY);


            });
            this.getChildren().addAll(rectangle);
        }
    }


    class toSchedule extends TimerTask {
        public void run() {

            nextStep();
            simTime += 10000L;
            t.schedule(new toSchedule(), speed);


        }
    }


}

