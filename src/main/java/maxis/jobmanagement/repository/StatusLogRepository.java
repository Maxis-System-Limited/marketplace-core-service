package maxis.jobmanagement.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import maxis.jobmanagement.model.StatusLog;

public interface StatusLogRepository extends MongoRepository<StatusLog,String>{
	List<StatusLog> getStatusLogByInstanceId(String instanceId);
}

