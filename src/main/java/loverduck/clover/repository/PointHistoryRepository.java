package loverduck.clover.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import loverduck.clover.entity.PointHistory;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>, QuerydslPredicateExecutor<PointHistory> {


	

}