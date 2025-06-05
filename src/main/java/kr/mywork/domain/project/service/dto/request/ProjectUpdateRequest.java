package kr.mywork.domain.project.service.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProjectUpdateRequest {

	private final UUID id;

	private final String name;

	private final LocalDateTime startAt;

	private final LocalDateTime endAt;

	private final String step;

	private final String detail;

	private final Boolean deleted;
}
