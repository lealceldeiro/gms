package com.gms.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.config.security.LoginPayloadSample;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.validation.ConstrainedFields;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * GmsSecurityUtil
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Dec 21, 2017
 */
public class GmsSecurityUtil {

    private final static ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String createSuperAdminAuthToken(DefaultConst dc, SecurityConst sc, MockMvc mvc, ObjectMapper objectMapper,
                                                   boolean shouldIFail) throws Exception {
        final MvcResult mvcResult = createSuperAdminAuthTokenMvcResult(dc, sc, mvc, objectMapper, shouldIFail).andReturn();

        return getValueInJSON(mvcResult.getResponse().getContentAsString(), sc.getATokenHolder());
    }

    public static String createSuperAdminRefreshToken(DefaultConst dc, SecurityConst sc, MockMvc mvc, ObjectMapper objectMapper,
                                                   boolean shouldIFail) throws Exception {
        final MvcResult mvcResult = createSuperAdminAuthTokenMvcResult(dc, sc, mvc, objectMapper, shouldIFail).andReturn();

        return getValueInJSON(mvcResult.getResponse().getContentAsString(), sc.getATokenHolder());
    }

    public static String createSuperAdminAuthToken(DefaultConst dc, SecurityConst sc, MockMvc mvc, ObjectMapper objectMapper,
                                                   RestDocumentationResultHandler restDocResHandler, boolean shouldIFail)
            throws Exception {

        ConstrainedFields fields = new ConstrainedFields(LoginPayloadSample.class);

        String WARN_T = " (The name of this field can vary depending on your api provider. If your are having any issues" +
                " on getting login properly, you should consult your api provider for more information.)";

        ResultActions resultActions = createSuperAdminAuthTokenMvcResult(dc, sc, mvc, objectMapper, shouldIFail)
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath(sc.getReqUsernameHolder())
                                                .description("User's identifier. It can be the email or username." + WARN_T),
                                        fields.withPath(sc.getReqPasswordHolder())
                                                .description("User's password." + WARN_T)
                                )
                        )
                );

        if (!shouldIFail) {
            resultActions.andDo(
                    restDocResHandler.document(
                            responseFields(
                                    //common
                                    fieldWithPath(sc.getIssuedTimeHolder())
                                            .description("Time where the access and refresh tokens were issued at."),
                                    fieldWithPath(sc.getAuthoritiesHolder())
                                            .description("User's authorities."),
                                    fieldWithPath(SecurityConst.USERNAME_HOLDER)
                                            .description("User's username."),

                                    // access token
                                    fieldWithPath(sc.getATokenHolder())
                                            .description("Access token."),
                                    fieldWithPath(sc.getATokenTypeHolder())
                                            .description("Token scheme to be used."),
                                    fieldWithPath(sc.getATokenHeaderToBeSentHolder())
                                            .description("Name of the header you have to use for sending the access token in every subsequent request."),
                                    fieldWithPath(sc.getATokenExpirationTimeHolder())
                                            .description("Time (expressed in milliseconds) in which the access token will expire."),
                                    fieldWithPath(sc.getATokenExpiresInHolder())
                                            .description("Time of validity of the access token."),

                                    //refresh token
                                    fieldWithPath(sc.getRTokenHolder())
                                            .description("Refresh token."),
                                    fieldWithPath(sc.getRTokenExpirationTimeHolder())
                                            .description("Time (expressed in milliseconds) in which the refresh token will expire."),
                                    fieldWithPath(sc.getRTokenExpiresInHolder())
                                            .description("Time of validity of the refresh token.")
                            )
                    )
            );
            final MvcResult mvcResult = resultActions.andReturn();

            JSONObject rawData = new JSONObject(mvcResult.getResponse().getContentAsString());
            return rawData.getString(sc.getATokenHolder());
        }

        return "";
    }

    private static ResultActions createSuperAdminAuthTokenMvcResult(DefaultConst dc, SecurityConst sc, MockMvc mvc,
                                                                    ObjectMapper objectMapper, boolean shouldIFail) throws Exception {
        Map<String, String> loginData = new HashMap<>();
        loginData.put(sc.getReqUsernameHolder(), shouldIFail ? "invalidUser" : dc.getUserAdminDefaultUsername());
        loginData.put(sc.getReqPasswordHolder(), shouldIFail ? "invalidPassword" : dc.getUserAdminDefaultPassword());

        return createSuperAdminAuthTokenMvcResult(dc, sc, mvc, objectMapper, loginData, shouldIFail);
    }

    private static ResultActions createSuperAdminAuthTokenMvcResult(DefaultConst dc, SecurityConst sc, MockMvc mvc,
                                                                    ObjectMapper objectMapper, Object loginData, boolean shouldIFail) throws Exception {
        return mvc.perform(
                post(dc.getApiBasePath() + sc.getSignInUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData))
        ).andExpect(shouldIFail ? status().isUnauthorized() : status().isOk());
    }
    private static String getValueInJSON(String source, String key) throws JSONException {
        return (source != null && !source.equals("")) ? new JSONObject(source).getString(key) : "";
    }
}
