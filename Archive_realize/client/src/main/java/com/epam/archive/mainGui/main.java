package com.epam.archive.mainGui;

import java.io.IOException;

import com.epam.archive.client.controllers.ClientEntranceController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class main extends Application {

	private static Stage stage;
	@Override
	public void start(Stage primaryStage) throws IOException{
		// TODO Auto-generated method stub
		stage = new Stage();

		stage.setScene(ClientEntranceController.getEntranceScene());
		stage.setTitle("Archive");
		stage.show();
		
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				Platform.exit();
				System.exit(0);
			}
		});
	}

	public static Stage get_stage() {
		return stage;
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}