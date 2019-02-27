package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.ShapeCache;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import graphic.GameGui;
import graphic.LoadTexture;
import logic.Buco;
import logic.Coordinate;
import utils.GameConfig;

public class GameManager extends ApplicationAdapter {
	private GameGui gameGui;
	private GameManagerDLV gameManagerDLV;

	public boolean scrivi = false;
	boolean done = true;

	static LoadTexture loadTexture;
	StartMenu startMenu;
	SpriteBatch batch;

	static Body[] shapeBodies = new Body[GameConfig.COUNT];
	static Buco[] buchi = new Buco[GameConfig.COUNT];

	long timer;

	///TEST FINALI ////

	ArrayList<Integer> coordinateX;
	ArrayList<ArrayList<Coordinate>> coordinateIniziali;
	ArrayList<ArrayList<Coordinate>> coordinateFinali;
	ArrayList<Coordinate> points;

	static boolean vincente = true;
	boolean first = false;
	boolean second = false;
	boolean aperto = false;
	int contatoreProve =0;
	int contatore = 0;
	BufferedWriter outputWriter = null;
	BufferedReader bufferedReader;

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

	/**
	 * Stores pointers to the sprites contained in {@link #sprites} that match the
	 * bodies in {@link #shapeBodies}.
	 */


	@Override
	public void create() {

		gameGui = new GameGui();
		batch = new SpriteBatch();
		viewport = new StretchViewport(200, 200, gameGui.getCamera());
		loadTexture=new LoadTexture();
		loadTexture.LoadSprite();
		startMenu=new StartMenu();
		world = new World(new Vector2(0, -120), true);
		physicsBodies = new PhysicsShapeCache("../core/assets/shapes2.xml");
		debugRenderer = new Box2DDebugRenderer();

		///TEST FINALE
		coordinateX = new ArrayList<Integer>();
		coordinateIniziali = new ArrayList<ArrayList<Coordinate>>();
		coordinateFinali = new ArrayList<ArrayList<Coordinate>>();
		gameManagerDLV = new GameManagerDLV(coordinateFinali,scrivi);

		try {
			//			leggiFile();
			//			generaBuchi();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Loads the sprites and caches them into {@link #sprites} and
	 * {@link #shapeSprites}.
	 */


	/**
	 * Populates {@link #shapeBodies}.
	 */
	private void generateShape(float x, float y) {
		shapeBodies[GameConfig.currentShapeIndex] = createBody(GameConfig.levels[GameConfig.currentLevel][GameConfig.currentShapeIndex], x, y, 0);
		GameConfig.currentShapeIndex++;
	}

	private void generateShape() {
		float x = GameConfig.mouseInWorld2D.x;
		float y = GameConfig.mouseInWorld2D.y;
		shapeBodies[GameConfig.currentShapeIndex] = createBody(GameConfig.levels[GameConfig.currentLevel][GameConfig.currentShapeIndex], x, y, 0);
		GameConfig.currentShapeIndex++;
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
		Body body;
		if(name.equals("circle"))
			body = physicsBodies.createBody(name, world, GameConfig.SCALE*GameConfig.SMULTIPLIER, GameConfig.SCALE*GameConfig.SMULTIPLIER);
		else
			body = physicsBodies.createBody(name, world, GameConfig.SCALE, GameConfig.SCALE);
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
		fixtureDef.friction = 1;
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

		if(startMenu.menu && !startMenu.lvlSelect) {
			gameGui.drawMenu(batch,loadTexture);

		}
		else if(startMenu.lvlSelect && startMenu.menu) {
			gameGui.drawlvlSelect(batch,loadTexture);
		}
		else {
			if(!GameConfig.buchi)
				generaBuchi();
			if(scrivi && !aperto) {
				aperto = true;
				try {
					System.out.println("apro file: " + GameConfig.currentLevel);
					outputWriter = new BufferedWriter(new FileWriter("../core/assets/livello"+(GameConfig.currentLevel+1)+".txt"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
				gameGui.gameScreen(batch, shapeBodies,loadTexture,buchi);

				for (int i = 0; i < GameConfig.currentShapeIndex; i++) {
					if (shapeBodies[i].getPosition().y < 40) {
						vincente = false;
						loseCheck();
					}
				}

				if(Gdx.input.isKeyPressed(Keys.R) && !first && scrivi) {
					first = true;
					System.out.println("Risolutore");
					try {
						gameManagerDLV.generaCombinazioniASP();
						gameManagerDLV.stampaCombinazioniASP();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Gdx.input.isKeyPressed(Keys.R) && !first && !scrivi) {
					first = true;
					try {
						//						generaBuchi();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				if(Gdx.input.isKeyPressed(Keys.T) && !second && scrivi) {
					second = true;
					contatore = coordinateFinali.size();
					System.out.println("contatore: " +contatore);
					points = coordinateFinali.remove(0);
					generateShape((float)points.get(0).getxIniziale(), (float)points.get(0).getyIniziale());
					generateShape((float)points.get(1).getxIniziale(), (float)points.get(1).getyIniziale());
					generateShape((float)points.get(2).getxIniziale(), (float)points.get(2).getyIniziale());
					contatoreProve++;
				}


				if(Gdx.input.isKeyPressed(Keys.T) && !second && !scrivi) {
					second = true;
					try {
						leggiFile();
						System.out.println("csss" + coordinateFinali.size());
						gameManagerDLV.calcolaAnswerSet(buchi);
						ArrayList<Coordinate> arrayList;
						if (gameManagerDLV.soluzioni.size() != 0) 
							arrayList = coordinateFinali.get(gameManagerDLV.soluzioni.remove(0));

						else
							arrayList = coordinateFinali.get(0);

						Coordinate coordinate = arrayList.get(0);
						Coordinate coordinate2 = arrayList.get(1);
						Coordinate coordinate3 = arrayList.get(2);

						generateShape((float)coordinate.getxIniziale(), (float)coordinate.getyIniziale());
						generateShape((float)coordinate2.getxIniziale(), (float)coordinate2.getyIniziale());
						generateShape((float)coordinate3.getxIniziale(), (float)coordinate3.getyIniziale());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if(GameConfig.currentShapeIndex!=loadTexture.shapeSprites.length)
					if(secondClick -startMenu.firstClick > 60)
						mouseClick();

				if (GameConfig.currentShapeIndex == loadTexture.shapeSprites.length) {

					if(done)
						timer = System.currentTimeMillis();
					done = false;
					try {
						winCheck();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			stepWorld();
		}

	}

	public int controlloFigura(int identificativo) {
		// 0 cerchio, 1 = rettangolo, 2 = cerchio 3 = triangolo
		if(GameConfig.levels[GameConfig.currentLevel][identificativo].equals("square"))
			return 0;
		else if(GameConfig.levels[GameConfig.currentLevel][identificativo].equals("rectangle"))
			return 1;
		else if(GameConfig.levels[GameConfig.currentLevel][identificativo].equals("circle"))
			return 2;
		return 3;
	}

	public static void restartLevel() {
		for(int i=0;i<GameConfig.currentShapeIndex;i++){
			world.destroyBody(shapeBodies[i]);
		}
		GameConfig.currentShapeIndex=0;
		vincente = true;
	}


	public void scriviFile(Coordinate figura1, Coordinate figura2, Coordinate figura3) throws IOException {
		outputWriter.write(figura1.getxIniziale()+" " + figura1.getyIniziale() + " " + figura1.getxFinale() + " " + figura1.getyFinale() + " " +
				figura2.getxIniziale()+" " + figura2.getyIniziale() + " " + figura2.getxFinale() + " " + figura2.getyFinale() + " " + 
				figura3.getxIniziale()+" " + figura3.getyIniziale() + " " + figura3.getxFinale() + " " + figura3.getyFinale());
		outputWriter.newLine();
	}

	public void leggiFile() {
		System.out.println("leggi");
		try {
			bufferedReader = new BufferedReader(new FileReader("../core/assets/livello"+(GameConfig.currentLevel+1)+".txt"));
			String line = bufferedReader.readLine();
			while(line != null) {
				line = line.replaceAll("[^-?0-9]+"," ");
				List<String> asList = Arrays.asList(line.trim().split(" "));
				Coordinate figura1 = new Coordinate(Integer.parseInt(asList.get(0)), Integer.parseInt(asList.get(1)), Integer.parseInt(asList.get(2)), Integer.parseInt(asList.get(3)), controlloFigura(0));
				Coordinate figura2 = new Coordinate(Integer.parseInt(asList.get(4)), Integer.parseInt(asList.get(5)), Integer.parseInt(asList.get(6)), Integer.parseInt(asList.get(7)),controlloFigura(1));
				Coordinate figura3 = new Coordinate(Integer.parseInt(asList.get(8)), Integer.parseInt(asList.get(9)), Integer.parseInt(asList.get(10)), Integer.parseInt(asList.get(11)), controlloFigura(2));
				ArrayList<Coordinate> finali = new ArrayList<Coordinate>();
				finali.add(figura1);
				finali.add(figura2);
				finali.add(figura3);
				coordinateFinali.add(finali);
				line = bufferedReader.readLine();
			}
			for(int i = 0; i < coordinateIniziali.size(); i++) {
				System.out.println(coordinateIniziali.get(i));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void winCheck() throws IOException {
		if(System.currentTimeMillis() - timer >= 8000 && scrivi) {
			System.out.println("coordinate iniziali: " + coordinateIniziali.size());
			if(vincente) {
				Coordinate figura1 = new Coordinate(points.get(0).getxIniziale(), points.get(0).getyIniziale(), (int)shapeBodies[0].getPosition().x, (int)shapeBodies[0].getPosition().y, controlloFigura(0));
				Coordinate figura2 = new Coordinate(points.get(1).getxIniziale(), points.get(1).getyIniziale(), (int)shapeBodies[1].getPosition().x, (int)shapeBodies[1].getPosition().y,controlloFigura(1));
				Coordinate figura3 = new Coordinate(points.get(2).getxIniziale(), points.get(2).getyIniziale(), (int)shapeBodies[2].getPosition().x, (int)shapeBodies[2].getPosition().y, controlloFigura(2));
				ArrayList<Coordinate> finali = new ArrayList<Coordinate>();
				finali.add(figura1);
				finali.add(figura2);
				finali.add(figura3);
				coordinateIniziali.add(finali);

				if(scrivi) {
					scriviFile(figura1,figura2, figura3);
				}

			}

			if(contatoreProve < contatore) {
				contatoreProve++;
				done = true;
				restartLevel();
				second = false;
				System.out.println("restart");
				points = coordinateFinali.remove(0);
				generateShape((float)points.get(0).getxIniziale(), (float)points.get(0).getyIniziale());
				generateShape((float)points.get(1).getxIniziale(),(float) points.get(1).getyIniziale());
				generateShape((float)points.get(2).getxIniziale(),(float) points.get(2).getyIniziale());
				return;

			}
			else {
				Gdx.app.exit();
			}
		}
		else if(System.currentTimeMillis() - timer >= 8000) {
			if(vincente && gameManagerDLV.soluzioni.size() != 0) {
				System.out.println("Hai vinto!");
				restartLevel();
				ArrayList<Coordinate> arrayList = coordinateFinali.get(gameManagerDLV.soluzioni.remove(0));
				Coordinate coordinate = arrayList.get(0);
				Coordinate coordinate2 = arrayList.get(1);
				Coordinate coordinate3 = arrayList.get(2);
				generateShape((float)coordinate.getxIniziale(), (float)coordinate.getyIniziale());

				generateShape((float)coordinate2.getxIniziale(), (float)coordinate2.getyIniziale());

				generateShape((float)coordinate3.getxIniziale(), (float)coordinate3.getyIniziale());
				timer = System.currentTimeMillis();
			}

			else if(GameConfig.currentLevel!=GameConfig.levelnum-1) 
				//				Gdx.app.exit();
				nextLevel();
			else {
				System.out.println("Gioco Finito");
				Gdx.app.exit();
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
		if(contatoreProve < contatore && scrivi) {
			contatoreProve++;
			done = true;
			restartLevel();
			second = false;
			System.out.println("restart");
			points = coordinateFinali.remove(0);
			generateShape((float)points.get(0).getxIniziale(), (float)points.get(0).getyIniziale());
			generateShape((float)points.get(1).getxIniziale(),(float) points.get(1).getyIniziale());
			generateShape((float)points.get(2).getxIniziale(),(float) points.get(2).getyIniziale());
			//		Gdx.app.exit();
		}
	}

	@Override
	public void dispose() {
		System.out.println("Close");
		loadTexture.textureAtlas.dispose();

		loadTexture.sprites.clear();

		world.dispose();

		debugRenderer.dispose();

		if(scrivi) {
			try {
				outputWriter.flush();
				outputWriter.close();  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}

	}
	public void nextLevel() {
		done = true;
		first=false;
		second = false;
		GameConfig.currentLevel++;
		generaBuchi();
		GameConfig.currentShapeIndex=0;
		for(Body bod: shapeBodies){
			world.destroyBody(bod);
		}
		loadTexture.LoadSprite();
		coordinateFinali.clear();
	}

	public int calcolaUpperY() {
		if(GameConfig.currentLevel == 0)
			return 135;
		else if(GameConfig.currentLevel == 1)
			return 60;
		else if(GameConfig.currentLevel == 2)
			return 56;
		else if(GameConfig.currentLevel == 3)
			return 56;
		else
			return 56;
	}


	public void generaBuchi() {
		GameConfig.buchi = true;
		System.out.println("genero " + GameConfig.currentLevel);
		//		buchi[0] = new Buco(45,55,0);
		//		buchi[1] = new Buco(85,55,0);
		//		buchi[2] = new Buco(125,55,0);
		Random random = new Random();
		for(int i=0;i<buchi.length;i++)
			buchi[i]=new Buco(random.nextInt(135 - 45)+45,random.nextInt(calcolaUpperY() - 55)+ 55, 0);
	}
	//	125 135 125 51 115 55 113 51 75 95 74 51
	//	
	//	public void generaBuchi() throws Exception {
	//		gameManagerDLV.generaBuchiASP();
	//		buchi[0] = new Buco(115,51,0,0);
	//		buchi[1] = new Buco(125,90,0,0);
	//		buchi[2] = new Buco(130,130,0,0);
	//
	//	}

	public static void changeLevel(int lvl) {
		for(int i=0;i<GameConfig.currentShapeIndex;i++){
			world.destroyBody(shapeBodies[i]);
		}
		GameConfig.currentLevel=lvl;
		GameConfig.currentShapeIndex=0;
		loadTexture.LoadSprite();
	}
}

