package com.nadyagrishina.models;

import com.nadyagrishina.lwjglutils.OGLTexture2D;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class House implements Model {

    private OGLTexture2D textureWalls, textureDoor, textureChimney, textureWindow;

    private static final String[] ROOF_FILES = {
            "textures/roof_snow_00.jpg",
            "textures/roof_snow_25.png",
            "textures/roof_snow_50.png",
            "textures/roof_snow_75.png",
            "textures/roof_snow_90.png"
    };
    private final OGLTexture2D[] roofStages = new OGLTexture2D[ROOF_FILES.length];
    private int  currentRoofStage = 0;
    private float stageTimer      = 0f;
    private static final float STAGE_INTERVAL = 6f;

    public House() {
        try {
            textureWalls = new OGLTexture2D("textures/walls.jpg");
            textureDoor = new OGLTexture2D("textures/door.png");
            textureChimney = new OGLTexture2D("textures/chimney.jpg");
            textureWindow = new OGLTexture2D("textures/window.png");

            for (int i = 0; i < ROOF_FILES.length; i++) {
                roofStages[i] = new OGLTexture2D(ROOF_FILES[i]);
            }

        } catch (IOException e) {
            System.out.println("Error loading texture");
        }
    }

    public void update(float dt) {
        stageTimer += dt;
        if (stageTimer >= STAGE_INTERVAL && currentRoofStage < roofStages.length - 1) {
            stageTimer = 0f;
            currentRoofStage++;
        }
    }

    @Override
    public void draw() {
        glPushMatrix();

        glScalef(6.0f, 3.0f, 6.0f);

        drawWalls();
        drawWindows();
        drawRoof();
        drawDoor();
        drawChimney();

        glPopMatrix();
    }

    private void drawWalls() {
        glEnable(GL_TEXTURE_2D);
        textureWalls.bind();

        glBegin(GL_QUADS);
        // Front wall
        glTexCoord2f(0, 0);
        glVertex3f(-0.5f, 0.0f, 0.5f);
        glTexCoord2f(1, 0);
        glVertex3f(0.5f, 0.0f, 0.5f);
        glTexCoord2f(1, 1);
        glVertex3f(0.5f, 1.0f, 0.5f);
        glTexCoord2f(0, 1);
        glVertex3f(-0.5f, 1.0f, 0.5f);

        // Back wall
        glTexCoord2f(0, 0);
        glVertex3f(-0.5f, 0.0f, -0.5f);
        glTexCoord2f(1, 0);
        glVertex3f(0.5f, 0.0f, -0.5f);
        glTexCoord2f(1, 1);
        glVertex3f(0.5f, 1.0f, -0.5f);
        glTexCoord2f(0, 1);
        glVertex3f(-0.5f, 1.0f, -0.5f);

        // Left wall
        glTexCoord2f(0, 0);
        glVertex3f(-0.5f, 0.0f, -0.5f);
        glTexCoord2f(1, 0);
        glVertex3f(-0.5f, 0.0f, 0.5f);
        glTexCoord2f(1, 1);
        glVertex3f(-0.5f, 1.0f, 0.5f);
        glTexCoord2f(0, 1);
        glVertex3f(-0.5f, 1.0f, -0.5f);

        // Right wall
        glTexCoord2f(0, 0);
        glVertex3f(0.5f, 0.0f, -0.5f);
        glTexCoord2f(1, 0);
        glVertex3f(0.5f, 0.0f, 0.5f);
        glTexCoord2f(1, 1);
        glVertex3f(0.5f, 1.0f, 0.5f);
        glTexCoord2f(0, 1);
        glVertex3f(0.5f, 1.0f, -0.5f);
        glEnd();
    }

    private void drawChimney() {
        glEnable(GL_TEXTURE_2D);
        textureChimney.bind();

        float x1 = 0.30f;
        float x2 = 0.20f;
        float z1 = -0.10f;
        float z2 = -0.00f;
        float y1 = 1.2f;
        float y2 = 1.6f;

        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex3f(x1, y1, z2);
        glTexCoord2f(1, 0);
        glVertex3f(x2, y1, z2);
        glTexCoord2f(1, 1);
        glVertex3f(x2, y2, z2);
        glTexCoord2f(0, 1);
        glVertex3f(x1, y2, z2);

        glTexCoord2f(0, 0);
        glVertex3f(x1, y1, z1);
        glTexCoord2f(1, 0);
        glVertex3f(x2, y1, z1);
        glTexCoord2f(1, 1);
        glVertex3f(x2, y2, z1);
        glTexCoord2f(0, 1);
        glVertex3f(x1, y2, z1);

        glTexCoord2f(0, 0);
        glVertex3f(x1, y1, z1);
        glTexCoord2f(1, 0);
        glVertex3f(x1, y1, z2);
        glTexCoord2f(1, 1);
        glVertex3f(x1, y2, z2);
        glTexCoord2f(0, 1);
        glVertex3f(x1, y2, z1);

        glTexCoord2f(0, 0);
        glVertex3f(x2, y1, z1);
        glTexCoord2f(1, 0);
        glVertex3f(x2, y1, z2);
        glTexCoord2f(1, 1);
        glVertex3f(x2, y2, z2);
        glTexCoord2f(0, 1);
        glVertex3f(x2, y2, z1);

        glEnd();

        glDisable(GL_TEXTURE_2D);
    }

    private void drawRoof() {
        glEnable(GL_TEXTURE_2D);
        textureWalls.bind();

        glBegin(GL_TRIANGLES);
        glTexCoord2f(0, 0);
        glVertex3f(-0.5f, 1.0f, 0.5f);
        glTexCoord2f(1, 0);
        glVertex3f(0.5f, 1.0f, 0.5f);
        glTexCoord2f(0.5f, 1);
        glVertex3f(0.0f, 1.5f, 0.5f);

        glTexCoord2f(0, 0);
        glVertex3f(-0.5f, 1.0f, -0.5f);
        glTexCoord2f(1, 0);
        glVertex3f(0.5f, 1.0f, -0.5f);
        glTexCoord2f(0.5f, 1);
        glVertex3f(0.0f, 1.5f, -0.5f);
        glEnd();

        glDisable(GL_TEXTURE_2D);

        glEnable(GL_TEXTURE_2D);
        roofStages[currentRoofStage].bind();

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-0.5f, 1.0f, -0.6f);
        glTexCoord2f(1, 0);
        glVertex3f(-0.5f, 1.0f, 0.6f);
        glTexCoord2f(1, 1);
        glVertex3f(0.0f, 1.5f, 0.6f);
        glTexCoord2f(0, 1);
        glVertex3f(0.0f, 1.5f, -0.6f);

        glTexCoord2f(0, 0);
        glVertex3f(0.5f, 1.0f, -0.6f);
        glTexCoord2f(1, 0);
        glVertex3f(0.5f, 1.0f, 0.6f);
        glTexCoord2f(1, 1);
        glVertex3f(0.0f, 1.5f, 0.6f);
        glTexCoord2f(0, 1);
        glVertex3f(0.0f, 1.5f, -0.6f);
        glEnd();

        glDisable(GL_TEXTURE_2D);
    }

    private void drawWindows() {
        glEnable(GL_TEXTURE_2D);
        textureWindow.bind();

        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex3f(0.25f, 0.4f, 0.51f);
        glTexCoord2f(1, 0);
        glVertex3f(0.45f, 0.4f, 0.51f);
        glTexCoord2f(1, 1);
        glVertex3f(0.45f, 0.7f, 0.51f);
        glTexCoord2f(0, 1);
        glVertex3f(0.25f, 0.7f, 0.51f);

        glTexCoord2f(0, 0);
        glVertex3f(-0.25f, 0.4f, 0.51f);
        glTexCoord2f(1, 0);
        glVertex3f(-0.45f, 0.4f, 0.51f);
        glTexCoord2f(1, 1);
        glVertex3f(-0.45f, 0.7f, 0.51f);
        glTexCoord2f(0, 1);
        glVertex3f(-0.25f, 0.7f, 0.51f);

        glTexCoord2f(0, 0);
        glVertex3f(0.51f, 0.4f, -0.2f);
        glTexCoord2f(1, 0);
        glVertex3f(0.51f, 0.4f, 0.2f);
        glTexCoord2f(1, 1);
        glVertex3f(0.51f, 0.7f, 0.2f);
        glTexCoord2f(0, 1);
        glVertex3f(0.51f, 0.7f, -0.2f);

        glTexCoord2f(0, 0);
        glVertex3f(-0.51f, 0.4f, -0.2f);
        glTexCoord2f(1, 0);
        glVertex3f(-0.51f, 0.4f, 0.2f);
        glTexCoord2f(1, 1);
        glVertex3f(-0.51f, 0.7f, 0.2f);
        glTexCoord2f(0, 1);
        glVertex3f(-0.51f, 0.7f, -0.2f);

        glEnd();
        glDisable(GL_TEXTURE_2D);
    }


    private void drawDoor() {
        glEnable(GL_TEXTURE_2D);
        textureDoor.bind();

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-0.2f, 0.0f, 0.51f);
        glTexCoord2f(1, 0);
        glVertex3f(0.2f, 0.0f, 0.51f);
        glTexCoord2f(1, 1);
        glVertex3f(0.2f, 0.8f, 0.51f);
        glTexCoord2f(0, 1);
        glVertex3f(-0.2f, 0.8f, 0.51f);
        glEnd();

        glDisable(GL_TEXTURE_2D);
    }
}
