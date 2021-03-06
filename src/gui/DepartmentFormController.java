package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.DataChangeListener;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exception.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{
	
	private Department entity;
	
	private DepartmentService service;
	
	private List<DataChangeListener> dataChangeListners = new ArrayList<>();

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
	
	public void setDepartmentService(DepartmentService service) {
		
		this.service = service;
		
	}
	
	public void subscriberDataChangeListener(DataChangeListener listener) {
		
		dataChangeListners.add(listener);
	}
	
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		
		if(entity == null) {
			
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			
			throw new IllegalStateException("Service was null");
		}
		
		try {
		
		entity = getFormData(); //PEGA OS DADOS DO FORMULARIO E INSTANCIA UM SELLER
		service.saveOrUpdate(entity); //SALVA NO BANCO DE DADOS
		notifyDataChangeListeners();
		Utils.currentStage(event).close(); //FECHA A JANELA
		
		}
		
		catch(ValidationException e) {
			
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private void notifyDataChangeListeners() { //EMITE O EVENTO
	
		for(DataChangeListener listener : dataChangeListners) {
			
			listener.onDataChanged();
		}
		
	}

	private Department getFormData() {
		Department obj = new Department();
		
		ValidationException exception = new ValidationException("Validation Error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtname.getText() == null || txtname.getText().trim().equals("")) {//VERIFICA SE A CAIXA DE TEXTO ESTA VAZIA
			
			exception.addError("name", "Field can't be open");
		}
		obj.setName(txtname.getText());
		
		if(exception.getErrors().size() > 0) {//TEM PELO MENOS UM ERRO
			
			throw exception;
			
		}
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		
		Utils.currentStage(event).close(); 
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
	
	private void setErrorMessages(Map<String, String> errors) {
		
		//Percorre a cole��o preenchendo o label com as mensagens de erro
		
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			
			labelErrorName.setText(errors.get("name"));
			
		}
		
	}

}
