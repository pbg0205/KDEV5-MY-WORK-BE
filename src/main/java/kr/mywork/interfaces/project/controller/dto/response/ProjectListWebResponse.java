package kr.mywork.interfaces.project.controller.dto.response;

import java.util.List;

public record ProjectListWebResponse(
	List<ProjectSelectWebResponse> projects,
	long totalCount
) {}
