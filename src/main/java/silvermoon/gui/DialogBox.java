package silvermoon.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/** A single chat bubble with text and a small avatar. */
public class DialogBox extends HBox {
    private final Label text = new Label();
    private final ImageView displayPicture = new ImageView();

    private DialogBox(String message, Image avatar, boolean isUser) {
        // Text bubble
        text.setText(message);
        text.setWrapText(true);
        text.setMaxWidth(360);
        text.setPadding(new Insets(8, 12, 8, 12));
        text.setStyle(
                "-fx-background-color:" + (isUser ? "#1aede3" : "#e09665") + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #111111;"
        );

        // Avatar
        displayPicture.setImage(avatar);
        displayPicture.setFitWidth(32);
        displayPicture.setFitHeight(32);
        displayPicture.setPreserveRatio(true);
        displayPicture.setSmooth(true);
        displayPicture.setClip(new Circle(16, 16, 16)); // circle crop

        setSpacing(8);
        setFillHeight(false);
        setAlignment(isUser ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
        setPadding(new Insets(2, 8, 2, 8));

        if (isUser) {
            getChildren().addAll(text, displayPicture);
        } else {
            getChildren().addAll(displayPicture, text);
        }
    }

    public static DialogBox forUser(String message, Image avatar) {
        return new DialogBox(message, avatar, true);
    }

    public static DialogBox forBot(String message, Image avatar) {
        return new DialogBox(message, avatar, false);
    }
}
