package kr.mywork.interfaces.project.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kr.mywork.common.api.support.response.ApiResponse;
import kr.mywork.domain.project.service.ProjectService;
import kr.mywork.domain.project.service.dto.request.ProjectCreateRequest;
import kr.mywork.domain.project.service.dto.request.ProjectUpdateRequest;
import kr.mywork.domain.project.service.dto.response.ProjectSelectResponse;
import kr.mywork.domain.project.service.dto.response.ProjectSelectWithAssignResponse;
import kr.mywork.domain.project.service.dto.response.ProjectUpdateResponse;
import kr.mywork.interfaces.project.controller.dto.request.ProjectCreateWebRequest;
import kr.mywork.interfaces.project.controller.dto.request.ProjectDeleteWebRequest;
import kr.mywork.interfaces.project.controller.dto.request.ProjectUpdateWebRequest;
import kr.mywork.interfaces.project.controller.dto.response.ProjectCreateWebResponse;
import kr.mywork.interfaces.project.controller.dto.response.ProjectDeleteWebResponse;
import kr.mywork.interfaces.project.controller.dto.response.ProjectDetailWebResponse;
import kr.mywork.interfaces.project.controller.dto.response.ProjectListWebResponse;
import kr.mywork.interfaces.project.controller.dto.response.ProjectSelectWebResponse;
import kr.mywork.interfaces.project.controller.dto.response.ProjectUpdateWebResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@Validated
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@PostMapping
	public ApiResponse<ProjectCreateWebResponse> createProject(
		@RequestBody @Valid final ProjectCreateWebRequest webRequest
	) {
		// 1) WebRequest → Service DTO
		final ProjectCreateRequest dto = webRequest.toServiceDto();

		// 2) 서비스 호출
		final UUID createdId = projectService.createProject(dto);

		// 3) WebResponse 생성 후 반환
		return ApiResponse.success(new ProjectCreateWebResponse(createdId));
	}

	@PutMapping("/{projectId}")
	public ApiResponse<ProjectUpdateWebResponse> updateProject(
		@RequestBody @Valid final ProjectUpdateWebRequest webRequest,
		@PathVariable final UUID projectId
	) {
		final ProjectUpdateRequest dto = webRequest.toServiceDto(projectId);

		final ProjectUpdateResponse serviceResponse = projectService.updateProject(dto);

		final ProjectUpdateWebResponse webResponse = ProjectUpdateWebResponse.from(serviceResponse);

		return ApiResponse.success(webResponse);
	}

	@DeleteMapping
	public ApiResponse<ProjectDeleteWebResponse> deleteProject(
		@RequestBody final ProjectDeleteWebRequest webRequest
	) {
		// 1) WebRequest → service 로직 (ID만 필요)
		final UUID deletedId = projectService.deleteProject(webRequest.getId());

		// 2) WebResponse 생성
		return ApiResponse.success(new ProjectDeleteWebResponse(deletedId));
	}

	@GetMapping("/{projectId}")
	public ApiResponse<ProjectDetailWebResponse> getProjectDetail(
		@PathVariable("projectId") final UUID projectId
	) {
		// 1) 서비스에서 단건 조회
		ProjectSelectResponse serviceDetail = projectService.findProjectById(projectId);

		// 2) ServiceResponse → WebResponse
		ProjectDetailWebResponse webDetail = ProjectDetailWebResponse.from(serviceDetail);

		return ApiResponse.success(webDetail);
	}

	@GetMapping
	public ApiResponse<ProjectListWebResponse> listProjects(
		@RequestParam(name = "page") @Min(value = 1, message = "{invalid.page}") final int page,
		@RequestParam(name = "memberId", required = false) final UUID memberId,
		@RequestParam(name = "nameKeyword", required = false) final String nameKeyword,
		@RequestParam(name = "deleted", required = false) final Boolean deleted
	) {
		List<ProjectSelectWithAssignResponse> serviceList =
			projectService.findProjectsBySearchConditionWithPaging(page, memberId, nameKeyword, deleted);

		List<ProjectSelectWebResponse> webList = serviceList.stream()
			.map(ProjectSelectWebResponse::from)
			.collect(Collectors.toList());

		long totalCount = projectService.countTotalProjectsByCondition(memberId, nameKeyword, deleted);

		return ApiResponse.success(new ProjectListWebResponse(webList, totalCount));
	}
}
