package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{
	
	//ATRIBUTOS DOS ITENS DE MENU
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		
		System.out.println("onMenuItemSellerAction");
		
	}
	
	//TRATAR OS EVENTOS DO MENU
	@FXML
	public void onMenuItemDepartmentAction() {
		
		System.out.println("onMenuItemDepartmentAction");
		
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		
		loadView("/gui/About.fxml");
		
	}
	
	
	//INTERFACE INITIALIZABLE
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	private void loadView(String absoluteName) {
		
	try {	
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		VBox newVBox = loader.load();
		
		Scene mainScene = Main.getMainScene();
		VBox mainVBox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();//PEGA O PRIMEIRO ELEMENTO DA VIEW
		
		
		//INCLUI NA JANELA PRINCIPAL O MAIN MENU E OS FILHOS DAS JANELAS QUE EU ESTIVER ABRINDO(newVBOX)
		Node mainMenu = mainVBox.getChildren().get(0);//PRIMEIRO FILHO DO MAINVBOX - MAINMENU
		mainVBox.getChildren().clear();//LIMPA TODOS OS FILHOS DO MAI VBOX
		mainVBox.getChildren().add(mainMenu);//PEGA O MAINMENU
		mainVBox.getChildren().addAll(newVBox.getChildren());//PEGA OS FILHOS DO NEWVBOX
		
		
		}
		catch(IOException e) {
			
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
			
		}

	}
	

}
