package maxis.warehousestockissuer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.warehousestockissuer.model.ModelWarehouseStockIssuer;

public interface RepositoryWarehouseStockIssuer extends MongoRepository<ModelWarehouseStockIssuer, String> {
	
	public List<ModelWarehouseStockIssuer> findByCode(String code);

	public List<ModelWarehouseStockIssuer> findByTanentId(String code);

	public List<ModelWarehouseStockIssuer> findByCreatedById(String code);

	public ModelWarehouseStockIssuer findByAttemptId(String attemptId);
	
	public ModelWarehouseStockIssuer findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelWarehouseStockIssuer> getByTanentIdAndRoleId(String tanentId, String roleId);
}
