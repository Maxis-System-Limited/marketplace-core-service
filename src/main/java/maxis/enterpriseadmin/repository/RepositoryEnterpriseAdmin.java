package maxis.enterpriseadmin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.enterpriseadmin.model.ModelEnterpriseAdmin;

public interface RepositoryEnterpriseAdmin extends MongoRepository<ModelEnterpriseAdmin, String> {
	
	public List<ModelEnterpriseAdmin> findByCode(String code);

	public List<ModelEnterpriseAdmin> findByTanentId(String code);

	public List<ModelEnterpriseAdmin> findByCreatedById(String code);

	public ModelEnterpriseAdmin findByAttemptId(String attemptId);
	
	public ModelEnterpriseAdmin findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelEnterpriseAdmin> getByTanentIdAndRoleId(String tanentId, String roleId);
}
