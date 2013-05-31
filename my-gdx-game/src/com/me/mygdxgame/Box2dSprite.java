package com.me.mygdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.me.mygdxgame.Player.State;

public class Box2dSprite extends Box2dGameObject {

	
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
	
	public Box2dSprite(World world, Vector2 pos, BodyDef bodyDef,
			Fixture fixture, Shape shape, boolean isActive) {
		super(world, pos, bodyDef, fixture, shape, isActive);
		
		
	}
	
	public void draw(SpriteBatch batch)
	{
		batch.draw(currentFrame,
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

	public Vector3 getWorldPosition() {
		worldPos.x =  sprite.getX() / 32;
		worldPos.y =  sprite.getY() / 32;
		return worldPos;
	}	

}
