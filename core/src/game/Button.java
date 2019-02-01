package game;

import com.badlogic.gdx.graphics.Texture;

public class Button {
	public String name;
	public Texture texture;
	public int x;
	public int y;
	public int width;
	public int height;
	public boolean selected;


	public Button(String name, Texture texture, int x, int y, int width, int height) {
		this.name = name;
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		selected = false;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
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



}
