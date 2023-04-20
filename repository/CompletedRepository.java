package engine.repository;

import engine.entity.Completed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompletedRepository extends PagingAndSortingRepository<Completed, Long>, JpaRepository<Completed, Long> {
    Page<Completed> findByEmailOrderByCompletedAtDesc(String email, Pageable pageable);
}
