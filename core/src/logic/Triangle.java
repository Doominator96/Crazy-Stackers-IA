package logic;

import java.awt.Point;

public class Triangle implements Shape{

	private int base;
	private int height;
	private Point points;
	
	
	public Triangle() {
		// TODO Auto-generated constructor stub
	}
	
	public Triangle(int base, int height, Point points) {
		this.base = base;
		this.height = height;
		this.points = points;
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
	
	
}
