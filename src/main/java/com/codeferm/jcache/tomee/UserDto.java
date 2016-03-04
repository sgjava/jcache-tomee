/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on September 1, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import java.beans.ConstructorProperties;
import javax.validation.constraints.NotNull;

/**
 * Simple DTO.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
public class UserDto extends AbstractDto {

    /**
     * Serializable object.
     */
    private static final long serialVersionUID = 3077868741267353366L;
    /**
     * Numeric ID.
     */
    @NotNull
    private Integer id;
    /**
     * User name.
     */
    @NotNull
    private String userName;
    /**
     * Full name.
     */
    @NotNull
    private String fullName;

    /**
     * Default constructor.
     */
    public UserDto() {
        super();
    }

    /**
     * Constructor used to set all fields.
     *
     * @param transId Transaction ID.
     * @param id Numeric ID.
     * @param userName User name,
     * @param fullName Full name.
     */
    @ConstructorProperties({"transId", "id", "userName", "fullName"})
    public UserDto(final Long transId, final Integer id, final String userName,
            final String fullName) {
        super(transId);
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
    }

    /**
     * Get ID.
     *
     * @return ID.
     */
    public final Integer getId() {
        return id;
    }

    /**
     * Set ID.
     *
     * @param id ID.
     */
    public final void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Get user name.
     *
     * @return User name.
     */
    public final String getUserName() {
        return userName;
    }

    /**
     * Set user name.
     *
     * @param userName User name.
     */
    public final void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * Get full name.
     *
     * @return Full name.
     */
    public final String getFullName() {
        return fullName;
    }

    /**
     * Set full name.
     *
     * @param fullName Full name.
     */
    public final void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    /**
     * String representation of DTO.
     *
     * @return String representation of DTO.
     */
    @Override
    public String toString() {
        return super.toString() + ", UserDto{" + "id=" + id + ", userName="
                + userName + ", fullName=" + fullName + '}';
    }
}
