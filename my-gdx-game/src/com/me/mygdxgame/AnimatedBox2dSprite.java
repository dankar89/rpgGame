//package com.me.mygdxgame;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
//import com.badlogic.gdx.physics.box2d.CircleShape;
//import com.badlogic.gdx.physics.box2d.Fixture;
//import com.badlogic.gdx.physics.box2d.FixtureDef;
//import com.badlogic.gdx.physics.box2d.Shape;
//import com.badlogic.gdx.physics.box2d.Shape.Type;
//import com.badlogic.gdx.physics.box2d.World;
//
//public class AnimatedBox2dSprite extends Sprite {
//
//	protected BodyDef bodyDef;	
//	protected Body body;
//	protected Shape shape;
//	protected FixtureDef fixtureDef;
//	protected Fixture fixture;
//	protected Map<String, Animation> animations = new HashMap<String, Animation>();
//	
//	public AnimatedBox2dSprite(BodyType bodyType, Shape.Type shapeType, World world, Vector2 worldPosition, ) {
//		super();
//		
//		bodyDef = new BodyDef();
//		bodyDef.type = bodyType;
//		// Set our body's starting position in the world
//		bodyDef.position.set(worldPosition.x, worldPosition.y);
//		
//		
//		// Create our body in the world using our body definition
//		body = world.createBody(bodyDef);
//
//		if(shapeType == Type.Circle)
//		{
//			circle = new CircleShape();
//			circle.setRadius((getRegionWidth() / 2) * Constants.WORLD_TO_BOX);	
//		}
//		
//
//		// Create a fixture definition to apply our shape to
//		fixtureDef = new FixtureDef();
//		fixtureDef.shape = circle;
//		fixtureDef.density = 0.5f; 
//		fixtureDef.friction = 0.4f;
//		fixtureDef.restitution = 0.6f; // Make it bounce a little bit
//
//		// Create our fixture and attach it to the body
//		fixture = body.createFixture(fixtureDef);
//	}
//	
//	protected void addAnimation(String name, Animation animation)
//	{
//		animations.put(name, animation);
//	}
//}
