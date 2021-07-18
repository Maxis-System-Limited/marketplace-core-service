package maxis.accountadmin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.accountadmin.model.ModelAccountAdmin;

public interface RepositoryAccountAdmin extends MongoRepository<ModelAccountAdmin, String> {
	
	public List<ModelAccountAdmin> findByCode(String code);

	public List<ModelAccountAdmin> findByTanentId(String code);

	public List<ModelAccountAdmin> findByCreatedById(String code);

	public ModelAccountAdmin findByAttemptId(String attemptId);
	
	public ModelAccountAdmin findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelAccountAdmin> getByTanentIdAndRoleId(String tanentId, String roleId);
}
