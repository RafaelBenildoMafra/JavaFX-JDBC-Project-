package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exception.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable{
	
	private Seller entity;
	
	private SellerService service;
	
	private DepartmentService departmentService;
	
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
	private ComboBox<Department> comboBoxDepartment;
	
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
	
	private ObservableList<Department> obsList;
	
	public void setSeller(Seller entity) {
		
		this.entity = entity;	
	}
	
	public void setServices(SellerService service, DepartmentService departmentService) {
		
		this.service = service;
		this.departmentService = departmentService;
		
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
							//TRATAMENTO
		ValidationException exception = new ValidationException("Validation Error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));//PEGA O VALOR EM STRING E TENTA CONVERTER EM INTEIRO SE NAO DER SERTO RETORNA NULL
		
		if(txtname.getText() == null || txtname.getText().trim().equals("")) {//VERIFICA SE A CAIXA DE TEXTO ESTA VAZIA
			
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtname.getText());
		
		
		if (Email.getText() == null || Email.getText().trim().equals("")) {// VERIFICA SE A CAIXA DE TEXTO ESTA
																				// VAZIA

			exception.addError("Email", "Field can't be empty");
		}
		obj.setEmail(Email.getText());
		
		//TRATAMENTO DA DATA
		if(birthDate.getValue() == null) {
			
			exception.addError("birthDate", "Field can't be empty");
		}
		else {
			
		
		Instant instant = Instant.from(birthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setBirthDate(Date.from(instant));
		}
		
		
		//TRATAMENTO DO SAL�RIO
		if (baseSalary.getText() == null || baseSalary.getText().trim().equals("")) {// VERIFICA SE A CAIXA DE TEXTO ESTA															// VAZIA

			exception.addError("baseSalary", "Field can't be empty");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(baseSalary.getText()));
		
		obj.setDepartment(comboBoxDepartment.getValue());
		
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
		
		initializeComboBoxDepartment();
		
	}
	
//COLOCAR OS DADOS DO TIPO Seller NAS CAIXAS DE TEXTO(PREENCHE OS DADOS)
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
		
		if(entity.getDepartment() == null) {
			
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else {
			
			comboBoxDepartment.setValue(entity.getDepartment());
		}
		
		
	}
	
	public void loadAssocietadeObjects() {
		
		if(departmentService == null) {
			
			throw new IllegalStateException("Department Service Was Null!");
		}
			
		List<Department> list = departmentService.findAll();//CARREGA OS DEPARTAMENTOS DO BANCO DE DADOS
		
		obsList = FXCollections.observableArrayList(list); //JOGA OS DEPARTAMENTOS PARA A OBSLIST
		comboBoxDepartment.setItems(obsList);
		
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		
		//Percorre a cole��o preenchendo o label com as mensagens de erro
		
		Set<String> fields = errors.keySet();
	
		//TERCEIRA SOLU��O IF ELSE COMPRIMIDO SE NAO CONTEM O ESPA�O PREENCHIDO MOSTRA O ERRO SE NAO DEIXA VAZIO!!
		labelErrorName.setText((fields.contains("name")) ? errors.get("name"): "");
		labelErrorEmail.setText((fields.contains("Email")) ? errors.get("Email"): "");
		labelErrorBaseSalary.setText((fields.contains("baseSalary")) ? errors.get("baseSalary"): "");
		labelErrorBirthDate.setText((fields.contains("birthDate")) ? errors.get("birthDate"): "");
	
	}
	
	private void initializeComboBoxDepartment() {
		
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};

		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}
