package maxis.route.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {
	private int vertexNumber;
	private Set<Integer> adjacencySet = new HashSet<Integer>();

	public Node(int vertexNumber) {
		this.vertexNumber = vertexNumber;
	}

	public int getVertexNumber() {
		return vertexNumber;
	}

	public void addEdge(int vertexNumber) {
		adjacencySet.add(vertexNumber);
	}

	public List<Integer> getAdjacentVertices() {
		List<Integer> sortedList = new ArrayList<>(adjacencySet);
		Collections.sort(sortedList);
		return sortedList;
	}

}
