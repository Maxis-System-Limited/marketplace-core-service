package maxis.shipmentcontroller.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.shipmentcontroller.model.ModelShipmentController;

public interface RepositoryShipmentController extends MongoRepository<ModelShipmentController, String> {
	
	public List<ModelShipmentController> findByCode(String code);

	public List<ModelShipmentController> findByTanentId(String code);

	public List<ModelShipmentController> findByCreatedById(String code);

	public ModelShipmentController findByAttemptId(String attemptId);
	
	public ModelShipmentController findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelShipmentController> getByTanentIdAndRoleId(String tanentId, String roleId);
}
