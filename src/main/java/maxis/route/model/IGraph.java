package maxis.route.model;

import java.util.List;

public interface IGraph {
	enum GraphType {
		DIRECTED, UNDIRECTED
	}

	void addEdge(int v1, int v2);
	
	List<Integer> getAdjacentVertices(int v);
}
