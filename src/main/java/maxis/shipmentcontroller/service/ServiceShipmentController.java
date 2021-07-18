package maxis.shipmentcontroller.service;

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
import maxis.shipmentcontroller.model.FilterShipmentController;
import maxis.shipmentcontroller.model.ModelShipmentController;
import maxis.shipmentcontroller.repository.RepositoryShipmentController;

@Component
public class ServiceShipmentController {

	@Autowired
	private RepositoryShipmentController repositoryShipmentController;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceShipmentController(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelShipmentController add(ModelShipmentController modelShipmentController) {

		modelShipmentController.setId(UUID.randomUUID().toString());
		// modelShipmentController.setCreatedDate(new Date().toString());
		// modelShipmentController.setModifiedDate(new Date().toString());

		try {
			repositoryShipmentController.save(modelShipmentController);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelShipmentController;
	}

	public List<ModelShipmentController> getList(FilterShipmentController filterShipmentController) {
		List<ModelShipmentController> modelShipmentController = new ArrayList<ModelShipmentController>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterShipmentController.getCode())) {
			modelShipmentController = repositoryShipmentController.findByCode(filterShipmentController.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterShipmentController.getTanentId())) {
			modelShipmentController = repositoryShipmentController.findByTanentId(filterShipmentController.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterShipmentController.getCreatedById())) {
			modelShipmentController = repositoryShipmentController.findByCreatedById(filterShipmentController.getCreatedById());
		} else {
			modelShipmentController = repositoryShipmentController.findAll();
		}
		return modelShipmentController;
	}

	public ModelShipmentController create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelShipmentController modelShipmentController = new ModelShipmentController();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelShipmentController = repositoryShipmentController.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelShipmentController == null) {
				ModelShipmentController newModelShipmentController = new ModelShipmentController();
				newModelShipmentController = (ModelShipmentController) UtilityProperty.initializeBaseModel(newModelShipmentController,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPSHIPMENTC");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelShipmentController.setId(corelationId);
				newModelShipmentController.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelShipmentController.setCreatedDate(new Date().toString());
				newModelShipmentController.setModifiedDate(new Date().toString());
				
				newModelShipmentController.setRole(modelDesign.getRole());
				newModelShipmentController.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				
				newModelShipmentController.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelShipmentController.getRole().getRoleId());
				authenticationRole.setCode(newModelShipmentController.getRole().getCode());
				authenticationRole.setCreatedAt(newModelShipmentController.getCreatedDate());
				authenticationRole.setCreatedBy(newModelShipmentController.getCreatedById());
				authenticationRole.setDisplayName(newModelShipmentController.getRole().getDisplayName());
				authenticationRole.setLevel(newModelShipmentController.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelShipmentController.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelShipmentController.getRole().getParentId());
				authenticationRole.setStatus(newModelShipmentController.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelShipmentController.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelShipmentController.getModifiedById());

				for (Step step : newModelShipmentController.getSteps()) {
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
				authenticationModel.setTanentId(newModelShipmentController.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryShipmentController.save(newModelShipmentController);
					return newModelShipmentController;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelShipmentController update(ModelShipmentController modelShipmentController) {

		ModelShipmentController existingModelShipmentController = new ModelShipmentController();
		String password = null;

		if (modelShipmentController.getAttemptId() != null) {
			existingModelShipmentController = repositoryShipmentController
					.findFirstByAttemptId(modelShipmentController.getAttemptId());
			if (existingModelShipmentController != null) {
				try {

					for (Step step : existingModelShipmentController.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelShipmentController.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryShipmentController.save(modelShipmentController);
					return modelShipmentController;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelShipmentController> getListByPropertyCodeAndValue(FilterShipmentController filterShipmentController) {
		List<ModelShipmentController> modelShipmentControllerList = new ArrayList<ModelShipmentController>();
		List<ModelShipmentController> modifedModelShipmentControllerList = new ArrayList<ModelShipmentController>();
		boolean isRecordFound = false;
		if (filterShipmentController.getTanentId() != null) {
			modelShipmentControllerList = repositoryShipmentController
					.getByTanentIdAndRoleId(filterShipmentController.getTanentId(), filterShipmentController.getRoleId());
		}

		if (modelShipmentControllerList.size() > 0) {
			for (ModelShipmentController modelShipmentController : modelShipmentControllerList) {
				for (Step step : modelShipmentController.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterShipmentController.getPropertyCode())) {
								modifedModelShipmentControllerList.add(modelShipmentController);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelShipmentControllerList;
	}

}
