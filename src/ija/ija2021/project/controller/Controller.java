package ija.ija2021.project.controller;

import ija.ija2021.project.model.Grid;
import ija.ija2021.project.model.Simulation;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;


public class Controller {

    private Simulation simulation;
    private final int GRID_WIDTH = 600;
    private final int GRID_HEIGHT = 460;


    @FXML
    public TextArea objednavka;
    public TextField zadat;
    public GridPane maingrid;


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

         this.redraw(grid);
         //simulation.simulate();

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
                else if (grid.layout[i][j].isBlockage()){
                    this.setAsBlockage(this.maingrid, i, j);
                }
                else {
                    this.setAsPath(this.maingrid, i, j);
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

    /**private void changeTile(GridPane grid, int col, int row){
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                UITile tile = (UITile) node;
                tile.rectangle.setFill(Color.BLUE);
            }
        }

    }*/

    public void testPrint(){
        System.out.println(zadat.getText());
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
                System.out.println(positionX + " " + positionY);
                Grid grid = simulation.getGrid();
                grid.blockAndClear(positionX, positionY);
                redraw(grid);


            });
            this.getChildren().addAll(rectangle);
        }
    }

}

