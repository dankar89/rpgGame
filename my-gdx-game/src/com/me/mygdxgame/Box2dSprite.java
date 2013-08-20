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
	
	public Box2dSprite(World world, Vector2 pos, Body body, boolean isActive)
	{
		this.sprite = new Sprite();
		this.box2dObject = new Box2dObject(world, body, isActive);	
		this.startPos = pos;	
	}
	
	public Box2dSprite(World world, TextureRegion textureRegion, Vector2 pos, Body body, boolean isActive)
	{
		init(world, textureRegion, pos, body, isActive);
	}
	
	public void init(World world, TextureRegion textureRegion, Vector2 pos, Body body, boolean isActive)
	{
		this.sprite = new Sprite();
		this.box2dObject = new Box2dObject(world, body, isActive);
		
		this.startPos = pos;	
		
		sprite.setRegion(textureRegion);
		sprite.setBounds(startPos.x, startPos.y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
	}

	
	public void update(float dt)
	{		
//		sprite.setPosition(getBody().getPosition().x * Constants.BOX_TO_WORLD, getBody().getPosition().y * Constants.BOX_TO_WORLD);
	}
		
	public void draw(SpriteBatch batch)
	{
		//maybe sprite doesnt need a texture? is that right?
		batch.draw(sprite.getTexture(),
				getWorldPosition().x,
				getWorldPosition().y,
//				getBody().getPosition().x * Constants.BOX_TO_WORLD,
//				getBody().getPosition().y * Constants.BOX_TO_WORLD,
				sprite.getWidth() / Constants.TILE_SIZE,
				sprite.getHeight() / Constants.TILE_SIZE);
	}
	
	public void draw(SpriteBatch batch, TextureRegion region)
	{
		//maybe sprite doesnt need a texture? is that right?		
		batch.draw(region,
				getWorldPosition().x,
				getWorldPosition().y,
//				getBody().getPosition().x * Constants.BOX_TO_WORLD,
//				getBody().getPosition().y * Constants.BOX_TO_WORLD,
				sprite.getWidth() / Constants.TILE_SIZE,
				sprite.getHeight() / Constants.TILE_SIZE);
	}
	
	public Sprite getSprite()
	{
		return this.sprite;
	}
	
	public Body getBody()
	{
		return box2dObject.getBody();
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
	
	
	public void translate(float xAmount, float yAmount)
	{			
		this.box2dObject.getBody().setLinearVelocity(xAmount * Constants.WORLD_TO_BOX,
						yAmount * Constants.WORLD_TO_BOX);		
//		this.sprite.setPosition(this.box2dObject.getPosition().x, this.box2dObject.getPosition().y);
		
	}
	
	public void setPosition(Vector2 v)
	{
//		this.sprite.setPosition(v.x, v.y);
		this.box2dObject.setPosition(v.x * Constants.WORLD_TO_BOX, v.y * Constants.WORLD_TO_BOX);		
	}
	
	public void setPosition(float x, float y)
	{
//		this.sprite.setPosition(x, y);
		this.box2dObject.setPosition(x * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX);
	}
	
	public Vector2 getStartPos()
	{
		return this.startPos;
	}
}
