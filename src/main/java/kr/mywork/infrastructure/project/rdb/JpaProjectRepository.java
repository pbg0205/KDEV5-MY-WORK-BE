package kr.mywork.infrastructure.project.rdb;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.mywork.domain.project.model.Project;

public interface JpaProjectRepository extends JpaRepository<Project, UUID> {
}
