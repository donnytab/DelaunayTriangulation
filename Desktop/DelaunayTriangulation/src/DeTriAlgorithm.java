import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class DeTriAlgorithm {
	protected static TriangleSet triangleSet;
	protected static Triangle superTriangle;
	
	public static void DelaunayTriangulation(List<Vertex> vertexList, ArrayList<ArrayList<Integer>> graph){
		triangleSet = new TriangleSet();
		triangleSet.triangleSet.clear();
		if(vertexList.size() >= 3){
			double maxCoordinate = 0.0;		
			// Find the max coordinate in the vertexList
			for(int i=0; i<vertexList.size(); i++){
				maxCoordinate = 
						maxCoordinate > vertexList.get(i).getMaxCoordinate()? maxCoordinate : vertexList.get(i).getMaxCoordinate();
			}			
			maxCoordinate *= 20.0;
			
			// Dummy Triangle vertices
			Vertex p1 = new Vertex(0.0, 3.0 * maxCoordinate, 100);
			Vertex p2 = new Vertex(3.0 * maxCoordinate, 0.0, 101);
			Vertex p3 = new Vertex(-3.0 * maxCoordinate, -3.0 * maxCoordinate, 102);			
			superTriangle = new Triangle(p1, p2, p3);
			triangleSet.add(superTriangle);

			for(int i=0;i < vertexList.size(); i++) {				
				Vertex currentVertex = vertexList.get(i);
				Triangle triangle = triangleSet.findContainingTriangle(currentVertex);
				
				// New vertex lies on an edge
				if(triangle == null) {
					Edge edge = triangleSet.findEdgeThatContains(currentVertex);

					// Two triangle that share the same edge
					Triangle first = findTriangleSharing(edge);
					Triangle second = findNeighbour(first, edge);
					Vertex nonEdgeFirst = first.nonEdgeVertex(edge);
					Vertex nonEdgeSecond = second.nonEdgeVertex(edge);

					triangleSet.remove(first);
					triangleSet.remove(second);

					// Generate 4 new triangles
					Triangle tri1 = new Triangle(edge.getOrigin(), nonEdgeFirst, currentVertex);
					Triangle tri2 = new Triangle(edge.getEnd(), nonEdgeFirst, currentVertex);
					Triangle tri3 = new Triangle(edge.getOrigin(), nonEdgeSecond, currentVertex);
					Triangle tri4 = new Triangle(edge.getEnd(), nonEdgeSecond, currentVertex);
					triangleSet.add(tri1);
					triangleSet.add(tri2);
					triangleSet.add(tri3);
					triangleSet.add(tri4);

					// Check edges
					legalizeEdge(tri1, new Edge(edge.getOrigin(), nonEdgeFirst), currentVertex);
					legalizeEdge(tri2, new Edge(edge.getEnd(), nonEdgeFirst), currentVertex);
					legalizeEdge(tri3, new Edge(edge.getOrigin(), nonEdgeSecond), currentVertex);
					legalizeEdge(tri4, new Edge(edge.getEnd(), nonEdgeSecond), currentVertex);
				} 
				// New vertex is inside a triangle
				else { 
					Vertex a = triangle.a;
					Vertex b = triangle.b;
					Vertex c = triangle.c;

					triangleSet.remove(triangle);

					// Generate 3 new triangles
					Triangle first = new Triangle(a, b, currentVertex);
					Triangle second = new Triangle(b, c, currentVertex);
					Triangle third = new Triangle(c, a, currentVertex);
					triangleSet.add(first);
					triangleSet.add(second);
					triangleSet.add(third);

					// Check edges
					legalizeEdge(first, new Edge(a, b), currentVertex);
					legalizeEdge(second, new Edge(b, c), currentVertex);
					legalizeEdge(third, new Edge(c, a), currentVertex);
				}
			}

			// remove all triangles that contain vertices of dummy points	
			triangleSet.removeTrianglesUsing(superTriangle.a);
			triangleSet.removeTrianglesUsing(superTriangle.b);
			triangleSet.removeTrianglesUsing(superTriangle.c);
		}
		else
		{
			System.out.println("No Triangulation. More vertices needed.");
			return;
		}
		
		for(int j=0; j<triangleSet.triangleSet.size(); j++){
			int aId = triangleSet.triangleSet.get(j).a.getId();
			int bId = triangleSet.triangleSet.get(j).b.getId();
			int cId = triangleSet.triangleSet.get(j).c.getId();
			
			// Add connected points to graph
			graph.get(aId).add(bId);
			graph.get(aId).add(cId);
			graph.get(bId).add(aId);
			graph.get(bId).add(cId);
			graph.get(cId).add(aId);
			graph.get(cId).add(bId);
		}
	}
	
	public static void legalizeEdge(Triangle triangle, Edge edge, Vertex newVertex) {
		Triangle neighbourTriangle = findNeighbour(triangle, edge);
		// Check neighbour triangle
		if(neighbourTriangle != null) {
			// Illegal edge
			if(neighbourTriangle.pointInCircumcircle(newVertex)) {
				
				triangleSet.remove(triangle);
				triangleSet.remove(neighbourTriangle);

				// Flip the edge by adding new triangles
				Vertex nonEdge = neighbourTriangle.nonEdgeVertex(edge);
				Triangle first = new Triangle(nonEdge, edge.getOrigin(), newVertex);
				Triangle second = new Triangle(nonEdge, edge.getEnd(), newVertex);
				triangleSet.add(first);
				triangleSet.add(second);

				legalizeEdge(first, new Edge(nonEdge, edge.getOrigin()), newVertex);
				legalizeEdge(second, new Edge(nonEdge, edge.getEnd()), newVertex);
			}			
		}
	}
	
	// Find the neighbour triangle
	protected static Triangle findNeighbour(Triangle tri, Edge edge) {
		for(Triangle triangle : triangleSet.triangleSet) {
			if(triangle.isNeighbour(edge) && triangle != tri) {
				return triangle;
			}
		}
		return null;
	}

	// Find the triangle that shares an edge
	protected static Triangle findTriangleSharing(Edge edge) {
		for(Triangle triangle : triangleSet.triangleSet) {
			if(triangle.isNeighbour(edge)) {
				return triangle;
			}
		}
		return null;
	}

	public ArrayList<Triangle> getTriangleSet() {
		return triangleSet.triangleSet;
	}
}

class TriangleSet {

	ArrayList<Triangle> triangleSet;

	public TriangleSet() {
		this.triangleSet = new ArrayList<Triangle>();
	}

	// Find edge wiht a given point
	public Edge findEdgeThatContains(Vertex point) {

		Vector<EdgeDistancePack> edgeVector = new Vector<EdgeDistancePack>();

		for(Triangle triangle : triangleSet) {
			edgeVector.add(triangle.findNearestEdge(point));
		}

		EdgeDistancePack[] edgeDistancePacks = new EdgeDistancePack[edgeVector.size()];
		edgeVector.toArray(edgeDistancePacks);

		// sort by distance
		Arrays.sort(edgeDistancePacks);

		return edgeDistancePacks[0].edge;
	}

	public void add(Triangle triangle) {
		this.triangleSet.add(triangle);
	}

	public void remove(Triangle triangle) {
		this.triangleSet.remove(triangle);
	}

	public void removeTrianglesUsing(Vertex point) {
		Vector<Triangle> removeList = new Vector<Triangle>();
		for(Triangle triangle : triangleSet) {
			if(triangle.hasVertex(point)) {
				removeList.add(triangle);
			}
		}
		triangleSet.removeAll(removeList);
	}

	public Triangle findContainingTriangle(Vertex point) {
		for(Triangle triangle : triangleSet) {
			if(triangle.contains(point)) {
				return triangle;
			}
		}
		return null;
	}
}

class EdgeDistancePack implements Comparable<EdgeDistancePack>{
	
	Edge edge;
	double distance;
	
	public EdgeDistancePack(Edge edge, double distance) {
		this.edge = edge;
		this.distance = distance;
	}

	// For comparison between two distances
	@Override
	public int compareTo(EdgeDistancePack o) {
		if(o.distance == distance)
			return 0;
		else if(o.distance < distance)
			return 1;
		else
			return -1;
	}
}