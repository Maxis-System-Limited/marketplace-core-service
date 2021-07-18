package maxis.configurationmanagement.repository;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.configurationmanagement.model.ModelConfigurationManagement;


public interface RepositoryConfigurationManagement extends MongoRepository<ModelConfigurationManagement, String>{
	@Query("{'userMELRoleCode' : ?0 , 'entityMOBRoleCode' : ?1}")
	public ArrayList<ModelConfigurationManagement> getConfigurationManagementByManagerRoleEntityRole(String userMELRoleCode, String entityMOBRoleCode);
}
