package maxis.design.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.design.model.FilterDesign;
import maxis.design.model.ModelDesign;
import maxis.design.repository.RepositoryDesign;
@Service
public class ServiceDesign {

	@Autowired
	private RepositoryDesign repositoryDesign;

	public ModelDesign add(ModelDesign modelDesign) {
	
		modelDesign.setId(UUID.randomUUID().toString());
		//modelDesign.setCreatedDate(new Date().toString());
		//modelDesign.setModifiedDate(new Date().toString());
		try {
			repositoryDesign.save(modelDesign);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		return modelDesign;
	}

	public List<ModelDesign> getList(FilterDesign filterDesign) {
		
		 
		List<ModelDesign> modelDesign = new ArrayList<ModelDesign>();
		modelDesign = repositoryDesign.findAll();
		return modelDesign;
	}
	
	public ModelDesign getByCode(FilterDesign filterDesign) {
		ModelDesign modelDesign = new ModelDesign();
		modelDesign = repositoryDesign.findByCode(filterDesign.getCode());
		return modelDesign;
	}
	
}
