package maxis.configurationmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.configurationmanagement.model.FilterConfigurationManagement;
import maxis.configurationmanagement.model.ModelConfigurationManagement;
import maxis.configurationmanagement.repository.RepositoryConfigurationManagement;

@Service
public class ServiceConfigurationManagement {

	@Autowired
	private RepositoryConfigurationManagement repositoryConfigurationManagement;

	public ModelConfigurationManagement add(ModelConfigurationManagement modelConfigurationManagement) {

		try {
			repositoryConfigurationManagement.save(modelConfigurationManagement);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelConfigurationManagement;
	}

	public List<ModelConfigurationManagement> getList(FilterConfigurationManagement filterConfigurationManagement) {
		ArrayList<ModelConfigurationManagement> modelConfigurationManagement = new ArrayList<ModelConfigurationManagement>();

		modelConfigurationManagement = repositoryConfigurationManagement.getConfigurationManagementByManagerRoleEntityRole(filterConfigurationManagement.getConfigurationManagerUserRoleCode(),
				filterConfigurationManagement.getConfigurationManagerEntityRoleCode());
		return modelConfigurationManagement;
	}

}
