package maxis.route.model;

import java.util.Comparator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Route implements Comparator<Route>{
	private ModelRoute modelRoute;
	private List<Edge> edges;
	private Integer orderNo;
	@Override
	public int compare(Route o1, Route o2) {
		// TODO Auto-generated method stub
		return o1.getOrderNo() - o2.getOrderNo();
	}
	
	
}
