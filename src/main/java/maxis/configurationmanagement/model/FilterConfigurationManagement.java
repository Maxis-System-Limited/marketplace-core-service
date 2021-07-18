package maxis.configurationmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterConfigurationManagement {
	private String configurationManagerUserId;
	private String configurationManagerUserRoleCode;

	private String configurationManagerEntityId;
	private String configurationManagerEntityRoleCode;

	private String configurationTenantId;
	private String consigurationEntityId;
	private String consigurationType;
}
