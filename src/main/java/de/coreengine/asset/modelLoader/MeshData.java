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

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.collision.shapes.TriangleMeshShape;
import de.coreengine.asset.meta.MetaMaterial;
import de.coreengine.asset.meta.MetaMesh;
import de.coreengine.rendering.model.Material;
import de.coreengine.rendering.model.Mesh;
import de.coreengine.util.bullet.Physics;
import de.coreengine.util.gl.IndexBuffer;
import de.coreengine.util.gl.VertexArrayObject;
import javafx.util.Pair;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector2D;
import org.lwjgl.assimp.AIVector3D;

import java.util.Objects;

public class MeshData {

    //Input
    private final AIMesh aiMesh;
    private final Pair<Material, MetaMaterial>[] materials;

    //Output
    private Mesh mesh = null;
    private MetaMesh metaMesh = null;

    /**Creating new mesh data that can parse ai meshes into meshes and meta meshes
     *
     * @param aiMesh AIMesh to parse
     * @param materials Materials of the AIScene
     */
    public MeshData(AIMesh aiMesh, Pair<Material, MetaMaterial>[] materials) {
        this.aiMesh = aiMesh;
        this.materials = materials;
    }

    /**Parse ai meshes into meshes and meta meshes
     */
    public void parse(CollisionShape shape){

        //Get material, load empty material if id not exist
        Pair<Material, MetaMaterial> material;
        if(aiMesh.mMaterialIndex() >= 0 && aiMesh.mMaterialIndex() < materials.length){
            material = materials[aiMesh.mMaterialIndex()];
        }else{
            material = new Pair<>(new Material(), new MetaMaterial());
        }

        //Get data from mesh
        float[] vertices = bufferToArray(aiMesh.mVertices(), true);
        float[] texCoords = bufferToArray(Objects.requireNonNull(aiMesh.mTextureCoords(0)), false);
        float[] normals = bufferToArray(Objects.requireNonNull(aiMesh.mNormals()), true);
        float[] tangents = bufferToArray(Objects.requireNonNull(aiMesh.mTangents()), true);
        int[] indices = getIndices();

        //Calculate collision shape
        if(shape instanceof ConvexHullShape){
            shape = Physics.createConvexHullShape(vertices);
        }else if(shape instanceof TriangleMeshShape){
            shape = Physics.createTriangleMeshShape(vertices, indices);
        }

        //Construct meta mesh
        metaMesh = new MetaMesh();
        metaMesh.setVertices(vertices);
        metaMesh.setTexCoords(texCoords);
        metaMesh.setNormals(normals);
        metaMesh.setTangents(tangents);
        metaMesh.setIndices(indices);
        metaMesh.setMaterial(material.getValue());
        metaMesh.setShape(shape);

        //Construct mesh
        VertexArrayObject vao = new VertexArrayObject();
        vao.addVertexBuffer(vertices, 3, 0);
        vao.addVertexBuffer(texCoords, 2, 1);
        vao.addVertexBuffer(normals, 3, 2);
        vao.addVertexBuffer(tangents, 3, 3);
        IndexBuffer indexBuffer = vao.addIndexBuffer(indices);
        mesh = new Mesh(vao, indexBuffer, material.getKey(), shape);
    }

    /**Extract indices from ai scene
     *
     * @return Indices array
     */
    private int[] getIndices(){
        int faceCount = aiMesh.mNumFaces();
        int[] indices = new int[faceCount * 3];
        for(int i = 0; i < faceCount; i++){
            for(int j = 0; j < 3; j++){
                indices[i*3+j] = aiMesh.mFaces().get(i).mIndices().get(j);
            }
        }
        return indices;
    }

    /**Transfer float from vec3d buffer to a float array
     *
     * @param buffer Buffer to convert
     * @param loadAll If true the whole vector will be loaded, else only x and y
     * @return Float from vec3d buffer as (x0, y0, (z0), x1, y1, (z1), ...)
     */
    private float[] bufferToArray(AIVector3D.Buffer buffer, boolean loadAll){
        int c = 0, s = (loadAll ? 3 : 2);
        float[] out = new float[buffer.remaining() * s];
        while(buffer.remaining() > 0){
            AIVector3D vector = buffer.get();
            out[c*s] = vector.x();
            out[c*s+1] = vector.y();
            if(loadAll)out[c*s+2] = vector.z();
            c++;
        }
        return out;
    }

    /**@return Parsed mesh
     */
    public Mesh getMesh() {
        return mesh;
    }

    /**@return Parsed meta mesh
     */
    public MetaMesh getMetaMesh() {
        return metaMesh;
    }
}