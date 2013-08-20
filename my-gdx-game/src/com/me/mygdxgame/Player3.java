package com.me.mygdxgame;

import net.dermetfan.libgdx.graphics.AnimatedBox2DSprite;
import net.dermetfan.libgdx.graphics.AnimatedSprite;
import net.dermetfan.libgdx.graphics.Box2DSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Player3 {
//	private AnimatedFixtureSprite afs;
	private Box2DSprite box2dSprite;
	private Sprite sprite;
	private AnimatedSprite animatedSprite;
	private Animation walk_up_anim;
	private Animation walk_down_anim;
	private Animation walk_left_anim;
	private Animation walk_right_anim;
	private Animation idle_anim;
	private Array<AtlasRegion> standing_regions;
	private TextureAtlas walking_spriteSheet;
	private TextureRegion currentFrame;	
	private TextureRegion idle_region;
	private Fixture fixture;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private CircleShape shape;
	private PolygonShape boxShape;
	private Body playerBody;
	private AnimatedBox2DSprite animatedBox2dSprite;
	public static final String TEXTURE_PATH = "spritesheet/player/player_walking.png";
	public static final String TEST_TEXTURE_PATH = "spritesheet/player/player_spritesheet/standing_2.png";
	public static final String PACKFILE_PATH = "spritesheet/player/player_walking.txt";
	public static final String WALK_UP = "walk_up";
	public static final String WALK_DOWN = "walk_down";
	public static final String WALK_LEFT = "walk_left";
	public static final String WALK_RIGHT = "walk_right";
	
	enum State {
		Standing,
		WalkingLeft,
		WalkingRight,
		WalkingUp,
		WalkingDown,
	}
	private State walking_state;
	private State last_walking_state;
	private boolean isStrafing = false;
	
	
	public Player3(World world, Vector2 startPos)
	{
		walking_state = State.Standing;
		last_walking_state = State.WalkingDown;
		walking_spriteSheet = new TextureAtlas(PACKFILE_PATH);
		
		//get idle texture
		standing_regions = walking_spriteSheet.findRegions("standing"); 		
		idle_region = standing_regions.get(1);		
		idle_region.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		//create animations and create the animatedFixtureSprite
		walk_up_anim = new Animation(0.05f, walking_spriteSheet.findRegions(WALK_UP));
		walk_down_anim = new Animation(0.05f, walking_spriteSheet.findRegions(WALK_DOWN));
		walk_left_anim = new Animation(0.05f, walking_spriteSheet.findRegions(WALK_LEFT));
		walk_right_anim = new Animation(0.05f, walking_spriteSheet.findRegions(WALK_RIGHT));
		idle_anim = new Animation(0.05f, idle_region);
		
		animatedSprite = new AnimatedSprite(idle_anim);		
		animatedBox2dSprite = new AnimatedBox2DSprite(animatedSprite);
		animatedBox2dSprite.stop();
		
		Sprite sprite = new Sprite(idle_region);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		box2dSprite = new Box2DSprite(sprite);
		
		
		//physics stuff
		bodyDef = new BodyDef();
		shape = new CircleShape();
		boxShape = new PolygonShape();
		fixtureDef = new FixtureDef();
		
		bodyDef.type = BodyType.DynamicBody;		
		float bodyOffset = .5f;
		Vector2 bodyPos = new Vector2(
				(startPos.x / Constants.TILE_SIZE) + ((box2dSprite.getWidth() / 2) / Constants.PIXELS_PER_METER),
				(startPos.y / Constants.TILE_SIZE) + ((box2dSprite.getHeight() / 2) / Constants.PIXELS_PER_METER));
		bodyDef.position.set(bodyPos.x, bodyPos.y);		
		
		shape.setRadius((idle_region.getRegionWidth() / Constants.PIXELS_PER_METER) / 2);
//		shape.setPosition(new Vector2(bodyOffset, bodyOffset));
		
		boxShape.setAsBox((idle_region.getRegionWidth() / 2f) / Constants.PIXELS_PER_METER, (idle_region.getRegionHeight() / 2f) / Constants.PIXELS_PER_METER);		
//		fixtureDef.shape = shape;
		
		playerBody = world.createBody(bodyDef);
		fixture = playerBody.createFixture(shape, 0);			
		shape.dispose();
		boxShape.dispose();
		
//		playerBody.setUserData(box2dSprite);
				
//		Sprite tmpSprite = new Sprite(region);
//		tmpSprite.setOrigin(tmpSprite.getWidth() / 2, tmpSprite.getHeight() / 2);
//		fixtureSprite = new FixtureSprite(tmpSprite);
		
//		float spriteOffset = 10;
//		box2dSprite.setOrigin((idle_region.getRegionWidth() / Constants.PIXELS_PER_METER), (idle_region.getRegionHeight() / Constants.PIXELS_PER_METER));
//		
		
//		animatedBox2dSprite.setAdjustWidth(false);
//		animatedBox2dSprite.setAdjustHeight(false);	
//		animatedBox2dSprite.setScale(1f / Constants.PIXELS_PER_METER);
//		


//		box2dSprite.setOrigin((idle_region.getRegionWidth() / Constants.PIXELS_PER_METER) , (idle_region.getRegionHeight() / Constants.PIXELS_PER_METER));
//		System.out.println(playerBody.getPosition());
//		System.out.println(animatedBox2dSprite.getX());
//		fixture.setUserData(animatedBox2dSprite);
//		box2dSprite.setAdjustWidth(false);
//		box2dSprite.setAdjustHeight(false);
//		box2dSprite.setUseOriginX(true);
//		box2dSprite.setUseOriginY(true);		
//		box2dSprite.setScale(1f / Constants.PIXELS_PER_METER);

		
		System.out.println("############# " + playerBody.getPosition());
//		System.out.println("############# " + box2dSprite.getX());
		
		playerBody.setUserData(box2dSprite);
//		fixture.setUserData(box2dSprite);
	}
	
	public void update()
	{				
		handleInput();
//		updateAnimation();
//		animatedBox2dSprite.update();		
	}
	
	private void updateAnimation()
	{
		switch (walking_state) {
		case Standing:
			if(last_walking_state == State.WalkingUp)
				idle_region = standing_regions.get(0); //get the standing up texture
			else if(last_walking_state == State.WalkingDown)
				idle_region = standing_regions.get(1); //get the standing down texture
			else if(last_walking_state == State.WalkingLeft)
				idle_region = standing_regions.get(2); //get the standing left texture
			else if(last_walking_state == State.WalkingRight)
				idle_region =  standing_regions.get(3); //get the standing right texture
			break;
		case WalkingUp:
			
			break;
		case WalkingDown:
			
			break;
		case WalkingLeft:
			
			break;
		case WalkingRight:
			
			break;
		default:
			
			break;
		}
		
		last_walking_state = walking_state;
//		}
//		else if(walking_state == State.WalkingUp)
//		{
//			
//		}
//		else if(walking_state == State.WalkingDown)
//		{
//			currentFrame = this.sprite.getCurrentFrame(WALK_DOWN);
//		}
//		else if(walking_state == State.WalkingLeft)
//		{
//			currentFrame = this.sprite.getCurrentFrame(WALK_LEFT);
//		}
//		else if(walking_state == State.WalkingRight)
//		{
//			currentFrame = this.sprite.getCurrentFrame(WALK_RIGHT);
//		}
//		else
//		{
//			System.out.println("FAIL! " + walking_state);
//		}
		
		
//		this.sprite.setCurrentFrame(currentFrame);
		
	}
	
	private void handleInput()
	{
		
		//TEMP!!!
		if (Gdx.input.isTouched()) {
//			Vector2 mouseDelta = new Vector2(Gdx.input.getDeltaX() * 1000, 0);
//			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {				
//				playerBody.setLinearVelocity(mouseDelta);				
//			}
		}
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT))
			isStrafing = true;
		else
			isStrafing = false;			
		
		float speed = 10;
		if(Gdx.input.isKeyPressed(Keys.RIGHT))
		{
			playerBody.setLinearVelocity(speed, 0);
			if(!isStrafing)
				walking_state = State.WalkingRight;
		}
		else if(Gdx.input.isKeyPressed(Keys.LEFT))
		{
			playerBody.setLinearVelocity(-speed, 0);
			if(!isStrafing)
				walking_state = State.WalkingLeft;
		}
		else if(Gdx.input.isKeyPressed(Keys.UP))
		{
			playerBody.setLinearVelocity(0, speed);
			if(!isStrafing)
				walking_state = State.WalkingUp;
		}
		else if(Gdx.input.isKeyPressed(Keys.DOWN))
		{
			playerBody.setLinearVelocity(0, -speed);
			if(!isStrafing)
				walking_state = State.WalkingDown;
		}
		else
		{
			playerBody.setLinearVelocity(0, 0);
			if(!isStrafing)
				walking_state = State.Standing;
		}
	}
	
	public void draw(SpriteBatch batch)
	{
//		batch.begin();
//		animatedBox2dSprite.draw(batch, fixture);
		box2dSprite.draw(batch, playerBody);
//		batch.end();
	}
	
	public Box2DSprite getSprite()
	{
		return box2dSprite;
	}
	
	public void setPos(Vector2 pos)
	{
		this.playerBody.setTransform(pos, 0);
	}
	
	public void setPos(float x, float y)
	{
		this.playerBody.setTransform(x, y, 0);
	}
	
	public Body getBody()
	{
		return playerBody;
	}
}
