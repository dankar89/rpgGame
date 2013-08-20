package com.me.mygdxgame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2dObject {
	private Body body;
//	private BodyDef bodyDef;
//	private Fixture fixture;
	private boolean markedForDeletion;
	private boolean isActive = true;
	
	public Box2dObject()
	{}
	
	public Box2dObject(World world, Body body, boolean isActive)
	{
		init(world, body, isActive);
	}
	
	public Box2dObject(World world, Body body, Shape shape)
	{
		init(world, body, true);
	}
	
	public void init(World world, Body body, boolean isActive)
	{
//		this.bodyDef = bodyDef;
//		this.fixture = fixture;	
		this.isActive = isActive;
		this.markedForDeletion = false;
		
		this.body = body;
//		this.body.setUserData(this); //?
	}
	
	public void update(World world)
	{
		if(isToBeDeleted())
			world.destroyBody(this.body);
	}
	
	public boolean isToBeDeleted()
	{
		return markedForDeletion;
	}
	
	public void markForDeletion(boolean bool)
	{
		this.markedForDeletion = bool;
	}
	
	public Vector2 getPosition()
	{
		return body.getPosition();
	}
	
	public void setPosition(float x, float y)
	{
		this.body.getPosition().set(x, y);
	}
	
	public Body getBody()
	{
		return this.body;
	}
}
