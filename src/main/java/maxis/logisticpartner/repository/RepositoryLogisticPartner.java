package maxis.logisticpartner.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.logisticpartner.model.ModelLogisticPartner;

public interface RepositoryLogisticPartner extends MongoRepository<ModelLogisticPartner, String> {
	
	@Query("{'code' : ?0}")
	public List<ModelLogisticPartner> findByCode(String code);

	public List<ModelLogisticPartner> findByTanentId(String code);

	public List<ModelLogisticPartner> findByCreatedById(String code);

	public ModelLogisticPartner findByAttemptId(String attemptId);
	
	public ModelLogisticPartner findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelLogisticPartner> getByTanentIdAndRoleId(String tanentId, String roleId);
}
