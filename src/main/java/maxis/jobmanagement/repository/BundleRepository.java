package maxis.jobmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import maxis.jobmanagement.model.Bundle;

public interface BundleRepository extends MongoRepository<Bundle,String>{

}
