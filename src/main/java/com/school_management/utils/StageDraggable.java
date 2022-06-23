package com.school_management.utils;

import com.school_management.main.Launcher;
import javafx.scene.layout.AnchorPane;
public class StageDraggable {
    static double xOffset;
    static double yOffset;

    public static void setStageDraggable(AnchorPane mainPane) {
        mainPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        mainPane.setOnMouseDragged(event -> {
            Launcher.stage.setX(event.getScreenX() - xOffset);
            Launcher.stage.setY(event.getScreenY() - yOffset);
            Launcher.stage.setOpacity(0.5f);
        });

        mainPane.setOnMouseReleased(event -> Launcher.stage.setOpacity(1));
    }
}
