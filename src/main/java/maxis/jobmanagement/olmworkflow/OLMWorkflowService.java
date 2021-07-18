package maxis.jobmanagement.olmworkflow;

import java.util.Collections;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class OLMWorkflowService {
	private RestTemplate restTemplate;

	public OLMWorkflowService(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public OLMNextStateResponseModel getNextAllowableState(OLMRequestModel oLMRequestModel) throws JsonMappingException, JsonProcessingException {
		
		OLMNextStateResponseModel oLMNextStateResponseModel = new OLMNextStateResponseModel();
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		
		
		HttpEntity<OLMRequestModel> entity = new HttpEntity<>(oLMRequestModel, httpHeaders);

		ResponseEntity<OLMNextStateResponseModel> response = restTemplate.exchange(
				"http://elog-workflow-service-maxis.nagadpay.com/states/nextAllowableState", HttpMethod.POST, entity,
				OLMNextStateResponseModel.class);
		
		
		oLMNextStateResponseModel.setData(response.getBody().getData());

		return oLMNextStateResponseModel;
	}
}
