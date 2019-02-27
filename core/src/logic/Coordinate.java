package logic;

public class Coordinate {
	int xIniziale;
	int yIniziale;
	int xFinale;
	int yFinale;
	int type;

	public Coordinate(int xI, int yI,int xF,int yF,int type) {
		xIniziale = xI;
		yIniziale = yI;
		xFinale = xF;
		yFinale = yF;
		this.type = type;
	}



	public int getxIniziale() {
		return xIniziale;
	}



	public void setxIniziale(int xIniziale) {
		this.xIniziale = xIniziale;
	}



	public int getyIniziale() {
		return yIniziale;
	}



	public void setyIniziale(int yIniziale) {
		this.yIniziale = yIniziale;
	}



	public int getxFinale() {
		return xFinale;
	}



	public void setxFinale(int xFinale) {
		this.xFinale = xFinale;
	}



	public int getyFinale() {
		return yFinale;
	}



	public void setyFinale(int yFinale) {
		this.yFinale = yFinale;
	}



	public int getType() {
		return type;
	}



	public void setType(int type) {
		this.type = type;
	}



	@Override
	public String toString() {
		return ("xIniziale: " + xIniziale + " yIniziale: " + yIniziale);
	}
}
