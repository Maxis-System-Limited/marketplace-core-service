package maxis.deliveryadmin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.deliveryadmin.model.ModelDeliveryAdmin;

public interface RepositoryDeliveryAdmin extends MongoRepository<ModelDeliveryAdmin, String> {
	
	public List<ModelDeliveryAdmin> findByCode(String code);

	public List<ModelDeliveryAdmin> findByTanentId(String code);

	public List<ModelDeliveryAdmin> findByCreatedById(String code);

	public ModelDeliveryAdmin findByAttemptId(String attemptId);
	
	public ModelDeliveryAdmin findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelDeliveryAdmin> getByTanentIdAndRoleId(String tanentId, String roleId);
}
