package ui.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.fx.HomeScreen;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) {
        ui.fx.HomeScreen.show(stage);
    }


    public static void main(String[] args) {
        launch();
    }
}
