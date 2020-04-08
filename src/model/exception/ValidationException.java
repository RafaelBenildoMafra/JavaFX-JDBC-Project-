package model.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	//COLEÇÃO QUE CARREGA TODOS OS ERROS POSSIVEIS
	private Map<String, String> errors = new HashMap<>();//MAP <CHAVE,VALOR>
	
	public ValidationException(String msg) {
		super(msg);
	}

	public Map<String, String> getErrors(){
		
		return errors;
	}
	
	public void addError(String fieldName, String errorMessage) {
		
		errors.put(fieldName, errorMessage);
	}
}
