
package ija.ija2021.project;

import ija.ija2021.project.view.GUI;

public class MainClass {
    /**
     * Main class responsible for launching app
     * authors: Vanessa Jóriová, Marián Zimmerman
     *
     * @param args program arguments
     */

    public static void main(String[] args) {
        /***
         * main method launching application GUI
         */
        GUI gui = new GUI();
        gui.launchApp(args);
    }
}
