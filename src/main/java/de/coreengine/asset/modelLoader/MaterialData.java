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

package de.coreengine.asset.modelLoader;

import de.coreengine.asset.TextureLoader;
import de.coreengine.asset.meta.MetaMaterial;
import de.coreengine.rendering.model.Color;
import de.coreengine.rendering.model.Material;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIString;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

import static org.lwjgl.assimp.Assimp.*;

public class MaterialData {

    //Input
    private final AIMaterial aiMaterial;

    //Output
    private final Material material = new Material();
    private final MetaMaterial metaMaterial = new MetaMaterial();

    /**Creating new material data that can parse ai materials into materials and meta materials
     *
     * @param aiMaterial AIMaterial to parse
     */
    public MaterialData(AIMaterial aiMaterial) {
        this.aiMaterial = aiMaterial;
    }

    /**Parse ai materials into materials and meta materials
     *
     * @param texPath Location of the texture files
     */
    public void parse(String texPath){

        //Data buffers
        AIColor4D color = AIColor4D.create();
        AIString path = AIString.create();
        float[] floot = new float[1];

        //Load diffuse texture
        if(getTexturePath(aiTextureType_DIFFUSE, path, texPath)){
            material.diffuseMap = texPath +path.dataString();
            metaMaterial.diffuseMap = texPath +path.dataString();
        }

        //Load normal texture
        if(getTexturePath(aiTextureType_NORMALS, path, texPath)){
            material.normalMap = texPath +path.dataString();
            metaMaterial.normalMap = texPath +path.dataString();
        }

        //Load specular texture
        if(getTexturePath(aiTextureType_SPECULAR, path, texPath)){
            material.specularMap = texPath +path.dataString();
            metaMaterial.specularMap = texPath +path.dataString();
        }

        //Load ambient texture
        if(getTexturePath(aiTextureType_AMBIENT, path, texPath)){
            material.ambientOcclusionMap = texPath +path.dataString();
            metaMaterial.ambientOcclusionMap = texPath +path.dataString();
        }

        //Load alpha texture
        if(getTexturePath(aiTextureType_OPACITY, path, texPath)){
            material.alphaMap = texPath +path.dataString();
            metaMaterial.alphaMap = texPath +path.dataString();
        }

        //Load displacement texture
        if(getTexturePath(aiTextureType_DISPLACEMENT, path, texPath)){
            material.displacementMap = texPath +path.dataString();
            metaMaterial.displacementMap = texPath +path.dataString();
        }

        //Load diffuse color
        if(aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color)
                == aiReturn_SUCCESS){
            material.diffuseColor.set(color.r(), color.g(), color.b());
            metaMaterial.diffuseColor = new Color(color.r(), color.g(), color.b());
        }

        //Load shininess
        if(aiGetMaterialFloatArray(aiMaterial, AI_MATKEY_SHININESS, aiTextureType_NONE, 0,
                floot, new int[] {1}) == aiReturn_SUCCESS){
            material.shininess = floot[0];
            metaMaterial.shininess = floot[0];
        }

        //Load bump/displacement factor
        if(aiGetMaterialFloatArray(aiMaterial, AI_MATKEY_BUMPSCALING, aiTextureType_NONE, 0,
                floot, new int[] {1}) == aiReturn_SUCCESS){
            material.displacementFactor = floot[0];
            metaMaterial.displacementFactor = floot[0];
        }
    }

    /**Getting texture from ai material an load if exist
     *
     * @param type Type of texture to get
     * @param path Path to store tex path in
     * @param texPath Location of the texture files
     * @return True if texture exist, else false
     */
    private boolean getTexturePath(int type, AIString path, String texPath){
        if(aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, path,
                (IntBuffer) null, null, null, null, null, null) == aiReturn_SUCCESS){
            TextureLoader.loadTextureFile(texPath +path.dataString(), true, GL11.GL_LINEAR, false);
            return true;
        }else return false;
    }

    /**@return Parsed material
     */
    public Material getMaterial() {
        return material;
    }

    /**@return Parsed meta material
     */
    public MetaMaterial getMetaMaterial() {
        return metaMaterial;
    }
}