package application;
// Imports
import javafx.application.Application;
import javafx.stage.Stage;
import menu.MenuDesign;
// pl
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			MenuDesign menu = new MenuDesign();
			primaryStage = menu.getMainStage();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}










  





