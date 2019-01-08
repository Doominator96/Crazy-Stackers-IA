package game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

class Button {
	public String name;
	public Texture texture;
	public int x;
	public int y;
	public int width;
	public int height;
	public boolean selected;

	public Button(String name, Texture texture, int x, int y, int width, int height) {
		this.name = name;
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		selected = false;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}



}

public class GameManager extends ApplicationAdapter {
	long firstClick = 0;
	public boolean menu;
	public boolean pausedMenu;

	Button newGameButton;
	Button exitButton;
	Button startMenu;
	Button resume;
	Button restart;

	private GameGui gameGui;
	private final Vector2 mouseInWorld2D = new Vector2();
	private final Vector3 mouseInWorld3D = new Vector3();
	LoadTexture loadTexture;
	private int levelnum=2;

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
	static final float STEP_TIME = 1f / 60f;

	/**
	 * Velocity iterations will improve the stability of the physics simulation. A
	 * higher value will provide greater precision for collision detection, at the
	 * cost of consuming more of the CPU.
	 */
	static final int VELOCITY_ITERATIONS = 6;

	int currentShapeIndex = 0;
	/**
	 * This affects the way bodies react to collisions. A higher value improves the
	 * simulations overlap resolution.
	 * <p/>
	 * I recommend reading this article on the anatomy of a collision for a clearer
	 * understanding of both velocity and position iterations:
	 * http://www.iforce2d.net/b2dtut/collision-anatomy
	 */
	static final int POSITION_ITERATIONS = 4;

	/**
	 * This is a scalar used to make our sprites fit within the physics simulation.
	 * Without it the sprites would be too big to be drawn on the screen.
	 */
	static final float SCALE = 0.25f;

	/**
	 * Adjust this value to change the amount of Shapes.
	 */
	static final int COUNT = 5;

	/**
	 * Used to convert our sprite sheet found at android/assets/sprites.png and
	 */
	TextureAtlas textureAtlas;

	Texture background;
	Texture base;
	Texture container;

	Texture newGame;
	Texture exit;
	Texture resumeT;
	Texture restartT;
	Texture startMenuT;

	Texture paused;

	SpriteBatch batch;

	/**
	 * Used to cache our sprites so we only have to load them in when the game first
	 * starts. For a production-worthy game I would recommend using a
	 * {@link com.badlogic.gdx.assets.AssetManager} instead of this.
	 */
	final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

	String[] shapeNames = new String[] { "circle", "exagon", "rectangle", "triangle", "square", "triangle2" };

	/**
	 * Provides scaling while maintaining an aspect ratio when the screen is
	 * resized.
	 */
	Viewport viewport;

	/**
	 * Our Box2D physics world.
	 */
	World world;

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
	Body[] shapeBodies = new Body[COUNT];

	/**
	 * Stores pointers to the sprites contained in {@link #sprites} that match the
	 * bodies in {@link #shapeBodies}.
	 */
	Sprite[] shapeSprites = new Sprite[COUNT];
	String[][] levels = new String[][] {{ "square", "square", "square", "triangle", "triangle" },{ "circle", "circle", "rectangle", "square", "triangle" }};
	//	Animation anim = tools.GifDecoder.loadGIFAnimation(0, Gdx.files.internal("/Crazy Stackers-core/assets/Countdown.gif").read());
	int currentLevel=0;
	@Override
	public void create() {
		menu = true;

		gameGui = new GameGui();

		Box2D.init();

		batch = new SpriteBatch();

		viewport = new StretchViewport(200, 200, gameGui.getCamera());

		textureAtlas = new TextureAtlas("shapes.txt");

		loadSprites();

		world = new World(new Vector2(0, -120), true);

		physicsBodies = new PhysicsShapeCache("shapes2.xml");

		debugRenderer = new Box2DDebugRenderer();


	}

	/**
	 * Loads the sprites and caches them into {@link #sprites} and
	 * {@link #shapeSprites}.
	 */
	private void loadSprites() {
		Array<AtlasRegion> regions = textureAtlas.getRegions();

		for (AtlasRegion region : regions) {
			Sprite sprite = textureAtlas.createSprite(region.name);

			float width = sprite.getWidth() * SCALE;
			float height = sprite.getHeight() * SCALE;

			sprite.setSize(width, height);
			sprite.setOrigin(0, 0);

			sprites.put(region.name, sprite);
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

		newGameButton = new Button("newgame", newGame, 75, 100, 20, 16);
		exitButton = new Button("exit", exit, 105, 100, 20, 16);
		resume = new Button("resume", resumeT, 50, 90, 20, 16);
		restart = new Button("restart", restartT, 90, 90, 20, 16);
		startMenu = new Button("startMenu", startMenuT, 130, 90, 20, 16);

		for (int i = 0; i < levels[currentLevel].length; i++) {
			shapeSprites[i] = sprites.get(levels[currentLevel][i]);
		}
	}

	/**
	 * Populates {@link #shapeBodies}.
	 */
	private void generateShape() {

		//		Random random = new Random();
		//			String name = shapeNames[random.nextInt(shapeNames.length)];
		//		String name = levels[currentLevel][currentShapeIndex];

		float x = mouseInWorld2D.x;
		float y = mouseInWorld2D.y;

		shapeBodies[currentShapeIndex] = createBody(levels[currentLevel][currentShapeIndex], x, y, 0);
		currentShapeIndex++;
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
		Body body = physicsBodies.createBody(name, world, SCALE, SCALE);
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
		if(menu) {
			mouseInWorld3D.x = Gdx.input.getX();
			mouseInWorld3D.y = Gdx.input.getY();
			mouseInWorld3D.z = 0;
			gameGui.getCamera().unproject(mouseInWorld3D);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			batch.begin();

			batch.draw(background, 0, 0, 200, 200);

			mouseClickMenu(mouseInWorld3D.x, mouseInWorld3D.y);
			batch.draw(newGameButton.texture, newGameButton.x, newGameButton.y, newGameButton.width, newGameButton.height);
			batch.draw(exitButton.texture, exitButton.x, exitButton.y, exitButton.width, exitButton.height);

			batch.end();

		}



		else {

			if(Gdx.input.isKeyPressed(Keys.P)) {
				pausedMenu = true;
			}
			if(Gdx.input.isKeyPressed(Keys.C)) {
				pausedMenu = false;
			}
			if(pausedMenu) {
				mouseInWorld3D.x = Gdx.input.getX();
				mouseInWorld3D.y = Gdx.input.getY();
				mouseInWorld3D.z = 0;
				gameGui.getCamera().unproject(mouseInWorld3D);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.begin();



				mouseClickMenu(mouseInWorld3D.x, mouseInWorld3D.y);

				batch.draw(background, 0, 0, 200, 200);
				batch.draw(base, 50, 42, 105, 10);
				batch.draw(paused, 40, 80, 120, 60);
				batch.draw(resume.texture, resume.x, resume.y, resume.width, resume.height);
				batch.draw(restart.texture, restart.x, restart.y, restart.width, restart.height);
				batch.draw(startMenu.texture, startMenu.x, startMenu.y, startMenu.width, startMenu.height);

				batch.end();
			}

			if(!pausedMenu) {
				mouseInWorld3D.x = Gdx.input.getX();
				mouseInWorld3D.y = Gdx.input.getY();
				mouseInWorld3D.z = 0;
				gameGui.getCamera().unproject(mouseInWorld3D);
				mouseInWorld2D.x = mouseInWorld3D.x;
				mouseInWorld2D.y = mouseInWorld3D.y;

				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

				// Step the physics world.
				stepWorld();

				batch.begin();

				batch.draw(background, 0, 0, 200, 200);
				batch.draw(base, 50, 42, 105, 10);


				//		batch.draw((Texture) anim.getKeyFrame(Gdx.graphics.getDeltaTime(), true),0,0);
				// Disegna la Forma Corrente sul puntatore
				if (currentShapeIndex < shapeSprites.length) {
					drawSprite(shapeSprites[currentShapeIndex], mouseInWorld2D.x, mouseInWorld2D.y, 0);
				}
				// draw the shapes on the screen
				for (int i = 0; i < currentShapeIndex; i++)
					if (shapeBodies.length != 0) {
						drawSprite(shapeSprites[i], shapeBodies[i].getPosition().x, shapeBodies[i].getPosition().y,
								(float) Math.toDegrees(shapeBodies[i].getAngle()));

						if (shapeBodies[i].getPosition().y < 0)
							loseCheck();
					}
				batch.draw(container, 2, 2, 150, 30);
				batch.draw(container, 157, 2, 40, 30);

				// Draw shapes in the container
				for (int i = 0; i < shapeSprites.length; i++)
					drawSpriteResized(shapeSprites[i], 165 - (i) * 40+(currentShapeIndex*40), 4, 0);

				batch.end();

				// Shape Debug outline
				debugRenderer.render(world, gameGui.getCamera().combined);

				if(currentShapeIndex!=shapeSprites.length)
					if(secondClick -firstClick > 60	)
					mouseClick();

				if (currentShapeIndex == shapeSprites.length)
					winCheck();
			}
		}

	}

	private void mouseClickMenu(float mouseX, float mouseY) {
		//System.out.println(mouseX + "   " + mouseY);
		long secondClick = 0;
		if(menu) {
			if((mouseX >= newGameButton.x && mouseX <= (newGameButton.width + newGameButton.x)) && (mouseY >= newGameButton.y && mouseY <= (newGameButton.height +newGameButton.y))) {
				newGameButton.setX(72);
				newGameButton.setY(98);
				newGameButton.setWidth(26);
				newGameButton.setHeight(20);
				newGameButton.setSelected(true);
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					secondClick = System.currentTimeMillis();
					if(secondClick - firstClick > 60) 
						this.menu = false;
				}
			}
			else {
				newGameButton.setX(75);
				newGameButton.setY(100);
				newGameButton.setWidth(20);
				newGameButton.setHeight(16);
				newGameButton.setSelected(false);
			}

			if((mouseX >= exitButton.x && mouseX <= (exitButton.width + exitButton.x)) && (mouseY >= exitButton.y && mouseY <= (exitButton.height +exitButton.y))) {
				exitButton.setX(102);
				exitButton.setY(98);
				exitButton.setWidth(26);
				exitButton.setHeight(20);
				exitButton.setSelected(true);
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					secondClick = System.currentTimeMillis();
					if(secondClick - firstClick > 60) 
						Gdx.app.exit();
				}
			}
			else {
				exitButton.setX(105);
				exitButton.setY(100);
				exitButton.setWidth(20);
				exitButton.setHeight(16);
				exitButton.setSelected(false);
			}
		}

		if(pausedMenu) {
			if((mouseX >= resume.x && mouseX <= (resume.width + resume.x)) && (mouseY >= resume.y && mouseY <= (resume.height +resume.y))) {
				resume.setX(48);
				resume.setY(88);
				resume.setWidth(26);
				resume.setHeight(20);
				resume.setSelected(true);
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					secondClick = System.currentTimeMillis();
					if(secondClick - firstClick > 60) 
						pausedMenu = false;
					
				}
			}
			else {
				resume.setX(50);
				resume.setY(90);
				resume.setWidth(20);
				resume.setHeight(16);
				resume.setSelected(false);
			}

			if((mouseX >= restart.x && mouseX <= (restart.width + restart.x)) && (mouseY >= restart.y && mouseY <= (restart.height +restart.y))) {
				restart.setX(88);
				restart.setY(88);
				restart.setWidth(26);
				restart.setHeight(20);
				restart.setSelected(true);
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					secondClick = System.currentTimeMillis();
					//implementare restart livello
					if(secondClick - firstClick > 60) 
						Gdx.app.exit();
				}
			}
			else {
				restart.setX(90);
				restart.setY(90);
				restart.setWidth(20);
				restart.setHeight(16);
				restart.setSelected(false);
			}

			if((mouseX >= startMenu.x && mouseX <= (startMenu.width + startMenu.x)) && (mouseY >= startMenu.y && mouseY <= (startMenu.height +startMenu.y))) {
				startMenu.setX(128);
				startMenu.setY(88);
				startMenu.setWidth(26);
				startMenu.setHeight(20);
				startMenu.setSelected(true);
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					secondClick = System.currentTimeMillis();
					if(secondClick - firstClick > 60) {
						pausedMenu = false;
						menu = true;
					}
				}

			}
			else {
				startMenu.setX(130);
				startMenu.setY(90);
				startMenu.setWidth(20);
				startMenu.setHeight(16);
				startMenu.setSelected(false);
			}
		}
		firstClick = secondClick;
		System.out.println(firstClick +"    " + secondClick);
	}

	private TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
			System.out.println("Hai Vinto");
			nextLevel();
		}
	};
	boolean run=true;
	LocalTime time;
	public void winCheck() {
		//		time=java.time.LocalTime.now().plusSeconds(5);
		//		
		//		System.out.println(time);
		//		System.out.println(java.time.LocalTime.now()+" Corrente");
		//		while(java.time.LocalTime.now()!=time) {
		//			System.out.println("In attesa");
		//		}
		//		System.out.println("132");

		if(currentLevel!=levelnum-1)
			nextLevel();
		else {
			System.out.println("Gioco Finito");
			//TODO Schermata finale
		}

		//		final Timer timer = new Timer();
		//		timer.schedule(new TimerTask() {
		//
		//			@Override
		//			public void run() {
		//				if(run) {
		//				System.out.println("Hai Vinto");
		//				nextLevel();
		//				}
		//				else {
		////				Gdx.app.exit();
		//					this.cancel();
		//					timer.purge();
		//				}
		//				//TODO nextLevel
		//			}
		//		}, 5000);
	}

	/**
	 * Steps the physics simulation. This is called every render frame.
	 */
	private void stepWorld() {
		float delta = Gdx.graphics.getDeltaTime();

		accumulator += Math.min(delta, 0.25f);

		if (accumulator >= STEP_TIME) {
			accumulator -= STEP_TIME;

			world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		}
	}

	public void drawSprite(Sprite sprite, float x, float y, float degrees) {
		sprite.setPosition(x, y);
		sprite.setScale(1, 1);
		sprite.setRotation(degrees);

		sprite.draw(batch);
	}

	public void drawSpriteResized(Sprite sprite, float x, float y, float degrees) {
		sprite.setPosition(x, y);
		sprite.setScale(0.7f, 0.7f);
		sprite.setRotation(degrees);

		sprite.draw(batch);
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
		textureAtlas.dispose();

		sprites.clear();

		world.dispose();

		debugRenderer.dispose();

	}
	public void nextLevel() {
		currentLevel++;
		currentShapeIndex=0;
		for(Body bod: shapeBodies){
			world.destroyBody(bod);
		}
		loadSprites();
	}
}
