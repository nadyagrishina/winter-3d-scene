package com.nadyagrishina.models;

import com.nadyagrishina.lwjglutils.OGLTexture2D;

import java.io.IOException;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class Snowflake implements Model {

    private float x, y, z;
    private final float speed;

    // Náhodný postranní pohyb
    private final float driftX;
    private final float driftZ;

    private final float size;
    private float rotation;
    private final float rotationSpeed;

    private static final Random random = new Random();
    private static OGLTexture2D texture;

    public Snowflake(float x, float y, float z, float speed) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.speed = speed;

        // Náhodné odchylky v pohybu
        this.driftX = (random.nextFloat() - 0.5f) * 0.01f;
        this.driftZ = (random.nextFloat() - 0.5f) * 0.01f;

        // Náhodná velikost a počáteční rotace
        this.size = 0.5f + random.nextFloat();
        this.rotation = random.nextFloat() * 360f;
        this.rotationSpeed = 0.5f + random.nextFloat() * 1.5f;

        if (texture == null) {
            try {
                texture = new OGLTexture2D("textures/snowflake.png");
            } catch (IOException e) {
                System.out.println("Error loading snowflake texture");
            }
        }
    }

    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void draw() {

        glPushMatrix();
        glTranslatef(x, y, z);
        glRotatef(rotation, 0f, 0.3f, 0.05f);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        texture.bind();

        glColor4f(1f, 1f, 1f, 0.8f);

        float halfSize = size * 0.05f;

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-halfSize, -halfSize, 0);
        glTexCoord2f(1, 0);
        glVertex3f(halfSize, -halfSize, 0);
        glTexCoord2f(1, 1);
        glVertex3f(halfSize, halfSize, 0);
        glTexCoord2f(0, 1);
        glVertex3f(-halfSize, halfSize, 0);
        glEnd();

        glDisable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);

        glPopMatrix();
    }

    public void fall() {
        y -= speed;
        x += driftX;
        z += driftZ;

        rotation += rotationSpeed;
        if (rotation >= 360f) rotation -= 360f;

        if (y < 0.0f) {
            y = 0.0f;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}