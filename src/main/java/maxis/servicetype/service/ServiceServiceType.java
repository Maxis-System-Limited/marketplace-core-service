package maxis.servicetype.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.servicetype.model.ModelServiceType;
import maxis.servicetype.repository.RepositoryServiceType;

@Service
public class ServiceServiceType {

	@Autowired
	private RepositoryServiceType repositoryServiceType;

	public ModelServiceType add(ModelServiceType modelServiceType) {
		try {
			repositoryServiceType.save(modelServiceType);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelServiceType;
	}

	public List<ModelServiceType> getList() {
		List<ModelServiceType> modelServiceType = new ArrayList<ModelServiceType>();

		modelServiceType = repositoryServiceType.findAll();
		return modelServiceType;
	}
}
