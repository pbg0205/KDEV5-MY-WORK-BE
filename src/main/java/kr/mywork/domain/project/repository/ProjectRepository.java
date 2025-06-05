package kr.mywork.domain.project.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import kr.mywork.domain.project.model.Project;
import kr.mywork.domain.project.service.dto.request.ProjectCreateRequest;
import kr.mywork.domain.project.service.dto.response.ProjectSelectResponse;
import kr.mywork.domain.project.service.dto.response.ProjectSelectWithAssignResponse;

public interface ProjectRepository {

	Project save(ProjectCreateRequest projectCreateRequest);

	Optional<Project> findById(UUID projectId);

	List<ProjectSelectWithAssignResponse> findProjectsBySearchConditionWithPaging(
		int page,
		int size,
		UUID memberId,
		String nameKeyword,
		Boolean deleted
	);

	Long countTotalProjectsByCondition(
		UUID memberId,
		String nameKeyword,
		Boolean deleted
	);
}
