package maxis.jobmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import maxis.jobmanagement.model.Status;

public interface StatusRepository extends MongoRepository<Status, String> {

}
