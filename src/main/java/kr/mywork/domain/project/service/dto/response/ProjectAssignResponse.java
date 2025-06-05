package kr.mywork.domain.project.service.dto.response;

import java.util.UUID;

public record ProjectAssignResponse(
	UUID projectId,
	UUID devCompanyId,
	UUID clientCompanyId
) {}
