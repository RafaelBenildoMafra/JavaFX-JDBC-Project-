package model.services;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao dao = DaoFactory.createSellerDao();
	
	//BUSCA OS DADOS DO BANCO DE DADOS PARA SEREM INSERIDOS
	public List<Seller> findAll(){
		
		return dao.findAll();//BUSCA OS DEPARTAMENTOS
		
	}
	
	public void saveOrUpdate(Seller obj) {
		
		if(obj.getId() == null) { //ID NULO SIGNIFICA QUE UM NOVO DEP. ESTA SENDO INSERIDO
			dao.insert(obj);
			
		}
		else {
			
			dao.update(obj);
			
		}
	}
	
	//REMOVE UM DEPARTAMENTO DO DATABSE
	public void remove(Seller obj) {
		
		dao.deleteById(obj.getId());
	}
}
