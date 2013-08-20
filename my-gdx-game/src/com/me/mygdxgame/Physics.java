package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Physics {

	private World world;
	private Box2DDebugRenderer debugRenderer;
		
	private Shape tmpShape;
	private Body tmpBody;
	private BodyDef tmpBodyDef;
	private Iterator<Body> bodies = null;
	private Body nextBody;
	private Box2dObject userData = null;
	private ArrayList<CollisionData> collisionDataList;
	private ArrayList<Body> deletionList;;
	
	public Physics()
	{	
		world = new World(new Vector2(0, 0), true);		
		debugRenderer = new Box2DDebugRenderer();
		
		tmpBodyDef = new BodyDef();
		
		collisionDataList = new ArrayList<CollisionData>();
		deletionList = new ArrayList<Body>();
	}
	
	public void createMapObjects(ArrayList<CollisionData> colData)
	{		
		for (CollisionData cd : colData) {
			createMapObject(cd);
		}
	}
	
	public void createMapObject(CollisionData colData)
	{
		tmpBodyDef.type = colData.bodyType;
		tmpBodyDef.position.set(colData.pos);
		
		tmpBody = world.createBody(tmpBodyDef);
		
		if(colData.shapeType == Shape.Type.Circle)
		{
			tmpShape = new CircleShape();
			tmpShape.setRadius(16 * Constants.WORLD_TO_BOX);
		}
		else //default to a box
		{
			//why cant I use SetAsBox?
			tmpShape = new PolygonShape();
			tmpShape.setRadius(16 * Constants.WORLD_TO_BOX);
		}
		
		tmpBody.createFixture(tmpShape, 0.0f);
		tmpShape.dispose();
	}
	
	public Iterator<Body> getBodies()
	{
		return world.getBodies();
	}
	
	public void  destroyBodies()
	{
		while(world.getBodies().hasNext())
			world.destroyBody(world.getBodies().next());
	}
//	
//	public void deleteBodies()
//	{		
//		Body body = null;
//		if(world.getBodyCount() > 1)
//		{
//			bodies = world.getBodies();	
//			while(bodies.hasNext())
//			{
//				body = bodies.next();
//				//TODO extend player with Box2dGameObject
//				if(body != null)
//				{
//					//get user data
//					userData = (Box2dObject)body.getUserData();
//	
//					//if user data is flagged for deletion
//					if(userData.isToBeDeleted())
//					{
//						body.setUserData(null);
//						world.destroyBody(body);
//						body = null; 
//					}
//				}
//			}
//		}
//	}
	
	public Body createCircle(BodyType type, Vector2 pos, float radius, float density)
	{
		Body body = createBody(type, pos);

		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
		body.createFixture(shape, density);
		shape.dispose();
//		MassData mass = new MassData();
//		mass.mass = 1000f;
//		body.setMassData(mass);

		return body;
	}
	
	public Body createBox(BodyType type, Vector2 pos, float width, float height, float density) {		
		Body body = createBody(type, pos);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		body.createFixture(shape, density);
		shape.dispose();

		return body;
	}
	
	private Body createBody(BodyType type, Vector2 pos)	
	{
		BodyDef def = new BodyDef();
		def.type = type;
		def.position.set(pos);
		return world.createBody(def);
	}
	
	public void update(float timeStep, OrthographicCamera cam)
	{
//		System.out.println(world.getBodyCount());
		if(world.getBodyCount() > 1) //TODO add check for player body 
		{
			bodies = world.getBodies();	
			while(bodies.hasNext())
			{
				nextBody = bodies.next();

//				System.out.println("bodypos: " + nextBody.getPosition().x);
//				System.out.println("campos: " + cam.position.x);
				if(nextBody.getPosition().x < cam.position.x - (cam.viewportWidth / 2) ||
						nextBody.getPosition().y < cam.position.y - (cam.viewportHeight / 2) ||
						nextBody.getPosition().x > cam.position.x + (cam.viewportWidth / 2) ||
						nextBody.getPosition().y > cam.position.y + (cam.viewportHeight / 2))
//				if(!screenCamRect.contains(nextBody.getPosition().x * Constants.BOX_TO_WORLD,
//						nextBody.getPosition().y * Constants.BOX_TO_WORLD))
				{
					deletionList.add(nextBody);
					System.out.println("waah");
				}
			}
			
		}
	
		world.step(timeStep, 6, 2);
				
		//destroy bodies marked for deletion
		for (Body body : deletionList) {
			world.destroyBody(body);
		}
		deletionList.clear();
	}
	
	public void renderDebug(Matrix4 projMatrix)
	{
		debugRenderer.render(this.world, projMatrix);
	}
	
	public World getWorld()
	{
		return world;
	}
	
}
