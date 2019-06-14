/*
 * Copyright (c) 2019, Darius Dinger
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.coreengine.rendering.renderable;

import de.coreengine.rendering.model.Color;
import de.coreengine.rendering.model.Material;
import de.coreengine.rendering.renderer.MasterRenderer;
import de.coreengine.rendering.renderable.light.PointLight;
import de.coreengine.util.Configuration;
import javax.vecmath.Vector3f;

/**Class that reprsents a sun for post processing and lighting calculation
 *
 * @author Darius Dinger
 */
public class Sun extends Moon{
    private static final float DEFAULT_SIZE = 
            Configuration.getValuef("SUN_DEFAULT_SIZE");
    
    //Is sun currently visible in the scene
    private boolean lensFlareEnabled = true;
    
    /**Creating new white sun and setting its attenuation to infinity
     */
    public Sun() {
        setSize(DEFAULT_SIZE);
    }
    
    /**@return Is lens flare currently enabled for the sun
     */
    public final boolean isLensFlareEnabled() {
        return lensFlareEnabled;
    }
    
    /**@param lensFlareEnabled Enable/disable lens flare
     */
    public final void setLensFlareEnabled(boolean lensFlareEnabled) {
        this.lensFlareEnabled = lensFlareEnabled;
    }
}
