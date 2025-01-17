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
package de.coreengine.rendering.renderable.gui;

import de.coreengine.rendering.model.Color;
import de.coreengine.rendering.model.Material;
import de.coreengine.rendering.model.Transformation;
import de.coreengine.rendering.renderable.Camera;
import de.coreengine.rendering.renderer.MasterRenderer;
import de.coreengine.util.Toolbox;

/**
 * Class that represent a pane for a gui
 *
 * @author Darius Dinger
 */
public class GUIPane {

    // Text of the pane
    private GUIText text = new GUIText();

    // Transformation of the gui
    private Transformation transform = new Transformation();

    // Color and texture of the pane
    private Color color = new Color();
    private String texture = Material.TEXTURE_BLACK;

    // Mouse picking
    private final Color pickColor;

    // Rendering text of the pane?
    private boolean renderText = false;

    // Is the gui pane always facing the camera?
    private boolean facingCamera = false;

    /**
     * Creating new GUI Pane and setting its parent or null, if no parent gui exist
     * 
     * @param parent Parent GUI or null
     */
    public GUIPane(GUIPane parent) {

        // Add to parent if exist
        if (parent != null)
            parent.transform.addChild(transform);

        pickColor = Toolbox.generateRandomColor();
    }

    /**
     * Enable text of the pane
     */
    public void enableText() {
        renderText = true;
    }

    /**
     * Disable text of the pane
     */
    public void disableText() {
        renderText = false;
    }

    /**
     * @return Rendering text of the pane?
     */
    public boolean renderText() {
        return renderText;
    }

    /**
     * @return Read/Writeable color of the pane
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return TextureData of the pane
     */
    public String getTexture() {
        return texture;
    }

    /**
     * @param texture New texture of the pane
     */
    public void setTexture(String texture) {
        this.texture = texture;
    }

    /**
     * Setting vertical scale of the pane relative to the parent pane.<br>
     * scaleX = scaleX * parent.scaleX
     * 
     * @param scaleX Vertical scale
     */
    public void setScaleX(float scaleX) {
        transform.setScaleX(scaleX);
        text.setLineWidth(transform.getGlobalScaleX() * 2.0f);
    }

    /**
     * Setting horizontal scale of the pane relative to the parent pane.<br>
     * scaleY = scaleY * parent.scaleY
     * 
     * @param scaleY Horizontal scale
     */
    public void setScaleY(float scaleY) {
        transform.setScaleY(scaleY);
    }

    /**
     * Getting vertical scale of the pane relative to the parent pane.
     * 
     * @return Vertical scale
     */
    public float getScaleX() {
        return transform.getScaleX();
    }

    /**
     * Getting horizontal scale of the pane relative to the parent pane.
     * 
     * @return Horizontal scale
     */
    public float getScaleY() {
        return transform.getScaleY();
    }

    /**
     * Getting Z scale of the pane relative to the parent pane.
     * 
     * @return Z scale
     */
    public float getScaleZ() {
        return transform.getScaleZ();
    }

    /**
     * Setting x pos of the pane relative to the parent pane.<br>
     * posX = posX + parent.posX
     * 
     * @param posX X pos
     */
    public void setPosX(float posX) {
        transform.setPosX(posX);
    }

    /**
     * Setting y pos of the pane relative to the parent pane.<br>
     * posY = posY + parent.posY
     * 
     * @param posY Y pos
     */
    public void setPosY(float posY) {
        transform.setPosY(posY);
    }

    /**
     * Setting z pos of the pane relative to the parent pane.<br>
     * posZ = posZ + parent.posZ
     * 
     * @param posZ Z pos
     */
    public void setPosZ(float posZ) {
        transform.setPosZ(posZ);
    }

    /**
     * Setting x rotation of the pane relative to the parent pane.<br>
     * rotX = rotX + parent.rotX
     * 
     * @param rotX X rotation
     */
    public void setRotX(float rotX) {
        transform.setRotX(rotX);
    }

    /**
     * Setting y rotation of the pane relative to the parent pane.<br>
     * rotY = rotY + parent.rotY
     * 
     * @param rotY Y rotation
     */
    public void setRotY(float rotY) {
        transform.setRotY(rotY);
    }

    /**
     * Setting z rotation of the pane relative to the parent pane.<br>
     * rotZ = rotZ + parent.rotZ
     * 
     * @param rotZ Z rotation
     */
    public void setRotZ(float rotZ) {
        transform.setRotZ(rotZ);
    }

    /**
     * Getting x rotation of the pane relative to the parent pane.
     * 
     * @return X rotation
     */
    public float getRotX() {
        return transform.getRotX();
    }

    /**
     * Getting y rotation of the pane relative to the parent pane.
     * 
     * @return Y rotation
     */
    public float getRotY() {
        return transform.getRotY();
    }

    /**
     * Getting z rotation of the pane relative to the parent pane.
     * 
     * @return Z rotation
     */
    public float getRotZ() {
        return transform.getRotZ();
    }

    /**
     * Getting x pos of the pane relative to the parent pane.
     * 
     * @return X pos
     */
    public float getPosX() {
        return transform.getPosX();
    }

    /**
     * Getting y pos of the pane relative to the parent pane.
     * 
     * @return Y pos
     */
    public float getPosY() {
        return transform.getPosY();
    }

    /**
     * Getting z pos of the pane relative to the parent pane.
     * 
     * @return Z pos
     */
    public float getPosZ() {
        return transform.getPosZ();
    }

    /**
     * Returns true if mouse is over gui pane. <br>
     * 
     * <b>Only works, if picking is enabled!!!</b>
     * 
     * @return Is mouse over gui
     */
    public boolean isMouseOver() {
        return pickColor.compare(MasterRenderer.getPickedColor());
    }

    /**
     * @return Transformation matrix of the gui
     */
    public float[] getTransMat() {
        return transform.getTransMatArr();
    }

    /**
     * @return Transformation matrix of the gui, that always facing the camera
     * 
     * @param cam Camera that the transmat should face to
     */
    public float[] getTransMatFacing(Camera cam) {
        float[] transformFloats = Toolbox.matrixToFloatArray(cam.getFacingMatrix());
        transformFloats[12] = getPosX();
        transformFloats[13] = getPosY();
        transformFloats[14] = getPosZ();
        transformFloats[15] = 1;

        return transformFloats;
    }

    /**
     * @return Picking color for this gui
     */
    public Color getPickColor() {
        return pickColor;
    }

    /**
     * @return Get the ext for the gui pane
     */
    public GUIText getText() {
        return text;
    }

    /**
     * When facing is enabled, the gui pane is always facing the camera. Rotations
     * of the gui element then became redundant!<br>
     * Currenly this behave a little buggy with parent gui objects, so its not
     * recommended to use this with parent objects.
     * 
     * @param facingCamera Should the gui pane always facing the camera
     */
    public void setFacingCamera(boolean facingCamera) {
        this.facingCamera = facingCamera;
    }

    /**
     * @return True if the gui pane always facing the camera, else false
     */
    public boolean isFacingCamera() {
        return facingCamera;
    }
}
