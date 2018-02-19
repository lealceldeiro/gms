package com.gms.controller.configuration;

/**
 * SampleConfigurationPayload
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 19, 2018
 */

class SampleConfigurationPayload {
    private Boolean is_multi_entity_app_in_server;
    private Boolean is_user_registration_allowed_in_server;
    private String language;
    private Long last_accessed_entity;
    private Long userId;

    SampleConfigurationPayload(Boolean is_multi_entity_app_in_server, Boolean is_user_registration_allowed_in_server) {
        this.is_multi_entity_app_in_server = is_multi_entity_app_in_server;
        this.is_user_registration_allowed_in_server = is_user_registration_allowed_in_server;
    }

    SampleConfigurationPayload(String language, Long last_accessed_entity) {
        this.language = language;
        this.last_accessed_entity = last_accessed_entity;
    }

    public Boolean isIs_multi_entity_app_in_server() {
        return is_multi_entity_app_in_server;
    }

    public void setIs_multi_entity_app_in_server(boolean is_multi_entity_app_in_server) {
        this.is_multi_entity_app_in_server = is_multi_entity_app_in_server;
    }

    public Boolean isIs_user_registration_allowed_in_server() {
        return is_user_registration_allowed_in_server;
    }

    public void setIs_user_registration_allowed_in_server(boolean is_user_registration_allowed_in_server) {
        this.is_user_registration_allowed_in_server = is_user_registration_allowed_in_server;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getLast_accessed_entity() {
        return last_accessed_entity;
    }

    public void setLast_accessed_entity(Long last_accessed_entity) {
        this.last_accessed_entity = last_accessed_entity;
    }
}
