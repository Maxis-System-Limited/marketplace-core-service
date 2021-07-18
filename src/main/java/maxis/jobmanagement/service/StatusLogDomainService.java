package maxis.jobmanagement.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.jobmanagement.model.StatusLog;
import maxis.jobmanagement.repository.StatusLogRepository;

@Service
public class StatusLogDomainService {
	@Autowired
	private StatusLogRepository statusLogRepository;

	public void addStatusLog(StatusLog statusLog) {
		statusLog.setId(UUID.randomUUID().toString());
		statusLog.setUpdatedAt(new Date().toString());
		statusLogRepository.insert(statusLog);
	}

	public List<StatusLog> getStatusLogByInstanceById(String instanceId) {

		return statusLogRepository.getStatusLogByInstanceId(instanceId);
	}

}
