package com.me.mygdxgame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

//
public class Box2dSprite{
	private Sprite sprite;
	private Box2dObject box2dObject;
	
	private Vector3 worldPos = Vector3.Zero;
//	private Vector3 tmpPos = Vector3.Zero;
	private Vector3 worldCenterPos = Vector3.Zero;
	private Vector2 startPos = Vector2.Zero;
	private Vector2 bodyPos = Vector2.Zero;
	private float scale;
	
	public Box2dSprite(World world, Vector2 pos, Body body, Shape shape, boolean isActive)
	{
		this.sprite = new Sprite();
		this.box2dObject = new Box2dObject(world, body, shape, isActive);	
		this.startPos = pos;	
	}
	
	public Box2dSprite(World world, TextureRegion textureRegion, Vector2 pos, Body body, Shape shape, boolean isActive)
	{
		init(world, textureRegion, pos, body, shape, isActive);
	}
	
	public void init(World world, TextureRegion textureRegion, Vector2 pos, Body body, Shape shape, boolean isActive)
	{
		this.sprite = new Sprite();
		this.box2dObject = new Box2dObject(world, body, shape, isActive);
		
		this.startPos = pos;	
		
		sprite.setRegion(textureRegion);
		sprite.setBounds(startPos.x, startPos.y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setScale(2.0f);
	}

	
	public void update(float dt)
	{		
	
	}
		
	public void draw(SpriteBatch batch)
	{
		//maybe sprite doesnt need a texture? is that right?
		batch.draw(sprite.getTexture(),
				getWorldPosition().x,
				getWorldPosition().y,
//				body.getPosition().x,
//				body.getPosition().y,
				sprite.getWidth() / Constants.TILE_SIZE,
				sprite.getHeight() / Constants.TILE_SIZE);
	}
	
	public void draw(SpriteBatch batch, TextureRegion region)
	{
		//maybe sprite doesnt need a texture? is that right?		
		batch.draw(region,
				getWorldPosition().x,
				getWorldPosition().y,
//				body.getPosition().x,
//				body.getPosition().y,
				sprite.getWidth() / Constants.TILE_SIZE,
				sprite.getHeight() / Constants.TILE_SIZE);
	}
	
	public Sprite getSprite()
	{
		return this.sprite;
	}
	
	public Box2dObject getBox2dObject()
	{
		return this.box2dObject;
	}

	public Vector3 getWorldPosition() {
		worldPos.x =  sprite.getX() / Constants.TILE_SIZE;
		worldPos.y =  sprite.getY() / Constants.TILE_SIZE;
		return worldPos;
	}
	
	public Vector3 getWorldCenterPosition() {
		worldCenterPos.x = Math.round(sprite.getX() + sprite.getOriginX()) / 32;
		worldCenterPos.y = Math.round(sprite.getY() + sprite.getOriginY()) / 32;
		return worldCenterPos;
	}
	
	public Vector2 getStartPos()
	{
		return this.startPos;
	}
}
