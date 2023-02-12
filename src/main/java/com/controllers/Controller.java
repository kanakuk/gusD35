package com.controllers;

import com.window.ErrorWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class Controller {


    /**
     * Method to open up a new window with an error message
     *
     * @param e the exception that is passed in from a catch statement.
     */
    protected void openErrorWindow(Exception e) {
        ErrorWindow errorWindow = new ErrorWindow(e);
        errorWindow.showWindowAndWait();
    }


    /**
     * ADD A openNotifyWindow HERE.
     */
    protected void openNotifyWindow(String notifyMessage) {
        ErrorWindow notifyWindow = new ErrorWindow(notifyMessage);
        notifyWindow.showWindowAndWait();
    }

    /**
     * Method used to close the active window
     *
     * @param actionEvent is used to gather the information needed to close the window.
     */
    @FXML
    protected void closeWindow(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
        actionEvent.consume();
    }
}