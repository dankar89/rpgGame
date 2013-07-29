package com.me.mygdxgame;

import com.badlogic.gdx.math.Vector3;

public class GameObject {

	private Vector3 worldPos = Vector3.Zero;
	private Vector3 tmpPos = Vector3.Zero;
	private Vector3 worldCenterPos = Vector3.Zero;
	private Vector3 startPos = Vector3.Zero;
	private float scale;
	
	public GameObject(Vector3 startPos)
	{
		this.startPos = startPos;
	}
}
