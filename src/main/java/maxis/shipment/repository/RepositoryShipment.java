package maxis.shipment.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.shipment.model.ModelShipment;

public interface RepositoryShipment extends MongoRepository<ModelShipment, String> {
	
	public List<ModelShipment> findByCode(String code);

	public List<ModelShipment> findByTanentId(String code);

	public List<ModelShipment> findByCreatedById(String code);

	public ModelShipment findByAttemptId(String attemptId);
	
	public ModelShipment findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelShipment> getByTanentIdAndRoleId(String tanentId, String roleId);
}
