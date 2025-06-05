package kr.mywork.domain.project.service.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.mywork.domain.project.model.Project;

public record ProjectSelectWithAssignResponse(
	UUID id,
	String name,            
	LocalDateTime startAt,
	LocalDateTime endAt,
	String step,
	String detail,
	Boolean deleted,
	LocalDateTime createdAt,
	UUID devCompanyId,
	UUID clientCompanyId
) {
	public static ProjectSelectWithAssignResponse of(UUID projectId,
		String projectName,
		LocalDateTime startAt,
		LocalDateTime endAt,
		String step,
		String detail,
		Boolean deleted,
		LocalDateTime createdAt,
		UUID devCompanyId,
		UUID clientCompanyId) {
		return new ProjectSelectWithAssignResponse(
			projectId, projectName, startAt, endAt,
			step, detail, deleted, createdAt,
			devCompanyId, clientCompanyId
		);
	}
}
