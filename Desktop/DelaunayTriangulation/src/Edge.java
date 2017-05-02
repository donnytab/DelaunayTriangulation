
public class Edge {
	private int id;
	private Vertex origin;
	private Vertex end;
	
	public Edge(Vertex origin,Vertex end){
		this.origin = origin;
		this.end = end;
	}
	
	public Edge(int id,Vertex origin,Vertex end){
		this.id = id;
		this.origin = origin;
		this.end = end;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setOrigin(Vertex origin){
		this.origin = origin;
	}
	
	public void setEnd(Vertex end){
		this.end = end;
	}
	
	public Vertex getOrigin(){
		return this.origin;
	}
	
	public Vertex getEnd(){
		return this.end;
	}
}
