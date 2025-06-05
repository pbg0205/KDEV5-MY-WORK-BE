package kr.mywork.interfaces.project.controller.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.mywork.domain.project.service.dto.response.ProjectUpdateResponse;

public record ProjectUpdateWebResponse(
	UUID id,
	String name,
	LocalDateTime startAt,
	LocalDateTime endAt,
	String step,
	String detail,
	Boolean deleted
) {
	public static ProjectUpdateWebResponse from(ProjectUpdateResponse response) {
		return new ProjectUpdateWebResponse(
			response.id(),
			response.name(),
			response.startAt(),
			response.endAt(),
			response.step(),
			response.detail(),
			response.deleted()
		);
	}
}
