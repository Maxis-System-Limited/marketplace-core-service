package maxis.warehousestockreceiver.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.warehousestockreceiver.model.ModelWarehouseStockReceiver;

public interface RepositoryWarehouseStockReceiver extends MongoRepository<ModelWarehouseStockReceiver, String> {
	
	public List<ModelWarehouseStockReceiver> findByCode(String code);

	public List<ModelWarehouseStockReceiver> findByTanentId(String code);

	public List<ModelWarehouseStockReceiver> findByCreatedById(String code);

	public ModelWarehouseStockReceiver findByAttemptId(String attemptId);
	
	public ModelWarehouseStockReceiver findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelWarehouseStockReceiver> getByTanentIdAndRoleId(String tanentId, String roleId);
}
