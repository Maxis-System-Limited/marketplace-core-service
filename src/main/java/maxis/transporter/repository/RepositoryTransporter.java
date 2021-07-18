package maxis.transporter.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.transporter.model.ModelTransporter;

public interface RepositoryTransporter extends MongoRepository<ModelTransporter, String> {
	
	public List<ModelTransporter> findByCode(String code);

	public List<ModelTransporter> findByTanentId(String code);

	public List<ModelTransporter> findByCreatedById(String code);

	public ModelTransporter findByAttemptId(String attemptId);
	
	public ModelTransporter findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelTransporter> getByTanentIdAndRoleId(String tanentId, String roleId);
}
