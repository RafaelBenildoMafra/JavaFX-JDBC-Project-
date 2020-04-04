package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		
		System.out.println("onMenuItemAboutAction");
		
	}
	
	
	//INTERFACE INITIALIZABLE
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	

}
