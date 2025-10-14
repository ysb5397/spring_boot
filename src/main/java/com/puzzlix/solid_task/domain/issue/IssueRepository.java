package com.puzzlix.solid_task.domain.issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long>{

    @Query("select i from Issue i join fetch i.reporter r ")
    List<Issue> findAll();

    @Query("select i from Issue i join fetch i.project p join fetch i.reporter r join fetch i.assignee a where i.id = :issueId")
    Optional<Issue> findById(@Param("issueId") Long issueId);
}
