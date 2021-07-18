package maxis.maxissupportadmin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.maxissupportadmin.model.ModelMaxisSupportAdmin;

public interface RepositoryMaxisSupportAdmin extends MongoRepository<ModelMaxisSupportAdmin, String> {
	
	public List<ModelMaxisSupportAdmin> findByCode(String code);

	public List<ModelMaxisSupportAdmin> findByTanentId(String code);

	public List<ModelMaxisSupportAdmin> findByCreatedById(String code);

	public ModelMaxisSupportAdmin findByAttemptId(String attemptId);
	
	public ModelMaxisSupportAdmin findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelMaxisSupportAdmin> getByTanentIdAndRoleId(String tanentId, String roleId);
}
