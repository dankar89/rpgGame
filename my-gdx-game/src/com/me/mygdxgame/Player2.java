package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Player2 {	
	private Box2dAnimatedSprite sprite;
//	private Sprite sprite;
//	private Vector3 worldPos = Vector3.Zero;
	private Vector3 tmpPos = Vector3.Zero;
//	private Vector3 worldCenterPos = Vector3.Zero;
//	private Vector3 startPos = Vector3.Zero;
	private float speed = 200.0f;
//	private float scale;
	public static final String TEXTURE_PATH = "spritesheet/player/player_walking.png";
	public static final String TEST_TEXTURE_PATH = "spritesheet/player/player_spritesheet/standing_2.png";
	public static final String PACKFILE_PATH = "spritesheet/player/player_walking.txt";
	public static final String WALK_UP = "walk_up";
	public static final String WALK_DOWN = "walk_down";
	public static final String WALK_LEFT = "walk_left";
	public static final String WALK_RIGHT = "walk_right";
	private Animation walk_up_anim;
	private Animation walk_down_anim;
	private Animation walk_left_anim;
	private Animation walk_right_anim;
	private Array<AtlasRegion> standing_regions;
	private TextureAtlas walking_spriteSheet;
	private TextureRegion currentFrame;	
	private State state = State.Standing;
	private State LastWalkingState = State.WalkingDown;
//	private float stateTime = 0;
	private boolean isStrafing = false;
//	
	private BodyDef bodyDef;	
	private Body body;
	private CircleShape circle;
	private FixtureDef fixtureDef;
	private Vector2 bodyPos = Vector2.Zero;
	
	enum State {
		Standing,
		WalkingLeft,
		WalkingRight,
		WalkingUp,
		WalkingDown,
	}
	
	public Player2(Vector2 startPos, World world) {	
//		super((Texture) MyGdxGame.assetManager.get(TEST_TEXTURE_PATH));
//		super();
				
//		
		walking_spriteSheet = new TextureAtlas(PACKFILE_PATH);
		
		//get idle texture
		standing_regions = walking_spriteSheet.findRegions("standing"); 		
		TextureRegion region = standing_regions.get(1);		
		region.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		
//		///BOX2d TEST STUFF
		bodyDef = new BodyDef();				
		
		//add a small offset to the position to better match the sprite
		float shapeOffset = 5.0f * Constants.WORLD_TO_BOX;
		
		//set body start pos
		bodyPos = new Vector2(
				(startPos.x / Constants.TILE_SIZE) + ((Constants.TILE_SIZE / 2) * Constants.WORLD_TO_BOX),
				(startPos.y / Constants.TILE_SIZE) + (((Constants.TILE_SIZE / 2) * Constants.WORLD_TO_BOX) - shapeOffset));
		bodyDef.position.set(bodyPos.x, bodyPos.y);
		bodyDef.type = BodyType.DynamicBody;
		
//		// Create our body in the world using our body definition
		body = world.createBody(bodyDef);
//
//		// Create a circle shape and set its radius
		circle = new CircleShape();
		circle.setRadius(((region.getRegionWidth() / 2) * Constants.WORLD_TO_BOX) - (shapeOffset / 2));

//		// Create our fixture and attach it to the body
		body.createFixture(circle, 0);			
		circle.dispose();
		
		body.setUserData((Box2dSprite)sprite);
		body.setFixedRotation(true);
		body.setLinearVelocity(Vector2.Zero);
//		body.setLinearDamping(5f);					
		
		this.sprite = Box2dSpriteFactory.createAnimatedBox2dSprite(world, region, startPos, body, true);	
		
		walk_up_anim = new Animation(0.05f, walking_spriteSheet.findRegions(WALK_UP));
		walk_down_anim = new Animation(0.05f, walking_spriteSheet.findRegions(WALK_DOWN));
		walk_left_anim = new Animation(0.05f, walking_spriteSheet.findRegions(WALK_LEFT));
		walk_right_anim = new Animation(0.05f, walking_spriteSheet.findRegions(WALK_RIGHT));
		
		this.sprite.addAnimation(WALK_UP, walk_up_anim);
		this.sprite.addAnimation(WALK_DOWN, walk_down_anim);
		this.sprite.addAnimation(WALK_LEFT, walk_left_anim);
		this.sprite.addAnimation(WALK_RIGHT, walk_right_anim);
		
	}
	
	public void update(Rectangle worldBounds, float deltaTime) {		
		sprite.update(deltaTime);

		setCurrentAnimationFrame();
		handleInput(worldBounds);		
	}
	
	public void draw(SpriteBatch spriteBatch) {
		
		//the sprite should know which frame to draw!!!!		
		sprite.draw(spriteBatch);
	}
	
	public void dispose()
	{
		circle.dispose();
	}
	
	//refactor and move functionality to box2danimatedSprite class
	private void setCurrentAnimationFrame()
	{
		if(state == State.Standing)
		{
			if(LastWalkingState == State.WalkingUp)
				currentFrame = standing_regions.get(0); //get the standing up texture
			else if(LastWalkingState == State.WalkingDown)
				currentFrame = standing_regions.get(1); //get the standing down texture
			else if(LastWalkingState == State.WalkingLeft)
				currentFrame = standing_regions.get(2); //get the standing left texture
			else if(LastWalkingState == State.WalkingRight)
				currentFrame = standing_regions.get(3); //get the standing right texture			
		}
		else if(state == State.WalkingUp)
		{
			currentFrame = this.sprite.getCurrentFrame(WALK_UP);
		}
		else if(state == State.WalkingDown)
		{
			currentFrame = this.sprite.getCurrentFrame(WALK_DOWN);
		}
		else if(state == State.WalkingLeft)
		{
			currentFrame = this.sprite.getCurrentFrame(WALK_LEFT);
		}
		else if(state == State.WalkingRight)
		{
			currentFrame = this.sprite.getCurrentFrame(WALK_RIGHT);
		}
		else
		{
			System.out.println("FAIL! " + state);
		}
		
		this.sprite.setCurrentFrame(currentFrame);
		LastWalkingState = state;
	}
	
	
	private void handleInput(Rectangle worldBounds) {
		
		//oldPos
		tmpPos.x = sprite.getSprite().getX();
		tmpPos.y = sprite.getSprite().getY();
		
		if (Gdx.input.isKeyPressed(Keys.SPACE))
		{
			sprite.translate(15, 15);
			sprite.getSprite().translate(15, 15);
			System.out.println(sprite.getSprite().getX() + ":" + sprite.getSprite().getY());
		}
		
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT))
		{
			isStrafing = true;
		}
		else
		{
			isStrafing = false;			
		}
		
//		float movement = (Gdx.graphics.getDeltaTime() * speed) * Constants.WORLD_TO_BOX;
//		Vector2 movement = new Vector2((Gdx.graphics.getDeltaTime() * speed) / Constants.BOX_TO_WORLD, 0);
		if (Gdx.input.isKeyPressed(Keys.UP)) {					
			tmpPos.y = sprite.getSprite().getY() + (Gdx.graphics.getDeltaTime() * speed);
			
			if(!isStrafing)
				state = State.WalkingUp;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
//			body.applyLinearImpulse(new Vector2(0f, -movement), body.getWorldCenter(), true);
			tmpPos.y = sprite.getSprite().getY() - (Gdx.graphics.getDeltaTime() * speed);
			
			
			if(!isStrafing)
				state = State.WalkingDown;
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
//			body.applyLinearImpulse(new Vector2(-movement, 0f), body.getWorldCenter(), true);
			tmpPos.x = sprite.getSprite().getX() - (Gdx.graphics.getDeltaTime() * speed);
//			body.setLinearVelocity(-movement * 10, 0f);
			
			if(!isStrafing)
				state = State.WalkingLeft;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {	
			body.setLinearVelocity((Gdx.graphics.getDeltaTime() * speed) / Constants.WORLD_TO_BOX, 0);
//			body.applyLinearImpulse(movement, body.getWorldCenter(), true);
			tmpPos.x = sprite.getSprite().getX() + (Gdx.graphics.getDeltaTime() * speed);//
//			body.setLinearVelocity(movement * 10, 0f);			
			
			if(!isStrafing)
				state = State.WalkingRight;
		}
		else			
		{
//			body.setLinearVelocity(0f, 0f);
			if(!isStrafing)
				state = State.Standing;
		}

		if (Gdx.input.isTouched()) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
//				System.out.println(sprite.getX() + ":" + sprite.getY());//				
				tmpPos.x = Math.round(sprite.getSprite().getX() + -Gdx.input.getDeltaX());
				tmpPos.y = Math.round(sprite.getSprite().getY() + Gdx.input.getDeltaY());				
			}
		}

		
		
//		this.sprite = (Sprite)body.getUserData();
//		sprite.setX(body.getPosition().x * Constants.BOX_TO_WORLD);
//		sprite.setY(tmpPos.y);
		sprite.getSprite().setX(tmpPos.x);
		sprite.getSprite().setY(tmpPos.y);		
	}
	
	public Body getBody()
	{
		return this.body;
	}
	
	public Sprite getSprite()
	{
		return this.sprite.getSprite();
	}

	public Vector3 getWorldPosition() {
		return sprite.getWorldPosition();
	}	
//	
//	public Vector3 getWorldCenterPosition() {
//		worldCenterPos.x = Math.round(sprite.getX() + sprite.getOriginX()) / 32;
//		worldCenterPos.y = Math.round(sprite.getY() + sprite.getOriginY()) / 32;
//		return worldCenterPos;
//	}
		
	public Vector2 getStartPos()
	{
		return this.sprite.getStartPos();
	}
}
