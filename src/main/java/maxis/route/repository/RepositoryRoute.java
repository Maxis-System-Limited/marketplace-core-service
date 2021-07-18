package maxis.route.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.route.model.ModelRoute;


public interface RepositoryRoute extends MongoRepository<ModelRoute, String> {
	
	public List<ModelRoute> findByCode(String code);

	public List<ModelRoute> findByTanentId(String code);

	public List<ModelRoute> findByCreatedById(String code);

	public ModelRoute findByAttemptId(String attemptId);
	
	public ModelRoute findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelRoute> getByTanentIdAndRoleId(String tanentId, String roleId);
}
