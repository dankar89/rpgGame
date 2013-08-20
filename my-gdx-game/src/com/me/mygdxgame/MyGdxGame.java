package com.me.mygdxgame;


import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class MyGdxGame implements ApplicationListener {
	private OrthographicCamera camera;
	private Player3 playerTest;
	private SpriteBatch worldBatch;
	private SpriteBatch hudBatch;
	public static AssetManager assetManager;
	private BitmapFont font;
	private Vector2 maxCamPos, minCamPos;
	Vector3 screenCamPos;
	private Rectangle worldBounds;
	private Rectangle camRect;
	private int w, h;
	private Player3 player;
	private TileMapManager tileMapManager;
	private Physics physics;
	private ShapeRenderer shapeRenderer;
	private boolean debug = true;
	private Input prevInput;
	
	
	//TEST
	Box2DDebugRenderer debugRenderer;
//	private BodyDef treeBodyDef;
//	private Body treeBody;
//	private PolygonShape treeShape;
	//
	
	
//	private ArrayList<Rectangle> adjacentRects = new ArrayList<Rectangle>();

	@Override
	public void create() {				
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		hudBatch = new SpriteBatch();
		
		shapeRenderer = new ShapeRenderer();

		prevInput = Gdx.input;
		
		assetManager = new AssetManager();
		loadResources();

		font = new BitmapFont();
		font.scale(0.1f);
		font.setColor(Color.BLACK);

		tileMapManager = new TileMapManager();
		
		worldBatch = tileMapManager.getMapRenderer().getSpriteBatch();
		
		
		minCamPos = new Vector2(w / (tileMapManager.getTileSize() * 2), h / (tileMapManager.getTileSize() * 2));
		maxCamPos = new Vector2(tileMapManager.getMapWidth() - minCamPos.x, tileMapManager.getMapHeight() - minCamPos.y);
		
		//remove maxCamPos and minCamPos!!!
		worldBounds = new Rectangle(minCamPos.x, minCamPos.y, maxCamPos.x - minCamPos.x, maxCamPos.y - minCamPos.y);
				
//		camera = new OrthographicCamera(
//				tileMapManager.getMapWidth() * tileMapManager.getTileSize(),
//				tileMapManager.getMapHeight() * tileMapManager.getTileSize());
		camera = new OrthographicCamera();
		
		camera.setToOrtho(false, w / Constants.TILE_SIZE, h / Constants.TILE_SIZE);		
//		camera.setToOrtho(false, (w/h) * 32, 20);
		
		camera.update();	
		
//		screenCamPos = camera.position;
//		camera.project(screenCamPos);
		camRect = new Rectangle((camera.position.x) - ((w / 32) / 2), (camera.position.y) - ((h / 32) / 2), w, h);
		System.out.println(camRect);
		//TEST
		physics = new Physics();

		//
		
		Vector2 startpos = new Vector2(tileMapManager.getPlayerStartPos().x, tileMapManager.getPlayerStartPos().y);
//		Vector2 player1pos = new Vector2(startpos.x - 100, startpos.y - 100);
//		player = new Player2(player1pos, physics.getWorld());
		
		ArrayList<CollisionData> colData = tileMapManager.getBox2dMapObjects("shape",
				Constants.BACKGROUND_LAYER_INDEX,
//				5,
				(int)camRect.x,
				(int)camRect.y,
				(int)camRect.width / tileMapManager.getTileSize(),
				(int)camRect.height / tileMapManager.getTileSize());
		
		if(!colData.isEmpty())
		{ 
			for (CollisionData cd : colData) {
				System.out.println(cd);
			}
			
			physics.createMapObjects(colData);
		}
		else
		{
			System.out.println("no collision data found!");
		}
		
		player = new Player3(physics.getWorld(), startpos);
//		playerTest = new Player3(physics.getWorld(), new Vector2((w / 3) / Constants.PIXELS_PER_METER, (h/ 3) / Constants.PIXELS_PER_METER));
//		playerTest.setPos(new Vector2((w / 2) / Constants.PIXELS_PER_METER, (h/ 2) / Constants.PIXELS_PER_METER));
//		playerTest.setPos(startpos.x + 10, startpos.y + 10);
	}

	@Override
	public void dispose() {
		worldBatch.dispose();
		tileMapManager.dispose();
		assetManager.dispose();		
		shapeRenderer.dispose();
		physics.destroyBodies();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if (assetManager.update()) {
			 handleInput();	

//			player.update(worldBounds, Gdx.graphics.getDeltaTime());
			player.update();
			
			setCameraPos(player.getBody().getPosition().x, player.getBody().getPosition().y);					

			camera.update();							
			
			tileMapManager.renderBgLayers(camera);

			worldBatch.begin();			
			player.draw(worldBatch);
//			playerTest.draw(worldBatch);
			worldBatch.end();	
			
						
			tileMapManager.renderFgLayers(camera);				
			
			//Debug stuff
			if(debug)
			{							
				//draw camera bounds
				shapeRenderer.setProjectionMatrix(camera.combined);
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(Color.RED);
				shapeRenderer.rect(camera.position.x - 0.1f, camera.position.y - 0.1f, 0.2f, 0.2f);
				shapeRenderer.end();
				
				shapeRenderer.begin(ShapeType.Line);
//				shapeRenderer.setColor(new Color(255,0,0,100));								
//				
				//draw player bounding rect
//				shapeRenderer.rect(
//						player.getWorldPosition().x, 
//						player.getWorldPosition().y, 
//						player.getSprite().getBoundingRectangle().width / tileMapManager.getTileSize(), 
//						player.getSprite().getBoundingRectangle().height / tileMapManager.getTileSize());			
				
//				//draw player bounding rect
//				shapeRenderer.rect(
//						player.getBody().getPosition().x - ((player.getSprite().getWidth() / tileMapManager.getTileSize()) / 2), 
//						player.getBody().getPosition().y - ((player.getSprite().getHeight() / tileMapManager.getTileSize()) / 2),
//						player.getSprite().getWidth() / tileMapManager.getTileSize(), 
//						player.getSprite().getHeight() / tileMapManager.getTileSize());								
			
			
				shapeRenderer.end();
				
				hudBatch.begin();
				font.draw(hudBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 40);
				hudBatch.end();
				
//				physics.renderDebug(camera.combined.cpy().scl(1f/Constants.WORLD_TO_BOX));
				physics.renderDebug(camera.combined);
			}
			
			
			physics.update(1/60f, camera);			
		}
	}

	@Override
	public void resize(int width, int height) {
        camera.setToOrtho(false, width / Constants.TILE_SIZE, height / Constants.TILE_SIZE);
        camera.update();

        worldBatch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	private void handleInput() {	
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
			Gdx.app.exit();
		
		if (Gdx.input.isKeyPressed(Keys.F1) && prevInput.isKeyPressed(Keys.F1)) {
			debug = !debug;
		}
		
		if (Gdx.input.justTouched()) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println(camRect);
//				if(worldManager.getBodies().hasNext())
//					worldManager.destroyBody(worldManager.getBodies().next());
			}
		}
		
		prevInput = Gdx.input;
	}

	private void loadResources() {
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(
				new InternalFileHandleResolver()));

		// queue stuff for loading
		assetManager.load("maps/map2.tmx", TiledMap.class);

		// player textures
		assetManager.load(Player.TEST_TEXTURE_PATH, Texture.class);
		assetManager.load(Player.TEXTURE_PATH, Texture.class);
		assetManager.load(Player.PACKFILE_PATH, TextureAtlas.class);

		// do the actual loading
		assetManager.finishLoading();
	}

	private void setCameraPos(float x, float y) {	
		if (x < minCamPos.x)
			camera.position.x = minCamPos.x;
		else if (x > maxCamPos.x)
			camera.position.x = maxCamPos.x;
		else
			camera.position.x = x;
		
		if (y < minCamPos.y)
			camera.position.y = minCamPos.y;
		else if (y > maxCamPos.y)
			camera.position.y = maxCamPos.y;
		else
			camera.position.y = y;
		
		camRect.x = camera.position.x - ((w / tileMapManager.getTileSize()) / 2);
		camRect.y = camera.position.y - ((h / tileMapManager.getTileSize()) / 2);
	}
}
