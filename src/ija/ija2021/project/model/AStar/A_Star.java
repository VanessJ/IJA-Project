package ija.ija2021.project.model.AStar;

import ija.ija2021.project.model.tiles.Tile;
import ija.ija2021.project.model.Grid;
import ija.ija2021.project.model.AStar.*;

import java.awt.*;
import java.util.ArrayList;

public class A_Star {
    /***
     * A* pathfinding algorithm
     * Authors: Vanessa Jóriová, Marián Zimmerman
     */

    Grid grid;

    /***
     *
     * @param grid internal representation of map
     * @param start_x x coordinate of starting point
     * @param start_y y coordinate of starting point
     * @param end_x x coordinate of ending point
     * @param end_y y coordinate of ending point
     * @return null if no path found, arraylist of tiles if found
     */
    public ArrayList<Tile> a_Star(Grid grid, int start_x, int start_y, int end_x, int end_y) {
        SquareGraph graph = new SquareGraph(grid.dimension, grid.dimension);
        this.grid = grid;



        for (Tile[] tile_row : grid.layout) {
            for (Tile tile : tile_row) {
                if (tile.isOccupied() || tile.hasVehicle()) {
                    int i = tile.getX();
                    int j = tile.getY();
                    Node n = new Node(i, j, "OBSTACLE");
                    graph.setMapCell(new Point(i, j), n);
                } else {
                    int i = tile.getX();
                    int j = tile.getY();
                    Node n = new Node(i, j, "NORMAL");
                    graph.setMapCell(new Point(i, j), n);
                }
            }
            graph.setStartPosition(new Point(start_x, start_y));
            graph.setTargetPosition(new Point(end_x,end_y));
        }


        ArrayList<Node> path = graph.executeAStar();

        if(path == null){
            //System.out.println("There is no path to target");
            return null;
        }
        else {

                /*System.out.println("--- Path to target ---");
                graph.printPath(path);*/
                ArrayList<Tile> tiles = this.convertToTile(path);
                if (tiles.size() >= 1){
                    tiles.remove(0);
                }
                tiles.add(this.grid.layout[end_x][end_y]);
                return tiles;


            }


    }

    /**
     *
     * @param path algorithm output that will be converted to tiles
     * @return tiles path consisting of tiles in proper format
     */

    private ArrayList<Tile> convertToTile(ArrayList<Node> path){
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Node node : path){
            int x = node.getX();
            int y = node.getY();
            Tile tile = this.grid.layout[x][y];
            tiles.add(tile);

        }
        return tiles;
    }


}



