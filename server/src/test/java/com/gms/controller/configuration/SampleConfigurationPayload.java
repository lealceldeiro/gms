package com.gms.controller.configuration;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
class SampleConfigurationPayload {

    /**
     * Payload argument: is the app handling multiple entities?
     */
    private Boolean isMultiEntityAppInServer;
    /**
     * Payload argument: is the app handling user registration?
     */
    private Boolean isUserRegistrationAllowedInServer;
    /**
     * Payload argument: language.
     */
    private String language;
    /**
     * Payload argument: last accessed entity by a user.
     */
    private Long lastAccessedEntity;
    /**
     * Payload argument: user id.
     */
    private Long userId;
    /**
     * Payload argument: user id.
     */
    private Long user;

    SampleConfigurationPayload(final Boolean isMultiEntityAppInServer,
                               final Boolean isUserRegistrationAllowedInServer) {
        this.isMultiEntityAppInServer = isMultiEntityAppInServer;
        this.isUserRegistrationAllowedInServer = isUserRegistrationAllowedInServer;
    }

    SampleConfigurationPayload(final String language, final Long lastAccessedEntity) {
        this.language = language;
        this.lastAccessedEntity = lastAccessedEntity;
    }

    /**
     * Returns whether the app iss the app handling multiple entities or not.
     *
     * @return {@code true} if it is handling multiple entities, {@code false} otherwise.
     */
    @JsonGetter("is_multi_entity_app_in_server")
    public Boolean isMultiEntityAppInServer() {
        return isMultiEntityAppInServer;
    }

    @JsonSetter("is_multi_entity_app_in_server")
    public void setIsMultiEntityAppInServer(final boolean isMultiEntityAppInServer) {
        this.isMultiEntityAppInServer = isMultiEntityAppInServer;
    }

    @JsonGetter("is_user_registration_allowed_in_server")
    public Boolean isUserRegistrationAllowedInServer() {
        return isUserRegistrationAllowedInServer;
    }

    @JsonSetter("is_user_registration_allowed_in_server")
    public void setIsUserRegistrationAllowedInServer(final boolean isUserRegistrationAllowedInServer) {
        this.isUserRegistrationAllowedInServer = isUserRegistrationAllowedInServer;
    }

    @JsonGetter("userId")
    public Long getUserId() {
        return userId;
    }

    @JsonSetter("userId")
    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    @JsonGetter("language")
    public String getLanguage() {
        return language;
    }

    @JsonSetter("language")
    public void setLanguage(final String language) {
        this.language = language;
    }

    @JsonGetter("last_accessed_entity")
    public Long getLastAccessedEntity() {
        return lastAccessedEntity;
    }

    @JsonSetter("last_accessed_entity")
    public void setLastAccessedEntity(final Long lastAccessedEntity) {
        this.lastAccessedEntity = lastAccessedEntity;
    }

    @JsonGetter("user")
    public Long getUser() {
        return user;
    }

    @JsonSetter("user")
    public void setUser(final Long user) {
        this.user = user;
    }

}
