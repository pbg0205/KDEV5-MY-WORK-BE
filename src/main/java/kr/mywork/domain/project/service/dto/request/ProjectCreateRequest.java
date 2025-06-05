package kr.mywork.domain.project.service.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.mywork.domain.project.model.Project;
import kr.mywork.domain.project.model.ProjectAssign;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProjectCreateRequest {
	private final String name;
	private final LocalDateTime startAt;
	private final LocalDateTime endAt;
	private final String step;
	private final String detail;
	private final UUID devCompanyId;
	private final UUID clientCompanyId;

	public Project toEntity() {
		return new Project(
			this.name,
			this.startAt,
			this.endAt,
			this.step,
			this.detail
		);
	}

	public ProjectAssign toAssignEntity(UUID projectId) {
		return new ProjectAssign(
			projectId,
			this.devCompanyId,
			this.clientCompanyId
		);
	}
}

