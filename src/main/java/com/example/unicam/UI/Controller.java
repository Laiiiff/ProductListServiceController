package com.example.unicam.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;

public class Controller {
    @FXML
    private Label welcomeText;

    @FXML
    private Button button;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onMouseOver() {
        System.out.println("Mouse over");
        Effect shadow = new DropShadow ();
        button.setEffect(shadow);
    }

    @FXML
    protected void onMouseExit() {
        button.setEffect(null);
    }

}