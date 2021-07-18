package maxis.maxissuperadmin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.maxissuperadmin.model.ModelMaxisSuperAdmin;

public interface RepositoryMaxisSuperAdmin extends MongoRepository<ModelMaxisSuperAdmin, String> {
	
	public List<ModelMaxisSuperAdmin> findByCode(String code);

	public List<ModelMaxisSuperAdmin> findByTanentId(String code);

	public List<ModelMaxisSuperAdmin> findByCreatedById(String code);

	public ModelMaxisSuperAdmin findByAttemptId(String attemptId);
	
	public ModelMaxisSuperAdmin findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelMaxisSuperAdmin> getByTanentIdAndRoleId(String tanentId, String roleId);
}
