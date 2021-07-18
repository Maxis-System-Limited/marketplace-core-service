package maxis.thana.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import maxis.thana.model.Thana;

public interface RepositoryThana extends MongoRepository<Thana,String>{
	List<Thana> findAllBydistrictId(String districtId);
}
