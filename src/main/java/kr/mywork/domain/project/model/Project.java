package kr.mywork.domain.project.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import kr.mywork.common.rdb.id.UnixTimeOrderedUuidGeneratedValue;
import kr.mywork.domain.project.service.dto.request.ProjectUpdateRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

	@Id
	@UnixTimeOrderedUuidGeneratedValue
	private UUID id;

	@Column(length = 200, nullable = false)
	private String name;

	@Column(nullable = false, columnDefinition = "timestamp")
	private LocalDateTime startAt;

	@Column(nullable = false, columnDefinition = "timestamp")
	private LocalDateTime endAt;

	@Column(length = 200)
	private String step;

	@Column(nullable = false, columnDefinition = "timestamp")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(nullable = false, columnDefinition = "timestamp")
	@UpdateTimestamp
	private LocalDateTime modifiedAt;

	@Column(name = "detail", length = 500)
	private String detail;

	@Column(name = "deleted", nullable = false)
	@Setter
	private Boolean deleted = false;

	public Project(
		final String name,
		final LocalDateTime startAt,
		final LocalDateTime endAt,
		final String step,
		final String detail
	) {
		this.name = name;
		this.startAt = startAt;
		this.endAt = endAt;
		this.step = step;
		this.detail = detail;
	}

	public void updateFrom(ProjectUpdateRequest request) {
		this.name = request.getName();
		this.startAt = request.getStartAt();
		this.endAt = request.getEndAt();
		this.step = request.getStep();
		this.detail = request.getDetail();
		this.deleted = request.getDeleted();
	}
}
