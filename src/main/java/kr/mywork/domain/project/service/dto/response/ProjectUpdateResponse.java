package kr.mywork.domain.project.service.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.mywork.domain.project.model.Project;

public record ProjectUpdateResponse(
	UUID id,
	String name,
	LocalDateTime startAt,
	LocalDateTime endAt,
	String step,
	String detail,
	Boolean deleted,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {
	public static ProjectUpdateResponse from(Project project) {
		return new ProjectUpdateResponse(
			project.getId(),
			project.getName(),
			project.getStartAt(),
			project.getEndAt(),
			project.getStep(),
			project.getDetail(),
			project.getDeleted(),
			project.getCreatedAt(),
			project.getModifiedAt()
		);
	}
}
