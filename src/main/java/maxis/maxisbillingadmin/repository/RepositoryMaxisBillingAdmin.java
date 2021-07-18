package maxis.maxisbillingadmin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.maxisbillingadmin.model.ModelMaxisBillingAdmin;

public interface RepositoryMaxisBillingAdmin extends MongoRepository<ModelMaxisBillingAdmin, String> {
	
	public List<ModelMaxisBillingAdmin> findByCode(String code);

	public List<ModelMaxisBillingAdmin> findByTanentId(String code);

	public List<ModelMaxisBillingAdmin> findByCreatedById(String code);

	public ModelMaxisBillingAdmin findByAttemptId(String attemptId);
	
	public ModelMaxisBillingAdmin findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelMaxisBillingAdmin> getByTanentIdAndRoleId(String tanentId, String roleId);
}
