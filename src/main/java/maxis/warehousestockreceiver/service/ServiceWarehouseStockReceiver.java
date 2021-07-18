package maxis.warehousestockreceiver.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import maxis.common.UtilityFunction;
import maxis.common.UtilityProperty;
import maxis.common.model.Context;
import maxis.common.model.OnboardInputModel;
import maxis.common.model.Property;
import maxis.common.model.Step;
import maxis.design.model.AuthenticationModel;
import maxis.design.model.AuthenticationRole;
import maxis.design.model.FilterDesign;
import maxis.design.model.ModelDesign;
import maxis.design.service.ServiceDesign;
import maxis.warehousestockreceiver.model.FilterWarehouseStockReceiver;
import maxis.warehousestockreceiver.model.ModelWarehouseStockReceiver;
import maxis.warehousestockreceiver.repository.RepositoryWarehouseStockReceiver;

@Component
public class ServiceWarehouseStockReceiver {

	@Autowired
	private RepositoryWarehouseStockReceiver repositoryWarehouseStockReceiver;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceWarehouseStockReceiver(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelWarehouseStockReceiver add(ModelWarehouseStockReceiver modelWarehouseStockReceiver) {

		modelWarehouseStockReceiver.setId(UUID.randomUUID().toString());
		// modelWarehouseStockReceiver.setCreatedDate(new Date().toString());
		// modelWarehouseStockReceiver.setModifiedDate(new Date().toString());

		try {
			repositoryWarehouseStockReceiver.save(modelWarehouseStockReceiver);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelWarehouseStockReceiver;
	}

	public List<ModelWarehouseStockReceiver> getList(FilterWarehouseStockReceiver filterWarehouseStockReceiver) {
		List<ModelWarehouseStockReceiver> modelWarehouseStockReceiver = new ArrayList<ModelWarehouseStockReceiver>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouseStockReceiver.getCode())) {
			modelWarehouseStockReceiver = repositoryWarehouseStockReceiver.findByCode(filterWarehouseStockReceiver.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouseStockReceiver.getTanentId())) {
			modelWarehouseStockReceiver = repositoryWarehouseStockReceiver.findByTanentId(filterWarehouseStockReceiver.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouseStockReceiver.getCreatedById())) {
			modelWarehouseStockReceiver = repositoryWarehouseStockReceiver.findByCreatedById(filterWarehouseStockReceiver.getCreatedById());
		} else {
			modelWarehouseStockReceiver = repositoryWarehouseStockReceiver.findAll();
		}
		return modelWarehouseStockReceiver;
	}

	public ModelWarehouseStockReceiver create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelWarehouseStockReceiver modelWarehouseStockReceiver = new ModelWarehouseStockReceiver();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelWarehouseStockReceiver = repositoryWarehouseStockReceiver.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelWarehouseStockReceiver == null) {
				ModelWarehouseStockReceiver newModelWarehouseStockReceiver = new ModelWarehouseStockReceiver();
				newModelWarehouseStockReceiver = (ModelWarehouseStockReceiver) UtilityProperty.initializeBaseModel(newModelWarehouseStockReceiver,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPWSRECEIVER");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelWarehouseStockReceiver.setId(corelationId);
				newModelWarehouseStockReceiver.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelWarehouseStockReceiver.setCreatedDate(new Date().toString());
				newModelWarehouseStockReceiver.setModifiedDate(new Date().toString());
				
				newModelWarehouseStockReceiver.setRole(modelDesign.getRole());
				newModelWarehouseStockReceiver.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelWarehouseStockReceiver.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelWarehouseStockReceiver.getRole().getRoleId());
				authenticationRole.setCode(newModelWarehouseStockReceiver.getRole().getCode());
				authenticationRole.setCreatedAt(newModelWarehouseStockReceiver.getCreatedDate());
				authenticationRole.setCreatedBy(newModelWarehouseStockReceiver.getCreatedById());
				authenticationRole.setDisplayName(newModelWarehouseStockReceiver.getRole().getDisplayName());
				authenticationRole.setLevel(newModelWarehouseStockReceiver.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelWarehouseStockReceiver.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelWarehouseStockReceiver.getRole().getParentId());
				authenticationRole.setStatus(newModelWarehouseStockReceiver.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelWarehouseStockReceiver.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelWarehouseStockReceiver.getModifiedById());

				for (Step step : newModelWarehouseStockReceiver.getSteps()) {
					for (Context context : step.getContext()) {
						for (Property property : context.getProperties()) {

							switch (property.getPropertyCode()) {
							case "PASSWORD":
								password = property.getPropertyValue();
								break;
							case "USERID":
								userId = property.getPropertyValue();
								break;
							}

						}
					}
				}
				
				final String baseUrl = "http://maxis-elog-security.nagadpay.com/api/user/" + userId +"/exists";
				URI uri = new URI(baseUrl);
				 
				ResponseEntity<Boolean> isExistInAuthServier = restTemplate.getForEntity(uri, Boolean.class);
				
				authenticationModel.setPassword(password);
				authenticationModel.setRoles(null);
				authenticationModel.setRoles(authenticationRoles);
				authenticationModel.setStatus("");
				authenticationModel.setTanentId(newModelWarehouseStockReceiver.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange("http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryWarehouseStockReceiver.save(newModelWarehouseStockReceiver);
					return newModelWarehouseStockReceiver;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelWarehouseStockReceiver update(ModelWarehouseStockReceiver modelWarehouseStockReceiver) {

		ModelWarehouseStockReceiver existingModelWarehouseStockReceiver = new ModelWarehouseStockReceiver();
		String password = null;

		if (modelWarehouseStockReceiver.getAttemptId() != null) {
			existingModelWarehouseStockReceiver = repositoryWarehouseStockReceiver
					.findFirstByAttemptId(modelWarehouseStockReceiver.getAttemptId());
			if (existingModelWarehouseStockReceiver != null) {
				try {

					for (Step step : existingModelWarehouseStockReceiver.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelWarehouseStockReceiver.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryWarehouseStockReceiver.save(modelWarehouseStockReceiver);
					return modelWarehouseStockReceiver;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelWarehouseStockReceiver> getListByPropertyCodeAndValue(FilterWarehouseStockReceiver filterWarehouseStockReceiver) {
		List<ModelWarehouseStockReceiver> modelWarehouseStockReceiverList = new ArrayList<ModelWarehouseStockReceiver>();
		List<ModelWarehouseStockReceiver> modifedModelWarehouseStockReceiverList = new ArrayList<ModelWarehouseStockReceiver>();
		boolean isRecordFound = false;
		if (filterWarehouseStockReceiver.getTanentId() != null) {
			modelWarehouseStockReceiverList = repositoryWarehouseStockReceiver
					.getByTanentIdAndRoleId(filterWarehouseStockReceiver.getTanentId(), filterWarehouseStockReceiver.getRoleId());
		}

		if (modelWarehouseStockReceiverList.size() > 0) {
			for (ModelWarehouseStockReceiver modelWarehouseStockReceiver : modelWarehouseStockReceiverList) {
				for (Step step : modelWarehouseStockReceiver.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterWarehouseStockReceiver.getPropertyCode())) {
								modifedModelWarehouseStockReceiverList.add(modelWarehouseStockReceiver);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelWarehouseStockReceiverList;
	}

}
