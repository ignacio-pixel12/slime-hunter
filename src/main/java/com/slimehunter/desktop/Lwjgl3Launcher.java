package com.slimehunter.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.slimehunter.Constantes;
import com.slimehunter.SlimeHunter;

public class Lwjgl3Launcher {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration configuracion = new Lwjgl3ApplicationConfiguration();
        configuracion.setTitle("Slime Hunter");
        configuracion.setWindowedMode(Constantes.ANCHO_VENTANA, Constantes.ALTO_VENTANA);
        configuracion.useVsync(true);
        configuracion.setForegroundFPS(Constantes.FPS_OBJETIVO);

        new Lwjgl3Application(new SlimeHunter(), configuracion);
    }
}
