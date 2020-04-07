package gui;

import java.net.URL;
import java.util.ResourceBundle;
import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable{
	
	private Department entity;

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtname;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setDepartment(Department entity) {
		
		this.entity = entity;	
	}
	
	
	@FXML
	public void onBtSaveAction() {
		
		System.out.println("Saved!");
	}
	
	@FXML
	public void onBtCancelAction() {
		
		System.out.println("Canceled!");
	}
	
		
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	private void initializeNodes() {
		
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtname, 30);
	}
//COLOCAR OS DADOS DO TIPO DEPARTAMENTO NAS CAIXAS DE TEXTO
	public void updateFormdata() {
		
		if(entity == null) {
			
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));//CONVERTE INTEIRO PARA STRING
		txtname.setText(entity.getName());
		
		
	}
	
	

}
