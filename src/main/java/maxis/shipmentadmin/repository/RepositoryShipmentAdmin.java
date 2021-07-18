package maxis.shipmentadmin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.shipmentadmin.model.ModelShipmentAdmin;

public interface RepositoryShipmentAdmin extends MongoRepository<ModelShipmentAdmin, String> {
	
	public List<ModelShipmentAdmin> findByCode(String code);

	public List<ModelShipmentAdmin> findByTanentId(String code);

	public List<ModelShipmentAdmin> findByCreatedById(String code);

	public ModelShipmentAdmin findByAttemptId(String attemptId);
	
	public ModelShipmentAdmin findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelShipmentAdmin> getByTanentIdAndRoleId(String tanentId, String roleId);
}
