package maxis.deliverypndc.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.deliverypndc.model.ModelDeliveryPNDC;

public interface RepositoryDeliveryPNDC extends MongoRepository<ModelDeliveryPNDC, String> {
	
	@Query("{'code' : ?0}")
	public List<ModelDeliveryPNDC> findByCode(String code);

	@Query("{'code' : ?0}")
	public ModelDeliveryPNDC findSingleByCode(String code);

	public List<ModelDeliveryPNDC> findByTanentId(String code);

	public List<ModelDeliveryPNDC> findByCreatedById(String code);

	public ModelDeliveryPNDC findByAttemptId(String attemptId);
	
	public ModelDeliveryPNDC findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelDeliveryPNDC> getByTanentIdAndRoleId(String tanentId, String roleId);
}
