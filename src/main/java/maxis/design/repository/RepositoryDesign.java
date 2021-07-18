package maxis.design.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import maxis.design.model.ModelDesign;

public interface RepositoryDesign extends MongoRepository<ModelDesign, String>{
    	public ModelDesign findByCode(String code);
}
