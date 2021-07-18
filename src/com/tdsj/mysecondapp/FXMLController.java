
package com.tdsj.mysecondapp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;


public class FXMLController implements Initializable {

    private static final int Columns = 7;
    private static final int Rows = 6;
    private static final int Circle_Dia = 80;       
    private static final String discColor1 = "24303E";
    private static final String discColor2 = "4CAA88";
    
    private static String Player_one = "Player One";
    private static String Player_two = "Player Two";
    
    private boolean isPlayerOneTurn = true;
    
    private Disc[][] insertedDiscsArray = new Disc[Rows][Columns];
    
    @FXML
    public Button setNameButton;
    
    @FXML
    public TextField playerOneTextField, playerTwoTextField;
    
    @FXML
    public GridPane rootGridPane;
    
    @FXML
    public Pane insertedDiscPane;
    
    @FXML
    public Label playerNameLabel;
    
    private boolean isAllowedToInsert = true;
    
    public void createPlayground() {
        setNameButton.setOnAction(event -> {
            
            Player_one = playerOneTextField.getText();
            Player_two = playerTwoTextField.getText();
            playerNameLabel.setText(Player_one);
            resetGame();
        }); 
        
        Shape rectangleWithHoles = createGameStructuralGrid();
        rootGridPane.add(rectangleWithHoles, 0, 1);
        
        List<Rectangle> rectList = createClickableColumns();
        for(Rectangle rect:rectList) {
            rootGridPane.add(rect, 0, 1);
        }
        
        
    
    }
        
        
    
    
    private Shape createGameStructuralGrid() {
        Shape rectangleWithHoles = new Rectangle((Columns + 1) * Circle_Dia, (Rows + 1) * Circle_Dia);
        
        
        for(int row = 0; row < Rows; row++) {
            for(int col = 0; col < Columns; col++) { 
                Circle circle = new Circle();
                circle.setRadius(Circle_Dia / 2);
                circle.setCenterX(Circle_Dia / 2);
                circle.setCenterY(Circle_Dia / 2);
                circle.setSmooth(true);
                
                circle.setTranslateX(col * (Circle_Dia + 5) + Circle_Dia / 4);
                circle.setTranslateY(row * (Circle_Dia + 5) + Circle_Dia / 4); 
        
                rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
            }
        } 
                
        rectangleWithHoles.setFill(Color.WHITE);
        return rectangleWithHoles;
    }
    
    private List<Rectangle> createClickableColumns() {
        List<Rectangle> rectList = new ArrayList<>();
        
        for(int col = 0; col < Columns; col++) {
            Rectangle rect = new Rectangle(Circle_Dia, (Rows + 1) * Circle_Dia);
            rect.setFill(Color.TRANSPARENT);
            rect.setTranslateX(col * (Circle_Dia + 5) + Circle_Dia / 4);
            
            rect.setOnMouseEntered(event -> rect.setFill(Color.valueOf("#eeeeee26")));
            rect.setOnMouseExited(event -> rect.setFill(Color.TRANSPARENT));
            
            final int column = col;
            rect.setOnMouseClicked(event -> {
                if(isAllowedToInsert) {
                    isAllowedToInsert = false;
                    insertDisc(new Disc(isPlayerOneTurn), column);
                }
                
            });
            
            rectList.add(rect);
        }
        
        return rectList;
    }
    
    private void insertDisc(Disc disc, int column) {
        
        int row = Rows - 1;
        while (row >= 0) {
            if(getDiscPresent(row, column) == null)
                break;
            
            row--;
        }
        
        if(row < 0)
            return;
        
        
        insertedDiscsArray[row][column] = disc;
        insertedDiscPane.getChildren().add(disc);
        
        disc.setTranslateX(column * (Circle_Dia + 5) + Circle_Dia / 4);
        int currentRow = row;
        TranslateTransition transTrans = new TranslateTransition(Duration.seconds(0.5), disc);
        transTrans.setToY(row * (Circle_Dia + 5) + Circle_Dia / 4);
        
        transTrans.setOnFinished(event -> {
            isAllowedToInsert = true;
            if(gameEnded(currentRow, column)) {
                gameOver();
                return;
            }
            isPlayerOneTurn = !isPlayerOneTurn;
            
            playerNameLabel.setText(isPlayerOneTurn? Player_one : Player_two);
        });
        transTrans.play();
    } 
    
    private boolean gameEnded(int row, int column) {
        
        List<Point2D> verticalPoints = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(r, column))
                .collect(Collectors.toList());
        
        List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(c -> new Point2D(row, c))
                .collect(Collectors.toList());
        
        Point2D startPoint1 = new Point2D(row - 3, column + 3);
        List<Point2D> diagonal1Points = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startPoint1.add(i, -i))
                .collect(Collectors.toList());
        
        Point2D startPoint2 = new Point2D(row - 3, column - 3);
        List<Point2D> diagonal2Points = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startPoint2.add(i, i))
                .collect(Collectors.toList());
        
        boolean isEnded = checkComb(verticalPoints) || checkComb(horizontalPoints) 
                || checkComb(diagonal1Points) || checkComb(diagonal2Points);
        
        return isEnded;
        
    }
    
    private boolean checkComb(List<Point2D> points) {
       
        int chain = 0;
        for(Point2D point: points) {
           
           int rowIndexForArray = (int) point.getX();
           int colIndexForArray = (int) point.getY();
           
           Disc disc = getDiscPresent(rowIndexForArray, colIndexForArray);
           
           if(disc != null && disc.isPlayerOneMove == isPlayerOneTurn) {
               
               chain++;
               if(chain == 4) {
                   return true;
               }
           } else {
               chain = 0;
           }
       } 
      return false;
    }
    
    private Disc getDiscPresent(int row, int column) {
        if(row >= Rows || row < 0 || column >= Columns || column < 0) 
            return null;
        
        return insertedDiscsArray[row][column];
    }
    
    private void gameOver() {
        String winner = isPlayerOneTurn ? Player_one : Player_two;
        System.out.println("Winner is:" + winner);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Coonect Four");
        alert.setHeaderText("The Winner is: " + winner);
        alert.setContentText("Do you want to play again?");
        
        ButtonType yesBtn = new ButtonType("Yes");
        ButtonType noBtn = new ButtonType("No, Exit");
        alert.getButtonTypes().setAll(yesBtn, noBtn);
        
        Platform.runLater(() -> {
            Optional<ButtonType> btnClicked = alert.showAndWait();
            if(btnClicked.isPresent() && btnClicked.get() == yesBtn) {
                 resetGame();
            } else {
                 Platform.exit();
                 System.exit(0);
            }
        });
        
        
    }
    
    public void resetGame() {
        insertedDiscPane.getChildren().clear();
        
        for(int row = 0; row < insertedDiscsArray.length; row++) {
            for(int col = 0; col < insertedDiscsArray[row].length; col++) {
                insertedDiscsArray[row][col] = null;
            }
        }
        
        isPlayerOneTurn = true;
        
        
        playerNameLabel.setText(Player_one);
        
        
        createPlayground();
    }

    
    
    private static class Disc extends Circle {
        private final boolean isPlayerOneMove;
        
        public Disc(boolean isPlayerOneMove) {
            this.isPlayerOneMove = isPlayerOneMove;
            setRadius(Circle_Dia / 2);
            setFill(isPlayerOneMove? Color.valueOf(discColor1): Color.valueOf(discColor2));
            setCenterX(Circle_Dia / 2);
            setCenterY(Circle_Dia / 2);
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle rb) {
        
    } 

    
    
}
