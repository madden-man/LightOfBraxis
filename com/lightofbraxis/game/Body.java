package com.lightofbraxis.game;

import com.badlogic.gdx.graphics.Texture;

public class Body {
    protected String m_id;
    protected int[] m_position;
    protected int[] m_velocity;
    protected float[] m_acceleration;
    protected Texture m_texture;

    public static final int[] ZERO_TWICE = { 0, 0 };

    public Body(String id, int[] position, Texture texture) {
        m_id = id;
        m_position = position;
        m_texture = texture;
        m_velocity = new int[2];
        m_acceleration = new float[2];
    }

    public String getId() { return m_id; }

    public int[] getPlacement() {
        int[] placement = new int[4];

        placement[0] = m_position[0];
        placement[1] = m_position[1];
        placement[2] = m_position[0] + m_texture.getWidth();
        placement[3] = m_position[1] + m_texture.getHeight();

        return placement;
    }

    public void handleCollision(boolean[] placesOfCollision, int[] prevPosition) {
        // left and right
        if ((m_velocity[0] < 0 && placesOfCollision[1])
            || (m_velocity[0] > 0 && placesOfCollision[3])) {
            m_velocity[0] = 0;
            m_position[0] = prevPosition[0];
        }
        // up and down
        if ((m_velocity[1] < 0 && placesOfCollision[0])
        || (m_velocity[1] > 0 && placesOfCollision[2])) {
            m_velocity[1] = 0;
            m_position[1] = prevPosition[1];
        }
    }

    public int[] getPosition() { return m_position; }

    public void setPosition(int[] position) {
        m_position = position;
    }

    public int[] getVelocity() { return m_velocity; }

    public void setVelocity(int[] velocity) { m_velocity = velocity; }

    public float[] getAcceleration() { return m_acceleration; }

    public void setAcceleration(float[] acceleration) { m_acceleration = acceleration; }

    public Texture getTexture() {
        return m_texture;
    }
}
