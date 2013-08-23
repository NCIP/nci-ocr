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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Provides byte storage and retrieval via a LOB in the database.
 */
@Entity(name = "lob_holder")
@DiscriminatorValue("LOB_HOLDER")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class LobHolder extends AbstractByteDataSource {

    private static final long serialVersionUID = 1L;
    private byte[] data;

    /**
     * Default constructor.
     */
    public LobHolder() {
        // do nothing
    }

    /**
     * Constructor.
     * @param data the data
     */
    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public LobHolder(byte[] data) {
        this.data = data;
    }

    /**
     * @return the data
     */
    @Lob
    @Column(name = "data")
    @SuppressWarnings("PMD.MethodReturnsInternalArray")
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public void setData(byte[] data) {
        this.data = data;
    }

}