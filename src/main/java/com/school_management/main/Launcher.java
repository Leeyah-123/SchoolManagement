package com.school_management.main;

import com.school_management.io.UserWriter;
import com.school_management.utils.Alerts;
import com.school_management.utils.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.DecoderException;

import java.io.IOException;
import java.util.Objects;

public class Launcher extends Application {
    public static Stage stage = null;

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            if (UserWriter.getCurrentUser() != null) {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.MAIN_FXML_DIR)));
            }
            else root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.LOGIN_FXML_DIR)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }

        assert root != null;
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(Constants.STYLESHEET_DIR)).toExternalForm());

        primaryStage.setOnCloseRequest(event-> {
            event.consume();
            if (Alerts.AlertConfirmation("Exit", "Are you sure you want to exit the program?") == ButtonType.OK) {
                if (!UserWriter.getUserSelection()) {
                    UserWriter.clearCurrentUser();
                }
                primaryStage.close();
            }
        });
        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        stage = primaryStage;
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
