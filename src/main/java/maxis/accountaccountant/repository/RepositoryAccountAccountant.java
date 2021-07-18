package maxis.accountaccountant.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.accountaccountant.model.ModelAccountAccountant;

public interface RepositoryAccountAccountant extends MongoRepository<ModelAccountAccountant, String> {
	
	public List<ModelAccountAccountant> findByCode(String code);

	public List<ModelAccountAccountant> findByTanentId(String code);

	public List<ModelAccountAccountant> findByCreatedById(String code);

	public ModelAccountAccountant findByAttemptId(String attemptId);
	
	public ModelAccountAccountant findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelAccountAccountant> getByTanentIdAndRoleId(String tanentId, String roleId);
}
