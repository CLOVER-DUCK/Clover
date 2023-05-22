package loverduck.clover.service;

import java.util.List;

import loverduck.clover.entity.Ordered;
import loverduck.clover.entity.UserDetail;

import loverduck.clover.entity.Users;

public interface UsersService {
	
	
	/**
	 * 회원가입하기
	 */
	int register(Users users);
	
	/**
	 * 로그인채크
	 */
	Users logincheck(String email, String password);
	

	/**
	 * 진짜 - 이메일 중복 체크
	 */
	boolean checkEmailExists(String email);	
	
	/**
	 * 마이페이지 투자한 펀딩 목록 출력 
	 */
	List<Ordered> findOrderdByUser(Long id);
  
  /**
	 * 수정하기 - 비밀번호, 닉네임, 폰, 주소3개
	 */
	Users update(String password, String nickname, String phone,String postalCode,String address, String detailAddress, String email);
	
}
