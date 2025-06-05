package kr.mywork.interfaces.project.controller.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.mywork.domain.project.service.dto.response.ProjectSelectResponse;

public record ProjectDetailWebResponse(
	UUID id,
	String name,
	LocalDateTime startAt,
	LocalDateTime endAt,
	String step,
	String detail,
	Boolean deleted
) {
	public static ProjectDetailWebResponse from(ProjectSelectResponse response) {
		return new ProjectDetailWebResponse(
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
