package ui.fx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeScreen {

    public static void show(Stage stage) {

        Label title = new Label("🐾 Pet Adoption App 🐾");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button registerBtn = new Button("Register");
        Button loginBtn = new Button("Login");
        Button exitBtn = new Button("Exit");

        registerBtn.setOnAction(e -> RegisterScreen.show(stage));
        loginBtn.setOnAction(e -> LoginScreen.show(stage));
        exitBtn.setOnAction(e -> stage.close());

        registerBtn.setPrefWidth(200);
        loginBtn.setPrefWidth(200);
        exitBtn.setPrefWidth(200);


        exitBtn.setOnAction(e -> stage.close());

        VBox layout = new VBox(15, title, registerBtn, loginBtn, exitBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);

        stage.setTitle("Pet Adoption App");
        stage.setScene(scene);
        stage.show();
    }
}
