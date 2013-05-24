package com.me.mygdxgame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2dGameObject {
	private Body body;
	private BodyDef bodyDef;
	private Shape shape;
	private Fixture fixture;
	private boolean isActive;
	private Vector2 pos;
	
	public Box2dGameObject(World world, Vector2 pos, BodyDef bodyDef, Fixture fixture, Shape shape, boolean isActive)
	{
		this.bodyDef = bodyDef;
		this.fixture = fixture;
		this.shape = shape;		
		this.isActive = isActive;
		this.pos = pos;
		
		this.body = world.createBody(this.bodyDef);
	}
	
	public void update()
	{
		
	}
	
	public boolean isToBeDeleted()
	{
		return isActive;
	}
	
	public void setToBeDeleted(boolean bool)
	{
		isActive = bool;
	}
}
