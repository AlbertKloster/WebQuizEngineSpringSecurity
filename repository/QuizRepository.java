package engine.repository;

import engine.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long>, JpaRepository<Quiz, Long> {
    Page<Quiz> findAll(Pageable pageable);
}
