package maxis.jobmanagement.viewmodel;

import lombok.*;

@Getter
@Setter
public class JobViewModel {
	private String id;
	private String status;
	private String message;
	private Long previousWarehouseId;
	private String previousWarehouseCode;
	private Long currentWarehouseId;
	private String currentWarehouseCode;
}
