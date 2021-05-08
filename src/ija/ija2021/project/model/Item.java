package ija.ija2021.project.model;

import java.util.Objects;

/***
 * Class representing one item in racks
 * Author: Vanessa Jóriová, Marián Zimmerman
 */

public class Item {

    private String type;
    private boolean reserved;

    /***
     *
     * @param type Type of goods
     */
    public Item(String type) {
        this.type = type;
        this.reserved = false;
    }

    /***
     * Sets item as reserved - another vehicle cannot target it
     */

    public void reserveItem(){
        this.reserved = true;

    }

    /***
     * Cancels reservation and makes item reachable by other cars
     */

    public void cancelReservation(){
        this.reserved = false;

    }

    /***
     *
     * @return true if item is reserved, false if not
     */

    public boolean isReserved(){
        if (this.reserved){
            return true;
        }
        return false;
    }

    /***
     *
     * @return Type of given item
     */
    public String getType() {
        return type;
    }

}
