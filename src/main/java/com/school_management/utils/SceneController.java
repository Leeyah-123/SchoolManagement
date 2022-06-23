package com.school_management.utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class SceneController {
    public static void initStage(AnchorPane mainPane, String dir) {
        Parent pane = null;
        try {
            pane = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource(dir)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        mainPane.getChildren().removeAll(mainPane.lookup("#primaryPane"));
        mainPane.getChildren().add(1, pane);
        mainPane.lookup("#primaryPane").setLayoutX(259.0);
        mainPane.lookup("#primaryPane").setLayoutY(7.0);
    }
    public static void switchScene(String dir, AnchorPane mainPane) {
        Parent pane = null;
        try {
            pane = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource(dir)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        mainPane.lookup("#spinnerPane").setVisible(true);
        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(1500), new KeyValue(mainPane.lookup("#spinnerPane").opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);
        Parent finalPane = pane;
        mainPane.getChildren().get(0).setOpacity(0.2);
        fadeInTimeline.setOnFinished((ae) ->
                new Thread(() -> {
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    Timeline fadeOutTimeline = new Timeline();
                    KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(1500), new KeyValue (mainPane.lookup("#spinnerPane").opacityProperty(), 0));
                    fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                    fadeOutTimeline.setOnFinished((aeb) -> {
                        mainPane.lookup("#spinnerPane").setVisible(false);
                        mainPane.getChildren().get(0).setOpacity(1.0);
                        mainPane.getChildren().remove(0);
                        mainPane.getChildren().add(0, finalPane);
                        StageDraggable.setStageDraggable(mainPane);
                    });
                    fadeOutTimeline.play();
                }).start());
        fadeInTimeline.play();
    }

    public static void switchScene(AnchorPane mainPane) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource(Constants.MAIN_FXML_DIR)));
            switchScene(mainPane, Constants.DASHBOARD_FXML_DIR);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        mainPane.lookup("#spinnerPane").setVisible(true);
        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(1500), new KeyValue(mainPane.lookup("#spinnerPane").opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);
        mainPane.getChildren().get(0).setOpacity(0.2);
        final Parent finalParent = parent;
        fadeInTimeline.setOnFinished((ae) ->
                new Thread(() -> {
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    Timeline fadeOutTimeline = new Timeline();
                    KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(1500), new KeyValue (mainPane.lookup("#spinnerPane").opacityProperty(), 0));
                    fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                    fadeOutTimeline.setOnFinished((aeb) -> {
                        mainPane.lookup("#spinnerPane").setVisible(false);
                        Stage stage = (Stage) mainPane.getScene().getWindow();

                        stage.setScene(MyScene.getScene(finalParent));
                    });
                    fadeOutTimeline.play();
                }).start());
        fadeInTimeline.play();
    }

    public static void switchScene(AnchorPane mainPane, String dir) {
        Parent pane = null;
        try {
            pane = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource(dir)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        mainPane.lookup("#spinnerPane").setVisible(true);
        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(1500), new KeyValue(mainPane.lookup("#spinnerPane").opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);
        mainPane.getChildren().get(0).setOpacity(0.2);
        mainPane.getChildren().get(1).setOpacity(0.2);
        final Parent finalPane = pane;
        fadeInTimeline.setOnFinished((ae) ->
                new Thread(() -> {
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    Timeline fadeOutTimeline = new Timeline();
                    KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(1500), new KeyValue (mainPane.lookup("#spinnerPane").opacityProperty(), 0));
                    fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                    fadeOutTimeline.setOnFinished((aeb) -> {
                        mainPane.lookup("#spinnerPane").setVisible(false);
                        mainPane.getChildren().get(0).setOpacity(1.0);
                        mainPane.getChildren().get(1).setOpacity(1.0);
                        mainPane.getChildren().removeAll(mainPane.lookup("#primaryPane"));
                        mainPane.getChildren().add(1, finalPane);
                        mainPane.lookup("#primaryPane").setLayoutX(259.0);
                        mainPane.lookup("#primaryPane").setLayoutY(7.0);
                    });
                    fadeOutTimeline.play();
                }).start());
        fadeInTimeline.play();
    }
}
