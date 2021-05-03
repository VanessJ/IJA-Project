package ija.ija2021.project.model;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

//import java.awt.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    public TextArea objednavka;
    public TextField zadat;


    public void testPrint(){
        System.out.println(zadat.getText());
    }

    public void getString(){

    }

}
