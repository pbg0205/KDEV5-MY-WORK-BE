package kr.mywork.domain.project.service.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.mywork.domain.project.model.Project;

public record ProjectSelectResponse(
	UUID id,                // 1
	String name,            // 2
	LocalDateTime startAt,  // 3
	LocalDateTime endAt,    // 4
	String step,            // 5
	String detail,          // 6
	Boolean deleted,        // 7
	LocalDateTime createdAt // 8
) {
	public static ProjectSelectResponse fromEntity(Project project) {
		return new ProjectSelectResponse(
			project.getId(),
			project.getName(),
			project.getStartAt(),
			project.getEndAt(),
			project.getStep(),
			project.getDetail(),
			project.getDeleted(),
			project.getCreatedAt()
		);
	}
}
