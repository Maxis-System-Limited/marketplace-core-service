package maxis.configuration.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import maxis.configuration.model.ConfigurationPNDCEnterprise;
import maxis.configuration.model.ConfigurationPriceEnterprise;
import maxis.configuration.model.ConfigurationPriceShipment;
import maxis.configuration.model.ConfigurationPriceTransporter;
import maxis.configuration.model.ConfigurationServiceAreaEnterprise;
import maxis.configuration.model.ConfigurationServiceAreaPNDC;
import maxis.configuration.model.ConfigurationServiceAreaRider;
import maxis.configuration.model.ConfigurationServiceAreaWarehouse;
import maxis.configuration.model.ConfigurationWarehouseDispatcher;
import maxis.configuration.model.FilterConfiguration;
import maxis.configuration.model.ModelConfigurationGeneric;
import maxis.configuration.model.ModelConfigurationGenericList;
import maxis.configuration.service.ServiceConfiguration;
import maxis.thana.model.Thana;

@RestController
@RequestMapping("/configuration")
public class ControllerConfiguration {
	@Autowired
	private ServiceConfiguration serviceConfiguration;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public HttpEntity<ModelConfigurationGeneric> save(@RequestBody ModelConfigurationGeneric modelConfigurationGeneric) {
		try {
			ModelConfigurationGeneric res = serviceConfiguration.save(modelConfigurationGeneric);
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/save-list", method = RequestMethod.POST)
	public HttpEntity<ArrayList<ModelConfigurationGeneric>> saveList1(@RequestBody ModelConfigurationGenericList modelConfigurationGenericList) {
		ArrayList<ModelConfigurationGeneric> newModelConfigurationGenericList = new ArrayList<ModelConfigurationGeneric>();
		try {
			for (int i = 0; i < modelConfigurationGenericList.getList().size(); i++) {
				ModelConfigurationGeneric modelConfigurationGeneric = modelConfigurationGenericList.getList().get(i);
				ModelConfigurationGeneric newModelConfigurationGeneric = serviceConfiguration.save(modelConfigurationGeneric);
				newModelConfigurationGenericList.add(newModelConfigurationGeneric);
			}
			return new ResponseEntity<>(newModelConfigurationGenericList, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/saveList", method = RequestMethod.POST)
	public HttpEntity<ArrayList<ModelConfigurationGeneric>> saveList2(@RequestBody ModelConfigurationGenericList modelConfigurationGenericList) {
		ArrayList<ModelConfigurationGeneric> newModelConfigurationGenericList = new ArrayList<ModelConfigurationGeneric>();
		try {
			for (int i = 0; i < modelConfigurationGenericList.getList().size(); i++) {
				ModelConfigurationGeneric modelConfigurationGeneric = modelConfigurationGenericList.getList().get(i);
				ModelConfigurationGeneric newModelConfigurationGeneric = serviceConfiguration.save(modelConfigurationGeneric);
				newModelConfigurationGenericList.add(newModelConfigurationGeneric);
			}
			return new ResponseEntity<>(newModelConfigurationGenericList, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public HttpEntity<ModelConfigurationGeneric> delete(@RequestBody ModelConfigurationGeneric modelConfigurationGeneric) {
		try {
			ModelConfigurationGeneric res = serviceConfiguration.delete(modelConfigurationGeneric);
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public HttpEntity<List<ModelConfigurationGeneric>> get(@RequestBody FilterConfiguration filterConfiguration) {
		try {
			ArrayList<ModelConfigurationGeneric> res = serviceConfiguration.get(filterConfiguration.getProperty1(), filterConfiguration.getProperty2(), filterConfiguration.getProperty3());
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/get-lp-w-thana-list", method = RequestMethod.POST)
	public HttpEntity<List<Thana>> get_LP_W_Thana_List(@RequestBody FilterConfiguration filterConfiguration) {
		try {
			ArrayList<Thana> res = serviceConfiguration.get_LP_W_ThanaList(filterConfiguration.getProperty1());
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/get-w-thana-list", method = RequestMethod.POST)
	public HttpEntity<List<Thana>> get_W_Thana_List(@RequestBody FilterConfiguration filterConfiguration) {
		try {
			ArrayList<Thana> res = serviceConfiguration.get_W_ThanaList(filterConfiguration.getProperty2());
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/get-lp-e-pndc", method = RequestMethod.GET)
	public HttpEntity<List<ConfigurationPNDCEnterprise>> get_lp_e_pndc(@RequestBody FilterConfiguration filterConfiguration) {
		try {
			ArrayList<ConfigurationPNDCEnterprise> res = serviceConfiguration.getConfigurationPNDCEnterprise(filterConfiguration.getLogisticPartnerId(), filterConfiguration.getEnterpriseId(), FilterConfiguration.SERVICE_TYPE_PNDC_ENTERPRISE);
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/get-lp-e-p", method = RequestMethod.POST)
	public HttpEntity<List<ConfigurationPriceEnterprise>> get_lp_e_p(@RequestBody FilterConfiguration filterConfiguration) {
		try {
			ArrayList<ConfigurationPriceEnterprise> res = serviceConfiguration.getConfigurationPriceEnterprise(filterConfiguration.getLogisticPartnerId(), filterConfiguration.getEnterpriseId(), FilterConfiguration.SERVICE_TYPE_PRICE_ENTERPRISE);
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/get-lp-e-sa", method = RequestMethod.POST)
	public HttpEntity<List<ConfigurationServiceAreaEnterprise>> get_lp_e_sa(@RequestBody FilterConfiguration filterConfiguration) {
		try {
			ArrayList<ConfigurationServiceAreaEnterprise> res = serviceConfiguration.getConfigurationServiceAreaEnterprise(filterConfiguration.getLogisticPartnerId(), filterConfiguration.getEnterpriseId(),
					FilterConfiguration.SERVICE_TYPE_SERVICE_AREA_ENTERPRISE);
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/get-d-w", method = RequestMethod.POST)
	public HttpEntity<List<ConfigurationWarehouseDispatcher>> get_d_w(@RequestBody FilterConfiguration filterConfiguration) {
		try {
			ArrayList<ConfigurationWarehouseDispatcher> res = serviceConfiguration.getConfigurationWarehouseDispatcher(filterConfiguration.getLogisticPartnerId(), filterConfiguration.getDispatcherId(),
					FilterConfiguration.SERVICE_TYPE_WAREHOUSE_DISPATCHER);
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/get-lp-w-sa", method = RequestMethod.POST)
	public HttpEntity<List<ConfigurationServiceAreaWarehouse>> get_lp_w_sa(@RequestBody FilterConfiguration filterConfiguration) {
		try {
			ArrayList<ConfigurationServiceAreaWarehouse> res = serviceConfiguration.getConfigurationServiceAreaWarehouse(filterConfiguration.getLogisticPartnerId(), filterConfiguration.getWarehouseId(),
					FilterConfiguration.SERVICE_TYPE_SERVICE_AREA_WAREHOUSE);
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/get-lp-r-sa", method = RequestMethod.POST)
	public HttpEntity<List<ConfigurationServiceAreaRider>> get_lp_r_sa(@RequestBody FilterConfiguration filterConfiguration) {
		try {
			ArrayList<ConfigurationServiceAreaRider> res = serviceConfiguration.getConfigurationServiceAreaRider(filterConfiguration.getLogisticPartnerId(), filterConfiguration.getRiderId(), FilterConfiguration.SERVICE_TYPE_SERVICE_AREA_RIDER);
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/get-lp-pndc-sa", method = RequestMethod.POST)
	public HttpEntity<List<ConfigurationServiceAreaPNDC>> get_lp_pndc_sa(@RequestBody FilterConfiguration filterConfiguration) {
		try {
			ArrayList<ConfigurationServiceAreaPNDC> res = serviceConfiguration.getConfigurationServiceAreaPNDC(filterConfiguration.getLogisticPartnerId(), filterConfiguration.getEnterpriseId(), FilterConfiguration.SERVICE_TYPE_SERVICE_AREA_PNDC);
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/get-lp-r-p", method = RequestMethod.POST)
	public HttpEntity<List<ConfigurationPriceShipment>> get_lp_r_p(@RequestBody FilterConfiguration filterConfiguration) {
		try {
			ArrayList<ConfigurationPriceShipment> res = serviceConfiguration.getConfigurationPriceShipment(filterConfiguration.getLogisticPartnerId(), filterConfiguration.getShipmentId(), FilterConfiguration.SERVICE_TYPE_PRICE_SHIPMENT);
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/get-lp-t-p", method = RequestMethod.POST)
	public HttpEntity<List<ConfigurationPriceTransporter>> get_lp_t_p(@RequestBody FilterConfiguration filterConfiguration) {
		try {
			ArrayList<ConfigurationPriceTransporter> res = serviceConfiguration.getConfigurationPriceTransporter(filterConfiguration.getLogisticPartnerId(), filterConfiguration.getTransporterId(), FilterConfiguration.SERVICE_TYPE_PRICE_TRANSPORTER);
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
