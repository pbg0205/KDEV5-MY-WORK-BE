package kr.mywork.interfaces.project.controller.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.mywork.domain.project.service.dto.request.ProjectUpdateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ProjectUpdateWebRequest {
	private final UUID id;
	private final String name;
	private final LocalDateTime startAt;
	private final LocalDateTime endAt;
	private final String step;
	private final String detail;
	private final Boolean deleted;

	public ProjectUpdateRequest toServiceDto(UUID projectId) {
		return new ProjectUpdateRequest(
			this.id,
			this.name,
			this.startAt,
			this.endAt,
			this.step,
			this.detail,
			this.deleted
		);
	}
}
