package maxis.dispatcher.service;

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
import maxis.dispatcher.model.FilterDispatcher;
import maxis.dispatcher.model.ModelDispatcher;
import maxis.dispatcher.repository.RepositoryDispatcher;

@Component
public class ServiceDispatcher {

	@Autowired
	private RepositoryDispatcher repositoryDispatcher;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceDispatcher(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelDispatcher add(ModelDispatcher modelDispatcher) {

		modelDispatcher.setId(UUID.randomUUID().toString());
		// modelDispatcher.setCreatedDate(new Date().toString());
		// modelDispatcher.setModifiedDate(new Date().toString());

		try {
			repositoryDispatcher.save(modelDispatcher);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelDispatcher;
	}

	public List<ModelDispatcher> getList(FilterDispatcher filterDispatcher) {
		List<ModelDispatcher> modelDispatcher = new ArrayList<ModelDispatcher>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterDispatcher.getCode())) {
			modelDispatcher = repositoryDispatcher.findByCode(filterDispatcher.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterDispatcher.getTanentId())) {
			modelDispatcher = repositoryDispatcher.findByTanentId(filterDispatcher.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterDispatcher.getCreatedById())) {
			modelDispatcher = repositoryDispatcher.findByCreatedById(filterDispatcher.getCreatedById());
		} else {
			modelDispatcher = repositoryDispatcher.findAll();
		}
		return modelDispatcher;
	}

	public ModelDispatcher create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelDispatcher modelDispatcher = new ModelDispatcher();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelDispatcher = repositoryDispatcher.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelDispatcher == null) {
				ModelDispatcher newModelDispatcher = new ModelDispatcher();
				newModelDispatcher = (ModelDispatcher) UtilityProperty.initializeBaseModel(newModelDispatcher,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPDADISPATCHER");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelDispatcher.setId(corelationId);
				newModelDispatcher.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelDispatcher.setCreatedDate(new Date().toString());
				newModelDispatcher.setModifiedDate(new Date().toString());
				
				newModelDispatcher.setRole(modelDesign.getRole());
				newModelDispatcher.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelDispatcher.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelDispatcher.getRole().getRoleId());
				authenticationRole.setCode(newModelDispatcher.getRole().getCode());
				authenticationRole.setCreatedAt(newModelDispatcher.getCreatedDate());
				authenticationRole.setCreatedBy(newModelDispatcher.getCreatedById());
				authenticationRole.setDisplayName(newModelDispatcher.getRole().getDisplayName());
				authenticationRole.setLevel(newModelDispatcher.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelDispatcher.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelDispatcher.getRole().getParentId());
				authenticationRole.setStatus(newModelDispatcher.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelDispatcher.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelDispatcher.getModifiedById());

				for (Step step : newModelDispatcher.getSteps()) {
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
				authenticationModel.setTanentId(newModelDispatcher.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryDispatcher.save(newModelDispatcher);
					return newModelDispatcher;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelDispatcher update(ModelDispatcher modelDispatcher) {

		ModelDispatcher existingModelDispatcher = new ModelDispatcher();
		String password = null;

		if (modelDispatcher.getAttemptId() != null) {
			existingModelDispatcher = repositoryDispatcher
					.findFirstByAttemptId(modelDispatcher.getAttemptId());
			if (existingModelDispatcher != null) {
				try {

					for (Step step : existingModelDispatcher.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelDispatcher.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryDispatcher.save(modelDispatcher);
					return modelDispatcher;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelDispatcher> getListByPropertyCodeAndValue(FilterDispatcher filterDispatcher) {
		List<ModelDispatcher> modelDispatcherList = new ArrayList<ModelDispatcher>();
		List<ModelDispatcher> modifedModelDispatcherList = new ArrayList<ModelDispatcher>();
		boolean isRecordFound = false;
		if (filterDispatcher.getTanentId() != null) {
			modelDispatcherList = repositoryDispatcher
					.getByTanentIdAndRoleId(filterDispatcher.getTanentId(), filterDispatcher.getRoleId());
		}

		if (modelDispatcherList.size() > 0) {
			for (ModelDispatcher modelDispatcher : modelDispatcherList) {
				for (Step step : modelDispatcher.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterDispatcher.getPropertyCode())) {
								modifedModelDispatcherList.add(modelDispatcher);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelDispatcherList;
	}

}
