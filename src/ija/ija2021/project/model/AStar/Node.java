package ija.ija2021.project.model.AStar;

import java.awt.*;

class Node implements Comparable<Node>{
    /***
     * Class representation of node in A* grid
     * authors: Vanessa Jóriová, Marián Zimmerman
     */

    private enum State{
         OPEN, CLOSED, UNVISITED
    }

    private enum Type{
        NORMAL, OBSTACLE
    }

    private double costFromStart;
    private double costToTarget;
    private double totalCost;
    private int x;
    private int y;
    private Type type;
    private State state;
    private Node parent;

    /***
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param type type (NORMAL, OBSTACLE)
     */

    public Node(int x, int y, String type){
        this.costFromStart = 0;
        this.costToTarget = 0;
        this.totalCost = 0;
        this.parent = null;
        this.state = State.UNVISITED;
        this.type = Type.valueOf(type);
        this.x = x;
        this.y = y;
    }

    /***
     *
     * @return parent of node
     */

    public Node getParent(){
        return parent;
    }

    /***
     *
     * @param n node that will be set as parent
     */
    public void setParent(Node n){
        this.parent = n;
    }

    /***
     *
     * @return x coordinate
     */
    public int getX(){
        return x;
    }

    /**
     *
     * @return y coordinate
     */
    public int getY(){
        return y;
    }

    /***
     *
     * @return true if obstacle, false if not
     */
    public boolean isObstacle(){
        return this.type == Type.OBSTACLE;
    }

    /***
     *
     * @return true if open, false if not
     */
    public boolean isOpen(){
        return this.state == State.OPEN;
    }

    /***
     *
     * @return true if closed, false if not
     */
    public boolean isClosed(){
        return this.state == State.CLOSED;
    }

    /***
     * sets state as open
     */
    public void setOpen(){
        this.state = State.OPEN;
    }

    /***
     * sets state as closed
     */
    public void setClosed(){
        this.state = State.CLOSED;
    }

    /***
     *
     * @return x, y coordinates
     */
    public Point getPosition(){
        return new Point(x, y);
    }

    /***
     *
     * @return cost from start value
     */
    public double getCostFromStart(){
        return costFromStart;
    }

    /***
     *
     * @param cost cost from start
     */
    public void setCostFromStart(double cost){
        this.costFromStart = cost;
    }

    /***
     *
     * @return total cost value
     */
    public double getTotalCost(){
        return totalCost;
    }

    /***
     *
     * @param cost total cost value
     */
    public void setTotalCost(double cost){
        this.totalCost = cost;
    }


    @Override
    public int compareTo(Node n){
        if(this.getTotalCost() < n.getTotalCost()){
            return -1;
        }
        else if(this.getTotalCost() > n.getTotalCost()){
            return 1;
        }
        else{
            return 0;
        }
    }

    @Override
    public boolean equals(Object o){
        if(o == null)
            return false;
        else{
            Node n = (Node)o;
            Point firstPosition = this.getPosition();
            Point nextPosition = n.getPosition();
            return firstPosition.equals(nextPosition);
        }
    }

}
