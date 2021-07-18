package maxis.accountcashier.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.accountcashier.model.ModelAccountCashier;

public interface RepositoryAccountCashier extends MongoRepository<ModelAccountCashier, String> {
	
	public List<ModelAccountCashier> findByCode(String code);

	public List<ModelAccountCashier> findByTanentId(String code);

	public List<ModelAccountCashier> findByCreatedById(String code);

	public ModelAccountCashier findByAttemptId(String attemptId);
	
	public ModelAccountCashier findFirstByAttemptId(String attemptId);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelAccountCashier> getByTanentIdAndRoleId(String tanentId, String roleId);
}
