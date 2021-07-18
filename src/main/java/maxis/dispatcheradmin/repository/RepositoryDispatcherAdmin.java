package maxis.dispatcheradmin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.dispatcheradmin.model.ModelDispatcherAdmin;

public interface RepositoryDispatcherAdmin extends MongoRepository<ModelDispatcherAdmin, String> {
	
	public List<ModelDispatcherAdmin> findByCode(String code);

	public List<ModelDispatcherAdmin> findByTanentId(String code);

	public List<ModelDispatcherAdmin> findByCreatedById(String code);

	public ModelDispatcherAdmin findByAttemptId(String attemptId);
	
	public ModelDispatcherAdmin findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelDispatcherAdmin> getByTanentIdAndRoleId(String tanentId, String roleId);
}
