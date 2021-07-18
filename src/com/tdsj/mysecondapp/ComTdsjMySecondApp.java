
package com.tdsj.mysecondapp;

import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage; 


public class ComTdsjMySecondApp extends Application {
    
    public static void main (String[] args) {
        launch(args);
    }
    
    private FXMLController controller;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootGridPane = loader.load();
        controller = loader.getController();
        controller.createPlayground();
        
        MenuBar menuBar = createMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        
        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);
        Scene scene = new Scene(rootGridPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    private MenuBar createMenu() {
        
        Menu fileMenu = new Menu("File");
        
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(event -> controller.resetGame());
        
        MenuItem resetGame = new MenuItem("Reset Game");
        resetGame.setOnAction(event -> controller.resetGame());
        
        SeparatorMenuItem sepMenuItm = new SeparatorMenuItem();
        MenuItem exitGame = new MenuItem("Exit Game");
        exitGame.setOnAction(event -> exitGame());
        
        fileMenu.getItems().addAll(newGame, resetGame, sepMenuItm, exitGame);
        
        Menu helpMenu = new Menu("Help");
        
        MenuItem aboutGame = new MenuItem("About Connect4");
        aboutGame.setOnAction(event -> connect4());
        
        SeparatorMenuItem sepMenItm = new SeparatorMenuItem();
        MenuItem aboutMe = new MenuItem("About Me");
        aboutMe.setOnAction(event -> aboutMe());
        
        helpMenu.getItems().addAll(aboutGame, sepMenItm, aboutMe);
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        
        return menuBar;
    }

    
    private void resetGame() {
        
        
    }
    
    private void aboutMe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the Developer");
        alert.setHeaderText("Dushyant Singh");
        alert.setContentText("I love to play around with code and create games. "
                + "Connect4 is one of them. In free time"
                + "I like to spend time to do more code and developing different "
                + "kinds of Apps and Games.");
        alert.show();
    }
    
    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }
    
    private void connect4() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect Four Game");
        alert.setHeaderText("How To Play?");
        alert.setContentText("Connect Four is a two-player connection game in "
                + "which the players first choose a color and then take turns "
                + "dropping colored discs from the top into a seven-column, "
                + "six-row vertically suspended grid. The pieces fall straight "
                + "down, occupying the next available space within the column. "
                + "The objective of the game is to be the first to form a "
                + "horizontal, vertical, or diagonal line of four of one's own "
                + "discs. Connect Four is a solved game. The first player can "
                + "always win by playing the right moves." +
                "The game was first sold under the Connect Four trademark by "
                + "MILTON BRADLEY in February 1974.");
        alert.show();
    }

    
}
   