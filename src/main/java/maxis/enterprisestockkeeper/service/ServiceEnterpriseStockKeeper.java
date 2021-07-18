package maxis.enterprisestockkeeper.service;

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
import maxis.enterprisestockkeeper.model.FilterEnterpriseStockKeeper;
import maxis.enterprisestockkeeper.model.ModelEnterpriseStockKeeper;
import maxis.enterprisestockkeeper.repository.RepositoryEnterpriseStockKeeper;

@Component
public class ServiceEnterpriseStockKeeper {

	@Autowired
	private RepositoryEnterpriseStockKeeper repositoryEnterpriseStockKeeper;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceEnterpriseStockKeeper(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelEnterpriseStockKeeper add(ModelEnterpriseStockKeeper modelEnterpriseStockKeeper) {

		modelEnterpriseStockKeeper.setId(UUID.randomUUID().toString());
		// modelEnterpriseStockKeeper.setCreatedDate(new Date().toString());
		// modelEnterpriseStockKeeper.setModifiedDate(new Date().toString());

		try {
			repositoryEnterpriseStockKeeper.save(modelEnterpriseStockKeeper);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelEnterpriseStockKeeper;
	}

	public List<ModelEnterpriseStockKeeper> getList(FilterEnterpriseStockKeeper filterEnterpriseStockKeeper) {
		List<ModelEnterpriseStockKeeper> modelEnterpriseStockKeeper = new ArrayList<ModelEnterpriseStockKeeper>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterEnterpriseStockKeeper.getCode())) {
			modelEnterpriseStockKeeper = repositoryEnterpriseStockKeeper.findByCode(filterEnterpriseStockKeeper.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterEnterpriseStockKeeper.getTanentId())) {
			modelEnterpriseStockKeeper = repositoryEnterpriseStockKeeper.findByTanentId(filterEnterpriseStockKeeper.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterEnterpriseStockKeeper.getCreatedById())) {
			modelEnterpriseStockKeeper = repositoryEnterpriseStockKeeper.findByCreatedById(filterEnterpriseStockKeeper.getCreatedById());
		} else {
			modelEnterpriseStockKeeper = repositoryEnterpriseStockKeeper.findAll();
		}
		return modelEnterpriseStockKeeper;
	}

	public ModelEnterpriseStockKeeper create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelEnterpriseStockKeeper modelEnterpriseStockKeeper = new ModelEnterpriseStockKeeper();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelEnterpriseStockKeeper = repositoryEnterpriseStockKeeper.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelEnterpriseStockKeeper == null) {
				ModelEnterpriseStockKeeper newModelEnterpriseStockKeeper = new ModelEnterpriseStockKeeper();
				newModelEnterpriseStockKeeper = (ModelEnterpriseStockKeeper) UtilityProperty.initializeBaseModel(newModelEnterpriseStockKeeper,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPENTERPRISESK");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelEnterpriseStockKeeper.setId(corelationId);
				newModelEnterpriseStockKeeper.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelEnterpriseStockKeeper.setCreatedDate(new Date().toString());
				newModelEnterpriseStockKeeper.setModifiedDate(new Date().toString());
				
				newModelEnterpriseStockKeeper.setRole(modelDesign.getRole());
				newModelEnterpriseStockKeeper.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelEnterpriseStockKeeper.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelEnterpriseStockKeeper.getRole().getRoleId());
				authenticationRole.setCode(newModelEnterpriseStockKeeper.getRole().getCode());
				authenticationRole.setCreatedAt(newModelEnterpriseStockKeeper.getCreatedDate());
				authenticationRole.setCreatedBy(newModelEnterpriseStockKeeper.getCreatedById());
				authenticationRole.setDisplayName(newModelEnterpriseStockKeeper.getRole().getDisplayName());
				authenticationRole.setLevel(newModelEnterpriseStockKeeper.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelEnterpriseStockKeeper.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelEnterpriseStockKeeper.getRole().getParentId());
				authenticationRole.setStatus(newModelEnterpriseStockKeeper.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelEnterpriseStockKeeper.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelEnterpriseStockKeeper.getModifiedById());

				for (Step step : newModelEnterpriseStockKeeper.getSteps()) {
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
				authenticationModel.setTanentId(newModelEnterpriseStockKeeper.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryEnterpriseStockKeeper.save(newModelEnterpriseStockKeeper);
					return newModelEnterpriseStockKeeper;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelEnterpriseStockKeeper update(ModelEnterpriseStockKeeper modelEnterpriseStockKeeper) {

		ModelEnterpriseStockKeeper existingModelEnterpriseStockKeeper = new ModelEnterpriseStockKeeper();
		String password = null;

		if (modelEnterpriseStockKeeper.getAttemptId() != null) {
			existingModelEnterpriseStockKeeper = repositoryEnterpriseStockKeeper
					.findFirstByAttemptId(modelEnterpriseStockKeeper.getAttemptId());
			if (existingModelEnterpriseStockKeeper != null) {
				try {

					for (Step step : existingModelEnterpriseStockKeeper.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelEnterpriseStockKeeper.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryEnterpriseStockKeeper.save(modelEnterpriseStockKeeper);
					return modelEnterpriseStockKeeper;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelEnterpriseStockKeeper> getListByPropertyCodeAndValue(FilterEnterpriseStockKeeper filterEnterpriseStockKeeper) {
		List<ModelEnterpriseStockKeeper> modelEnterpriseStockKeeperList = new ArrayList<ModelEnterpriseStockKeeper>();
		List<ModelEnterpriseStockKeeper> modifedModelEnterpriseStockKeeperList = new ArrayList<ModelEnterpriseStockKeeper>();
		boolean isRecordFound = false;
		if (filterEnterpriseStockKeeper.getTanentId() != null) {
			modelEnterpriseStockKeeperList = repositoryEnterpriseStockKeeper
					.getByTanentIdAndRoleId(filterEnterpriseStockKeeper.getTanentId(), filterEnterpriseStockKeeper.getRoleId());
		}

		if (modelEnterpriseStockKeeperList.size() > 0) {
			for (ModelEnterpriseStockKeeper modelEnterpriseStockKeeper : modelEnterpriseStockKeeperList) {
				for (Step step : modelEnterpriseStockKeeper.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterEnterpriseStockKeeper.getPropertyCode())) {
								modifedModelEnterpriseStockKeeperList.add(modelEnterpriseStockKeeper);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelEnterpriseStockKeeperList;
	}

}
