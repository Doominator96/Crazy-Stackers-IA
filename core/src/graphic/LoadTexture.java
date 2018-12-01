package graphic;

import com.badlogic.gdx.graphics.Texture;

public class LoadTexture {
	
	 public Texture background;
	 public Texture base;
	 public Texture container;
	
	public LoadTexture() {
		Texture background = new Texture("background1.jpg");
		Texture base = new Texture("base.png");
		Texture container = new Texture("container.png");
	}
}
