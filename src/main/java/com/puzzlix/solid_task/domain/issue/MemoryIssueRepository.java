package com.puzzlix.solid_task.domain.issue;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryIssueRepository implements IssueRepository {

    private static Map<Long, Issue> store = new ConcurrentHashMap<>();
    private static AtomicLong sequence = new AtomicLong(0);

    @Override
    public Issue save(Issue issue) {
        if(issue.getId() == null) issue.setId(sequence.incrementAndGet());

        store.put(issue.getId(), issue);
        return issue;
    }

    @Override
    public Optional<Issue> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Issue> findAll() {
        return new ArrayList<>(store.values());
    }
}
