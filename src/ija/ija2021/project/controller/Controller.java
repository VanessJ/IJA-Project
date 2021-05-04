package ija.ija2021.project.controller;

import ija.ija2021.project.model.Grid;
import ija.ija2021.project.model.Item;
import ija.ija2021.project.model.Simulation;
import ija.ija2021.project.model.Vehicle;
import ija.ija2021.project.model.tiles.Shelf;
import ija.ija2021.project.model.tiles.Tile;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;


public class Controller {

    private Simulation simulation;
    private final int GRID_WIDTH = 600;
    private final int GRID_HEIGHT = 460;

    private Shelf focusShelf;
    private Vehicle focusVehicle;


    @FXML
    public TextArea objednavka;
    public TextField zadat;
    public GridPane maingrid;
    public Button replay;


    public void init(){

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
         this.redraw(grid);


    }

    public void manageFocus(){
        Grid grid = simulation.getGrid();
        if (focusVehicle != null){
            String toWrite = this.focusVehicle.printStats();
            objednavka.setText(toWrite);

        }
        if (focusShelf != null){
            this.focusShelf.printStats();
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
                    setAsVehiclePath(this.maingrid, tileGrid.getX(), tileGrid.getY());
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
                tile.rectangle.setFill(Color.BLUE);
            }
        }

    }


    private void setAsBlockage(GridPane grid, int row, int col){
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                UITile tile = (UITile) node;
                tile.rectangle.setFill(Color.RED);
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

    private void setAsVehiclePath(GridPane grid, int row, int col){
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                UITile tile = (UITile) node;
                tile.rectangle.setFill(Color.YELLOW);
            }
        }

    }



    /**private void changeTile(GridPane grid, int col, int row){
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                UITile tile = (UITile) node;
                tile.rectangle.setFill(Color.BLUE);
            }
        }

    }*/

    public void testPrint(){
        if (this.focusVehicle != null){
            String input = zadat.getText();
            System.out.println(input);
            String[] split = input.split("\\s+");
            String itemType = split[0];
            int amount = Integer.parseInt(split[1]);
            for (int i = 0; i < amount; i++){
                Item item = new Item(itemType);
                this.focusVehicle.addToOrdered(item);
            }
        }
        redraw(this.simulation.getGrid());
    }

    public void nextStep(){
        this.simulation.next_step();
        redraw(this.simulation.getGrid());
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
                //System.out.println(positionX + " " + positionY);
                onClick(positionX, positionY);


            });
            this.getChildren().addAll(rectangle);
        }
    }

}

