/**
 * Copyright (c) 2009, 5AM Solutions, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - Neither the name of the author nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
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
package gov.nih.nci.firebird.data;

import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.validator.NotEmpty;

import com.google.common.io.Resources;

/**
 * Provides byte storage and retrieval via a LOB in the database.
 */
@Entity(name = "classpath_resource")
@DiscriminatorValue("CLASSPATH_RESOURCE")
public class ClasspathResource extends AbstractByteDataSource {

    private static final long serialVersionUID = 1L;
    private String resourcePath;
    
    /**
     * No-argument constructor required by Hibernate.
     */
    public ClasspathResource() {
        super();
    }
    
    /**
     * Creates a new initialized instance.
     * 
     * @param resourcePath the path to the resource
     */
    public ClasspathResource(String resourcePath) {
        super();
        this.resourcePath = resourcePath;
    }

    /**
     * @param resourcePath the resourcePath to set
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * @return the resourcePath
     */
    @NotEmpty
    @Column(name = "resource_path")
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * @return the data
     */
    @Transient
    public byte[] getData() {
        try {
            return Resources.toByteArray(Resources.getResource(ClasspathResource.class, getResourcePath()));
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected exception retrieving resource", e);
        }
    }

}