package kr.mywork.domain.project.errors;

import lombok.Getter;

@Getter
public abstract class ProjectException extends RuntimeException {
	private final ProjectErrorType errorType;

	public ProjectException(final ProjectErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}
