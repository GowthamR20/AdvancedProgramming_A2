package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import model.DataBase;
import model.GenerateData;

import java.io.IOException;
import java.io.Serializable;


public class UniLink extends Application implements Serializable {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        DataBase db = new DataBase();
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login_View.fxml"));
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        db.setConnection();
        db.creatable();
//        GenerateData gd= new GenerateData();
//        gd.generateTableData();


    }
}
