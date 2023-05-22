package loverduck.clover.repository;

import loverduck.clover.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UsersRepository extends JpaRepository<Users, Long>, QuerydslPredicateExecutor<Users> {
    @Query("select u from Users u where u.email = :email")
    Users findByEmail(String email);

    @Query("select u from Users u where u.userid = :userid")
    Users findByUserid(String userid);

    boolean existsByEmail(String email);
    
    String FIND_USER_BY_ID_QUERY = "select * from users where user_id = :id";
	@Query(nativeQuery = true, value = FIND_USER_BY_ID_QUERY)
    Users findUserById(Long id);
    
}
