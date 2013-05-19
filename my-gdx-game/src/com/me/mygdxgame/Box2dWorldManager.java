package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2dWorldManager {

	private World world;
	private Box2DDebugRenderer debugRenderer;
		
	private Shape tmpShape;
	private Body tmpBody;
	private BodyDef tmpBodyDef;
	private Iterator<Body> bodies = null;
	
	public Box2dWorldManager()
	{	
		world = new World(new Vector2(0, 0), true);		
		debugRenderer = new Box2DDebugRenderer();
		
		tmpBodyDef = new BodyDef();
	}
	
	public void createBodies(ArrayList<CollisionData> colData)
	{		
		for (CollisionData cd : colData) {
			tmpBodyDef.type = cd.bodyType;
			tmpBodyDef.position.set(cd.pos);
			
			tmpBody = world.createBody(tmpBodyDef);
			
			if(cd.shapeType == Shape.Type.Circle)
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
		}
		tmpShape.dispose();
	}
	
	public void  destroyBodies()
	{
		while(world.getBodies().hasNext())
			world.destroyBody(world.getBodies().next());
	}
	
	public void destroyBodyAt(int x, int y)
	{
		
	}
	
	public void destroyBodyAt(int index)
	{}
	
	public void update(float timeStep, Rectangle camRect)
	{
//		System.out.println(world.getBodyCount());
		if(world.getBodyCount() > 1)
		{
//			bodies = world.getBodies();
//			System.out.println(world.getBodyCount());
//			if(bodies.hasNext())
//			{
//					
//			}
			
		}
		world.step(1/60f,6, 2);
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
