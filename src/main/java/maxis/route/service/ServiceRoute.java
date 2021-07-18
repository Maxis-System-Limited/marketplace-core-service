package maxis.route.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import maxis.common.UtilityFunction;
import maxis.route.model.AdjacencySetGraph;
import maxis.route.model.DistanceInfo;
import maxis.route.model.Edge;
import maxis.route.model.FilterRoute;
import maxis.route.model.IGraph.GraphType;
import maxis.route.model.ModelRoute;
import maxis.route.model.Route;
import maxis.route.repository.RepositoryRoute;

@Component
public class ServiceRoute {

	@Autowired
	private RepositoryRoute repositoryRoute;

	public ModelRoute add(ModelRoute modelRoute) {

		modelRoute.setId(UUID.randomUUID().toString());
		modelRoute.setCreatedDate(new Date().toString());
		modelRoute.setModifiedDate(new Date().toString());

		try {
			repositoryRoute.save(modelRoute);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelRoute;
	}

	public List<ModelRoute> getList(FilterRoute filterRoute) {
		List<ModelRoute> modelRoute = new ArrayList<ModelRoute>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterRoute.getCode())) {
			modelRoute = repositoryRoute.findByCode(filterRoute.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterRoute.getTanentId())) {
			modelRoute = repositoryRoute.findByTanentId(filterRoute.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterRoute.getCreatedById())) {
			modelRoute = repositoryRoute.findByCreatedById(filterRoute.getCreatedById());
		} else {
			modelRoute = repositoryRoute.findAll();
		}
		return modelRoute;
	}

	private AdjacencySetGraph buildGraph(String tanentId) {

		AdjacencySetGraph graph = new AdjacencySetGraph(26, GraphType.UNDIRECTED);

		List<ModelRoute> modelRoutes = new ArrayList<ModelRoute>();
		modelRoutes = repositoryRoute.findByTanentId(tanentId);
		for (ModelRoute modelRoute : modelRoutes) {
			if (modelRoute.getWarehouseList().size() > 1) {
				int begin = -1;
				int end = -1;
				for (Integer warehouseId : modelRoute.getWarehouseList()) {

					if (begin == -1) {
						begin = warehouseId;
						continue;
					} else {
						end = warehouseId;
						System.out.println(begin + " , " + end);
						graph.addEdge(begin, end);
						begin = end;
					}
				}
			}

		}
		return graph;
	}

	public List<Integer> generateShortestPath(String tanentId, int source, int destination) {

		return shortestPath(buildGraph(tanentId), source, destination);
	}

	private Map<Integer, DistanceInfo> buildDistanceTable(AdjacencySetGraph graph, int source) {
		Map<Integer, DistanceInfo> distanceTable = new HashMap<>();
		List<Integer> visitedList = new ArrayList<Integer>();
		for (int j = 0; j < graph.getNumVertices(); j++) {
			distanceTable.put(j, new DistanceInfo());
		}
		distanceTable.get(source).setDistance(0);
		distanceTable.get(source).setLastVertex(source);

		LinkedList<Integer> queue = new LinkedList<>();
		queue.add(source);

		while (!queue.isEmpty()) {
			int currentVertex = queue.pollFirst();
			visitedList.add(currentVertex);
			for (int i : graph.getAdjacentVertices(currentVertex)) {
				int currentDistance = distanceTable.get(i).getDistance();
				if (currentDistance == -1) {
					currentDistance = distanceTable.get(currentVertex).getDistance() + 1;
					distanceTable.get(i).setDistance(currentDistance);
					distanceTable.get(i).setLastVertex(currentVertex);
				}
				if (!visitedList.contains(i) && !graph.getAdjacentVertices(i).isEmpty()) {
					queue.add(i);
				}
			}
		}

		return distanceTable;
	}

	public List<Integer> shortestPath(AdjacencySetGraph graph, int source, int destination) {
		Map<Integer, DistanceInfo> distanceTable = buildDistanceTable(graph, source);
		Stack<Integer> stack = new Stack<>();
		List<Integer> sortestPath = new ArrayList<>();
		stack.push(destination);
		int previousVertext = distanceTable.get(destination).getLastVertex();
		while (previousVertext != -1 && previousVertext != source) {
			stack.push(previousVertext);
			previousVertext = distanceTable.get(previousVertext).getLastVertex();
			if (previousVertext == -1) {
				System.out.println("There is no path from: " + source + " To : " + destination);
			}
		}
		stack.push(source);
		while (!stack.isEmpty()) {
			sortestPath.add(stack.pop());
		}
		System.out.println(sortestPath);
		return sortestPath;
	}

	public List<Route> findDeliveryRoute(String tanentId, int source, int destination) {
		List<Integer> shortestPath = new ArrayList<>();
		List<ModelRoute> modelRoutes = new ArrayList<ModelRoute>();
		List<Route> deliveryRoutes = new ArrayList<>();
		modelRoutes = repositoryRoute.findByTanentId(tanentId);

		shortestPath = generateShortestPath(tanentId, source, destination);

		for (ModelRoute modelRoute : modelRoutes) {
			Route route = new Route();

			route = getDeliveryRouteWithEdges(shortestPath, modelRoute);
			if (route != null) {
				deliveryRoutes.add(route);
			}
		}

		return updateDeliverRouteWithOrder(shortestPath,deliveryRoutes);
	}

	public Route getDeliveryRouteWithEdges(List<Integer> shortestPath, ModelRoute modelRoute) {

		List<Edge> edges = new ArrayList<>();
		Route route = new Route();
		route.setModelRoute(modelRoute);
		int begin = -1;
		int end = -1;

		for (Integer warehouseId : shortestPath) {
			if (modelRoute.getWarehouseList().contains(warehouseId)) {
				if (begin == -1) {
					begin = warehouseId;
					continue;
				} else {
					end = warehouseId;
					Edge edge = new Edge();
					edge.setSourceNode(begin);
					edge.setDestinationNode(end);
					edges.add(edge);
					begin = end;
				}
			}
		}
		route.setEdges(edges);

		if (route.getEdges().size() > 0) {

			begin = route.getEdges().get(0).getSourceNode();
			end = route.getEdges().get(route.getEdges().size() - 1).getDestinationNode();

			Edge edge = new Edge();
			edge.setSourceNode(begin);
			edge.setDestinationNode(end);
			edges.clear();
			edges.add(edge);
			route.setEdges(edges);
		}

		if (route.getEdges().size() > 0) {
			return route;
		} else {
			return null;
		}

	}
	
	 private List<Route> updateDeliverRouteWithOrder(List<Integer> shortestPath,List<Route> deliveryRoutes){
		 
		 int routeNumber = 1;
		 while(!(shortestPath.size() == 1)) {
			 for(Route route : deliveryRoutes) {
				 
				 boolean isExist = false;
				 if(route.getEdges().size() == 1 && route.getEdges().get(0).getSourceNode().equals(shortestPath.get(0))) {
					 isExist = true;
					 route.setOrderNo(routeNumber);
					 shortestPath.remove(0);
					 while(!route.getEdges().get(0).getDestinationNode().equals(shortestPath.get(0))) {
						 shortestPath.remove(0);
					 }
				 }
				 if(isExist) {
					 routeNumber++; 
				 }
			 }
		 }
		 Collections.sort(deliveryRoutes,new Route());
		 return deliveryRoutes;
	 }

}
