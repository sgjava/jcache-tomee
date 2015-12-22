/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on December 21, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sgoldsmith
 */
public class AbstractDto implements Serializable {

    /**
     * Serializable object.
     */
    private static final long serialVersionUID = 3077868741267354879L;
    /**
     * Transaction ID.
     */
    @NotNull
    private Long transId;

    /**
     * Default constructor.
     */
    public AbstractDto() {
    }

    /**
     * Constructor used to set all fields.
     *
     * @param transId Transaction ID.
     */
    @ConstructorProperties({"transId"})
    public AbstractDto(final Long transId) {
        this.transId = transId;
    }

    /**
     * Get transaction ID.
     *
     * @return Transaction ID.
     */
    public Long getTransId() {
        return transId;
    }

    /**
     * Set transaction ID.
     *
     * @param transId Transaction ID.
     */
    public void setTransId(final Long transId) {
        this.transId = transId;
    }

    /**
     * String representation of DTO.
     *
     * @return String representation of DTO.
     */
    @Override
    public String toString() {
        return "AbstractDto{" + "transId=" + transId + '}';
    }
}
