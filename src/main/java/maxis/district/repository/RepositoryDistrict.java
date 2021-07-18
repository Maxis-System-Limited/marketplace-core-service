package maxis.district.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import maxis.district.model.District;

public interface RepositoryDistrict extends MongoRepository<District, String>{

}
