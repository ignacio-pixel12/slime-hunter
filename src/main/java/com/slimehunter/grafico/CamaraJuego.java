package com.slimehunter.grafico;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CamaraJuego {

    private final OrthographicCamera camara;
    private final float anchoVista;
    private final float altoVista;
    private float limiteIzquierdo;
    private float limiteDerecho;
    private float limiteInferior;
    private float limiteSuperior;

    public CamaraJuego(float anchoPantalla, float altoPantalla) {
        this.anchoVista = anchoPantalla;
        this.altoVista = altoPantalla;
        this.camara = new OrthographicCamera(anchoPantalla, altoPantalla);
        this.camara.position.set(anchoPantalla / 2f, altoPantalla / 2f, 0);
        this.limiteIzquierdo = 0;
        this.limiteDerecho = Float.MAX_VALUE;
        this.limiteInferior = 0;
        this.limiteSuperior = Float.MAX_VALUE;
    }

    public void seguir(Vector2 posicion) {
        float objetivoX = posicion.x + this.anchoVista / 4f;
        float objetivoY = posicion.y;

        this.camara.position.x = MathUtils.lerp(this.camara.position.x, objetivoX, 0.1f);
        this.camara.position.y = MathUtils.lerp(this.camara.position.y, objetivoY, 0.1f);

        float mediaAncho = this.anchoVista / 2f;
        float mediaAlto = this.altoVista / 2f;

        this.camara.position.x = MathUtils.clamp(
            this.camara.position.x,
            this.limiteIzquierdo + mediaAncho,
            this.limiteDerecho - mediaAncho
        );
        this.camara.position.y = MathUtils.clamp(
            this.camara.position.y,
            this.limiteInferior + mediaAlto,
            this.limiteSuperior - mediaAlto
        );

        this.camara.update();
    }

    public void setLimites(float izq, float der, float inf, float sup) {
        this.limiteIzquierdo = izq;
        this.limiteDerecho = der;
        this.limiteInferior = inf;
        this.limiteSuperior = sup;
    }

    public OrthographicCamera getCamara() {
        return this.camara;
    }
}
