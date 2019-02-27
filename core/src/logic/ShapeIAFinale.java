package logic;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("fina")
public class ShapeIAFinale {

	@Param(0)
	private int x;
	@Param(1)
	private int y;
	@Param(2)
	private int type;
	@Param(3)
	private int count;
	
	public ShapeIAFinale(int x, int y, int value,int count) {
		this.x = x;
		this.y = y;
		this.type = value;
		this.count = count;
	}
	

	public ShapeIAFinale() {
		// TODO Auto-generated constructor stub
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getValue() {
		return type;
	}

	public void setValue(int value) {
		this.type = value;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		String string = "X: " + x + " Y: " +y + " Type: " + type + " Count: " + count;
		return string;
	}
	
}
