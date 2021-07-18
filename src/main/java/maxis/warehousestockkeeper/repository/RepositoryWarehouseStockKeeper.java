package maxis.warehousestockkeeper.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.warehousestockkeeper.model.ModelWarehouseStockKeeper;

public interface RepositoryWarehouseStockKeeper extends MongoRepository<ModelWarehouseStockKeeper, String> {
	
	public List<ModelWarehouseStockKeeper> findByCode(String code);

	public List<ModelWarehouseStockKeeper> findByTanentId(String code);

	public List<ModelWarehouseStockKeeper> findByCreatedById(String code);

	public ModelWarehouseStockKeeper findByAttemptId(String attemptId);
	
	public ModelWarehouseStockKeeper findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelWarehouseStockKeeper> getByTanentIdAndRoleId(String tanentId, String roleId);
}
