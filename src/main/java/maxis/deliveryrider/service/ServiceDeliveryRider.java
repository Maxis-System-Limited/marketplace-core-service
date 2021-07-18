package maxis.deliveryrider.service;

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
import maxis.deliveryrider.model.FilterDeliveryRider;
import maxis.deliveryrider.model.ModelDeliveryRider;
import maxis.deliveryrider.repository.RepositoryDeliveryRider;
import maxis.design.model.AuthenticationModel;
import maxis.design.model.AuthenticationRole;
import maxis.design.model.FilterDesign;
import maxis.design.model.ModelDesign;
import maxis.design.service.ServiceDesign;

@Component
public class ServiceDeliveryRider {

	@Autowired
	private RepositoryDeliveryRider repositoryDeliveryRider;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceDeliveryRider(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelDeliveryRider add(ModelDeliveryRider modelDeliveryRider) {

		if (modelDeliveryRider.getId() == null)
			modelDeliveryRider.setId(UUID.randomUUID().toString());
		// modelDeliveryRider.setCreatedDate(new Date().toString());
		// modelDeliveryRider.setModifiedDate(new Date().toString());

		try {
			modelDeliveryRider = repositoryDeliveryRider.save(modelDeliveryRider);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelDeliveryRider;
	}

	public List<ModelDeliveryRider> getList(FilterDeliveryRider filterDeliveryRider) {
		List<ModelDeliveryRider> modelDeliveryRider = new ArrayList<ModelDeliveryRider>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterDeliveryRider.getCode())) {
			modelDeliveryRider = repositoryDeliveryRider.findByCode(filterDeliveryRider.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterDeliveryRider.getTanentId())) {
			modelDeliveryRider = repositoryDeliveryRider.findByTanentId(filterDeliveryRider.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterDeliveryRider.getCreatedById())) {
			modelDeliveryRider = repositoryDeliveryRider.findByCreatedById(filterDeliveryRider.getCreatedById());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterDeliveryRider.getPropertyValue())) {
			Long warehouseOrderId = Long.parseLong(filterDeliveryRider.getPropertyValue());
			modelDeliveryRider = repositoryDeliveryRider.findByTaggedWarehouseId(warehouseOrderId);
		} else {
			modelDeliveryRider = repositoryDeliveryRider.findAll();
		}
		return modelDeliveryRider;
	}

	public ModelDeliveryRider create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelDeliveryRider modelDeliveryRider = new ModelDeliveryRider();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		if (onboardInputModel.getAttemptId() != null) {
			modelDeliveryRider = repositoryDeliveryRider.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelDeliveryRider == null) {
				ModelDeliveryRider newModelDeliveryRider = new ModelDeliveryRider();
				newModelDeliveryRider = (ModelDeliveryRider) UtilityProperty.initializeBaseModel(newModelDeliveryRider,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPDDELIVERYRIDER");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelDeliveryRider.setId(corelationId);
				newModelDeliveryRider.setAttemptId(onboardInputModel.getAttemptId());

				newModelDeliveryRider.setCreatedDate(new Date().toString());
				newModelDeliveryRider.setModifiedDate(new Date().toString());

				newModelDeliveryRider.setRole(modelDesign.getRole());
				newModelDeliveryRider.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());

				newModelDeliveryRider.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelDeliveryRider.getRole().getRoleId());
				authenticationRole.setCode(newModelDeliveryRider.getRole().getCode());
				authenticationRole.setCreatedAt(newModelDeliveryRider.getCreatedDate());
				authenticationRole.setCreatedBy(newModelDeliveryRider.getCreatedById());
				authenticationRole.setDisplayName(newModelDeliveryRider.getRole().getDisplayName());
				authenticationRole.setLevel(newModelDeliveryRider.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelDeliveryRider.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelDeliveryRider.getRole().getParentId());
				authenticationRole.setStatus(newModelDeliveryRider.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelDeliveryRider.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelDeliveryRider.getModifiedById());

				for (Step step : newModelDeliveryRider.getSteps()) {
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

				final String baseUrl = "http://maxis-elog-security.nagadpay.com/api/user/" + userId + "/exists";
				URI uri = new URI(baseUrl);

				ResponseEntity<Boolean> isExistInAuthServier = restTemplate.getForEntity(uri, Boolean.class);

				authenticationModel.setPassword(password);
				authenticationModel.setRoles(null);
				authenticationModel.setRoles(authenticationRoles);
				authenticationModel.setStatus("");
				authenticationModel.setTanentId(newModelDeliveryRider.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if (!isExistInAuthServier.getBody()) {
						restTemplate.exchange("http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST,
								entity, String.class);
					}
					repositoryDeliveryRider.save(newModelDeliveryRider);
					return newModelDeliveryRider;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelDeliveryRider update(ModelDeliveryRider modelDeliveryRider) {

		ModelDeliveryRider existingModelDeliveryRider = new ModelDeliveryRider();
		String password = null;

		if (modelDeliveryRider.getAttemptId() != null) {
			existingModelDeliveryRider = repositoryDeliveryRider
					.findFirstByAttemptId(modelDeliveryRider.getAttemptId());
			if (existingModelDeliveryRider != null) {
				try {

					for (Step step : existingModelDeliveryRider.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelDeliveryRider.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryDeliveryRider.save(modelDeliveryRider);
					return modelDeliveryRider;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelDeliveryRider> getListByPropertyCodeAndValue(FilterDeliveryRider filterDeliveryRider) {
		List<ModelDeliveryRider> modelDeliveryRiderList = new ArrayList<ModelDeliveryRider>();
		List<ModelDeliveryRider> modifedModelDeliveryRiderList = new ArrayList<ModelDeliveryRider>();
		boolean isRecordFound = false;
		if (filterDeliveryRider.getTanentId() != null) {
			modelDeliveryRiderList = repositoryDeliveryRider.getByTanentIdAndRoleId(filterDeliveryRider.getTanentId(),
					filterDeliveryRider.getRoleId());
		}

		if (modelDeliveryRiderList.size() > 0) {
			for (ModelDeliveryRider modelDeliveryRider : modelDeliveryRiderList) {
				for (Step step : modelDeliveryRider.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterDeliveryRider.getPropertyCode())) {
								modifedModelDeliveryRiderList.add(modelDeliveryRider);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelDeliveryRiderList;
	}

}
