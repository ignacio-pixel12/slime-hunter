package com.slimehunter.entidad;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EntidadEstatica extends Entidad {

    private boolean plataformaUnidireccional;

    public EntidadEstatica(TextureRegion region, float x, float y, float anchoColision, float altoColision) {
        super(region, x, y, anchoColision, altoColision);
    }

    public boolean esPlataformaUnidireccional() {
        return this.plataformaUnidireccional;
    }

    public void setPlataformaUnidireccional(boolean plataformaUnidireccional) {
        this.plataformaUnidireccional = plataformaUnidireccional;
    }
}
