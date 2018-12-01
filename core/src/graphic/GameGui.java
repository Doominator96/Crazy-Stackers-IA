package graphic;

import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import logic.Shape;
import logic.Square;

public class GameGui {

	LoadTexture loadTexture;
	private SpriteBatch batch;
	OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private ArrayList<Shape> shapes;
	int endContainerX;
	int endContainerY;
	
	public int calcola(int numero) {
		return numero/2;
	}

	public GameGui(ArrayList<Shape> figure) {
		this.shapes = new ArrayList<Shape>();
		this.shapes = figure;
		endContainerX = 1400;
		endContainerY = 182;
		
		for (Shape shape : shapes) {
			if(shape instanceof Square) {
				((Square)shape).setPoints(new Point(endContainerX-((Square)shape).getBase(), (endContainerY-((Square)shape).getHeight()) - calcola(endContainerY-((Square)shape).getHeight())));
				endContainerX -= (((Square)shape).getBase() + 30); 
			}
		}
		
		loadTexture = new LoadTexture();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(1800, 900); 
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0); 
	}
	public GameGui() {
		
		loadTexture = new LoadTexture();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(1800, 900); 
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0); 
	}

	public void repaint() {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);


		batch.begin();
		batch.draw(loadTexture.background, 0, 0,1800,900);
		batch.draw(loadTexture.base, 400, 220,1000,60);
		batch.draw(loadTexture.container,30,2,1400,180);
		batch.draw(loadTexture.container,1440,2,350,180);
		batch.end();


		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		for (Shape shape : shapes) {
			if(shape instanceof Square) {
				shapeRenderer.setColor(new Color(255, 0, 0, 0));
				shapeRenderer.rect((int)((Square)shape).getPoints().getX(),(int)(((Square)shape).getPoints().getY()), 
										((Square)shape).getBase(), ((Square)shape).getHeight());
				
			}
		}


		shapeRenderer.end();
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

}
