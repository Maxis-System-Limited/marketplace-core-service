package maxis.logisticpartneradmin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.logisticpartneradmin.model.ModelLogisticPartnerAdmin;

public interface RepositoryLogisticPartnerAdmin extends MongoRepository<ModelLogisticPartnerAdmin, String> {
	
	public List<ModelLogisticPartnerAdmin> findByCode(String code);

	public List<ModelLogisticPartnerAdmin> findByTanentId(String code);

	public List<ModelLogisticPartnerAdmin> findByCreatedById(String code);

	public ModelLogisticPartnerAdmin findByAttemptId(String attemptId);
	
	public ModelLogisticPartnerAdmin findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelLogisticPartnerAdmin> getByTanentIdAndRoleId(String tanentId, String roleId);
}
