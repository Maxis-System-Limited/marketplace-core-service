package maxis.warehousestockkeeper.service;

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
import maxis.warehousestockkeeper.model.FilterWarehouseStockKeeper;
import maxis.warehousestockkeeper.model.ModelWarehouseStockKeeper;
import maxis.warehousestockkeeper.repository.RepositoryWarehouseStockKeeper;

@Component
public class ServiceWarehouseStockKeeper {

	@Autowired
	private RepositoryWarehouseStockKeeper repositoryWarehouseStockKeeper;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceWarehouseStockKeeper(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelWarehouseStockKeeper add(ModelWarehouseStockKeeper modelWarehouseStockKeeper) {

		modelWarehouseStockKeeper.setId(UUID.randomUUID().toString());
		// modelWarehouseStockKeeper.setCreatedDate(new Date().toString());
		// modelWarehouseStockKeeper.setModifiedDate(new Date().toString());

		try {
			repositoryWarehouseStockKeeper.save(modelWarehouseStockKeeper);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelWarehouseStockKeeper;
	}

	public List<ModelWarehouseStockKeeper> getList(FilterWarehouseStockKeeper filterWarehouseStockKeeper) {
		List<ModelWarehouseStockKeeper> modelWarehouseStockKeeper = new ArrayList<ModelWarehouseStockKeeper>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouseStockKeeper.getCode())) {
			modelWarehouseStockKeeper = repositoryWarehouseStockKeeper.findByCode(filterWarehouseStockKeeper.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouseStockKeeper.getTanentId())) {
			modelWarehouseStockKeeper = repositoryWarehouseStockKeeper.findByTanentId(filterWarehouseStockKeeper.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouseStockKeeper.getCreatedById())) {
			modelWarehouseStockKeeper = repositoryWarehouseStockKeeper.findByCreatedById(filterWarehouseStockKeeper.getCreatedById());
		} else {
			modelWarehouseStockKeeper = repositoryWarehouseStockKeeper.findAll();
		}
		return modelWarehouseStockKeeper;
	}

	public ModelWarehouseStockKeeper create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelWarehouseStockKeeper modelWarehouseStockKeeper = new ModelWarehouseStockKeeper();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelWarehouseStockKeeper = repositoryWarehouseStockKeeper.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelWarehouseStockKeeper == null) {
				ModelWarehouseStockKeeper newModelWarehouseStockKeeper = new ModelWarehouseStockKeeper();
				newModelWarehouseStockKeeper = (ModelWarehouseStockKeeper) UtilityProperty.initializeBaseModel(newModelWarehouseStockKeeper,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPWSKEEPER");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelWarehouseStockKeeper.setId(corelationId);
				newModelWarehouseStockKeeper.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelWarehouseStockKeeper.setCreatedDate(new Date().toString());
				newModelWarehouseStockKeeper.setModifiedDate(new Date().toString());
				
				newModelWarehouseStockKeeper.setRole(modelDesign.getRole());
				newModelWarehouseStockKeeper.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelWarehouseStockKeeper.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelWarehouseStockKeeper.getRole().getRoleId());
				authenticationRole.setCode(newModelWarehouseStockKeeper.getRole().getCode());
				authenticationRole.setCreatedAt(newModelWarehouseStockKeeper.getCreatedDate());
				authenticationRole.setCreatedBy(newModelWarehouseStockKeeper.getCreatedById());
				authenticationRole.setDisplayName(newModelWarehouseStockKeeper.getRole().getDisplayName());
				authenticationRole.setLevel(newModelWarehouseStockKeeper.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelWarehouseStockKeeper.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelWarehouseStockKeeper.getRole().getParentId());
				authenticationRole.setStatus(newModelWarehouseStockKeeper.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelWarehouseStockKeeper.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelWarehouseStockKeeper.getModifiedById());

				for (Step step : newModelWarehouseStockKeeper.getSteps()) {
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
				authenticationModel.setTanentId(newModelWarehouseStockKeeper.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange("http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryWarehouseStockKeeper.save(newModelWarehouseStockKeeper);
					return newModelWarehouseStockKeeper;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelWarehouseStockKeeper update(ModelWarehouseStockKeeper modelWarehouseStockKeeper) {

		ModelWarehouseStockKeeper existingModelWarehouseStockKeeper = new ModelWarehouseStockKeeper();
		String password = null;

		if (modelWarehouseStockKeeper.getAttemptId() != null) {
			existingModelWarehouseStockKeeper = repositoryWarehouseStockKeeper
					.findFirstByAttemptId(modelWarehouseStockKeeper.getAttemptId());
			if (existingModelWarehouseStockKeeper != null) {
				try {

					for (Step step : existingModelWarehouseStockKeeper.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelWarehouseStockKeeper.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryWarehouseStockKeeper.save(modelWarehouseStockKeeper);
					return modelWarehouseStockKeeper;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelWarehouseStockKeeper> getListByPropertyCodeAndValue(FilterWarehouseStockKeeper filterWarehouseStockKeeper) {
		List<ModelWarehouseStockKeeper> modelWarehouseStockKeeperList = new ArrayList<ModelWarehouseStockKeeper>();
		List<ModelWarehouseStockKeeper> modifedModelWarehouseStockKeeperList = new ArrayList<ModelWarehouseStockKeeper>();
		boolean isRecordFound = false;
		if (filterWarehouseStockKeeper.getTanentId() != null) {
			modelWarehouseStockKeeperList = repositoryWarehouseStockKeeper
					.getByTanentIdAndRoleId(filterWarehouseStockKeeper.getTanentId(), filterWarehouseStockKeeper.getRoleId());
		}

		if (modelWarehouseStockKeeperList.size() > 0) {
			for (ModelWarehouseStockKeeper modelWarehouseStockKeeper : modelWarehouseStockKeeperList) {
				for (Step step : modelWarehouseStockKeeper.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterWarehouseStockKeeper.getPropertyCode())) {
								modifedModelWarehouseStockKeeperList.add(modelWarehouseStockKeeper);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelWarehouseStockKeeperList;
	}

}
