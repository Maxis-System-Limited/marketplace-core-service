package maxis.jobmanagement.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.jobmanagement.model.Job;

public interface JobRepository extends MongoRepository<Job, String> {
	public List<Job> findBycurrentstateId(Long id);

	public List<Job> findByTanentId(String tanentId);

	public List<Job> findByCreatedBy(String createdBy);

	public List<Job> findByAssignedRiderId(String assignedRiderId);

	public List<Job> findByCode(String code);

	public List<Job> findByCreatedById(String code);

	public Job findByAttemptId(String attemptId);

	public Job findFirstByAttemptId(String attemptId);

	@Query("{'tanentId' : ?0 , 'role.roleId' : ?1}")
	public List<Job> getByTanentIdAndRoleId(String tanentId, String roleId);
	
	@Query("{'tanentId' : ?0 , 'currentstateId' : ?1}")
	public List<Job> getByTanentIdAndCurrentStateid(String tanentId, Long currentstateId);

}
