package kr.mywork.domain.project.repository;

import java.util.Optional;
import java.util.UUID;

import kr.mywork.domain.project.model.ProjectAssign;
import kr.mywork.domain.project.service.dto.response.ProjectAssignResponse;

public interface ProjectAssignRepository {

	ProjectAssign save(ProjectAssign projectAssign);

	Optional<ProjectAssign> findByProjectId(UUID projectId);

	Optional<ProjectAssignResponse> findDtoByProjectId(UUID projectId);
}
