package game;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.TimerTask;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.utils.ShapeCache;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import graphic.GameGui;
import graphic.LoadTexture;
import utils.GameConfig;

public class GameManager extends ApplicationAdapter {
	private GameGui gameGui;
	private GameManagerDLV gameManagerDLV;
	
	static LoadTexture loadTexture;
	StartMenu startMenu;
	
	boolean done = true;

	long timer;
	
	SpriteBatch batch;

	/**
	 * Used to cache our sprites so we only have to load them in when the game first
	 * starts. For a production-worthy game I would recommend using a
	 * {@link com.badlogic.gdx.assets.AssetManager} instead of this.
	 */

	String[] shapeNames = new String[] { "circle", "exagon", "rectangle", "triangle", "square", "triangle2" };

	/**
	 * Provides scaling while maintaining an aspect ratio when the screen is
	 * resized.
	 */
	Viewport viewport;

	/**
	 * Our Box2D physics world.
	 */
	static World world;

	/**
	 * Used to draw the collision polygons to the screen for debugging purposes.
	 * {@link #render()}.
	 */
	Box2DDebugRenderer debugRenderer;

	/**
	 * Parses XML data exported from PhysicsEditor into Box2D bodies.
	 */
	PhysicsShapeCache physicsBodies;

	/**
	 * Used to fix our physics step time. You can read more on what that means in
	 * this article: http://gafferongames.com/game-physics/fix-your-timestep/
	 */
	float accumulator = 0;

	/**
	 * A physics body for the ground. This is a static body that does not move..
	 */
	Body ground;

	/**
	 * Stores the physics bodies of the shapes that fall from the sky.
	 */
	static Body[] shapeBodies = new Body[GameConfig.COUNT];

	/**
	 * Stores pointers to the sprites contained in {@link #sprites} that match the
	 * bodies in {@link #shapeBodies}.
	 */
	
	//	Animation anim = tools.GifDecoder.loadGIFAnimation(0, Gdx.files.internal("/Crazy Stackers-core/assets/Countdown.gif").read());
	
	@Override
	public void create() {

		gameGui = new GameGui();

		Box2D.init();

		batch = new SpriteBatch();

		viewport = new StretchViewport(200, 200, gameGui.getCamera());
		
		loadTexture=new LoadTexture();
		loadTexture.LoadSprite();
		startMenu=new StartMenu();

		world = new World(new Vector2(0, -120), true);

		physicsBodies = new PhysicsShapeCache("shapes2.xml");

		debugRenderer = new Box2DDebugRenderer();
		
		gameManagerDLV = new GameManagerDLV();

	}

	/**
	 * Loads the sprites and caches them into {@link #sprites} and
	 * {@link #shapeSprites}.
	 */
	

	/**
	 * Populates {@link #shapeBodies}.
	 */
	private void generateShape() {

		//		Random random = new Random();
		//			String name = shapeNames[random.nextInt(shapeNames.length)];
		//		String name = levels[currentLevel][currentShapeIndex];

		float x = GameConfig.mouseInWorld2D.x;
		float y = GameConfig.mouseInWorld2D.y;

		shapeBodies[GameConfig.currentShapeIndex] = createBody(GameConfig.levels[GameConfig.currentLevel][GameConfig.currentShapeIndex], x, y, 0);
		GameConfig.currentShapeIndex++;
		System.out.println(x+" "+y);
	}

	/**
	 * Uses {@link ShapeCache} to parse a body described in
	 * android/assets/physics.xml into a Box2D {@link Body}.
	 *
	 * @param name     The name of the body exactly as it appears in the XML.
	 * @param x        The body's initial X position in meters.
	 * @param y        The body's initial Y position in meters.
	 * @param rotation The body's initial rotation in radians.
	 * @return A Box2D {@link Body}.
	 */
	private Body createBody(String name, float x, float y, float rotation) {
		Body body = physicsBodies.createBody(name, world, GameConfig.SCALE, GameConfig.SCALE);
		body.setTransform(x, y, rotation);

		return body;
	}

	/**
	 * This is called when the application is resized, and can happen at any time,
	 * but will never be called before {@link #create()}.
	 *
	 * @param width  The screen's new width in pixels.
	 * @param height The screen's new height in pixels.
	 */
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		System.out.println(width +" "+ height);
		batch.setProjectionMatrix(gameGui.getCamera().combined);

		createGround();

		//		gameGui.getCamera().viewportWidth = 1800; 
		//		gameGui.getCamera().viewportHeight = 900; 
		gameGui.getCamera().update();
	}

	/**
	 * Creates the static ground {@link Body}. Without this the Shapes would
	 * continue to fall indefinitely.
	 */
	private void createGround() {
		if (ground != null)
			world.destroyBody(ground);

		BodyDef bodyDef = new BodyDef();

		bodyDef.type = BodyDef.BodyType.StaticBody;

		FixtureDef fixtureDef = new FixtureDef();

		fixtureDef.friction = 5;

		PolygonShape shape = new PolygonShape();

		shape.setAsBox(gameGui.getCamera().viewportWidth / 4, 1);

		fixtureDef.shape = shape;

		ground = world.createBody(bodyDef);
		ground.createFixture(fixtureDef);

		ground.setTransform(gameGui.getCamera().viewportWidth / 2, 50, 0);

		shape.dispose();
	}

	@Override
	public void render() {
		long secondClick = System.currentTimeMillis();
		if(startMenu.menu) {
			gameGui.drawMenu(batch,loadTexture);

		}
		else {
			if(Gdx.input.isKeyPressed(Keys.P)) {
				startMenu.pausedMenu = true;
			}
			if(Gdx.input.isKeyPressed(Keys.C)) {
				startMenu.pausedMenu = false;
			}
			if(startMenu.pausedMenu) {
				
				gameGui.drawPausedMenu(batch, loadTexture);
			}

			if(!startMenu.pausedMenu) {

				gameGui.gameScreen(batch, shapeBodies,loadTexture);
				
				for (int i = 0; i < GameConfig.currentShapeIndex; i++) {
						if (shapeBodies[i].getPosition().y < 0)
							loseCheck();
					}
					// Shape Debug outline
				debugRenderer.render(world, gameGui.getCamera().combined);

				if(GameConfig.currentShapeIndex!=loadTexture.shapeSprites.length)
					if(secondClick -startMenu.firstClick > 60	)
						mouseClick();

				if (GameConfig.currentShapeIndex == loadTexture.shapeSprites.length) {

					if(done)
						timer = System.currentTimeMillis();
					done = false;
					winCheck();
				}
			}
			stepWorld();
		}

	}



	public static void restartLevel() {
		for(int i=0;i<GameConfig.currentShapeIndex;i++){
			world.destroyBody(shapeBodies[i]);
		}
		GameConfig.currentShapeIndex=0;
		loadTexture.LoadSprite();
	}

	boolean run=true;
	LocalTime time;
	public void winCheck() {
		//				time=java.time.LocalTime.now().plusSeconds(5);
		//				
		//				System.out.println(time);
		//				System.out.println(java.time.LocalTime.now()+" Corrente");
		//				while(java.time.LocalTime.now()!=time) {
		//					System.out.println("In attesa");
		//				}
		//				System.out.println("132");


		System.out.println("jumbo");
		if(System.currentTimeMillis() - timer >= 5000) {
			if(GameConfig.currentLevel!=GameConfig.levelnum-1) 
				nextLevel();
			else {
				System.out.println("Gioco Finito");
				//TODO Schermata finale
			}	
		}
	}

	/**
	 * Steps the physics simulation. This is called every render frame.
	 */
	private void stepWorld() {
		float delta = Gdx.graphics.getDeltaTime();

		accumulator += Math.min(delta, 0.25f);

		if (accumulator >= GameConfig.STEP_TIME) {
			accumulator -= GameConfig.STEP_TIME;

			world.step(GameConfig.STEP_TIME, GameConfig.VELOCITY_ITERATIONS, GameConfig.POSITION_ITERATIONS);
		}
	}


	void mouseClick() {
		if (Gdx.input.justTouched()) {
			generateShape();
		}
	}

	void loseCheck() {
		System.out.println("Hai Perso Pezzo Caduto");
		Gdx.app.exit();
	}

	@Override
	public void dispose() {
		loadTexture.textureAtlas.dispose();

		loadTexture.sprites.clear();

		world.dispose();

		debugRenderer.dispose();

	}
	public void nextLevel() {
		done = true;
		GameConfig.currentLevel++;
		GameConfig.currentShapeIndex=0;
		for(Body bod: shapeBodies){
			world.destroyBody(bod);
		}
		loadTexture.LoadSprite();
	}
}
