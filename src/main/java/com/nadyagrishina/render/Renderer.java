package com.nadyagrishina.render;

import com.nadyagrishina.lwjglutils.OGLTexture2D;
import com.nadyagrishina.models.Ground;
import com.nadyagrishina.models.House;
import com.nadyagrishina.models.Model;
import com.nadyagrishina.models.SnowSystem;
import com.nadyagrishina.utils.GLCamera;
import com.nadyagrishina.utils.InfoBox;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nadyagrishina.utils.GluUtils.gluPerspective;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Renderer extends AbstractRenderer {
    private int frames = 0;
    private long timer = System.currentTimeMillis();
    private int currentFPS = 0;

    private boolean isInfoBox = true;
    private boolean isMousePressed = false;

    private int snowflakeCount = 2000;
    private SnowSystem snowSystem;
    private House house;

    private GLCamera camera;
    private List<Model> models;
    private OGLTexture2D texture;

    private double lastX = -1.0;
    private double lastY = -1.0;

    Set<Integer> pressedKeys = new HashSet<>();

    private double lastFrameTime;

    @Override
    public void init() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        camera = new GLCamera();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(45, 800 / 600.0, 0.1, 100.0);

        try {
            texture = new OGLTexture2D("textures/sky.jpg");
        } catch (Exception e) {
            System.out.println("Error loading texture");
        }

        snowSystem = new SnowSystem(snowflakeCount);
        house = new House();

        models = new ArrayList<>();
        models.add(new Ground());
        models.add(house);
        models.add(snowSystem);

        lastFrameTime = glfwGetTime();
    }

    @Override
    public void display() {

        double now   = glfwGetTime();
        float  dt    = (float) (now - lastFrameTime);
        lastFrameTime = now;

        house.update(dt);
        house.draw();

        handleContinuousInput();
        frames++;
        if (System.currentTimeMillis() - timer > 1000) {
            currentFPS = frames;
            frames = 0;
            timer += 1000;
        }

        int error = glGetError();
        if (error != GL_NO_ERROR) {
            System.out.println("OpenGL Error: " + error);
        }

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.1f, 0.1f, 0.2f, 1.0f);

        glDisable(GL_DEPTH_TEST);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnable(GL_TEXTURE_2D);
        texture.bind();

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-100, -100, -99);
        glTexCoord2f(1, 0);
        glVertex3f(100, -100, -99);
        glTexCoord2f(1, 1);
        glVertex3f(100, 100, -99);
        glTexCoord2f(0, 1);
        glVertex3f(-100, 100, -99);
        glEnd();

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);

        camera.setMatrix();

        drawScene();
    }

    public void drawScene() {
        for (Model model : models) {
            model.draw();
        }
        if (isInfoBox) {
            InfoBox.draw(currentFPS, snowflakeCount);
        }
    }

    protected GLFWKeyCallback glfwKeyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            } else if (key == GLFW_KEY_P && action == GLFW_RELEASE) {
                snowflakeCount += 100;
                snowSystem = new SnowSystem(snowflakeCount);
                models.set(2, snowSystem);
            } else if (key == GLFW_KEY_M && action == GLFW_RELEASE) {
                snowflakeCount -= 100;
                snowSystem = new SnowSystem(snowflakeCount);
                models.set(2, snowSystem);
            } else if (key == GLFW_KEY_I && action == GLFW_RELEASE) {
                isInfoBox = !isInfoBox;
            } else if (action == GLFW_PRESS) {
                pressedKeys.add(key);
            } else if (action == GLFW_RELEASE) {
                pressedKeys.remove(key);
            }
        }
    };

    protected GLFWMouseButtonCallback glfwMouseButtonCallback = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            if (button == GLFW_MOUSE_BUTTON_1) {
                isMousePressed = (action == GLFW_PRESS);
                if (isMousePressed) {
                    lastX = -1.0;
                    lastY = -1.0;
                }
            }
        }
    };

    protected GLFWScrollCallback glfwScrollCallback = new GLFWScrollCallback() {
        @Override
        public void invoke(long window, double xOffset, double yOffset) {
            double zoomSpeed = 0.5;
            if (yOffset > 0) {
                camera.forward(zoomSpeed);
            } else if (yOffset < 0) {
                camera.backward(zoomSpeed);
            }
        }
    };

    protected GLFWCursorPosCallback glfwCursorPosCallback = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double xpos, double ypos) {
            if (isMousePressed) {
                if (lastX == -1.0 || lastY == -1.0) {
                    lastX = xpos;
                    lastY = ypos;
                    return;
                }

                double deltaX = xpos - lastX;
                double deltaY = ypos - lastY;

                double smoothingFactor = 0.03;
                camera.addAzimuth(deltaX * smoothingFactor);
                camera.addZenith(-deltaY * smoothingFactor);

                lastX = xpos;
                lastY = ypos;
            }
        }
    };

    private void handleContinuousInput() {
        double speed = 0.1;

        if (pressedKeys.contains(GLFW_KEY_W)) camera.forward(speed);
        if (pressedKeys.contains(GLFW_KEY_S)) camera.backward(speed);
        if (pressedKeys.contains(GLFW_KEY_A)) camera.left(speed);
        if (pressedKeys.contains(GLFW_KEY_D)) camera.right(speed);
    }

    @Override
    public GLFWKeyCallback getGlfwKeyCallback() {
        return glfwKeyCallback;
    }

    @Override
    public GLFWMouseButtonCallback getGlfwMouseButtonCallback() {
        return glfwMouseButtonCallback;
    }

    @Override
    public GLFWCursorPosCallback getGlfwCursorPosCallback() {
        return glfwCursorPosCallback;
    }

    @Override
    public GLFWScrollCallback getGlfwScrollCallback() {
        return glfwScrollCallback;
    }
}