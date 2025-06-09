package kr.mywork.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParameters;

import kr.mywork.common.api.support.response.ResultType;

public class ProjectDocumentationTest extends RestDocsDocumentation {

	@Test
	@DisplayName("프로젝트 할당될 직원 ")
	@Sql("classpath:sql/project-for-member-list.sql")
	void 프로젝트_할당_멤버_조회_성공() throws Exception {
		//given
		final String accessToken = createDevAdminAccessToken();

		final UUID projectId = UUID.fromString("d73b1f10-47e2-7a2d-c1e5-f17125d62999");
		final UUID companyId = UUID.fromString("a62a0c20-91e2-7c2d-b0e5-e16115c61888");

		//when
		final ResultActions result = mockMvc.perform(
			get("/api/projects/members")
				.param("projectId", projectId.toString())
				.param("companyId", companyId.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, toBearerAuthorizationHeader(accessToken)));

		//then
		result.andExpectAll(
				status().isOk(),
				jsonPath("$.result").value(ResultType.SUCCESS.name()),
				jsonPath("$.data").exists(),
				jsonPath("$.error").doesNotExist())
			.andDo(document("project-member-get-success", projectMemberGetSuccessResource()));
	}

	private ResourceSnippet projectMemberGetSuccessResource() {
		return resource(
			ResourceSnippetParameters.builder()
				.tag("Project API")
				.summary("프로젝트 할당할 멤버 조회 API")
				.description("프로젝트 멤버를 조회한다")
				.requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입"),
					headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰"))
				.queryParameters(
					parameterWithName("projectId").description("프로젝트 아이디"),
					parameterWithName("companyId").description("회사 아이디"))
				.responseFields(
					fieldWithPath("result").type(JsonFieldType.STRING).description("응답 결과"),
					fieldWithPath("data.members[].memberId").type(JsonFieldType.STRING)
						.description("프로젝트 할당 가능한 멤버 아이디"),
					fieldWithPath("data.members[].memberName").type(JsonFieldType.STRING)
						.description("프로젝트 할당 가능한 멤버 이름"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보"))
				.build());
	}
}
