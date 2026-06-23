package ui.fx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class UserDashboard {

    public static void show(Stage stage, User user) {

        Label title = new Label("Welcome, " + user.getName() + " (User)");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Button viewPetsBtn = new Button("View Pets");
        Button searchPetsBtn = new Button("Search Pets");
        Button adoptHistoryBtn = new Button("My Adoptions");
        Button gameBtn = new Button("Pet Interview");
        Button logoutBtn = new Button("Logout");

        viewPetsBtn.setPrefWidth(200);
        searchPetsBtn.setPrefWidth(200);
        adoptHistoryBtn.setPrefWidth(200);
        gameBtn.setPrefWidth(200);
        logoutBtn.setPrefWidth(200);

        viewPetsBtn.setOnAction(e -> ViewPetsScreen.show(stage, user));
        searchPetsBtn.setOnAction(e -> SearchPetsScreen.show(stage, user));
        adoptHistoryBtn.setOnAction(e -> MyAdoptionsScreen.show(stage, user));
        gameBtn.setOnAction(e -> GameScreen.show(stage, user));
        logoutBtn.setOnAction(e -> HomeScreen.show(stage));

        VBox layout = new VBox(15, title, viewPetsBtn, searchPetsBtn, adoptHistoryBtn, gameBtn, logoutBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 450, 350);
        stage.setScene(scene);
        stage.show();
    }
}
