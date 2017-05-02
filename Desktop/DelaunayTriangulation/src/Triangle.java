import java.util.Arrays;

public class Triangle {
	Vertex a;
	Vertex b;
	Vertex c;

	public Triangle(Vertex a, Vertex b, Vertex c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	private Vertex computeClosestPoint(Vertex a, Vertex b, Vertex c) {
		Vertex ab = b.sub(a);
		double t = c.sub(a).dot(ab) / ab.dot(ab);
		if(t < 0.0) t = 0.0;
		if(t > 1.0) t = 1.0;
		return a.add(ab.mult(t));
	}

	public boolean contains(Vertex p) {
		double pab = p.sub(a).cross(b.sub(a));
		double pbc = p.sub(b).cross(c.sub(b));
		if(!hasSameSign(pab, pbc))
			return false;
		double pca = p.sub(c).cross(a.sub(c));
		if(!hasSameSign(pab, pca))
			return false;
		return true;
	}

	private boolean hasSameSign(double a, double b) {
		return Math.signum(a) == Math.signum(b);
	}

	// Check a point whether it locates inside this circumcircle
	public boolean pointInCircumcircle(Vertex d) {
		double a11 = a.getX() - d.getX();
		double a21 = b.getX() - d.getX();
		double a31 = c.getX() - d.getX();

		double a12 = a.getY() - d.getY();
		double a22 = b.getY() - d.getY();
		double a32 = c.getY() - d.getY();

		double a13 = (a.getX() - d.getX()) * (a.getX() - d.getX()) + (a.getY() - d.getY()) * (a.getY() - d.getY());
		double a23 = (b.getX() - d.getX()) * (b.getX() - d.getX()) + (b.getY() - d.getY()) * (b.getY() - d.getY());
		double a33 = (c.getX() - d.getX()) * (c.getX() - d.getX()) + (c.getY() - d.getY()) * (c.getY() - d.getY());

		double det = a11*a22*a33 + a12*a23*a31 + a13*a21*a32 - a13*a22*a31 - a12*a21*a33 - a11*a23*a32;

		if(isOrientedCCW())
			// lies to the left of the directed line
			return det > 0.0f;
		else
			return det < 0.0f;
	}

	// Check the location of a point to a line
	public boolean isOrientedCCW() {
		double a11 = a.getX() - c.getX();
		double a21 = b.getX() - c.getX();
		double a12 = a.getY() - c.getY();
		double a22 = b.getY() - c.getY();
		double det = a11 * a22 - a12 * a21;
		return det > 0.0f;
	}

	public boolean isNeighbour(Edge edge) {
		return (a == edge.getOrigin() || b == edge.getOrigin() || c == edge.getOrigin()) 
				&& (a == edge.getEnd() || b == edge.getEnd() || c == edge.getEnd());
	}

	public Vertex nonEdgeVertex(Edge edge) {
		if(a != edge.getOrigin() && a != edge.getEnd()) return a;
		if(b != edge.getOrigin() && b != edge.getEnd()) return b;
		if(c != edge.getOrigin() && c != edge.getEnd()) return c;
		return null;
	}

	public boolean hasVertex(Vertex v1) {
		if(a == v1 || b == v1 || c == v1)
			return true;
		else
			return false;
	}

	public EdgeDistancePack findNearestEdge(Vertex point) {
		EdgeDistancePack[] edges = new EdgeDistancePack[3];

		// Compute the nearest edge
		edges[0]= new EdgeDistancePack(new Edge(a,b), computeClosestPoint(a, b, point).sub(point).mag());
		edges[1]= new EdgeDistancePack(new Edge(b,c), computeClosestPoint(b, c, point).sub(point).mag());
		edges[2]= new EdgeDistancePack(new Edge(c,a), computeClosestPoint(c, a, point).sub(point).mag());

		Arrays.sort(edges);
		return edges[0];
	}
}
