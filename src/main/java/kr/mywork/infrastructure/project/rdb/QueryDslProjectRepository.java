package kr.mywork.infrastructure.project.rdb;

import static kr.mywork.domain.project.model.QProject.project;
import static kr.mywork.domain.project.model.QProjectAssign.projectAssign;
import static kr.mywork.domain.project.model.QProjectMember.projectMember;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.mywork.domain.project.model.Project;
import kr.mywork.domain.project.model.ProjectAssign;
import kr.mywork.domain.project.repository.ProjectRepository;
import kr.mywork.domain.project.service.dto.request.ProjectCreateRequest;
import kr.mywork.domain.project.service.dto.response.ProjectSelectResponse;
import kr.mywork.domain.project.service.dto.response.ProjectSelectWithAssignResponse;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional
public class QueryDslProjectRepository implements ProjectRepository {

	private final JpaProjectRepository       projectRepository;
	private final JpaProjectAssignRepository projectAssignRepository;
	private final JPAQueryFactory            queryFactory;

	@Override
	public Project save(ProjectCreateRequest request) {
		// 1) Project 저장(신규 생성 또는 수정)
		Project project = projectRepository.save(request.toEntity());

		// 2) 항상 새로운 ProjectAssign 객체 생성
		ProjectAssign assign = new ProjectAssign(
			project.getId(),                          // 방금 저장된 Project 엔티티
			request.getDevCompanyId(),       // 요청에서 전달된 개발사 ID
			request.getClientCompanyId()    // 요청에서 전달된 고객사 ID
		);

		// 3) 바로 저장 (기존 데이터 조회 없이 INSERT)
		projectAssignRepository.save(assign);

		return project;
	}

	@Override
	public Optional<Project> findById(UUID projectId) {
		return projectRepository.findById(projectId);
	}

	@Override
	public List<ProjectSelectWithAssignResponse> findProjectsBySearchConditionWithPaging(
		int page,
		int size,
		UUID memberId,
		String nameKeyword,
		Boolean deleted
	) {
		int offset = (page - 1) * size;

		return queryFactory
			.select(Projections.constructor(
				ProjectSelectWithAssignResponse.class,
				project.id,
				project.name,
				project.startAt,
				project.endAt,
				project.step,
				project.detail,
				project.deleted,
				project.createdAt,
				projectAssign.devCompanyId,
				projectAssign.clientCompanyId
			))
			.from(project)
			.leftJoin(projectAssign).on(projectAssign.projectId.eq(project.id))
			.leftJoin(projectMember).on(projectMember.projectId.eq(project.id))
			.where(
				eqMember(memberId),
				eqName(nameKeyword),
				eqDeleted(deleted)
			)
			.offset(offset)
			.limit(size)
			.fetch();
	}

	@Override
	public Long countTotalProjectsByCondition(
		UUID memberId,
		String nameKeyword,
		Boolean deleted
	) {
		return queryFactory
			.select(project.id.count())
			.from(project)
			.leftJoin(projectMember).on(projectMember.projectId.eq(project.id))
			.where(
				eqMember(memberId),
				eqName(nameKeyword),
				eqDeleted(deleted)
			)
			.fetchOne();
	}

	private BooleanExpression eqMember(UUID memberId) {
		if (memberId == null) {
			return null;
		}

		return projectMember.memberId.eq(memberId)
			.and(projectMember.deleted.isFalse());
	}

	private BooleanExpression eqName(String keyword) {
		if (keyword == null || keyword.isEmpty()) {
			return null;
		}
		return project.name.containsIgnoreCase(keyword);
	}

	private BooleanExpression eqDeleted(Boolean deleted) {
		if (deleted == null) {
			return null;
		}
		return project.deleted.eq(deleted);
	}
}
