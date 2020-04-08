package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exception.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable{
	
	private Seller entity;
	
	private SellerService service;
	
	private List<DataChangeListener> dataChangeListners = new ArrayList<>();

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtname;
	
	@FXML
	private DatePicker birthDate;
	
	@FXML
	private TextField baseSalary;
	
	@FXML
	private TextField Email;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorBaseSalary;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBirthDate;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setSeller(Seller entity) {
		
		this.entity = entity;	
	}
	
	public void setSellerService(SellerService service) {
		
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
		
		entity = getFormData(); //PEGA OS DADOS DO FORMULARIO E INSTANCIA UM DEPARTAMENTO
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

	private Seller getFormData() {
		Seller obj = new Seller();
		
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
		Constraints.setTextFieldMaxLength(txtname, 70);
		Constraints.setTextFieldDouble(baseSalary);
		Constraints.setTextFieldMaxLength(Email, 60);
		Utils.formatDatePicker(birthDate, "dd/MM/yyyy");
		
	}
	
//COLOCAR OS DADOS DO TIPO Seller NAS CAIXAS DE TEXTO
	public void updateFormdata() {
		
		if(entity == null) {
			
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));//CONVERTE INTEIRO PARA STRING
		txtname.setText(entity.getName());
		Email.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		baseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if(entity.getBirthDate() != null) {
		birthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(),ZoneId.systemDefault()) );
		}
		
		
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		
		//Percorre a coleção preenchendo o label com as mensagens de erro
		
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			
			labelErrorName.setText(errors.get("name"));
			
		}
		
	}

}
