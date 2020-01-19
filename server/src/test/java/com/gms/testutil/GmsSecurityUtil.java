package com.gms.testutil;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.config.security.LoginPayloadSample;
import com.gms.testutil.validation.ConstrainedFields;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class GmsSecurityUtil {

    /**
     * An instance of {@link ObjectMapper}.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Returns a (singlenton) instance of an {@link ObjectMapper}.
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * Creates an access token with the privileges of a Super Admin (an user with all of the possible privileges)
     * managed in the application.
     *
     * @param dc           A {@link DefaultConst} instance.
     * @param sc           A {@link SecurityConst} instance
     * @param mvc          An {@link MockMvc} instance.
     * @param objectMapper An {@link ObjectMapper} instance.
     * @param shouldIFail  Whether the token should produce a failure while trying to access a secured resource with it
     *                     or no.
     * @return A {@link String} with the newly created token.
     * @throws Exception If there is a failure in the request performed by the {@code mvc}.
     */
    public static String createSuperAdminAuthToken(final DefaultConst dc, final SecurityConst sc, final MockMvc mvc,
                                                   final ObjectMapper objectMapper, final boolean shouldIFail)
            throws Exception {
        final MvcResult mvcResult =
                createSuperAdminAuthTokenMvcResult(dc, sc, mvc, objectMapper, shouldIFail).andReturn();

        return shouldIFail ? "" : getValueInJSON(mvcResult.getResponse().getContentAsString(), sc.getATokenHolder());
    }

    /**
     * Creates a refresh token.
     *
     * @param dc              A {@link DefaultConst} instance.
     * @param sc              A {@link SecurityConst} instance
     * @param mvc             An {@link MockMvc} instance.
     * @param objectMapperArg An {@link ObjectMapper} instance.
     * @param shouldIFail     Whether the token should produce a failure while trying to access a secured resource with
     *                        it or no.
     * @return A {@link String} with the newly created token.
     * @throws Exception If there is a failure in the request performed by the {@code mvc}.
     */
    public static String createSuperAdminRefreshToken(final DefaultConst dc, final SecurityConst sc, final MockMvc mvc,
                                                      final ObjectMapper objectMapperArg, final boolean shouldIFail)
            throws Exception {
        final MvcResult mvcResult =
                createSuperAdminAuthTokenMvcResult(dc, sc, mvc, objectMapperArg, shouldIFail).andReturn();

        return shouldIFail ? "" : getValueInJSON(mvcResult.getResponse().getContentAsString(), sc.getATokenHolder());
    }

    /**
     * Creates an access token with the privileges of a Super Admin (an user with all of the possible privileges)
     * managed in the application and document the request performed to create the token.
     *
     * @param dc                A {@link DefaultConst} instance.
     * @param sc                A {@link SecurityConst} instance
     * @param mvc               An {@link MockMvc} instance.
     * @param objectMapperArg   An {@link ObjectMapper} instance.
     * @param restDocResHandler A {@link RestDocumentationResultHandler} for documenting the request.
     * @param shouldIFail       Whether the token should produce a failure while trying to access a secured resource
     *                          with it or no.
     * @return A {@link String} with the newly created token.
     * @throws Exception If there is a failure in the request performed by the {@code mvc}.
     */
    public static String createSuperAdminAuthToken(final DefaultConst dc, final SecurityConst sc, final MockMvc mvc,
                                                   final ObjectMapper objectMapperArg,
                                                   final RestDocumentationResultHandler restDocResHandler,
                                                   final boolean shouldIFail) throws Exception {

        ConstrainedFields fields = new ConstrainedFields(LoginPayloadSample.class);

        String warnT = " (The name of this field can vary depending on your API provider. If your are having any "
                + "issues on getting login properly, you should consult your api provider for more information.)";

        ResultActions resultActions = createSuperAdminAuthTokenMvcResult(dc, sc, mvc, objectMapperArg, shouldIFail)
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath(sc.getReqUsernameHolder())
                                                .description(
                                                        "User's identifier. It can be the email or username." + warnT
                                                ),
                                        fields.withPath(sc.getReqPasswordHolder())
                                                .description("User's password." + warnT)
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
                                            .description("Name of the header you have to use for sending the access"
                                                    + " token in every subsequent request."),
                                    fieldWithPath(sc.getATokenExpirationTimeHolder())
                                            .description("Time (expressed in milliseconds) in which the access token"
                                                    + " will expire."),
                                    fieldWithPath(sc.getATokenExpiresInHolder())
                                            .description("Time of validity of the access token."),

                                    //refresh token
                                    fieldWithPath(sc.getRTokenHolder())
                                            .description("Refresh token."),
                                    fieldWithPath(sc.getRTokenExpirationTimeHolder())
                                            .description("Time (expressed in milliseconds) in which the refresh token"
                                                    + " will expire."),
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

    private static ResultActions createSuperAdminAuthTokenMvcResult(final DefaultConst dc, final SecurityConst sc,
                                                                    final MockMvc mvc,
                                                                    final ObjectMapper objectMapperArg,
                                                                    final boolean shouldIFail) throws Exception {
        Map<String, String> loginData = new HashMap<>();
        loginData.put(sc.getReqUsernameHolder(), shouldIFail ? "invalidUser" : dc.getUserAdminDefaultUsername());
        loginData.put(sc.getReqPasswordHolder(), shouldIFail ? "invalidPassword" : dc.getUserAdminDefaultPassword());

        return createSuperAdminAuthTokenMvcResult(dc, sc, mvc, objectMapperArg, loginData, shouldIFail);
    }

    private static ResultActions createSuperAdminAuthTokenMvcResult(final DefaultConst dc, final SecurityConst sc,
                                                                    final MockMvc mvc, final ObjectMapper objectMapper,
                                                                    final Object loginData, final boolean shouldIFail)
            throws Exception {
        return mvc.perform(
                post(dc.getApiBasePath() + sc.getSignInUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData))
        ).andExpect(shouldIFail ? status().isUnauthorized() : status().isOk());
    }

    /**
     * Returns a value contained in a JSON {@code source} under a given {@code key}.
     *
     * @param source Source to retrieve the value from.
     * @param key    Key used to retrieve the value.
     * @return The string representation of the value.
     * @throws JSONException
     */
    public static String getValueInJSON(final String source, final String key) throws JSONException {
        return (source != null && !"".equals(source)) ? new JSONObject(source).getString(key) : "";
    }

    /**
     * Privates constructor to make class un-instantiable.
     */
    private GmsSecurityUtil() {
    }

}
