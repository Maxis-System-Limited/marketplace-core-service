package maxis.enterprisebusinessmanager.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.enterprisebusinessmanager.model.ModelEnterpriseBusinessManager;

public interface RepositoryEnterpriseBusinessManager extends MongoRepository<ModelEnterpriseBusinessManager, String> {
	
	public List<ModelEnterpriseBusinessManager> findByCode(String code);

	public List<ModelEnterpriseBusinessManager> findByTanentId(String code);

	public List<ModelEnterpriseBusinessManager> findByCreatedById(String code);

	public ModelEnterpriseBusinessManager findByAttemptId(String attemptId);
	
	public ModelEnterpriseBusinessManager findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelEnterpriseBusinessManager> getByTanentIdAndRoleId(String tanentId, String roleId);
}
