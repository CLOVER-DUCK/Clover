package loverduck.clover.repository;

import loverduck.clover.entity.Funding;
import loverduck.clover.entity.Users;
import loverduck.clover.entity.Wallet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UsersRepository extends JpaRepository<Users, Long>, QuerydslPredicateExecutor<Users> {
    @Query("select u from Users u where u.email = :email")
    Users findByEmail(String email);

    @Query("select u from Users u where u.userid = :userid")
    Users findByUserid(String userid);

    
    @Transactional
    @Modifying
    @Query("update Users u set u.wallet = :wallet where u.email = :email")
    void updateWallet(Wallet wallet, String email);
    
  
    @Transactional
    @Modifying
    @Query("update Users u set u.wallet = :wallet where u.email = :email")
    void updateImg(Wallet wallet, String email);
    
    
    @Transactional
    @Modifying
    @Query("update Users u set u.nickname = :nickname, u.password = :password, u.phone = :phone, u.postalCode = :postalCode, u.address = :address, u.detailAddress = :detailAddress where u.email = :email")
    void updateUser(String nickname, String password, String phone, String postalCode, String address, String detailAddress, String email);
    
    @Transactional
    @Modifying
    @Query("update Users u set u.name = :name ,u.nickname = :nickname, u.imgProfile = :imgProfile, u.type = :type, u.phone = :phone, u.postalCode = :postalCode, u.address = :address, u.detailAddress = :detailAddress where u.email = :email")
    void updateUserAll(String name, String nickname, String imgProfile, Integer type, String phone, String postalCode, String address,
			String detailAddress, String email);
    

    String FIND_USER_BY_ID_QUERY = "select * from users where user_id = :id";
   	@Query(nativeQuery = true, value = FIND_USER_BY_ID_QUERY)
     Users findUserById(Long id);

  

}
