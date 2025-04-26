package com.nadyagrishina.utils;

import static org.lwjgl.opengl.GL11.*;

public class InfoBox {

    public static void draw(int fps, int snowflakeCount) {
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, 800, 600, 0, -1, 1);

        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();

        glDisable(GL_DEPTH_TEST);
        glDisable(GL_TEXTURE_2D);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glColor4f(0f, 0f, 0f, 0.2f);
        glBegin(GL_QUADS);
        glVertex2f(10, 240);
        glVertex2f(310, 240);
        glVertex2f(310, 10);
        glVertex2f(10, 10);
        glEnd();

        glColor3f(1f, 1f, 1f);
        int y = 20;
        int lineHeight = 20;

        drawText(20, y, "FPS: " + fps);
        drawText(150, y, "Snowflakes: " + snowflakeCount);
        y += lineHeight;

        drawText(20, y, "Name: Zimni Krajina");
        y += lineHeight;
        drawText(20, y, "Course: PGRF2");
        y += lineHeight;
        drawText(20, y, "Author: Nadezhda Grishina");
        y += lineHeight;
        drawText(20, y, "Last Update: Apr 24, 2025");
        y += lineHeight * 2;

        drawText(20, y, "Controls:");
        y += lineHeight;
        drawText(20, y, "Mouse + WSAD, Wheel - move camera");
        y += lineHeight;
        drawText(20, y, "Keys P, M - +- 100 snowflakes");
        y += lineHeight;
        drawText(20, y, "Key I - show/hide InfoBox");
        y += lineHeight;
        drawText(20, y, "ESC - exit");

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);

        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
    }

    private static void drawText(float x, float y, String text) {
        var buffer = org.lwjgl.BufferUtils.createByteBuffer(9999);
        int quads = org.lwjgl.stb.STBEasyFont.stb_easy_font_print(0, 0, text, null, buffer);

        glPushMatrix();
        glTranslatef(x, y, 0);
        glScalef(1.4f, 1.4f, 1f);

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 16, buffer);
        glDrawArrays(GL_QUADS, 0, quads * 4);
        glDisableClientState(GL_VERTEX_ARRAY);

        glPopMatrix();
    }
}
