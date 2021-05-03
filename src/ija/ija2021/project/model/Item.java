package ija.ija2021.project.model;

import java.util.Objects;

/***
 * Class representing one item in racks
 * Author: Vanessa Jóriová
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

    public void reserveItem(){
        this.reserved = true;

    }

    public void cancelReservation(){
        this.reserved = false;

    }

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
