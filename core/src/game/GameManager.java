package game;

import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

import graphic.GameGui;
import logic.Shape;
import logic.Square;

public class GameManager implements ApplicationListener {


	private GameGui gameGui;
	ArrayList<Shape> shapes;
	private long firstClick;

	@Override
	public void render() {

		Vector3 vector = new Vector3();
		Vector3 mousePosition = gameGui.getCamera().unproject(vector.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		Vector3 mousePositionShape = gameGui.getCamera().unproject(vector.set(Gdx.input.getX(), Gdx.input.getY(), 0));

		for (Shape shape : shapes) {
			if(mousePosition.x >= ((Square)shape).getPoints().getX() && mousePosition.x <= ((Square)shape).getPoints().getX()+((Square)shape).getBase() && 
					mousePosition.y >= ((Square)shape).getPoints().getY() && mousePosition.y <= ((Square)shape).getPoints().getY()+((Square)shape).getHeight())
				if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					
					long secondClick = System.currentTimeMillis();
					if(secondClick - firstClick > 60) {
						if (((Square)shape).isSelected())
							((Square)shape).setSelected(false);
						else
							((Square)shape).setSelected(true);
					}
					firstClick = secondClick;
				}

			if(((Square)shape).isSelected()) {
				((Square)shape).setPoints(new Point(Math.round(mousePosition.x) - ((Square)shape).getBase()/2, 
											Math.round(mousePosition.y) - ((Square)shape).getHeight()/2));
			}
		}


		gameGui.repaint();
	}

	@Override
	public void create() {
		firstClick = 0;
		shapes = new ArrayList<Shape>();
		shapes.add(new Square(300,150));
		shapes.add(new Square(250, 150));
		shapes.add(new Square(100,50));
		shapes.add(new Square(200, 110));

		gameGui = new GameGui(shapes);
	}


	@Override
	public void resize(int width, int height) {
		gameGui.getCamera().viewportWidth = 1800;                 // Viewport of 30 units!
		gameGui.getCamera().viewportHeight = 900; // Lets keep things in proportion.
		gameGui.getCamera().update();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
