package com.lightofbraxis.game;

import com.badlogic.gdx.graphics.Texture;

public class Player extends Body {
    private boolean m_is_standing;

    public Player(String id, int[] position, Texture texture) {
        super(id, position, texture);
        m_acceleration[1] = (float)(-3.0);
        m_is_standing = true;
    }

    public void updateByInput(char[] inputs) {
        int[] m_velocity = getVelocity();
        float[] m_acceleration = getAcceleration();

        m_velocity[0] = 0; // Set X Velocity to 0 before inputs come in
        for (int i = 0; i < inputs.length; ++i) {
            if (inputs[i] == 'A') {
                m_velocity[0] -= 3;
            } else if (inputs[i] == 'D') {
                m_velocity[0] += 3;
            } else if (inputs[i] == ' ' && m_is_standing) {
                m_velocity[1] = 27;
                m_is_standing = false;
            }
        }
        m_velocity[0] += m_acceleration[0];
        m_velocity[1] += m_acceleration[1];
    }

    public void handleCollision(boolean[] placesOfCollision, int[] prevPosition) {
        super.handleCollision(placesOfCollision, prevPosition);

        if (placesOfCollision[0]) {
            m_is_standing = true;
        }
    }
}
