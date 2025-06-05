package kr.mywork.infrastructure.project.rdb;

import static kr.mywork.domain.project.model.QProjectAssign.*;
import static kr.mywork.domain.project.model.QProjectAssign.projectAssign;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.mywork.domain.project.model.ProjectAssign;
import kr.mywork.domain.project.repository.ProjectAssignRepository;
import kr.mywork.domain.project.service.dto.response.ProjectAssignResponse;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional
public class QueryDslProjectAssignRepository implements ProjectAssignRepository {

	private final JPAQueryFactory queryFactory;
	private final JpaProjectAssignRepository jpaProjectAssignRepository;

	@Override
	public ProjectAssign save(ProjectAssign projectAssign) {
		return jpaProjectAssignRepository.save(projectAssign);
	}


	@Override
	public Optional<ProjectAssign> findByProjectId(UUID projectId) {
		return jpaProjectAssignRepository.findByProjectId(projectId);
	}

	@Override
	public Optional<ProjectAssignResponse> findDtoByProjectId(UUID projectId) {
		return Optional.ofNullable(
			queryFactory
				.select(Projections.constructor(
					ProjectAssignResponse.class,
					projectAssign.projectId,
					projectAssign.devCompanyId,
					projectAssign.clientCompanyId
				))
				.from(projectAssign)
				.where(projectAssign.projectId.eq(projectId))
				.fetchOne()
		);
	}

}
