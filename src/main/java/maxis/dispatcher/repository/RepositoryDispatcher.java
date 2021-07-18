package maxis.dispatcher.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.dispatcher.model.ModelDispatcher;

public interface RepositoryDispatcher extends MongoRepository<ModelDispatcher, String> {
	
	public List<ModelDispatcher> findByCode(String code);

	public List<ModelDispatcher> findByTanentId(String code);

	public List<ModelDispatcher> findByCreatedById(String code);

	public ModelDispatcher findByAttemptId(String attemptId);
	
	public ModelDispatcher findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelDispatcher> getByTanentIdAndRoleId(String tanentId, String roleId);
}
