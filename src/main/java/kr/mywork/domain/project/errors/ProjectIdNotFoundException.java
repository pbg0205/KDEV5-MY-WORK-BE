package kr.mywork.domain.project.errors;

public class ProjectIdNotFoundException extends ProjectException {
  public ProjectIdNotFoundException(ProjectErrorType errorType) {
    super(errorType);
  }
}
