package kr.mywork.domain.project.service.dto.request;

import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProjectAssignUpdateRequest {

	private final UUID devCompanyId;
	private final UUID clientCompanyId;
}
