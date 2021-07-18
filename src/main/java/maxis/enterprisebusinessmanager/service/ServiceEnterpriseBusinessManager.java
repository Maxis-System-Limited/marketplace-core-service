package maxis.enterprisebusinessmanager.service;

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
import maxis.enterprisebusinessmanager.model.FilterEnterpriseBusinessManager;
import maxis.enterprisebusinessmanager.model.ModelEnterpriseBusinessManager;
import maxis.enterprisebusinessmanager.repository.RepositoryEnterpriseBusinessManager;

@Component
public class ServiceEnterpriseBusinessManager {

	@Autowired
	private RepositoryEnterpriseBusinessManager repositoryEnterpriseBusinessManager;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceEnterpriseBusinessManager(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelEnterpriseBusinessManager add(ModelEnterpriseBusinessManager modelEnterpriseBusinessManager) {

		modelEnterpriseBusinessManager.setId(UUID.randomUUID().toString());
		// modelEnterpriseBusinessManager.setCreatedDate(new Date().toString());
		// modelEnterpriseBusinessManager.setModifiedDate(new Date().toString());

		try {
			repositoryEnterpriseBusinessManager.save(modelEnterpriseBusinessManager);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelEnterpriseBusinessManager;
	}

	public List<ModelEnterpriseBusinessManager> getList(FilterEnterpriseBusinessManager filterEnterpriseBusinessManager) {
		List<ModelEnterpriseBusinessManager> modelEnterpriseBusinessManager = new ArrayList<ModelEnterpriseBusinessManager>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterEnterpriseBusinessManager.getCode())) {
			modelEnterpriseBusinessManager = repositoryEnterpriseBusinessManager.findByCode(filterEnterpriseBusinessManager.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterEnterpriseBusinessManager.getTanentId())) {
			modelEnterpriseBusinessManager = repositoryEnterpriseBusinessManager.findByTanentId(filterEnterpriseBusinessManager.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterEnterpriseBusinessManager.getCreatedById())) {
			modelEnterpriseBusinessManager = repositoryEnterpriseBusinessManager.findByCreatedById(filterEnterpriseBusinessManager.getCreatedById());
		} else {
			modelEnterpriseBusinessManager = repositoryEnterpriseBusinessManager.findAll();
		}
		return modelEnterpriseBusinessManager;
	}

	public ModelEnterpriseBusinessManager create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelEnterpriseBusinessManager modelEnterpriseBusinessManager = new ModelEnterpriseBusinessManager();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelEnterpriseBusinessManager = repositoryEnterpriseBusinessManager.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelEnterpriseBusinessManager == null) {
				ModelEnterpriseBusinessManager newModelEnterpriseBusinessManager = new ModelEnterpriseBusinessManager();
				newModelEnterpriseBusinessManager = (ModelEnterpriseBusinessManager) UtilityProperty.initializeBaseModel(newModelEnterpriseBusinessManager,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPENTERPRISEBM");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelEnterpriseBusinessManager.setId(corelationId);
				newModelEnterpriseBusinessManager.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelEnterpriseBusinessManager.setCreatedDate(new Date().toString());
				newModelEnterpriseBusinessManager.setModifiedDate(new Date().toString());
				
				newModelEnterpriseBusinessManager.setRole(modelDesign.getRole());
				newModelEnterpriseBusinessManager.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelEnterpriseBusinessManager.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelEnterpriseBusinessManager.getRole().getRoleId());
				authenticationRole.setCode(newModelEnterpriseBusinessManager.getRole().getCode());
				authenticationRole.setCreatedAt(newModelEnterpriseBusinessManager.getCreatedDate());
				authenticationRole.setCreatedBy(newModelEnterpriseBusinessManager.getCreatedById());
				authenticationRole.setDisplayName(newModelEnterpriseBusinessManager.getRole().getDisplayName());
				authenticationRole.setLevel(newModelEnterpriseBusinessManager.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelEnterpriseBusinessManager.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelEnterpriseBusinessManager.getRole().getParentId());
				authenticationRole.setStatus(newModelEnterpriseBusinessManager.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelEnterpriseBusinessManager.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelEnterpriseBusinessManager.getModifiedById());

				for (Step step : newModelEnterpriseBusinessManager.getSteps()) {
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
				authenticationModel.setTanentId(newModelEnterpriseBusinessManager.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryEnterpriseBusinessManager.save(newModelEnterpriseBusinessManager);
					return newModelEnterpriseBusinessManager;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelEnterpriseBusinessManager update(ModelEnterpriseBusinessManager modelEnterpriseBusinessManager) {

		ModelEnterpriseBusinessManager existingModelEnterpriseBusinessManager = new ModelEnterpriseBusinessManager();
		String password = null;

		if (modelEnterpriseBusinessManager.getAttemptId() != null) {
			existingModelEnterpriseBusinessManager = repositoryEnterpriseBusinessManager
					.findFirstByAttemptId(modelEnterpriseBusinessManager.getAttemptId());
			if (existingModelEnterpriseBusinessManager != null) {
				try {

					for (Step step : existingModelEnterpriseBusinessManager.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelEnterpriseBusinessManager.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryEnterpriseBusinessManager.save(modelEnterpriseBusinessManager);
					return modelEnterpriseBusinessManager;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelEnterpriseBusinessManager> getListByPropertyCodeAndValue(FilterEnterpriseBusinessManager filterEnterpriseBusinessManager) {
		List<ModelEnterpriseBusinessManager> modelEnterpriseBusinessManagerList = new ArrayList<ModelEnterpriseBusinessManager>();
		List<ModelEnterpriseBusinessManager> modifedModelEnterpriseBusinessManagerList = new ArrayList<ModelEnterpriseBusinessManager>();
		boolean isRecordFound = false;
		if (filterEnterpriseBusinessManager.getTanentId() != null) {
			modelEnterpriseBusinessManagerList = repositoryEnterpriseBusinessManager
					.getByTanentIdAndRoleId(filterEnterpriseBusinessManager.getTanentId(), filterEnterpriseBusinessManager.getRoleId());
		}

		if (modelEnterpriseBusinessManagerList.size() > 0) {
			for (ModelEnterpriseBusinessManager modelEnterpriseBusinessManager : modelEnterpriseBusinessManagerList) {
				for (Step step : modelEnterpriseBusinessManager.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterEnterpriseBusinessManager.getPropertyCode())) {
								modifedModelEnterpriseBusinessManagerList.add(modelEnterpriseBusinessManager);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelEnterpriseBusinessManagerList;
	}

}
