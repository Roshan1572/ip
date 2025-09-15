

package silvermoon.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import silvermoon.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * JavaFX entry point with a dialog-style UI and a background image.
 * No FXML/CSS — everything is built in code.
 */
public class App extends Application {
    private VBox dialogContainer;
    private TextField input;

    private TaskList tasks;
    private Storage storage;
    private FxUi ui;

    private Image userAvatar;
    private Image botAvatar;

    @Override
    public void start(Stage stage) {
        userAvatar = loadImageOrNull("/images/user.png");
        botAvatar  = loadImageOrNull("/images/bot.png");

        // --- UI: scrollable list of dialog boxes + input bar ---
        dialogContainer = new VBox(8);
        dialogContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(dialogContainer);
        scrollPane.setFitToWidth(true);
        // Make scroll content/background transparent so the root background is visible
        scrollPane.setBackground(Background.EMPTY);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        dialogContainer.setBackground(Background.EMPTY);
        //dialogContainer.setStyle("-fx-background-color: rgba(255,255,255,0.30); -fx-background-radius: 8;");


        input = new TextField();
        Button send = new Button("Send");
        HBox bar = new HBox(8, input, send);
        bar.setPadding(new Insets(8));
        HBox.setHgrow(input, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        root.setBottom(bar);

        // --- Background image on the root ---
        Image bg = loadImageOrNull("/images/backgrounds.png");
        if (bg != null) {
            BackgroundSize cover = new BackgroundSize(
                    BackgroundSize.AUTO, BackgroundSize.AUTO,
                    false, false, true, true); // cover the window, keep aspect
            BackgroundImage bimg = new BackgroundImage(
                    bg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, cover);
            root.setBackground(new Background(bimg));
        }

        Scene scene = new Scene(root, 480, 560);
        stage.setTitle("Silvermoon");
        stage.setScene(scene);
        stage.show();

        // --- Backend wiring ---
        storage = new Storage("silvermoon.txt");
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            tasks = new TaskList();
        }
        ui = new FxUi(this::appendBotLine);

        // Greeting
        ui.showGreeting("Silvermoon");

        // Send handlers (Enter key or button)
        Runnable doSend = () -> {
            String text = input.getText();
            if (text == null || text.isBlank()) return;
            appendUserLine(text);
            input.clear();
            try {
                boolean exit = Parser.parseAndExecute(text, tasks, ui, storage);
                if (exit) {
                    input.setDisable(true);
                    send.setDisable(true);
                }
            } catch (SilvermoonException ex) {
                ui.showError(ex.getMessage());
            }
        };
        input.setOnAction(e -> doSend.run());
        send.setOnAction(e -> doSend.run());
    }

    private void appendUserLine(String text) {
        dialogContainer.getChildren().add(DialogBox.forUser(text, userAvatar));
    }

    private void appendBotLine(String text) {
        dialogContainer.getChildren().add(DialogBox.forBot(text, botAvatar));
    }

    private static Image loadImageOrNull(String path) {
        try (InputStream in = App.class.getResourceAsStream(path)) {
            return in == null ? null : new Image(in);
        } catch (Exception e) {
            return null;
        }
    }
}

