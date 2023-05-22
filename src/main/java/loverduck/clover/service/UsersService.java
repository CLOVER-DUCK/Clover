package loverduck.clover.service;

import java.util.List;

import loverduck.clover.entity.Ordered;
import loverduck.clover.entity.UserDetail;
import loverduck.clover.entity.Users;

public interface UsersService {
	
	
	/**
	 * 회원가입하기
	 */
	int register(Users users,UserDetail userDetail);
	
	/**
	 * 로그인채크
	 */
	Users logincheck(String email, String password);
	
	/**
	 * 로그인
	 */
//	Users findByEmail(String email);

	
	/**
	 * 진짜 - 이메일 중복 체크
	 * @param email
	 * @return
	 */
	boolean checkEmailExists(String email);	
	
	/**
	 * 마이페이지 투자한 펀딩 목록 출력 
	 */
	List<Ordered> findOrderdByUser(Long id);

	
}
