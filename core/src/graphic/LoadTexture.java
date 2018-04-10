package graphic;

import com.badlogic.gdx.graphics.Texture;

public class LoadTexture {
	
	 Texture background;
	 Texture base;
	 Texture container;
	
	public LoadTexture() {
		background = new Texture("background1.jpg");
		base = new Texture("base.png");
		container = new Texture("container.png");
	}
}
