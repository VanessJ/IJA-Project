package ija.ija2021.project.model.AStar;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class SquareGraph {

    private Node[][] map;
    private Point startPosition;
    private Point targetPosition;
    private Heap<Node> openNodes;
    private Set<Node> closedNodes;

    /***
     *
     * @param width width of graph
     * @param length length of graph
     */
    public SquareGraph(int width, int length){
        map = new Node[width][length];
        startPosition = new Point();
        targetPosition = new Point();
        openNodes = new Heap<Node>();
        closedNodes = new HashSet<Node>();
    }


    /***
     *
     * @return start position
     */
    public Point getStartPosition(){
        return startPosition;
    }


    /***
     *
     * @param coord starting coordinates
     */
    public void setStartPosition(Point coord){
        startPosition.setLocation(coord);
    }

    /***
     *
     * @param coord coordinates of wanted map cell
     * @return map cell with given coordinates
     */
    public Node getMapCell(Point coord){
        return map[(int)coord.getX()][(int)coord.getY()];
    }


    /**
     *
     * @param coord coordinates of wanted map cell
     * @param n node
     */
    public void setMapCell(Point coord, Node n){
        map[(int)coord.getX()][(int)coord.getY()] = n;
    }


    /***
     *
     * @return target position
     */
    public Point getTargetPosition(){
        return targetPosition;
    }

    /***
     *
     * @param coord new coordinates of target
     */
    public void setTargetPosition(Point coord){
        targetPosition.setLocation(coord);
    }

    /***
     *
     * @return map dimension
     */
    public int getDimension(){
        return map.length;
    }

    /***
     *
     * @param p point
     * @return true if point is inside map, false if not
     */
    public boolean isInsideMap(Point p){
        return ( (p.getX() >= 0) && (p.getX() < getDimension())  && (p.getY() >= 0) && (p.getY() < getDimension()) );
    }

    /***
     *
     * @return path to target if successful, null if not
     */
    public ArrayList<Node> executeAStar(){
        Node start = getMapCell(getStartPosition());
        Node target = getMapCell(getTargetPosition());
        addToOpenNodes(start);

        start.setCostFromStart(0);
        start.setTotalCost( start.getCostFromStart() + calculateDistance(start.getPosition(), target.getPosition()) );
        while(!openNodes.isEmpty()){
            Node current = popBestOpenNode();
            if(current.equals(target)){
                return reconstructPath(target);
            }

            addToClosedNodes(current);
            Set<Node> neighbours = getNeighbours(current);
            for(Node neighbour : neighbours){
                if(!neighbour.isClosed()){
                    double tentativeCost = current.getCostFromStart() + calculateDistance(current.getPosition(), neighbour.getPosition());

                    if( (!neighbour.isOpen()) || (tentativeCost < neighbour.getCostFromStart()) ){
                        neighbour.setParent(current);
                        neighbour.setCostFromStart(tentativeCost);
                        neighbour.setTotalCost(neighbour.getCostFromStart() + calculateDistance(neighbour.getPosition(), start.getPosition()));
                        if(!neighbour.isOpen())
                            addToOpenNodes(neighbour);
                    }
                }

            }
        }

        return null;
    }


    /**
     *
     * @param n node
     * @return set of neighbours of given set
     */
    public Set<Node> getNeighbours(Node n){
        Set<Node> neighbours = new HashSet<Node>();
        for(int i=-1; i<=1; i++){
            for(int j=-1; j<=1; j++){
                if( !(i==0 && j==0) )
                    if(isInsideMap(new Point(n.getX() + i,n.getY() + j))){
                        Node temp = getMapCell(new Point(n.getX() + i,n.getY() +  j));
                        if(!temp.isObstacle())
                            neighbours.add(temp);
                    }

            }
        }
        return neighbours;
    }

    /***
     *
     * @param n node that will be added
     */
    public void addToOpenNodes(Node n){
        n.setOpen();
        openNodes.add(n);
    }

    /***
     *
     * @return best open node
     */
    public Node popBestOpenNode(){
        return openNodes.remove();
    }

    /***
     *
     * @param n node to add
     */
    public void addToClosedNodes(Node n){
        n.setClosed();
        closedNodes.add(n);
    }

    /***
     *
     * @param from starting point
     * @param to end point
     * @return distance
     */

    static double calculateDistance(Point from, Point to){
        return Math.pow(Math.pow(from.getX()-to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2) , 0.5);
    }

    /***
     *
     * @param target target node
     * @return final path
     */
    public ArrayList<Node> reconstructPath(Node target){
        ArrayList<Node> path = new ArrayList<Node>();
        Node current = target;

        while(current.getParent() != null){
            path.add(current.getParent());
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }



}
