package utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GameConfig {

	/**
	 * This affects the speed of our simulation, and how gravity behaves. This is
	 * set to our game's expected FPS rate for optimal performance for what we're
	 * doing in this tutorial. If you were simulating something that required
	 * greater precision, such as planets orbiting a star, you would want to set
	 * this to as high as double the frame rate, or 1/120.
	 * <p/>
	 * Setting it to a higher rate will result in a smoother, but slower simulation.
	 * Setting it to a lower value will result in a choppy frame rate, but increase
	 * the amount of polygons the simulation can process.
	 */
	public static final float STEP_TIME = 1f / 100f;

	/**
	 * Velocity iterations will improve the stability of the physics simulation. A
	 * higher value will provide greater precision for collision detection, at the
	 * cost of consuming more of the CPU.
	 */
	public static final int VELOCITY_ITERATIONS = 6; //c'era 6

	public static int currentShapeIndex = 0;
	/**
	 * This affects the way bodies react to collisions. A higher value improves the
	 * simulations overlap resolution.
	 * <p/>
	 * I recommend reading this article on the anatomy of a collision for a clearer
	 * understanding of both velocity and position iterations:
	 * http://www.iforce2d.net/b2dtut/collision-anatomy
	 */
	public static final int POSITION_ITERATIONS = 4; //c'era 4 prima

	/**
	 * This is a scalar used to make our sprites fit within the physics simulation.
	 * Without it the sprites would be too big to be drawn on the screen.
	 */
	public static final float SCALE = 0.25f;
	public static final float SMULTIPLIER =1.5f;
	/**
	 * Adjust this value to change the amount of Shapes.
	 */
	public static final int COUNT = 3;
	
//	public static String[][] levels = new String[][] {{ "square", "square", "square", "triangle", "triangle" },{ "circle", "circle", "rectangle", "square", "triangle" }};
	public static String[][] levels = new String[][] {{ "square", "square", "square"},{ "square", "rectangle", "circle" },
													{ "rectangle", "circle", "triangle" },{ "square", "rectangle", "triangle" }
													,{ "rectangle", "circle", "circle" }};
	
	public static int currentLevel=0;
	public static boolean test = true;
	public static int levelnum=5;
	public static boolean buchi = false;
	public static Vector2 mouseInWorld2D = new Vector2();
	public static Vector3 mouseInWorld3D = new Vector3();
}
