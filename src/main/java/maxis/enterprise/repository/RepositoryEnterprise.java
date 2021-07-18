package maxis.enterprise.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.enterprise.model.ModelEnterprise;

public interface RepositoryEnterprise extends MongoRepository<ModelEnterprise, String> {
	
	@Query("{'code' : ?0}")
	public List<ModelEnterprise> findByCode(String code);

	public List<ModelEnterprise> findByTanentId(String code);

	public List<ModelEnterprise> findByCreatedById(String code);

	public ModelEnterprise findByAttemptId(String attemptId);
	
	public ModelEnterprise findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelEnterprise> getByTanentIdAndRoleId(String tanentId, String roleId);
}
