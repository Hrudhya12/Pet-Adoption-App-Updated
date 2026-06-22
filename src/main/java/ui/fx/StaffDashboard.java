package ui.fx;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.User;

public class StaffDashboard {

    public static void show(Stage stage, User user) {
        Label label = new Label("Welcome, " + user.getName() + " (Staff)");
        Scene scene = new Scene(new StackPane(label), 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}
