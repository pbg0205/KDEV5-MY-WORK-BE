package kr.mywork.domain.project.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import kr.mywork.common.rdb.id.UnixTimeOrderedUuidGeneratedValue;
import kr.mywork.domain.project.service.dto.request.ProjectAssignUpdateRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectAssign {

	@Id
	@UnixTimeOrderedUuidGeneratedValue
	private UUID id;

	@Column(nullable = false, columnDefinition = "BINARY(16)")
	private UUID projectId;

	@Column(nullable = false, columnDefinition = "BINARY(16)")
	@Setter
	private UUID devCompanyId;

	@Column(nullable = false, columnDefinition = "BINARY(16)")
	@Setter
	private UUID clientCompanyId;

	@Column(nullable = false, columnDefinition = "timestamp")
	@CreationTimestamp
	private LocalDateTime createdAt;

	// ✔ 생성자: createdAt은 JPA가 @CreationTimestamp로 자동 채워주므로 제거
	public ProjectAssign(
		final UUID projectId,
		final UUID devCompanyId,
		final UUID clientCompanyId
	) {
		this.projectId = projectId;
		this.devCompanyId = devCompanyId;
		this.clientCompanyId = clientCompanyId;
	}

	public void updateFrom(ProjectAssignUpdateRequest request) {
		this.devCompanyId = request.getDevCompanyId();
		this.clientCompanyId = request.getClientCompanyId();
	}
}
