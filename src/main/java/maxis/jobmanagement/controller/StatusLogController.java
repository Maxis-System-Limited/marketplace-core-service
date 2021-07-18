package maxis.jobmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import maxis.jobmanagement.service.StatusLogService;
import maxis.jobmanagement.viewmodel.StatusLogListViewModel;

@RestController
@RequestMapping("/api")
public class StatusLogController {
	
	@Autowired
	private StatusLogService statusLogService;
	@RequestMapping(value = "/statuslogbyInstanceId", method = RequestMethod.GET)
	public StatusLogListViewModel getStatusLogListByInstanceId(@RequestParam String instanceId) {
		return statusLogService.getStatusLogByInstanceById(instanceId);
	}

}
