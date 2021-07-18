package maxis.configuration.repository;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.configuration.model.ModelConfigurationGeneric;

public interface RepositoryConfiguration extends MongoRepository<ModelConfigurationGeneric, String> 
{
	@Query("{'property1' : ?0, 'property2' : ?1, 'property3' : ?2}")
	public ArrayList<ModelConfigurationGeneric> getConfigurationPNDCEnterprise(String logisticPartnerUserId, String logisticPartnerEnterpriseUserId, String serviceType);

	@Query("{'property1' : ?0, 'property2' : ?1, 'property3' : ?2}")
	public ArrayList<ModelConfigurationGeneric> getConfigurationPriceEnterprise(String logisticPartnerUserId, String logisticPartnerEnterpriseUserId, String serviceType);

	@Query("{'property1' : ?0, 'property2' : ?1, 'property3' : ?2}")
	public ArrayList<ModelConfigurationGeneric> getConfigurationServiceAreaEnterprise(String logisticPartnerUserId, String logisticPartnerEnterpriseUserId, String serviceType);


	@Query("{'property1' : ?0, 'property2' : ?1, 'property3' : ?2}")
	public ArrayList<ModelConfigurationGeneric> getConfigurationPriceShipment(String logisticPartnerUserId, String logisticPartnerShipmentUserId, String serviceType);

	@Query("{'property1' : ?0, 'property2' : ?1, 'property3' : ?2}")
	public ArrayList<ModelConfigurationGeneric> getConfigurationPriceTransporter(String logisticPartnerUserId, String logisticPartnerTransporterUserId, String serviceType);

	@Query("{'property1' : ?0, 'property2' : ?1, 'property3' : ?2}")
	public ArrayList<ModelConfigurationGeneric> getConfigurationServiceAreaWarehouse(String logisticPartnerUserId, String logisticPartnerWarehouseId, String serviceType);

	@Query("{'property1' : ?0, 'property2' : ?1, 'property3' : ?2}")
	public ArrayList<ModelConfigurationGeneric> getConfigurationServiceAreaRider(String logisticPartnerUserId, String logisticPartnerRiderUserId, String serviceType);

	@Query("{'property1' : ?0, 'property2' : ?1, 'property3' : ?2}")
	public ArrayList<ModelConfigurationGeneric> getConfigurationServiceAreaPNDC(String logisticPartnerUserId, String logisticPartnerPNDCUserId, String serviceType);

	@Query("{'property1' : ?0, 'property2' : ?1, 'property3' : ?2}")
	public ArrayList<ModelConfigurationGeneric> getConfigurationWarehouseDispatcher(String logisticPartnerUserId, String logisticPartnerDispatcherUserId, String serviceType);

	@Query("{'property1' : ?0, 'property2' : ?1, 'property3' : ?2}")
	public ArrayList<ModelConfigurationGeneric> get(String property1, String property2, String property3);

	@Query("{'property1' : ?0, 'property3' : ?1}")
	public ArrayList<ModelConfigurationGeneric> getLPConfigurations(String property1, String property3);

	@Query("{'property2' : ?0, 'property3' : ?1}")
	public ArrayList<ModelConfigurationGeneric> getWConfigurations(String property2, String string);
	
	@Query("{'property1' : ?0, 'property3' : ?1}")
	public ArrayList<ModelConfigurationGeneric> getWConfigurationsByTanentAndType(String property1, String string);
}
