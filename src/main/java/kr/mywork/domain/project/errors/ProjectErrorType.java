package kr.mywork.domain.project.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectErrorType {

	PROJECT_NOT_FOUND(ProjectErrorCode.ERROR_PROJECT01, "해당 프로젝트를 찾을 수 없습니다."),
	PROJECT_ID_NOT_FOUND(ProjectErrorCode.ERROR_PROJECT02, "유효한 프로젝트 ID가 아닙니다.");

	private final ProjectErrorCode code;
	private final String message;
}
