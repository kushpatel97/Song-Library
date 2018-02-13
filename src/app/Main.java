package app;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.Controller;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;



public class Main extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UI2.fxml"));
			GridPane root = (GridPane) fxmlLoader.load();
			primaryStage.setResizable(false);
			primaryStage.setTitle("Song Library");
			
			Controller songController = fxmlLoader.getController();
			songController.start();
			
			Scene scene = new Scene(root);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	

	public static void main(String[] args) {
		launch(args);
	}
}
