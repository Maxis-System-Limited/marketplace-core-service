package maxis.jobmanagement.viewmodel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import maxis.deliveryrider.model.ModelDeliveryRider;

@Setter
@Getter
public class DispatcherMerchantAssignRiderViewModel {
	private List<ModelDeliveryRider> riders;
	private DeliveryDestination deliverDestination;
}
