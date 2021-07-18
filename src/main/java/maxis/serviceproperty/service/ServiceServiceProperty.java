package maxis.serviceproperty.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.serviceproperty.model.ModelServiceProperty;
import maxis.serviceproperty.repository.RepositoryServiceProperty;

@Service
public class ServiceServiceProperty {

	@Autowired
	private RepositoryServiceProperty repositoryServiceProperty;

	public ModelServiceProperty add(ModelServiceProperty modelServiceProperty) {
		try {
			repositoryServiceProperty.save(modelServiceProperty);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelServiceProperty;
	}

	public List<ModelServiceProperty> getList() {
		List<ModelServiceProperty> modelServiceProperty = new ArrayList<ModelServiceProperty>();

		modelServiceProperty = repositoryServiceProperty.findAll();
		return modelServiceProperty;
	}
}
