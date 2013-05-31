package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Player {	

	private Sprite sprite;
	private Vector3 worldPos = Vector3.Zero;
	private Vector3 tmpPos = Vector3.Zero;
	private Vector3 worldCenterPos = Vector3.Zero;
	private Vector3 startPos = Vector3.Zero;
	private float speed = 200.0f;
	private float scale;
	public static final String TEXTURE_PATH = "spritesheet/player/player_walking.png";
	public static final String TEST_TEXTURE_PATH = "spritesheet/player/player_spritesheet/standing_2.png";
	public static final String PACKFILE_PATH = "spritesheet/player/player_walking.txt";
	private Animation walk_up_anim;
	private Animation walk_down_anim;
	private Animation walk_left_anim;
	private Animation walk_right_anim;
	private Array<AtlasRegion> standing_regions;
	private TextureAtlas walking_spriteSheet;
	private TextureRegion currentFrame;	
	private State state = State.Standing;
	private State LastWalkingState = State.WalkingDown;
	private float stateTime = 0;
	private boolean isStrafing = false;
	
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
	
	public Player(Vector3 startPos, float scale, World world) {	
//		super((Texture) MyGdxGame.assetManager.get(TEST_TEXTURE_PATH));
//		super();
		
		walking_spriteSheet = new TextureAtlas(PACKFILE_PATH);
		
		walk_up_anim = new Animation(0.05f, walking_spriteSheet.findRegions("walk_up"));
		walk_down_anim = new Animation(0.05f, walking_spriteSheet.findRegions("walk_down"));
		walk_left_anim = new Animation(0.05f, walking_spriteSheet.findRegions("walk_left"));
		walk_right_anim = new Animation(0.05f, walking_spriteSheet.findRegions("walk_right"));
		standing_regions = walking_spriteSheet.findRegions("standing"); 
		
		currentFrame = standing_regions.get(1); //get the standing down sprite
		
		this.scale = scale;		
		
		this.startPos = startPos;				
		
//		this.setRegion(currentFrame);
		sprite = new Sprite(currentFrame);
		
//		this.setBounds(startPos.x, startPos.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());	
		sprite.setBounds(startPos.x, startPos.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

//		setOrigin(getWidth() / 2, getHeight() / 2);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		
		
//		///BOX2d TEST STUFF
		bodyDef = new BodyDef();		
		bodyDef.type = BodyType.DynamicBody;
//		// Set our body's starting position in the world
		
		//add a small offset to the position to better match the sprite
		float shapeOffset = 5.0f * Constants.WORLD_TO_BOX;
		bodyPos = new Vector2(
				getWorldPosition().x + ((Constants.TILE_SIZE / 2) * Constants.WORLD_TO_BOX),
				getWorldPosition().y + (((Constants.TILE_SIZE / 2) * Constants.WORLD_TO_BOX) - shapeOffset));
		bodyDef.position.set(bodyPos.x, bodyPos.y);
//		
//		
//		// Create our body in the world using our body definition
		body = world.createBody(bodyDef);
//
//		// Create a circle shape and set its radius
		circle = new CircleShape();
		circle.setRadius(((sprite.getRegionWidth() / 2) * Constants.WORLD_TO_BOX) - (shapeOffset / 2));
//
		// Create a fixture definition to apply our shape to
//		fixtureDef = new FixtureDef();
//		fixtureDef.shape = circle;
//		fixtureDef.density = 0.5f; 
//		fixtureDef.friction = 0.6f;
//		fixtureDef.restitution = 0.05f; // Make it bounce a little bit
		
//		// Create our fixture and attach it to the body
		body.createFixture(circle, 0);			
		circle.dispose();
		
		body.setUserData(sprite);
		body.setFixedRotation(true);
		body.setLinearVelocity(Vector2.Zero);
//		body.setLinearDamping(5f);			
//		////////////////////
	}
	
	public void update(Rectangle worldBounds, float deltaTime) {		
		stateTime += Gdx.graphics.getDeltaTime(); 

		setCurrentAnimationFrame();
		handleInput(worldBounds);			
	}
	
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(currentFrame,
				getWorldPosition().x,
				getWorldPosition().y,
//				body.getPosition().x,
//				body.getPosition().y,
				sprite.getWidth() / Constants.TILE_SIZE,
				sprite.getHeight() / Constants.TILE_SIZE);
	}
	
	public void dispose()
	{
//		circle.dispose();
	}
	
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
			currentFrame = walk_up_anim.getKeyFrame(this.stateTime, true);
		}
		else if(state == State.WalkingDown)
		{
			currentFrame = walk_down_anim.getKeyFrame(this.stateTime, true);
		}
		else if(state == State.WalkingLeft)
		{
			currentFrame = walk_left_anim.getKeyFrame(this.stateTime, true);
		}
		else if(state == State.WalkingRight)
		{
			currentFrame = walk_right_anim.getKeyFrame(this.stateTime, true);
		}
		else
		{
			System.out.println("FAIL! " + state);
		}
		
		LastWalkingState = state;
	}
	
	
	private void handleInput(Rectangle worldBounds) {
		tmpPos.x = sprite.getX();
		tmpPos.y = sprite.getY();
		
//		if (Gdx.input.isKeyPressed(Keys.SPACE))
//			body.applyLinearImpulse(new Vector2(0.2f, 0.1f), body.getWorldCenter(), false);
		
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT))
		{
			isStrafing = true;
		}
		else
		{
			isStrafing = false;			
		}
		
		float movement = (Gdx.graphics.getDeltaTime() * speed) * Constants.WORLD_TO_BOX;
		if (Gdx.input.isKeyPressed(Keys.UP)) {		
//			body.applyLinearImpulse(new Vector2(0f, movement), body.getWorldCenter(), true);
			tmpPos.y = sprite.getY() + (Gdx.graphics.getDeltaTime() * speed);
			
			if(!isStrafing)
				state = State.WalkingUp;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
//			body.applyLinearImpulse(new Vector2(0f, -movement), body.getWorldCenter(), true);
			tmpPos.y = sprite.getY() - (Gdx.graphics.getDeltaTime() * speed);
			
			
			if(!isStrafing)
				state = State.WalkingDown;
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
//			body.applyLinearImpulse(new Vector2(-movement, 0f), body.getWorldCenter(), true);
			tmpPos.x = sprite.getX() - (Gdx.graphics.getDeltaTime() * speed);
//			body.setLinearVelocity(-movement * 10, 0f);
			
			if(!isStrafing)
				state = State.WalkingLeft;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {			
//			body.applyLinearImpulse(new Vector2(movement, 0f), body.getWorldCenter(), true);
			tmpPos.x = sprite.getX() + (Gdx.graphics.getDeltaTime() * speed);//
//			body.setLinearVelocity(movement * 10, 0f);			
			
			if(!isStrafing)
				state = State.WalkingRight;
		}
		else			
		{
			body.setLinearVelocity(0f, 0f);
			if(!isStrafing)
				state = State.Standing;
		}

		if (Gdx.input.isTouched()) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
//				System.out.println(sprite.getX() + ":" + sprite.getY());//				
				tmpPos.x = Math.round(sprite.getX() + -Gdx.input.getDeltaX());
				tmpPos.y = Math.round(sprite.getY() + Gdx.input.getDeltaY());				
			}
		}
				
		
//		this.sprite = (Sprite)body.getUserData();
//		sprite.setX(body.getPosition().x * Constants.BOX_TO_WORLD);
//		sprite.setY(tmpPos.y);
		sprite.setX(tmpPos.x);
		sprite.setY(tmpPos.y);		
	}
	
	public Body getBody()
	{
		return this.body;
	}
	
	public Sprite getSprite()
	{
		return this.sprite;
	}

	public Vector3 getWorldPosition() {
		worldPos.x =  sprite.getX() / 32;
		worldPos.y =  sprite.getY() / 32;
		return worldPos;
	}	
	
	public Vector3 getWorldCenterPosition() {
		worldCenterPos.x = Math.round(sprite.getX() + sprite.getOriginX()) / 32;
		worldCenterPos.y = Math.round(sprite.getY() + sprite.getOriginY()) / 32;
		return worldCenterPos;
	}
		
	public Vector3 getStartPos()
	{
		return this.startPos;
	}
}
