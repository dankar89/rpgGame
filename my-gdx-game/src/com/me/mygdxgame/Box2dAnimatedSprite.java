package com.me.mygdxgame;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;


//TODO: this class doesnt do anything !!!
public class Box2dAnimatedSprite extends Box2dSprite{	
//	private Box2dSprite box2dSprite;
	private Map<String, Animation> animations = new HashMap<String, Animation>();
//	private Animation walk_up_anim;
//	private Animation walk_down_anim;
//	private Animation walk_left_anim;
//	private Animation walk_right_anim;
//	private Array<AtlasRegion> standing_regions;
//	private TextureAtlas walking_spriteSheet;
	private TextureRegion currentFrame;	
//	private State state = State.Standing;
//	private State LastWalkingState = State.WalkingDown;
	private float stateTime = 0;

	public void setCurrentFrame(TextureRegion currentFrame) {
		this.currentFrame = currentFrame;
	}
	
	public TextureRegion getCurrentFrame(String AnimName)
	{
		return this.getAnimation(AnimName).getKeyFrame(this.stateTime, true);
	}

	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public boolean isStrafing() {
		return isStrafing;
	}

	public void setStrafing(boolean isStrafing) {
		this.isStrafing = isStrafing;
	}

	private boolean isStrafing = false;
	
	public Box2dAnimatedSprite(World world, TextureRegion textureRegion, Vector2 pos, Body body,
			Shape shape, boolean isActive) {
		super(world, textureRegion, pos, body, shape, isActive);
		
		
	}
	
	public void update(float dt)
	{
		stateTime += dt; 
	}
	
	public void draw(SpriteBatch batch)
	{
		super.draw(batch, currentFrame);
	}
	
	public void addAnimation(String name, Animation anim)
	{
		this.animations.put(name, anim);
	}
	
	public Animation getAnimation(String name)
	{
		return this.animations.get(name);
	}

//
//	public Vector3 getWorldPosition() {
//		worldPos.x =  sprite.getX() / 32;
//		worldPos.y =  sprite.getY() / 32;
//		return worldPos;
//	}	
	

}
