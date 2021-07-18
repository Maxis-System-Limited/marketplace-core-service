package maxis.account.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.account.model.ModelAccount;


public interface RepositoryAccount extends MongoRepository<ModelAccount, String>{
	public List<ModelAccount> findByCode(String code);
	public List<ModelAccount> findByTanentId(String code);
	public List<ModelAccount> findByCreatedById(String code);
	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<ModelAccount> getAccountByTanentIdAndRoleId(String tanentId, String roleId);
}
