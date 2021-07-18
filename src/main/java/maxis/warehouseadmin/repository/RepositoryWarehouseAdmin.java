package maxis.warehouseadmin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.warehouseadmin.model.ModelWarehouseAdmin;

public interface RepositoryWarehouseAdmin extends MongoRepository<ModelWarehouseAdmin, String> {
	
	public List<ModelWarehouseAdmin> findByCode(String code);

	public List<ModelWarehouseAdmin> findByTanentId(String code);

	public List<ModelWarehouseAdmin> findByCreatedById(String code);

	public ModelWarehouseAdmin findByAttemptId(String attemptId);
	
	public ModelWarehouseAdmin findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelWarehouseAdmin> getByTanentIdAndRoleId(String tanentId, String roleId);
}
