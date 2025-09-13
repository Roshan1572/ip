package silvermoon.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import silvermoon.*;

import java.io.IOException;
import java.io.InputStream;

public class App extends Application {
    private VBox dialogContainer;
    private TextField input;

    private TaskList tasks;
    private Storage storage;
    private FxUi ui;

    // Avatars (under src/main/resources/images/)
    private Image userAvatar;
    private Image botAvatar;

    @Override
    public void start(Stage stage) {
        // Load avatars (fallback if files are missing)
        userAvatar = loadImageOrNull("/images/user.png");
        botAvatar  = loadImageOrNull("/images/bot.png");

        dialogContainer = new VBox(8);
        dialogContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(dialogContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        input = new TextField();
        Button send = new Button("Send");
        HBox bar = new HBox(8, input, send);
        bar.setPadding(new Insets(8));
        HBox.setHgrow(input, javafx.scene.layout.Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #e695d9;");
        root.setCenter(scrollPane);
        root.setBottom(bar);

        Scene scene = new Scene(root, 480, 560);
        stage.setTitle("Silvermoon");
        stage.setScene(scene);
        stage.show();

        storage = new Storage("silvermoon.txt");
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            tasks = new TaskList();
        }
        ui = new FxUi(this::appendBotLine);

        // Greeting
        ui.showGreeting("Silvermoon");

        // Send handlers
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

