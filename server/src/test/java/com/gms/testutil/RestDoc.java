package com.gms.testutil;

import com.gms.util.constant.DefaultConstant;
import com.gms.util.constant.LinkPath;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class RestDoc {

    /**
     * Location of the generated api documentation.
     */
    public static final String APIDOC_LOCATION = "build/generated-snippets";

    /**
     * Identifier used to generate the api documentation.
     */
    private static final String IDENTIFIER = "{class-name}/{method-name}";

    /**
     * Privates constructor to make class un-instantiable.
     */
    private RestDoc() {
    }

    /**
     * Returns a {@link RestDocumentationResultHandler}.
     *
     * @return RestDocumentationResultHandler
     */
    public static RestDocumentationResultHandler getRestDocumentationResultHandler() {
        return document(IDENTIFIER, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
    }

    /**
     * Link contained in page attributes in the services responses.
     */
    private static LinksSnippet pagingLinks = HypermediaDocumentation.links(
            HypermediaDocumentation.linkWithRel("page.size").optional()
                    .description("Total elements to be shown per page"),
            HypermediaDocumentation.linkWithRel("page.totalElements").optional()
                    .description("Total possible elements which can be shown"),
            HypermediaDocumentation.linkWithRel("page.totalPages").optional()
                    .description("Total pages according to the size of the page and the total elements"),
            HypermediaDocumentation.linkWithRel("page.number").optional()
                    .description("Current page number"),
            HypermediaDocumentation.linkWithRel(LinkPath.get("first")).optional()
                    .description("The first page of results"),
            HypermediaDocumentation.linkWithRel(LinkPath.get("last")).optional()
                    .description("The last page of results"),
            HypermediaDocumentation.linkWithRel(LinkPath.get("next")).optional()
                    .description("The next page of results"),
            HypermediaDocumentation.linkWithRel(LinkPath.get("prev")).optional()
                    .description("The previous page of results"),
            HypermediaDocumentation.linkWithRel(LinkPath.get("search")).optional()
                    .description("The search url provided for this resource"),
            HypermediaDocumentation.linkWithRel(LinkPath.get("profile")).optional()
                    .description("The resource \"profile\" ")
    );

    /**
     * Returns the fields contained in page attributes in the services responses.
     *
     * @return FieldDescriptor
     */
    private static FieldDescriptor[] pagingFields() {
        return new FieldDescriptor[]{
                fieldWithPath("page.size").type(JsonFieldType.NUMBER).optional()
                        .description("Total elements to be shown per page"),
                fieldWithPath("page.totalElements").type(JsonFieldType.NUMBER).optional()
                        .description("Total possible elements which can be shown"),
                fieldWithPath("page.totalPages").type(JsonFieldType.NUMBER).optional()
                        .description("Total pages according to the size of the page and the total elements"),
                fieldWithPath("page.number").type(JsonFieldType.NUMBER).optional()
                        .description("Current page number"),
                fieldWithPath(LinkPath.getSelf("templated")).type(JsonFieldType.BOOLEAN).ignored().optional()
                        .description("Whether the result is templated or not"),
                fieldWithPath(LinkPath.get("first")).type(JsonFieldType.STRING).optional()
                        .description("The first page of results"),
                fieldWithPath(LinkPath.get("last")).type(JsonFieldType.STRING).optional()
                        .description("The last page of results"),
                fieldWithPath(LinkPath.get("next")).type(JsonFieldType.STRING).optional()
                        .description("The next page of results"),
                fieldWithPath(LinkPath.get("prev")).type(JsonFieldType.STRING).optional()
                        .description("The previous page of results"),
                fieldWithPath(LinkPath.get("search")).type(JsonFieldType.STRING).optional()
                        .description("The search url provided for this resource"),
                fieldWithPath(LinkPath.get("profile")).type(JsonFieldType.STRING).optional()
                        .description("The resource \"profile\" ")
        };
    }

    /**
     * Returns the common fields contained in page attributes along with the provided descriptors in the services
     * responses.
     *
     * @param descriptors {@link FieldDescriptor}s to be included in the returned array of field descriptors.
     * @return Array of FieldDescriptor
     */
    public static FieldDescriptor[] getPagingFields(final FieldDescriptor... descriptors) {
        FieldDescriptor[] common = pagingFields();
        FieldDescriptor[] finalDescriptors = new FieldDescriptor[common.length + descriptors.length];

        System.arraycopy(descriptors, 0, finalDescriptors, 0, descriptors.length);
        System.arraycopy(common, 0, finalDescriptors, descriptors.length, common.length);

        return finalDescriptors;
    }

    /**
     * Returns the common fields specified for page attributes along with the provided descriptors (which may be
     * optional) in the services requests.
     *
     * @param dc          Instance of {@link DefaultConstant}.
     * @param descriptors Optional {@link javax.management.Descriptor}s to include in the returned array of descriptors.
     * @return An array of {@link ParameterDescriptor}s.
     */
    public static ParameterDescriptor[] getRelaxedPagingParameters(final DefaultConstant dc,
                                                                   final ParameterDescriptor... descriptors) {
        ParameterDescriptor[] common = {
                parameterWithName(dc.getPageSortParam()).optional().description(LinkPath.PAGE_SORT_PARAM_META),
                parameterWithName(dc.getPageSizeParam()).optional().description(LinkPath.PAGE_SIZE_PARAM_META),
                parameterWithName(dc.getPagePageParam()).optional().description(LinkPath.PAGE_PAGE_PARAM_META),
        };
        ParameterDescriptor[] finalDescriptors = new ParameterDescriptor[common.length + descriptors.length];
        System.arraycopy(descriptors, 0, finalDescriptors, 0, descriptors.length);
        System.arraycopy(common, 0, finalDescriptors, descriptors.length, common.length);

        return finalDescriptors;
    }

    /**
     * Returns the Links Snippet that will document the links in the API operation's response.
     *
     * @param descriptors One or more {@link LinkDescriptor}.
     * @return LinksSnippet
     */
    public static LinksSnippet links(final LinkDescriptor... descriptors) {
        return HypermediaDocumentation.links(HypermediaDocumentation.linkWithRel("self").ignored().optional())
                .and(descriptors);
    }

}
