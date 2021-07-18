package maxis.servicetype.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import maxis.servicetype.model.ModelServiceType;


public interface RepositoryServiceType extends MongoRepository<ModelServiceType, String>{
}
