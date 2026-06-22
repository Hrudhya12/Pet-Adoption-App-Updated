package ui.fx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import dao.UserDAO;
import model.User;

public class LoginScreen {

    public static void show(Stage stage) {

        Label title = new Label("Login");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginBtn = new Button("Login");
        Button backBtn = new Button("Back");

        loginBtn.setPrefWidth(200);
        backBtn.setPrefWidth(200);

        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            User user = UserDAO.loginUser(email, password);

            if (user == null) {
                errorLabel.setText("Invalid email or password.");
                return;
            }

            errorLabel.setText("");

            if (user.isStaff()) {
                StaffDashboard.show(stage, user);
            } else {
                UserDashboard.show(stage, user);
            }
        });

        backBtn.setOnAction(e -> HomeScreen.show(stage));

        VBox layout = new VBox(12, title, emailField, passwordField, loginBtn, backBtn, errorLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}
