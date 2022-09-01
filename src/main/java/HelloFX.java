import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.util.concurrent.*;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void main_startup(String[] args) {
        System.out.println("in main later");
        CompletableFuture<String> result = new CompletableFuture<>();
        
        // ne shutdownloaj javafx kada se zadnji prozor ugasi
        Platform.setImplicitExit(false);

        Platform.startup(() -> {
            System.out.println("creating gui");
            Stage stage = new Stage();
            ListView<String> list = new ListView<>();
            for (int i = 0; i <= 100; i++) {
                list.getItems().add(Integer.toString(i));
            }
            Button submit = new Button("OK");
            submit.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());
            submit.setOnAction(evt -> {
                String selection = list.getSelectionModel().getSelectedItem();
                result.complete(selection);
                stage.close();
            });
            Scene scene = new Scene(new VBox(list, submit));
            stage.setScene(scene);
            stage.setOnCloseRequest(evt -> {
                result.completeExceptionally(new IOException("User failed to select an element"));
            });
            stage.show();
        });
        System.out.println("start waiting for result");
        try {
            System.out.println("result: " + result.get());
        } catch (ExecutionException ex) {
            // handle user failing to select an element
            System.out.println(ex.getCause().getMessage());
        } catch (InterruptedException ex) {
            // TODO: make sure not to execute more logic except for shutting down after this
        }
        //Platform.exit(); // shutdown javafx
    }


    public static void main_runlater(String[] args) {
        // https://stackoverflow.com/questions/24320014/how-to-call-launch-more-than-once-in-java/24320562#24320562
        CompletableFuture<String> result = new CompletableFuture<>();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("creating gui2");
                
                Stage stage = new Stage();
                ListView<String> list = new ListView<>();
                for (int i = 0; i <= 3; i++) {
                    list.getItems().add("later: " + Integer.toString(i));
                }
                Button submit = new Button("OK2");
                submit.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());
                submit.setOnAction(evt -> {
                    String selection = list.getSelectionModel().getSelectedItem();
                    result.complete(selection);
                    stage.close();
                });
                Scene scene = new Scene(new VBox(list, submit));
                stage.setScene(scene);
                stage.setOnCloseRequest(evt -> {
                    result.completeExceptionally(new IOException("User failed to select an element"));
                });
                stage.show();
            }
        });

        System.out.println("gui2 waiting for result");
        try {
            System.out.println("result2: " + result.get());
        } catch (ExecutionException ex) {
            // handle user failing to select an element
            System.out.println(ex.getCause().getMessage());
        } catch (InterruptedException ex) {
            // TODO: make sure not to execute more logic except for shutting down after this
        }
    }
}



