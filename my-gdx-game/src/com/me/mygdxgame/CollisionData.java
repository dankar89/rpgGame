package com.me.mygdxgame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;

public class CollisionData {
	public Shape.Type shapeType;
	public Vector2 pos;

	public BodyType bodyType;
	
	
	public CollisionData(Shape.Type shapeType, BodyType bodyType, Vector2 pos)
	{
		this.shapeType = shapeType;
		this.bodyType = bodyType;
		
		this.pos = pos;
	}
	
	@Override
	public String toString()
	{
		return "shapeType: " + shapeType.toString() + "\n" +
				"bodyType: " + bodyType.toString() + "\n" + 
				"pos: " + pos.toString();
	}
}
