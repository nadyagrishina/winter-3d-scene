package com.nadyagrishina.models;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class SnowSystem implements Model {

    private final List<Snowflake> snowflakes;
    private final Queue<float[]> snowMarks;

    private static final float AREA_SIZE = 15.0f;
    private static final float START_Y = 10.0f;
    private static final float EXTRA_Y = 5.0f;
    private static final Random random = new Random();
    private static final int MAX_SNOW_MARKS = 4000;

    public SnowSystem(int numSnowflakes) {
        snowflakes = new ArrayList<>();
        snowMarks = new LinkedList<>();
        for (int i = 0; i < numSnowflakes; i++) {
            snowflakes.add(createNewFlake());
        }
    }

    private Snowflake createNewFlake() {
        float x = (random.nextFloat() - 0.5f) * AREA_SIZE;
        float y = START_Y + random.nextFloat() * EXTRA_Y;
        float z = (random.nextFloat() - 0.5f) * AREA_SIZE;
        float speed = 0.01f + random.nextFloat() * 0.01f;

        return new Snowflake(x, y, z, speed);
    }

    @Override
    public void draw() {
        for (float[] mark : snowMarks) {
            drawSnowMarks(mark[0], mark[1], mark[2]);
        }

        for (Snowflake snowflake : snowflakes) {
            snowflake.fall();

            float x = snowflake.getX();
            float y = snowflake.getY();
            float z = snowflake.getZ();

            if (y <= 0.0f) {
                if (snowMarks.size() >= MAX_SNOW_MARKS) {
                    snowMarks.poll();
                }
                snowMarks.add(new float[]{x, 0.0f, z});
                resetFlake(snowflake);
            }
            snowflake.draw();
        }
    }

    private void resetFlake(Snowflake flake) {
        flake.setPosition(
                (random.nextFloat() - 0.5f) * AREA_SIZE,
                START_Y + random.nextFloat() * EXTRA_Y,
                (random.nextFloat() - 0.5f) * AREA_SIZE
        );
    }

    private void drawSnowMarks(float x, float y, float z) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
        y += 0.01F;
        float radius = 0.1f;
        int segments = 16;

        glBegin(GL_TRIANGLE_FAN);
        glVertex3f(x, y, z);
        for (int i = 0; i <= segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            float dx = (float) Math.cos(angle) * radius;
            float dz = (float) Math.sin(angle) * radius;
            glVertex3f(x + dx, y, z + dz);
        }
        glEnd();
    }
}
