package graphic;

import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;

import game.GameManager;
import game.StartMenu;
import logic.Buco;
import logic.ShapeIA;
import logic.Square;
import utils.GameConfig;

public class GameGui {

	LoadTexture loadTexture;
	//	private SpriteBatch batch;
	OrthographicCamera camera;
	//	private ShapeRenderer shapeRenderer;
	//	private ArrayList<Shape> shapes;
	//	int endContainerX;
	//	int endContainerY;
	BitmapFont font = new BitmapFont();

	public int calcola(int numero) {
		return numero / 2;
	}

	public GameGui() {

		loadTexture = new LoadTexture();
		//		batch = new SpriteBatch();
		camera = new OrthographicCamera(1800, 900);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
	}

	public void repaint() {
		camera.update();
		//		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		//		batch.begin();
		//		batch.draw(loadTexture.background, 0, 0,1800,900);
		//		batch.draw(loadTexture.base, 400, 220,1000,60);
		//		batch.draw(loadTexture.container,30,2,1400,180);
		//		batch.draw(loadTexture.container,1440,2,350,180);
		//		batch.end();

	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public void drawMenu(SpriteBatch batch, LoadTexture loadTexture) {
		GameConfig.mouseInWorld3D.x = Gdx.input.getX();
		GameConfig.mouseInWorld3D.y = Gdx.input.getY();
		GameConfig.mouseInWorld3D.z = 0;
		getCamera().unproject(GameConfig.mouseInWorld3D);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		batch.draw(loadTexture.background, 0, 0, 200, 200);

		StartMenu.mouseClickMenu(GameConfig.mouseInWorld3D.x, GameConfig.mouseInWorld3D.y);
		batch.draw(loadTexture.newGame, StartMenu.newGameButton.x, StartMenu.newGameButton.y,
				StartMenu.newGameButton.width, StartMenu.newGameButton.height);
		batch.draw(loadTexture.exit, StartMenu.exitButton.x, StartMenu.exitButton.y, StartMenu.exitButton.width,
				StartMenu.exitButton.height);

		batch.end();
	}

	public void drawPausedMenu(SpriteBatch batch, LoadTexture loadTexture) {
		GameConfig.mouseInWorld3D.x = Gdx.input.getX();
		GameConfig.mouseInWorld3D.y = Gdx.input.getY();
		GameConfig.mouseInWorld3D.z = 0;
		getCamera().unproject(GameConfig.mouseInWorld3D);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		StartMenu.mouseClickMenu(GameConfig.mouseInWorld3D.x, GameConfig.mouseInWorld3D.y);

		batch.draw(loadTexture.background, 0, 0, 200, 200);
		batch.draw(loadTexture.base, 50, 42, 105, 10);
		batch.draw(loadTexture.paused, 40, 80, 120, 60);
		batch.draw(loadTexture.resumeT, StartMenu.resume.x, StartMenu.resume.y, StartMenu.resume.width,
				StartMenu.resume.height);
		batch.draw(loadTexture.restartT, StartMenu.restart.x, StartMenu.restart.y, StartMenu.restart.width,
				StartMenu.restart.height);
		batch.draw(loadTexture.startMenuT, StartMenu.startMenu.x, StartMenu.startMenu.y, StartMenu.startMenu.width,
				StartMenu.startMenu.height);

		batch.end();
	}

	public void gameScreen(SpriteBatch batch,Body shapeBodies[],LoadTexture loadTexture,Buco[] buchi) {

		//		System.out.println(Gdx.input.getX() + "   "+Gdx.input.getY());

		GameConfig.mouseInWorld3D.x = Gdx.input.getX();
		GameConfig.mouseInWorld3D.y = Gdx.input.getY();
		GameConfig.mouseInWorld3D.z = 0;

		getCamera().unproject(GameConfig.mouseInWorld3D);
		GameConfig.mouseInWorld2D.x = GameConfig.mouseInWorld3D.x;
		GameConfig.mouseInWorld2D.y = GameConfig.mouseInWorld3D.y;


		//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		batch.draw(loadTexture.background, 0, 0, 200, 200);
		batch.draw(loadTexture.base, 50, 42, 105, 10);

		if(GameConfig.buchi)
			for(int i=0;i<buchi.length;i++)
				batch.draw(loadTexture.hole, buchi[i].getX(), buchi[i].getY(), 20, 20);	

		// Disegna la Forma Corrente sul puntatore
		if (GameConfig.currentShapeIndex < loadTexture.shapeSprites.length) {
			drawSprite(loadTexture.shapeSprites[GameConfig.currentShapeIndex], GameConfig.mouseInWorld2D.x, GameConfig.mouseInWorld2D.y, 0,batch);
		}

		// draw the shapes on the screen
		for (int i = 0; i < GameConfig.currentShapeIndex; i++)
			if (shapeBodies.length != 0) {
				drawSprite(loadTexture.shapeSprites[i], shapeBodies[i].getPosition().x, shapeBodies[i].getPosition().y,
						(float) Math.toDegrees(shapeBodies[i].getAngle()),batch);
			}
		batch.draw(loadTexture.container, 2, 2, 150, 30);
		batch.draw(loadTexture.container, 157, 2, 40, 30);


		// Draw shapes in the container
		for (int i = 0; i < loadTexture.shapeSprites.length; i++)
			drawSpriteResized(loadTexture.shapeSprites[i], 165 - (i) * 40 + (GameConfig.currentShapeIndex * 40), 4, 0,batch);

		font.getData().setScale((float) 0.5);
		font.draw(batch, "Level: "+(int)(GameConfig.currentLevel+1),1,195);


		batch.end();

	}

	public void drawlvlSelect(SpriteBatch batch, LoadTexture loadTexture2) {
		GameConfig.mouseInWorld3D.x = Gdx.input.getX();
		GameConfig.mouseInWorld3D.y = Gdx.input.getY();
		GameConfig.mouseInWorld3D.z = 0;
		getCamera().unproject(GameConfig.mouseInWorld3D);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		batch.draw(loadTexture2.levelSelect, 0, 0, 200, 200);

		StartMenu.levelSelect(GameConfig.mouseInWorld3D.x, GameConfig.mouseInWorld3D.y);

		batch.end();
	}

	public void drawSprite(Sprite sprite, float x, float y, float degrees,SpriteBatch batch) {
		sprite.setPosition(x, y);
		sprite.setScale(1, 1);
		sprite.setRotation(degrees);
		//		System.out.println("Lunghezza: "+sprite.getWidth()+"Altezza: "+sprite.getHeight());
		sprite.draw(batch);
	}

	public void drawSpriteResized(Sprite sprite, float x, float y, float degrees,SpriteBatch batch) {
		sprite.setPosition(x, y);
		sprite.setScale(0.7f, 0.7f);
		sprite.setRotation(degrees);

		sprite.draw(batch);
	}
}
