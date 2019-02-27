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

	public static Texture hole;
	public Texture paused;
	
	public final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	public Sprite[] shapeSprites;

	public Texture levelSelect;


	public LoadTexture() {

		textureAtlas = new TextureAtlas("../core/assets/shapes.txt");
		shapeSprites=new Sprite[GameConfig.COUNT];
	}
	public void LoadSprite() {
		
		Array<AtlasRegion> regions = textureAtlas.getRegions();

		for (AtlasRegion region : regions) {
			Sprite sprite = textureAtlas.createSprite(region.name);
			
			float width;
			float height;
			if( region.name.equals("circle")) {
				 width = sprite.getWidth() * GameConfig.SCALE *GameConfig.SMULTIPLIER;
				 height = sprite.getHeight() * GameConfig.SCALE*GameConfig.SMULTIPLIER;
			}
			else {
				 width = sprite.getWidth() * GameConfig.SCALE;
				 height = sprite.getHeight() * GameConfig.SCALE;
			}
			sprite.setSize(width, height);
			sprite.setOrigin(0, 0);

			sprites.put(region.name, sprite);
		}
		

		

		for (int i = 0; i < GameConfig.levels[GameConfig.currentLevel].length; i++) {
			shapeSprites[i] = sprites.get(GameConfig.levels[GameConfig.currentLevel][i]);
		}
		
		background = new Texture(Gdx.files.internal("../core/assets/background2.jpg"));
		base = new Texture(Gdx.files.internal("../core/assets/base.png"));
		container = new Texture(Gdx.files.internal("../core/assets/container.png"));

		newGame = new Texture(Gdx.files.internal("../core/assets/newgame.png"));
		exit = new Texture(Gdx.files.internal("../core/assets/Exit.png"));
		resumeT = new Texture(Gdx.files.internal("../core/assets/resume.png"));
		restartT = new Texture(Gdx.files.internal("../core/assets/restart.png"));
		startMenuT = new Texture(Gdx.files.internal("../core/assets/startMenu.png"));

		paused = new Texture(Gdx.files.internal("../core/assets/Pausa.png"));
		
		hole = new Texture(Gdx.files.internal("../core/assets/hole1.png"));
		levelSelect = new Texture(Gdx.files.internal("../core/assets/levels.jpg"));
	}
}
