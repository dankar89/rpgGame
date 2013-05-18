package com.me.mygdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Shape.Type;

public class TileMapManager {
	private TiledMap map;
//	private World world;
//	private Box2DDebugRenderer debugRenderer;
	private OrthogonalTiledMapRenderer mapRenderer;
	private TiledMapTileLayer terrainLayer;
	private int[] backgroundLayers = { Constants.TERRAIN_LAYER_INDEX, Constants.BACKGROUND_LAYER_INDEX, 5/*test layer*/ };
	private int[] foregroundLayers = { Constants.FOREGROUND_LAYER_INDEX };
	private int[] objectLayers = { Constants.OBJECT_LAYER_INDEX };
	private float unitScale;
//	private Box2dWorldManager collisionManager;
	
	private ArrayList<Rectangle> adjacentCells = new ArrayList<Rectangle>();
	private ArrayList<Box2dMapObjectData> filteredDataObjects = new ArrayList<Box2dMapObjectData>();
	
	public TileMapManager() {
		//get map
		map = MyGdxGame.assetManager.get("maps/map2.tmx");

		terrainLayer = (TiledMapTileLayer) map.getLayers().get(Constants.TERRAIN_LAYER_NAME);

		unitScale = 1 / (float) getTileSize();
		mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);
		
//		collisionManager = new Box2dWorldManager();

	}
	
	public void renderBgLayers(OrthographicCamera cam)
	{
		mapRenderer.setView(cam);
		mapRenderer.render(backgroundLayers);
	}
	
	public void renderFgLayers(OrthographicCamera cam)
	{
		mapRenderer.render(foregroundLayers);
	}
	
	public void dispose()
	{
		map.dispose();
	}
	
	public MapObject getObjInLayer(String objName, int layerIndex)
	{
		MapLayer layer = map.getLayers().get(layerIndex);
	
		try {
			if(layer == null)
				throw new Exception("Layer '" + layerIndex +"' does not exist!");
			else if(layer.getObjects().get(objName) == null)
				throw new Exception("Object '" + objName + "' does not exist in layer " + layerIndex + "!");
			else
				return layer.getObjects().get(objName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Cell getCellAt(int x, int y, int layerIndex)
	{
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(layerIndex);
		
		try {
			if(layer == null)
				throw new Exception("Layer '" + layerIndex +"' does not exist!");
			else if(layer.getClass() != TiledMapTileLayer.class)
				throw new Exception("Layer '" + layerIndex +"' is not a TiledMapTileLayer object!");
			else if(layer.getCell(x, y) == null)
				throw new Exception("The cell at " + x + ":" + y + " is empty!");
			else
				return layer.getCell(x, y);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ArrayList<Box2dMapObjectData> getBox2dMapObjects(String prop, int layerIndex, int startX, int startY, int endX, int endY)
	{
		filteredDataObjects.clear();		
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(layerIndex);
		MapProperties tileProps = null;
		BodyType bType = BodyType.StaticBody;
		Shape.Type sType = Shape.Type.Circle;
		TiledMapTile tile = null;
						
		for(int y = startY; y <= endY; y++)
		{
			for(int x = startX; x <= endX; x++)
			{
				if(layer.getCell(x, y) != null)
				{
					tile = layer.getCell(x, y).getTile();
				
					if(tile != null)
					{
						tileProps = tile.getProperties();						
	//					for (String prop : props) {
							if(tileProps.containsKey(prop))						
							{
								//if it has a shape property, we know it should be made into a box2d body
								if(tileProps.get(prop).equals("circle"))	
									sType = Type.Circle;
								else if(tileProps.get(prop).equals("box"))	
									sType = Type.Polygon;												
	
								if(tileProps.containsKey("body"))
								{
									if(tileProps.get("body").equals("static"))	
									{
											bType = BodyType.StaticBody;
									}
									else if(tileProps.get("body").equals("kinematic"))	
									{
											bType = BodyType.KinematicBody;
									}
									else if(tileProps.get("body").equals("dynamic"))	
									{
											bType = BodyType.DynamicBody;
									}
								}
								
								filteredDataObjects.add(
										new Box2dMapObjectData(
												sType,
												bType,
												new Vector2(
														x + ((this.getTileSize() / 2) * Constants.WORLD_TO_BOX),
														y + ((this.getTileSize() / 2) * Constants.WORLD_TO_BOX)
														)
												)
										);						
							}
	//					}
					}
				}
			}
		}
		
		return filteredDataObjects;
		
	}
	
	public ArrayList<Rectangle> getAdjacentRects(int startX, int startY, int endX, int endY, int layerIndex)
	{
		adjacentCells.clear();
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(layerIndex);
		Cell cell = null;
		
		if(layer.getCell(startX, startY) != null)
		{
			
			for(int y = startY; y <= endY; y++)
			{
				for(int x = startX; x <= endX; x++)
				{
					cell = layer.getCell(x, y);
					if(cell != null)
					{						
						adjacentCells.add(new Rectangle(x, y, 1, 1));
					}
					
				}	
			}
		}
		
		return adjacentCells;
	}

	public TiledMap getMap() {
		return map;
	}

	public MapLayer getLayer(int i) {
		return map.getLayers().get(i);
	}
	
	public MapLayer getLayer(String name) {
		return map.getLayers().get(name);
	}
	
	public void setMap(TiledMap map) {
		this.map = map;
	}		

	public int getMapWidth() {
		return terrainLayer.getWidth();
	}

	public int getMapHeight() {
		return terrainLayer.getHeight();
	}

	public int getTileSize() {
		return (int) terrainLayer.getTileWidth();
	}

	public TiledMapTileLayer getBaseMapLayer() {
		return terrainLayer;
	}
	
	public Vector3 getPlayerStartPos()
	{
		Rectangle tmpRect = ((RectangleMapObject)this.getObjInLayer("player", 3)).getRectangle();
		
		//return rectangle center
//		return new Vector3(tmpRect.x + (tmpRect.width / 2) , tmpRect.y + (tmpRect.height / 2), 0);
		if(tmpRect != null)
			return new Vector3(tmpRect.x , tmpRect.y, 0);
		else
			return Vector3.Zero;
	}
	
	public OrthogonalTiledMapRenderer getMapRenderer()
	{
		return mapRenderer;
	}
	
	public float getScale()
	{
		return unitScale;
	}
}
