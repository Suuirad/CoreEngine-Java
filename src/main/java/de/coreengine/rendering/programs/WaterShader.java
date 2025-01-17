/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2019, Suuirad
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.coreengine.rendering.programs;

import de.coreengine.asset.AssetDatabase;
import de.coreengine.asset.FileLoader;
import de.coreengine.rendering.renderable.Camera;
import de.coreengine.rendering.renderable.Water;
import de.coreengine.util.Toolbox;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * Shader for the water renderer
 *
 * @author Darius Dinger
 */
public class WaterShader extends Shader {

    private final int dudvMapUnit = 0, reflectionTextureUnit = 1, refractionTextureUnit = 2, depthTexureUnit = 3,
            normalMapUnit = 4;

    private int mMatLoc, tilingLoc, offsetLoc, waveStrengthLoc, addColorLoc, mulColorLoc, softEdgeDepthLoc, vpMatLoc,
            camPosLoc, shininessLoc, shineDamperLoc, reflectionEnabledLoc, refractionEnabledLoc;

    @Override
    protected void addShaders() {
        addShader(FileLoader.getResource(Shader.SHADERS_LOCATION + "water.vert", true), GL20.GL_VERTEX_SHADER,
                "Water Vertex Shader");
        addShader(FileLoader.getResource(Shader.SHADERS_LOCATION + "water.frag", true), GL20.GL_FRAGMENT_SHADER,
                "Water Fragment Shader");
    }

    @Override
    protected void bindAttribs() {
        bindAttribute(0, "position");
    }

    @Override
    protected void loadUniforms() {
        bindTextureUnit("dudvMap", dudvMapUnit);
        bindTextureUnit("normalMap", normalMapUnit);
        bindTextureUnit("reflectionTexture", reflectionTextureUnit);
        bindTextureUnit("refractionTexture", refractionTextureUnit);
        bindTextureUnit("depthTexture", depthTexureUnit);

        mMatLoc = getUniformLocation("mMat");
        tilingLoc = getUniformLocation("tiling");
        offsetLoc = getUniformLocation("offset");
        waveStrengthLoc = getUniformLocation("waveStrength");
        addColorLoc = getUniformLocation("additiveColor");
        mulColorLoc = getUniformLocation("multiplicativeColor");
        softEdgeDepthLoc = getUniformLocation("softEdgeDepth");
        vpMatLoc = getUniformLocation("vpMat");
        camPosLoc = getUniformLocation("camPos");
        shininessLoc = getUniformLocation("shininess");
        shineDamperLoc = getUniformLocation("shineDamper");
        refractionEnabledLoc = getUniformLocation("refractionEnabled");
        reflectionEnabledLoc = getUniformLocation("reflectionEnabled");
    }

    /**
     * Setting camera for next water
     * 
     * @param cam Camera to set
     */
    public void setCamera(Camera cam) {
        setUniform(camPosLoc, cam.getPosition().x, cam.getPosition().y, cam.getPosition().z);
        setUniform(vpMatLoc, Toolbox.matrixToFloatArray(cam.getViewProjectionMatrix()));
    }

    /**
     * Prepare shader for next water to render
     * 
     * @param water Next water
     */
    public void prepareWater(Water water) {
        bindTexture(AssetDatabase.getTexture(water.getDudvMap()), dudvMapUnit, GL11.GL_TEXTURE_2D);
        bindTexture(AssetDatabase.getTexture(water.getNormalMap()), normalMapUnit, GL11.GL_TEXTURE_2D);
        bindTexture(water.getReflectionFbo().getColorAttachment0(), reflectionTextureUnit, GL11.GL_TEXTURE_2D);
        bindTexture(water.getRefractionFbo().getColorAttachment0(), refractionTextureUnit, GL11.GL_TEXTURE_2D);
        bindTexture(water.getRefractionFbo().getDepthAttachment(), depthTexureUnit, GL11.GL_TEXTURE_2D);

        setUniform(mMatLoc, Toolbox.matrixToFloatArray(water.getTransMat()));
        setUniform(tilingLoc, water.getTiling());
        setUniform(offsetLoc, water.getOffset());
        setUniform(waveStrengthLoc, water.getWaveStrength());
        setUniform(addColorLoc, water.getAdditiveColor());
        setUniform(mulColorLoc, water.getMultiplicativeColor().getRed(), water.getMultiplicativeColor().getGreen(),
                water.getMultiplicativeColor().getBlue(),
                water.getTransparency());
        setUniform(softEdgeDepthLoc, water.getSoftEdgeDepth());
        setUniform(shininessLoc, water.getShininess());
        setUniform(shineDamperLoc, water.getShineDamper());
        setUniform(reflectionEnabledLoc, water.isReflectionEnabled());
        setUniform(refractionEnabledLoc, water.isRefractionEnabled());
    }
}
