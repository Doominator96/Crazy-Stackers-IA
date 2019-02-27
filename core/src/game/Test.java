package game;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import graphic.GameGui;
import logic.Coordinate;
import utils.GameConfig;

public class Test extends Thread{

	boolean start;

	private GameGui gameGui;
	boolean done = true;
	long timer;
	String[] shapeNames = new String[] { "circle", "exagon", "rectangle", "triangle", "square", "triangle2" };
	private static Viewport viewport;
	private World world;
	private static PhysicsShapeCache physicsBodies;
	float accumulator = 0;
	private Body ground;
	private Body[] shapeBodies = new Body[GameConfig.COUNT];
	ArrayList<Coordinate> points;
	boolean first = false;
	int contatoreProve =0;
	float camera;
	Lock lock;


	public Test(ArrayList<Coordinate> points,float camera,GameGui gg,Lock lock, World w) {
		this.points = points;
		start = true;
		gameGui = gg;
		this.camera = camera;
		this.lock = lock;
		viewport = new StretchViewport(200, 200, gameGui.getCamera());
//		world = new World(new Vector2(0, -120), true);
		world = w;
		physicsBodies = new PhysicsShapeCache("shapes2.xml");
		createGround();
	}

	@Override
	public void run() {
		System.out.println(getId() + "posizione: " +points.get(0).getxIniziale());
		System.out.println(getId() + "posizione: " +points.get(1).getxIniziale());
		System.out.println(getId() + "posizione: " +points.get(2).getxIniziale());
		
		generateShape((float)points.get(0).getxIniziale(), (float)points.get(0).getyIniziale());
		generateShape((float)points.get(1).getxIniziale(),(float) points.get(1).getyIniziale());
		generateShape((float)points.get(2).getxIniziale(),(float) points.get(2).getyIniziale());
		timer = System.currentTimeMillis();
		
		while(start && (System.currentTimeMillis() - timer < 10000)) {
			for (int i = 0; i < contatoreProve; i++) {
				
				if (shapeBodies[i].getPosition().y < 0) {
						System.out.println(getId() + " Coordinate: " + shapeBodies[i].getPosition().x + "  " + shapeBodies[i].getPosition().y);
					start = false;
					System.out.println("Thread: " + getId() + " Perdente!");
				}
			}
			stepWorld();
		}
		if(start) {
			for (int i = 0; i < shapeBodies.length; i++) {
				System.out.println(getId() + " Coordinate: " + shapeBodies[i].getPosition().x + "  " + shapeBodies[i].getPosition().y);
			}
			System.out.println("Thread: " + getId() + " Vincente!");
		}
	}
	private void generateShape(float x, float y) {
		lock.lock();
		shapeBodies[contatoreProve] = createBody(GameConfig.levels[GameConfig.currentLevel][contatoreProve], x, y, 0);
		contatoreProve++;
		lock.unlock();
	}


	private Body createBody(String name, float x, float y, float rotation) {
		try {
			lock.lock();
			System.out.println(x + "  " + y);
		Body body = physicsBodies.createBody(name, world, GameConfig.SCALE, GameConfig.SCALE);
		body.setTransform(x, y, rotation);
		return body;
		}
		finally {
			lock.unlock();
		}
	}

	private void createGround() {
		if (ground != null)
			world.destroyBody(ground);

		BodyDef bodyDef = new BodyDef();

		bodyDef.type = BodyDef.BodyType.StaticBody;

		FixtureDef fixtureDef = new FixtureDef();

		fixtureDef.friction = 1;

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(camera / 4, 1);
		fixtureDef.shape = shape;
		ground = world.createBody(bodyDef);
		ground.createFixture(fixtureDef);
		ground.setTransform(camera / 2, 50, 0);
		shape.dispose();
	}
	
	private void stepWorld() {
		float delta = Gdx.graphics.getDeltaTime();
		accumulator += Math.min(delta, 0.25f);
		if (accumulator >= GameConfig.STEP_TIME) {
			accumulator -= GameConfig.STEP_TIME;
			lock.lock();
			world.step(GameConfig.STEP_TIME, GameConfig.VELOCITY_ITERATIONS, GameConfig.POSITION_ITERATIONS);
			lock.unlock();
		}
	}
}
