package maxis.maxissupportadmin.model;



import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import maxis.common.model.OnboardModel;


@Getter
@Setter
@Document("MaxisSupportAdmin")
public class ModelMaxisSupportAdmin extends OnboardModel{
	private String dob;
	private String nidnum;
	
}
