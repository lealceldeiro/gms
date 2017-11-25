package com.gmsboilerplatesbng.security.bpermission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmsboilerplatesbng.Application;
import com.gmsboilerplatesbng.domain.security.permission.BPermission;
import com.gmsboilerplatesbng.repository.security.permission.BPermissionRepository;
import com.gmsboilerplatesbng.util.GMSRandom;
import com.gmsboilerplatesbng.util.validation.ConstrainedFields;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class RepositoryTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Autowired private WebApplicationContext context;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired private BPermissionRepository repository;

    @Autowired private ObjectMapper objectMapper;

    private MockMvc mvc;

    private RestDocumentationResultHandler restDocResHandler;

    @Value("${page.size}")
    private int pageSize;

    @Value("${page.sizeAttr}")
    private String pageSizeAttr;

    @Value("${spring.data.rest.basePath}")
    private String apiPrefix;

    private final String rn = "RandomName-";
    private final String rl = "RandomLabel-";

    private final GMSRandom random = new GMSRandom();

    @Before
    public void setUp() {
        this.restDocResHandler = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.restDocResHandler)
                .build();
    }

    //C
    @Test
    public void createPermission() throws Exception {
        String reqString = "permission";

        Map<String, String> newPermission = new HashMap<>();
        newPermission.put("name", this.rn + this.random.nextString());
        newPermission.put("label", this.rl + this.random.nextString());

        ConstrainedFields fields = new ConstrainedFields(BPermission.class);

        this.mvc.perform(
                post(this.apiPrefix + "/" + reqString).contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(newPermission))
        ).andExpect(status().isCreated())
                .andDo(
                        this.restDocResHandler.document(
                                requestFields(
                                        fields.withPath("name").description("Name to be used for authenticating"),
                                        fields.withPath("label").description("Label to be shown to the final user")
                                )
                        )
                );

    }

    //R
    @Test
    public void listPermissions() throws Exception {
        String reqString = "permission";

        /*setUpPermissions();*/

        this.mvc.perform(
                get(this.apiPrefix + "/" + reqString + "?" + this.pageSizeAttr + "=" + this.pageSize)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(
                this.restDocResHandler.document(
                        responseFields(
                                fieldWithPath("_embedded." + reqString).description("Array of permissions"),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to permissions"),
                                fieldWithPath("page").description("Options for paging the permissions")
                        )
                )
        );
    }

    @Test
    public void getPermission() throws Exception {
        String reqString = "permission";

        BPermission p = createPermissionUsingRepository();

        this.mvc.perform(
                get(this.apiPrefix + "/" + reqString + "/" + p.getId()).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(
                this.restDocResHandler.document(
                        responseFields(
                                fieldWithPath("name").description("Name to be used for authenticating"),
                                fieldWithPath("label").description("Label to be shown to the final user"),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to the returned permission")
                        )
                )
        );

    }

    @Test
    public void getRolesPermission() throws Exception {
        String reqString = "permission";

        BPermission p = createPermissionUsingRepository();

        this.mvc.perform(
                get(this.apiPrefix + "/" + reqString + "/" + p.getId() + "/roles")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(
                        this.restDocResHandler.document(
                                responseFields(
                                        fieldWithPath("_embedded.role").description("Array of roles in which the permission is included"),
                                        fieldWithPath("_links").description("Available links for requesting other webservices related to the returned permission's roles")
                                )
                        )
                );

    }

    //U (partial)
    @Test
    public void partialUpdatePermission() throws Exception {
        String reqString = "permission";

        BPermission oldPermission = createPermissionUsingRepository();
        Map<String, String> newPermission = new HashMap<>();
        newPermission.put("name", this.rn + this.random.nextString());
        newPermission.put("label", this.rl + this.random.nextString());

        ConstrainedFields fields = new ConstrainedFields(BPermission.class);

        this.mvc.perform(
                patch(this.apiPrefix + "/" + reqString + "/" + oldPermission.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(newPermission))
        ).andExpect(status().isNoContent())
                .andDo(
                        this.restDocResHandler.document(
                                requestFields(
                                        fields.withPath("name").description("Name to be used for authenticating"),
                                        fields.withPath("label").description("Label to be shown to the final user")
                                )
                        )
                );
    }


    //D
    @Test
    public void deletePermission() throws Exception {
        String reqString = "permission";

        BPermission p = createPermissionUsingRepository();

        this.mvc.perform(
                delete(this.apiPrefix + "/" + reqString + "/" + p.getId()).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

    }


    private void setUpPermissions() {
        createPermissionUsingRepository();
    }

    private BPermission createPermissionUsingRepository() {
        return createPermissionUsingRepository(this.rn + this.random.nextString(), this.rl + this.random.nextString());
    }
    private BPermission createPermissionUsingRepository(String name, String label) {
        return this.repository.save(new BPermission(name, label));
    }

}
