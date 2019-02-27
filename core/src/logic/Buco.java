package logic;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("buco")
public class Buco {
	
	@Param(0)
	private int x;
	
	@Param(1)
	private int y;
	
	@Param(2)
	private int type;
	
//	@Param(3)
//	private int level;

	public Buco(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
//		this.level = level;
	}
	public Buco() {
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
//	public int getLevel() {
//		return level;
//	}
//	public void setLevel(int level) {
//		this.level = level;
//	}
//	
	
}
