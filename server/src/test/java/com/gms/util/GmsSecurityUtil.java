package com.gms.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.config.security.LoginPayloadSample;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.validation.ConstrainedFields;
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

    public static String createSuperAdminAuthToken(DefaultConst dc, SecurityConst sc, MockMvc mvc, ObjectMapper objectMapper)
            throws Exception {
        final MvcResult mvcResult = createSuperAdminAuthTokenMvcResult(dc, sc, mvc, objectMapper).andReturn();

        JSONObject rawData = new JSONObject(mvcResult.getResponse().getContentAsString());
        return rawData.getString(sc.getATokenHolder());
    }

    public static String createSuperAdminAuthToken(DefaultConst dc, SecurityConst sc, MockMvc mvc, ObjectMapper objectMapper,
                                                   RestDocumentationResultHandler restDocResHandler) throws Exception {

        ConstrainedFields fields = new ConstrainedFields(LoginPayloadSample.class);

        final MvcResult mvcResult = createSuperAdminAuthTokenMvcResult(dc, sc, mvc, objectMapper)
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath(sc.getReqUsernameHolder())
                                                .description("User's identifier. It can be the email or username."),
                                        fields.withPath(sc.getReqPasswordHolder())
                                                .description("User's password")
                                )
                        )
                )
                .andDo(
                        restDocResHandler.document(
                                responseFields(
                                        fieldWithPath(sc.getATokenHolder())
                                                .description("Variable in which the access token will be sent in the response in the login information."),
                                        fieldWithPath(sc.getRTokenHolder())
                                                .description("Variable in which the refresh token will be sent in the response in the login information."),
                                        fieldWithPath(sc.getATokenHeaderToBeSentHolder())
                                                .description("Variable which indicates the name of the header you have to use for sending the access token in every subsequent request."),
                                        fieldWithPath(sc.getExpirationHolder())
                                                .description("Time (expressed in milliseconds) in which the access token will expire."),
                                        fieldWithPath(sc.getATokenTypeHolder())
                                                .description("Token scheme to be used."),
                                        fieldWithPath(sc.getExpiresInHolder())
                                                .description("Variable in which the time of validity of the access token will be sent in the response in the login information."),
                                        fieldWithPath(sc.getIssuedTimeHolder())
                                                .description("Variable in which the time where the token was issued will be sent in the response in the login information."),
                                        fieldWithPath(sc.getAuthoritiesHolder())
                                                .description("Variable in which the user's authorities will be specified in an Array of String."),
                                        fieldWithPath(SecurityConst.USERNAME_HOLDER)
                                                .description("Variable in which the user's username will be specified.")
                                )
                        )
                )
                .andReturn();

        JSONObject rawData = new JSONObject(mvcResult.getResponse().getContentAsString());
        return rawData.getString(sc.getATokenHolder());
    }

    private static ResultActions createSuperAdminAuthTokenMvcResult(DefaultConst dc, SecurityConst sc, MockMvc mvc,
                                                                    ObjectMapper objectMapper) throws Exception {
        Map<String, String> loginData = new HashMap<>();
        loginData.put(sc.getReqUsernameHolder(), dc.getUserAdminDefaultUsername());
        loginData.put(sc.getReqPasswordHolder(), dc.getUserAdminDefaultPassword());

        return createSuperAdminAuthTokenMvcResult(dc, sc, mvc, objectMapper, loginData);
    }

    private static ResultActions createSuperAdminAuthTokenMvcResult(DefaultConst dc, SecurityConst sc, MockMvc mvc,
                                                                    ObjectMapper objectMapper, Object loginData) throws Exception {
        return mvc.perform(
                post(dc.getApiBasePath() + sc.getSignInUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData))
        ).andExpect(status().isOk());
    }

}
