package maxis.deliveryrider.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.deliveryrider.model.ModelDeliveryRider;

public interface RepositoryDeliveryRider extends MongoRepository<ModelDeliveryRider, String> {

	@Query("{'code' : ?0}")
	public List<ModelDeliveryRider> findByCode(String userId);

	public List<ModelDeliveryRider> findByTanentId(String code);

	public List<ModelDeliveryRider> findByCreatedById(String code);

	public ModelDeliveryRider findByAttemptId(String attemptId);

	public ModelDeliveryRider findFirstByAttemptId(String attemptId);

	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelDeliveryRider> getByTanentIdAndRoleId(String tanentId, String roleId);

	@Query("{'taggedWarehouse.warehouseOrderId' : ?0}")
	public List<ModelDeliveryRider> findByTaggedWarehouseId(Long warehouseOrderId);
}
