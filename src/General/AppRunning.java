package General;

import java.io.Serializable;

public class AppRunning implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Name;
	private int Id;
	
	@Override
	public String toString() {
		return "Name: " + Name + ", Id: " + Id;
	}
	
	public AppRunning(String Name, int Id) {
		this.setName(Name);
		this.setId(Id);
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}
}
