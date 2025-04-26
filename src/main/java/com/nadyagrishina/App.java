package com.nadyagrishina;

import com.nadyagrishina.render.Renderer;
import com.nadyagrishina.view.LwjglWindow;

public class App {
    public static void main(String[] args) {
        new LwjglWindow(new Renderer());
    }
}
