package loverduck.clover.repository;

import loverduck.clover.entity.Ordered;
import loverduck.clover.entity.Users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface OrderedRepository extends JpaRepository<Ordered, Long>, QuerydslPredicateExecutor<Ordered> {
	
	String FIND_FUNDINGS_BY_USER_ID_QUERY = "select * from orderd where user_id = :id";
	@Query(nativeQuery = true, value = FIND_FUNDINGS_BY_USER_ID_QUERY)
	List<Ordered> findFundingsByUserId(Long id);
	

}
