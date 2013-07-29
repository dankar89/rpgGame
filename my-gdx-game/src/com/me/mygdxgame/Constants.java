package com.me.mygdxgame;

public final class Constants {
	static final float WORLD_TO_BOX = 0.032f; //0.01
	static final float BOX_TO_WORLD = 100f;
	
	static final String TERRAIN_LAYER_NAME = "terrain";
	static final int TERRAIN_LAYER_INDEX = 0;
	static final String BACKGROUND_LAYER_NAME = "background";
	static final int BACKGROUND_LAYER_INDEX = 1;
	static final String FOREGROUND_LAYER_NAME = "foreground";
	static final int FOREGROUND_LAYER_INDEX = 2;
	static final String OBJECT_LAYER_NAME = "Object layer 1";
	static final int OBJECT_LAYER_INDEX = 3;
	
	static final int TILE_SIZE = 32;
	static final float UNIT_SCALE = 1 / ((float)TILE_SIZE);
}
