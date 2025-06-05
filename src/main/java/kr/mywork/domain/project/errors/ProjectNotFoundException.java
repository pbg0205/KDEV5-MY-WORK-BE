package kr.mywork.domain.project.errors;

public class ProjectNotFoundException extends ProjectException {
	public ProjectNotFoundException(ProjectErrorType errorType) {
		super(errorType);
	}
}
