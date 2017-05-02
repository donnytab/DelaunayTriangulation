
public class Vertex {
	private int id;
	private double x;
	private double y;
	
	public Vertex(double vertex_X,double vertex_Y){
		this.x = vertex_X;
		this.y = vertex_Y;
	}
	
	public Vertex(double vertex_X,double vertex_Y,int id){
		this.id = id;
		this.x = vertex_X;
		this.y = vertex_Y;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public double getX(){
		return this.x;
	}
	
	public void setX(double vertex_X){
		this.x = vertex_X;
	}
	
	public double getY(){
		return this.y;
	}
	
	public void setY(double vertex_Y){
		this.y = vertex_Y;
	}
	
	public double getDistance(Vertex end){
		return Math.sqrt((x-end.x)*(x-end.x)+(y-end.y)*(y-end.y));
	}
	
	public double getMaxCoordinate(){
		if(Math.abs(this.x) > Math.abs(this.y))
			return this.x;
		else
			return this.y;
	}
	
	public Vertex sub(Vertex v){
		return new Vertex(this.x-v.getX(), this.y-v.getY());
	}
	
	public Vertex add(Vertex v){
		return new Vertex(this.x+v.getX(), this.y+v.getY());
	}
	
	public double dot(Vertex v) {
		return this.x * v.x + this.y * v.y;
	}
	
	public double cross(Vertex v) {
		return this.y * v.x - this.x * v.y;
	}
	
	public double mag() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public Vertex mult(double s) {
		return new Vertex(this.x * s, this.y * s);
	}
}
