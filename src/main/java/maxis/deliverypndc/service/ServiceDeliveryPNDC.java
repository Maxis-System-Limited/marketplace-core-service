package maxis.deliverypndc.service;

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
import maxis.deliverypndc.model.FilterDeliveryPNDC;
import maxis.deliverypndc.model.ModelDeliveryPNDC;
import maxis.deliverypndc.repository.RepositoryDeliveryPNDC;
import maxis.design.model.AuthenticationModel;
import maxis.design.model.AuthenticationRole;
import maxis.design.model.FilterDesign;
import maxis.design.model.ModelDesign;
import maxis.design.service.ServiceDesign;

@Component
public class ServiceDeliveryPNDC {

	@Autowired
	private RepositoryDeliveryPNDC repositoryDeliveryPNDC;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceDeliveryPNDC(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelDeliveryPNDC add(ModelDeliveryPNDC modelDeliveryPNDC) {
		if (modelDeliveryPNDC.getId() == null)
			modelDeliveryPNDC.setId(UUID.randomUUID().toString());
		// modelDeliveryPNDC.setCreatedDate(new Date().toString());
		// modelDeliveryPNDC.setModifiedDate(new Date().toString());

		try {
			modelDeliveryPNDC = repositoryDeliveryPNDC.save(modelDeliveryPNDC);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelDeliveryPNDC;
	}

	public List<ModelDeliveryPNDC> getList(FilterDeliveryPNDC filterDeliveryPNDC) {
		List<ModelDeliveryPNDC> modelDeliveryPNDC = new ArrayList<ModelDeliveryPNDC>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterDeliveryPNDC.getCode())) {
			modelDeliveryPNDC = repositoryDeliveryPNDC.findByCode(filterDeliveryPNDC.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterDeliveryPNDC.getTanentId())) {
			modelDeliveryPNDC = repositoryDeliveryPNDC.findByTanentId(filterDeliveryPNDC.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterDeliveryPNDC.getCreatedById())) {
			modelDeliveryPNDC = repositoryDeliveryPNDC.findByCreatedById(filterDeliveryPNDC.getCreatedById());
		} else {
			modelDeliveryPNDC = repositoryDeliveryPNDC.findAll();
		}
		return modelDeliveryPNDC;
	}

	public ModelDeliveryPNDC create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelDeliveryPNDC modelDeliveryPNDC = new ModelDeliveryPNDC();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelDeliveryPNDC = repositoryDeliveryPNDC.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelDeliveryPNDC == null) {
				ModelDeliveryPNDC newModelDeliveryPNDC = new ModelDeliveryPNDC();
				newModelDeliveryPNDC = (ModelDeliveryPNDC) UtilityProperty.initializeBaseModel(newModelDeliveryPNDC,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPDDELIVERYPNDC");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelDeliveryPNDC.setId(corelationId);
				newModelDeliveryPNDC.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelDeliveryPNDC.setCreatedDate(new Date().toString());
				newModelDeliveryPNDC.setModifiedDate(new Date().toString());
				
				newModelDeliveryPNDC.setRole(modelDesign.getRole());
				newModelDeliveryPNDC.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
			

				newModelDeliveryPNDC.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelDeliveryPNDC.getRole().getRoleId());
				authenticationRole.setCode(newModelDeliveryPNDC.getRole().getCode());
				authenticationRole.setCreatedAt(newModelDeliveryPNDC.getCreatedDate());
				authenticationRole.setCreatedBy(newModelDeliveryPNDC.getCreatedById());
				authenticationRole.setDisplayName(newModelDeliveryPNDC.getRole().getDisplayName());
				authenticationRole.setLevel(newModelDeliveryPNDC.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelDeliveryPNDC.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelDeliveryPNDC.getRole().getParentId());
				authenticationRole.setStatus(newModelDeliveryPNDC.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelDeliveryPNDC.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelDeliveryPNDC.getModifiedById());

				for (Step step : newModelDeliveryPNDC.getSteps()) {
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
				authenticationModel.setTanentId(newModelDeliveryPNDC.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryDeliveryPNDC.save(newModelDeliveryPNDC);
					return newModelDeliveryPNDC;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelDeliveryPNDC update(ModelDeliveryPNDC modelDeliveryPNDC) {

		ModelDeliveryPNDC existingModelDeliveryPNDC = new ModelDeliveryPNDC();
		String password = null;

		if (modelDeliveryPNDC.getAttemptId() != null) {
			existingModelDeliveryPNDC = repositoryDeliveryPNDC
					.findFirstByAttemptId(modelDeliveryPNDC.getAttemptId());
			if (existingModelDeliveryPNDC != null) {
				try {

					for (Step step : existingModelDeliveryPNDC.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelDeliveryPNDC.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryDeliveryPNDC.save(modelDeliveryPNDC);
					return modelDeliveryPNDC;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelDeliveryPNDC> getListByPropertyCodeAndValue(FilterDeliveryPNDC filterDeliveryPNDC) {
		List<ModelDeliveryPNDC> modelDeliveryPNDCList = new ArrayList<ModelDeliveryPNDC>();
		List<ModelDeliveryPNDC> modifedModelDeliveryPNDCList = new ArrayList<ModelDeliveryPNDC>();
		boolean isRecordFound = false;
		if (filterDeliveryPNDC.getTanentId() != null) {
			modelDeliveryPNDCList = repositoryDeliveryPNDC
					.getByTanentIdAndRoleId(filterDeliveryPNDC.getTanentId(), filterDeliveryPNDC.getRoleId());
		}

		if (modelDeliveryPNDCList.size() > 0) {
			for (ModelDeliveryPNDC modelDeliveryPNDC : modelDeliveryPNDCList) {
				for (Step step : modelDeliveryPNDC.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterDeliveryPNDC.getPropertyCode())) {
								modifedModelDeliveryPNDCList.add(modelDeliveryPNDC);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelDeliveryPNDCList;
	}

}
