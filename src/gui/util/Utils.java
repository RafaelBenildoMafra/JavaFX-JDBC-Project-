package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	public static Stage currentStage(ActionEvent event) {
		
		//ACESSA O STAGE ONDE O CONTROLLER QUE RECEBEU O EVENTO ESTA
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
		
	}

}
