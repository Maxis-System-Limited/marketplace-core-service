package maxis.warehouse.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.warehouse.model.ModelWarehouse;

public interface RepositoryWarehouse extends MongoRepository<ModelWarehouse, String> {
	
	public List<ModelWarehouse> findByCode(String code);

	public List<ModelWarehouse> findByTanentId(String code);

	public List<ModelWarehouse> findByCreatedById(String code);

	public ModelWarehouse findByAttemptId(String attemptId);
	
	public ModelWarehouse findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelWarehouse> getByTanentIdAndRoleId(String tanentId, String roleId);
}
