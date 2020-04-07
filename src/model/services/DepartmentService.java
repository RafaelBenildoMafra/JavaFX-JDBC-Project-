package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	//BUSCA OS DADOS DO BANCO DE DADOS PARA SEREM INSERIDOS
	public List<Department> findAll(){
		
		return dao.findAll();//BUSCA OS DEPARTAMENTOS
		
	}
	
	public void saveOrUpdate(Department obj) {
		
		if(obj.getId() == null) { //ID NULO SIGNIFICA QUE UM NOVO DEP. ESTA SENDO INSERIDO
			dao.insert(obj);
			
		}
		else {
			
			dao.update(obj);
			
		}
	}
}
