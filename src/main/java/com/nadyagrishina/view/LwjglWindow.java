package com.nadyagrishina.view;

import com.nadyagrishina.render.AbstractRenderer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class LwjglWindow {

    public static int WIDTH = 800;
    public static int HEIGHT = 600;

    private long window;
    private final AbstractRenderer renderer;

    private static boolean DEBUG = false;

    static {
        if (DEBUG) {
            System.setProperty("org.lwjgl.util.Debug", "true");
            System.setProperty("org.lwjgl.util.NoChecks", "false");
            System.setProperty("org.lwjgl.util.DebugLoader", "true");
            System.setProperty("org.lwjgl.util.DebugAllocator", "true");
            System.setProperty("org.lwjgl.util.DebugStack", "true");
            Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
        }
    }

    public LwjglWindow(AbstractRenderer renderer) {
        this(WIDTH, HEIGHT, renderer, false);
    }

    public LwjglWindow(AbstractRenderer renderer, boolean debug) {
        this(WIDTH, HEIGHT, renderer, debug);
    }

    public LwjglWindow(int width, int height, AbstractRenderer renderer, boolean debug) {
        this.renderer = renderer;
        DEBUG = debug;
        WIDTH = width;
        HEIGHT = height;
        if (DEBUG)
            System.err.println("Run in debugging mode");
        run();
    }

    public void run() {
        init();
        loop();

        renderer.dispose();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);


        window = glfwCreateWindow(WIDTH, HEIGHT,
                "Zimní krajina – Nadezhda Grishina | PGRF2 | 24.04.2025",
                NULL, NULL);

        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, renderer.getGlfwKeyCallback());
        glfwSetWindowSizeCallback(window, renderer.getGlfwWindowSizeCallback());
        glfwSetMouseButtonCallback(window, renderer.getGlfwMouseButtonCallback());
        glfwSetCursorPosCallback(window, renderer.getGlfwCursorPosCallback());
        glfwSetScrollCallback(window, renderer.getGlfwScrollCallback());

        if (DEBUG)
            glfwSetErrorCallback(new GLFWErrorCallback() {
                final GLFWErrorCallback delegate = GLFWErrorCallback.createPrint(System.err);

                @Override
                public void invoke(int error, long description) {
                    if (error == GLFW_VERSION_UNAVAILABLE)
                        System.err.println("GLFW_VERSION_UNAVAILABLE: This demo requires OpenGL 2.0 or higher.");
                    if (error == GLFW_NOT_INITIALIZED)
                        System.err.println();
                    if (error == GLFW_NO_CURRENT_CONTEXT)
                        System.err.println("GLFW_NO_CURRENT_CONTEXT");
                    if (error == GLFW_INVALID_ENUM)
                        System.err.println("GLFW_INVALID_ENUM");
                    if (error == GLFW_INVALID_VALUE)
                        System.err.println("GLFW_INVALID_VALUE");
                    if (error == GLFW_OUT_OF_MEMORY)
                        System.err.println("GLFW_OUT_OF_MEMORY");
                    if (error == GLFW_API_UNAVAILABLE)
                        System.err.println("GLFW_API_UNAVAILABLE");
                    if (error == GLFW_VERSION_UNAVAILABLE)
                        System.err.println("GLFW_VERSION_UNAVAILABLE");
                    if (error == GLFW_PLATFORM_ERROR)
                        System.err.println("GLFW_PLATFORM_ERROR");
                    if (error == GLFW_FORMAT_UNAVAILABLE)
                        System.err.println("GLFW_FORMAT_UNAVAILABLE");
                    if (error == GLFW_FORMAT_UNAVAILABLE)
                        System.err.println("GLFW_FORMAT_UNAVAILABLE");

                    delegate.invoke(error, description);
                }

                @Override
                public void free() {
                    delegate.free();
                }
            });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();

        if (DEBUG)
            GLUtil.setupDebugMessageCallback();

        renderer.init();

        while (!glfwWindowShouldClose(window)) {
            renderer.display();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public long getWindow() {
        return window;
    }
}