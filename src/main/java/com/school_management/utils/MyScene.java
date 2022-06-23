package com.school_management.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Objects;

public class MyScene {
    public static Scene getScene(Parent node) {
        Scene scene = new Scene(node);
        scene.getStylesheets().add(Objects.requireNonNull(MyScene.class.getResource(Constants.STYLESHEET_DIR)).toExternalForm());
        return scene;
    }
}
