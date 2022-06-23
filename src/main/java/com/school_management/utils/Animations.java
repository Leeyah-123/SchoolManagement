package com.school_management.utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Animations {
    public static void slideUp(AnchorPane parent, Node element, int endYPosition) {
        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(500), new KeyValue (element.opacityProperty(), 0), new KeyValue(element.layoutYProperty(), endYPosition));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);
        fadeInTimeline.setOnFinished((ae) ->
                new Thread(() -> {
                    Timeline fadeOutTimeline = new Timeline();
                    KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(500), new KeyValue (element.opacityProperty(), 0), new KeyValue(element.layoutYProperty(), endYPosition));
                    fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                    fadeOutTimeline.setOnFinished((aeb) -> parent.getChildren().remove(element));
                    fadeOutTimeline.play();
                }).start());
        fadeInTimeline.play();
    }
}
