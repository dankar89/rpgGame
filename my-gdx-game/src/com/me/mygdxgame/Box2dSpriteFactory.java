package com.me.mygdxgame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2dSpriteFactory {

	public static Box2dAnimatedSprite createAnimatedBox2dSprite(World world, TextureRegion textureRegion, Vector2 pos, Body body, boolean isActive)
	{
		return new Box2dAnimatedSprite(world, textureRegion, pos, body, isActive);				
	}
	
	public static Box2dSprite createBox2dSprite(World world, Vector2 pos, Body body, boolean isActive)
	{
		//set sprite texture with initSprite()...
		return new Box2dSprite(world, pos, body, isActive);	
	}
	
	public static Box2dSprite createBox2dSprite(World world, TextureRegion textureRegion, Vector2 pos, Body body, boolean isActive)
	{
		return new Box2dSprite(world, textureRegion, pos, body, isActive);	
	}
}
