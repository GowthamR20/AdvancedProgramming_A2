package model;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertBox {
    private static boolean answer = false;

    public static boolean display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Confirmation Box");
        window.setMinWidth(500);
        Label label = new Label("Are you Sure yo are attending?");
        Button sBtn = new Button("Yes");
        Button nBtn = new Button("No");

        sBtn.setOnAction(e -> {
            answer = true;
            window.close();
        });
        nBtn.setOnAction(e -> {
            answer = false;
            window.close();
        });
        window.setOnCloseRequest(e -> answer = false);

        VBox layout = new VBox();
        layout.getChildren().addAll(label, sBtn, nBtn);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }


}
