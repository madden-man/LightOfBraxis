package com.lightofbraxis.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.*;

public class Ignition extends ApplicationAdapter {
	OrthographicCamera camera;
	SpriteBatch batch;
	private Player player;
	private ArrayList<Body> bodyList;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(1280, 720);
		bodyList = new ArrayList<Body>();

		camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		int[] playerCoords = { 100, 110 };
		player = new Player(IDTracker.PLAYER_ID, playerCoords, new Texture("player.png"));
		bodyList.add(player);

		int[] floorCoords = { 0, -83 };
		Body woodFloor = new Body(IDTracker.WOOD_FLOOR, floorCoords, new Texture("wood.jpg"));
		bodyList.add(woodFloor);

		int[] ledgeCoords = { 450, 100 };
		Body brickLedge = new Body(IDTracker.BRICK_LEDGE, ledgeCoords, new Texture("bricks.jpeg"));
		bodyList.add(brickLedge);
	}

	public void updatePlayerLocation() {
		char[] inputs = new char[3];
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			inputs[0] = 'A';
		} if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			inputs[1] = 'D';
		} if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			inputs[2] = ' ';
		}
		player.updateByInput(inputs);
	}

	public void checkForCollisions(Body body, int[] prevPosition) {
		int[] placement = body.getPlacement();

		for (int i = 0; i < bodyList.size(); ++i) {
			Body otherBody = bodyList.get(i);
			int[] otherPlacement = otherBody.getPlacement();

			boolean bottomLeft = placement[0] > otherPlacement[0] && placement[0] < otherPlacement[2]
				&& placement[1] < otherPlacement[3] && placement[1] > otherPlacement[1];

			boolean bottomRight = placement[2] > otherPlacement[0] && placement[2] < otherPlacement[2]
				&& placement[1] < otherPlacement[3] && placement[1] > otherPlacement[1];

			boolean topLeft = placement[0] > otherPlacement[0] && placement[0] < otherPlacement[2]
				&& placement[3] < otherPlacement[3] && placement[3] > otherPlacement[1];

			boolean topRight = placement[2] > otherPlacement[0] && placement[2] < otherPlacement[2]
				&& placement[3] < otherPlacement[3] && placement[3] > otherPlacement[1];

			boolean bottom = (bottomLeft || bottomRight) &&
				(Math.abs(placement[1] - otherPlacement[3]) < 10 || body.getVelocity()[1] < -10);
			boolean top = (topLeft || topRight) && Math.abs(placement[3] - otherPlacement[1]) < 10;
			boolean left = (bottomLeft || topLeft) && Math.abs(placement[0] - otherPlacement[2]) < 10;
			boolean right = (bottomRight || topRight) && Math.abs(placement[2] - otherPlacement[0]) < 10;

			if (bottom || bottom || top || right) {
				body.handleCollision(new boolean[]{ bottom, left, top, right }, prevPosition);
			}
		}
	}

	@Override
	public void render () {
		updatePlayerLocation();

		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		for (int i = 0; i < bodyList.size(); ++i) {
			Body body = bodyList.get(i);

			int[] velocity = body.getVelocity();
			int[] coords = body.getPosition();
			int updatedX = coords[0] + velocity[0];
			int updatedY = coords[1] + velocity[1];
			body.setPosition(new int[]{ updatedX, updatedY });

			if (velocity[0] != 0 || velocity[1] != 0) {
				checkForCollisions(body, coords);
			}
			batch.draw(body.getTexture(), body.getPosition()[0], body.getPosition()[1]);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		for (int i = 0; i < bodyList.size(); ++i) {
			bodyList.get(i).getTexture().dispose();
		}
	}
}
