package logic;

import java.awt.Point;

public class Square implements Shape {

	private int base;
	private int height;
	private Point points;
	private boolean selected;
	
	
	public Square() {
		// TODO Auto-generated constructor stub
	}
	
	public Square(int base, int height) {
		this.base = base;
		this.height = height;
		this.setSelected(false);
//		this.points = points;
	}
	public int getBase() {
		return base;
	}
	public void setBase(int base) {
		this.base = base;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Point getPoints() {
		return points;
	}
	public void setPoints(Point points) {
		this.points = points;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
