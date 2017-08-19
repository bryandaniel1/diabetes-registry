/*
 * Copyright 2017 Bryan Daniel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package registry.administration;

import java.io.Serializable;
import registry.QualityReference;

/**
 * This class contains the variables necessary to manage a quality reference.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class QualityReferenceConfiguration implements Serializable {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -2995321574479934140L;

    /**
     * The quality reference
     */
    private QualityReference qualityReference;

    /**
     * The active status indicator
     */
    private boolean active;

    /**
     * Get the value of qualityReference
     *
     * @return the value of qualityReference
     */
    public QualityReference getQualityReference() {
        return qualityReference;
    }

    /**
     * Set the value of qualityReference
     *
     * @param qualityReference new value of qualityReference
     */
    public void setQualityReference(QualityReference qualityReference) {
        this.qualityReference = qualityReference;
    }

    /**
     * Get the value of active
     *
     * @return the value of active
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Set the value of active
     *
     * @param active new value of active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
