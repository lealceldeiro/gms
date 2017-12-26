package com.gms.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * GmsSecurityUtil
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 * @version 0.1
 * Dec 21, 2017
 */
public class GmsSecurityUtil {

    public static String createSuperAdminAuthToken(DefaultConst dc, SecurityConst sc, MockMvc mvc, ObjectMapper objectMapper) throws Exception {
        Map<String, String> loginData = new HashMap<>();
        loginData.put(sc.getReqUsernameHolder(), dc.getUserAdminDefaultUsername());
        loginData.put(sc.getReqPasswordHolder(), dc.getUserAdminDefaultPassword());

        MvcResult resultD = mvc.perform(
                post(dc.getApiBasePath() + sc.getSignInUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData))
        ).andExpect(status().isOk())
                .andReturn();

        JSONObject rawData = new JSONObject(resultD.getResponse().getContentAsString());
        return rawData.getString(sc.getATokenHolder());
    }

}
