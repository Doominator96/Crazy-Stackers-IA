package graphic;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import game.Button;
import utils.GameConfig;

public class LoadTexture {
	
	/**
	 * Used to convert our sprite sheet found at android/assets/sprites.png and
	 */
	public TextureAtlas textureAtlas;

	public Texture background;
	public Texture base;
	public Texture container;

	public static Texture newGame;
	public static Texture exit;
	public static Texture resumeT;
	public static Texture restartT;
	public static Texture startMenuT;

	public Texture paused;
	
	public final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	public Sprite[] shapeSprites;


	public LoadTexture() {

		textureAtlas = new TextureAtlas("shapes.txt");
		shapeSprites=new Sprite[GameConfig.COUNT];
	}
	public void LoadSprite() {
		
		Array<AtlasRegion> regions = textureAtlas.getRegions();

		for (AtlasRegion region : regions) {
			Sprite sprite = textureAtlas.createSprite(region.name);

			float width = sprite.getWidth() * GameConfig.SCALE;
			float height = sprite.getHeight() * GameConfig.SCALE;

			sprite.setSize(width, height);
			sprite.setOrigin(0, 0);

			sprites.put(region.name, sprite);
		}
		

		

		for (int i = 0; i < GameConfig.levels[GameConfig.currentLevel].length; i++) {
			shapeSprites[i] = sprites.get(GameConfig.levels[GameConfig.currentLevel][i]);
		}
		
		background = new Texture(Gdx.files.internal("background1.jpg"));
		base = new Texture(Gdx.files.internal("base.png"));
		container = new Texture(Gdx.files.internal("container.png"));

		newGame = new Texture(Gdx.files.internal("newgame.png"));
		exit = new Texture(Gdx.files.internal("Exit.png"));
		resumeT = new Texture(Gdx.files.internal("resume.png"));
		restartT = new Texture(Gdx.files.internal("restart.png"));
		startMenuT = new Texture(Gdx.files.internal("startMenu.png"));

		paused = new Texture(Gdx.files.internal("Pausa.png"));
	}
}
