package maxis.configuration.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.configuration.model.ConfigurationPNDCEnterprise;
import maxis.configuration.model.ConfigurationPriceEnterprise;
import maxis.configuration.model.ConfigurationPriceShipment;
import maxis.configuration.model.ConfigurationPriceTransporter;
import maxis.configuration.model.ConfigurationServiceAreaEnterprise;
import maxis.configuration.model.ConfigurationServiceAreaPNDC;
import maxis.configuration.model.ConfigurationServiceAreaRider;
import maxis.configuration.model.ConfigurationServiceAreaWarehouse;
import maxis.configuration.model.ConfigurationWarehouseDispatcher;
import maxis.configuration.model.ModelConfigurationGeneric;
import maxis.configuration.repository.RepositoryConfiguration;
import maxis.thana.model.Thana;

@Service
public class ServiceConfiguration {
	@Autowired
	private RepositoryConfiguration repositoryConfiguration;

	public ModelConfigurationGeneric save(ModelConfigurationGeneric modelConfigurationGeneric) {
		ModelConfigurationGeneric returnModelConfigurationGeneric = null;
		try {
			returnModelConfigurationGeneric = repositoryConfiguration.save(modelConfigurationGeneric);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return returnModelConfigurationGeneric;
	}

	public ModelConfigurationGeneric delete(ModelConfigurationGeneric modelConfigurationGeneric) {
		ModelConfigurationGeneric returnModelConfigurationGeneric = null;
		try {
			repositoryConfiguration.delete(modelConfigurationGeneric);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return returnModelConfigurationGeneric;
	}

	public ArrayList<ModelConfigurationGeneric> get(String property1, String property2, String property3) {
		ArrayList<ModelConfigurationGeneric> returnModelConfigurationGeneric = null;
		try {
			returnModelConfigurationGeneric = repositoryConfiguration.get(property1, property2, property3);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return returnModelConfigurationGeneric;
	}

	public ArrayList<ConfigurationPNDCEnterprise> getConfigurationPNDCEnterprise(String logisticPartnerUserId,
			String logisticPartnerEnterpriseUserId, String serviceType) {
		ArrayList<ModelConfigurationGeneric> list = repositoryConfiguration
				.getConfigurationPNDCEnterprise(logisticPartnerUserId, logisticPartnerEnterpriseUserId, serviceType);
		ArrayList<ConfigurationPNDCEnterprise> returnList = new ArrayList<ConfigurationPNDCEnterprise>();

		for (ModelConfigurationGeneric model : list)
			returnList.add((ConfigurationPNDCEnterprise) model);

		return returnList;
	}

	public ArrayList<ConfigurationPriceEnterprise> getConfigurationPriceEnterprise(String logisticPartnerUserId,
			String logisticPartnerEnterpriseUserId, String serviceType) {
		ArrayList<ModelConfigurationGeneric> list = repositoryConfiguration
				.getConfigurationPriceEnterprise(logisticPartnerUserId, logisticPartnerEnterpriseUserId, serviceType);
		ArrayList<ConfigurationPriceEnterprise> returnList = new ArrayList<ConfigurationPriceEnterprise>();

		for (ModelConfigurationGeneric model : list)
			returnList.add((ConfigurationPriceEnterprise) model);

		return returnList;
	}

	public ArrayList<ConfigurationServiceAreaEnterprise> getConfigurationServiceAreaEnterprise(
			String logisticPartnerUserId, String logisticPartnerEnterpriseUserId, String serviceType) {
		ArrayList<ModelConfigurationGeneric> list = repositoryConfiguration.getConfigurationServiceAreaEnterprise(
				logisticPartnerUserId, logisticPartnerEnterpriseUserId, serviceType);
		ArrayList<ConfigurationServiceAreaEnterprise> returnList = new ArrayList<ConfigurationServiceAreaEnterprise>();

		for (ModelConfigurationGeneric model : list)
			returnList.add((ConfigurationServiceAreaEnterprise) model);

		return returnList;
	}

	public ArrayList<ConfigurationServiceAreaWarehouse> getConfigurationServiceAreaWarehouse(
			String logisticPartnerUserId, String logisticPartnerWarehouseId, String serviceType) {
		ArrayList<ModelConfigurationGeneric> list = repositoryConfiguration
				.getConfigurationServiceAreaWarehouse(logisticPartnerUserId, logisticPartnerWarehouseId, serviceType);
		ArrayList<ConfigurationServiceAreaWarehouse> returnList = new ArrayList<ConfigurationServiceAreaWarehouse>();

		for (ModelConfigurationGeneric model : list)
			returnList.add((ConfigurationServiceAreaWarehouse) model);

		return returnList;
	}

	public ArrayList<ConfigurationServiceAreaRider> getConfigurationServiceAreaRider(String logisticPartnerUserId,
			String logisticPartnerRiderUserId, String serviceType) {
		ArrayList<ModelConfigurationGeneric> list = repositoryConfiguration
				.getConfigurationServiceAreaRider(logisticPartnerUserId, logisticPartnerRiderUserId, serviceType);
		ArrayList<ConfigurationServiceAreaRider> returnList = new ArrayList<ConfigurationServiceAreaRider>();

		for (ModelConfigurationGeneric model : list)
			returnList.add((ConfigurationServiceAreaRider) model);

		return returnList;
	}

	public ArrayList<ConfigurationServiceAreaPNDC> getConfigurationServiceAreaPNDC(String logisticPartnerUserId,
			String logisticPartnerPNDCUserId, String serviceType) {
		ArrayList<ModelConfigurationGeneric> list = repositoryConfiguration
				.getConfigurationServiceAreaPNDC(logisticPartnerUserId, logisticPartnerPNDCUserId, serviceType);
		ArrayList<ConfigurationServiceAreaPNDC> returnList = new ArrayList<ConfigurationServiceAreaPNDC>();

		for (ModelConfigurationGeneric model : list)
			returnList.add((ConfigurationServiceAreaPNDC) model);

		return returnList;
	}

	public ArrayList<ConfigurationWarehouseDispatcher> getConfigurationWarehouseDispatcher(String logisticPartnerUserId,
			String logisticPartnerDispatcherUserId, String serviceType) {
		ArrayList<ModelConfigurationGeneric> list = repositoryConfiguration.getConfigurationWarehouseDispatcher(
				logisticPartnerUserId, logisticPartnerDispatcherUserId, serviceType);
		ArrayList<ConfigurationWarehouseDispatcher> returnList = new ArrayList<ConfigurationWarehouseDispatcher>();

		for (ModelConfigurationGeneric model : list)
			returnList.add((ConfigurationWarehouseDispatcher) model);

		return returnList;
	}

	public ArrayList<ConfigurationPriceShipment> getConfigurationPriceShipment(String logisticPartnerUserId,
			String logisticPartnerShipmentUserId, String serviceType) {
		ArrayList<ModelConfigurationGeneric> list = repositoryConfiguration
				.getConfigurationPriceShipment(logisticPartnerUserId, logisticPartnerShipmentUserId, serviceType);
		ArrayList<ConfigurationPriceShipment> returnList = new ArrayList<ConfigurationPriceShipment>();

		for (ModelConfigurationGeneric model : list)
			returnList.add((ConfigurationPriceShipment) model);

		return returnList;
	}

	public ArrayList<ConfigurationPriceTransporter> getConfigurationPriceTransporter(String logisticPartnerUserId,
			String logisticPartnerTransporterUserId, String serviceType) {
		ArrayList<ModelConfigurationGeneric> list = repositoryConfiguration
				.getConfigurationPriceTransporter(logisticPartnerUserId, logisticPartnerTransporterUserId, serviceType);
		ArrayList<ConfigurationPriceTransporter> returnList = new ArrayList<ConfigurationPriceTransporter>();

		for (ModelConfigurationGeneric model : list)
			returnList.add((ConfigurationPriceTransporter) model);

		return returnList;
	}

	public ArrayList<Thana> get_LP_W_ThanaList(String property1) {
		ArrayList<Thana> res = new ArrayList<Thana>();
		ArrayList<ModelConfigurationGeneric> configList = null;
		try {
			configList = repositoryConfiguration.getLPConfigurations(property1, "SERVICE_TYPE_SERVICE_AREA_WAREHOUSE");

			for (ModelConfigurationGeneric currentModel : configList) {
				ArrayList<Thana> currentThanaList = (ArrayList<Thana>) currentModel.getProperty6();
				res.addAll(currentThanaList);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return res;
	}

	public ArrayList<Thana> get_W_ThanaList(String property2) {
		ArrayList<Thana> res = new ArrayList<Thana>();
		ArrayList<ModelConfigurationGeneric> configList = null;
		try {
			configList = repositoryConfiguration.getWConfigurations(property2, "SERVICE_TYPE_SERVICE_AREA_WAREHOUSE");

			for (ModelConfigurationGeneric currentModel : configList) {
				ArrayList<LinkedHashMap<String, String>> currentThanaMapList = (ArrayList<LinkedHashMap<String, String>>) currentModel
						.getProperty6();

				for (LinkedHashMap<String, String> currentThanaMap : currentThanaMapList) {
					Thana t = new Thana();
					t.setId(currentThanaMap.get("_id"));
					t.setCode(currentThanaMap.get("code"));
					t.setName(currentThanaMap.get("name"));
					t.setDistrictId(currentThanaMap.get("districtId"));
					t.setDisplayName(currentThanaMap.get("displayName"));

					if (t.getId() == null)
						t.setId(currentThanaMap.get("id"));
					res.add(t);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return res;
	}

	public String get_W_From_ThanaList(String property1, String thanaCode) {
		ArrayList<Thana> res = new ArrayList<Thana>();
		ArrayList<ModelConfigurationGeneric> configList = null;
		String warehouseCode = null;
		try {
			configList = repositoryConfiguration.getWConfigurationsByTanentAndType(property1,
					"SERVICE_TYPE_SERVICE_AREA_WAREHOUSE");

			for (ModelConfigurationGeneric currentModel : configList) {
				ArrayList<LinkedHashMap<String, String>> currentThanaMapList = (ArrayList<LinkedHashMap<String, String>>) currentModel
						.getProperty6();
				if (warehouseCode == null) {
					for (LinkedHashMap<String, String> currentThanaMap : currentThanaMapList) {
						if (thanaCode.equals(currentThanaMap.get("code"))) {
							warehouseCode = currentModel.getProperty2().toString();
						}

					}
				}

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return warehouseCode;
	}
}
