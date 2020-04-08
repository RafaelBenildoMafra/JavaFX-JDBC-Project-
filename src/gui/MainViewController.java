package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.DepartmentService;
import model.services.SellerService;

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

		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {

			controller.setSellerService(new SellerService());
			controller.updateTableView();

		});

	}

	//TRATAR OS EVENTOS DO MENU
	@FXML
	public void onMenuItemDepartmentAction() {
		
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) ->{
			
			controller.setDepartmentService(new DepartmentService ());
			controller.updateTableView();
			
		});
		
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		
		loadView("/gui/About.fxml", x -> {});
		
	}
	
	
	//INTERFACE INITIALIZABLE
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		
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
		
		
			T controller = loader.getController(); //MEU GET CONTROLER RETORNA O CONTROLADOR DO TIPO QUE EU CHAMAR
			initializingAction.accept(controller); //EXECUTA A FUNÇÃO QUE EU PASSEI COMO ARGUMENTO
		
		}
		catch(IOException e) {
			
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
			
		}

	}

}
