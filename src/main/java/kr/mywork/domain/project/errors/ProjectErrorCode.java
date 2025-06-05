package kr.mywork.domain.project.errors;

public enum ProjectErrorCode {
	ERROR_PROJECT01,    // 프로젝트를 찾을 수 없음 (예: 조회/수정/삭제 시)
	ERROR_PROJECT02     // 프로젝트 ID 생성/검증 과정에서 문제가 발생했을 때
}
