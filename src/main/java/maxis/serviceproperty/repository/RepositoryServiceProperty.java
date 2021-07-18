package maxis.serviceproperty.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import maxis.serviceproperty.model.ModelServiceProperty;


public interface RepositoryServiceProperty extends MongoRepository<ModelServiceProperty, String>{
}
