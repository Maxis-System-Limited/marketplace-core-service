package maxis.enterprisestockkeeper.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.enterprisestockkeeper.model.ModelEnterpriseStockKeeper;

public interface RepositoryEnterpriseStockKeeper extends MongoRepository<ModelEnterpriseStockKeeper, String> {
	
	public List<ModelEnterpriseStockKeeper> findByCode(String code);

	public List<ModelEnterpriseStockKeeper> findByTanentId(String code);

	public List<ModelEnterpriseStockKeeper> findByCreatedById(String code);

	public ModelEnterpriseStockKeeper findByAttemptId(String attemptId);
	
	public ModelEnterpriseStockKeeper findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelEnterpriseStockKeeper> getByTanentIdAndRoleId(String tanentId, String roleId);
}
