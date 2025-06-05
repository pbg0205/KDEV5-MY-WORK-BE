package kr.mywork.infrastructure.project.rdb;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.mywork.domain.project.model.ProjectAssign;
import kr.mywork.domain.project.repository.ProjectAssignRepository;

public interface JpaProjectAssignRepository extends JpaRepository<ProjectAssign, UUID> {
	Optional<ProjectAssign> findByProjectId(UUID projectId);

}
