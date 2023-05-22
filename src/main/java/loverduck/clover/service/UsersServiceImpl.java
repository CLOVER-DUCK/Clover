package loverduck.clover.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import loverduck.clover.entity.Ordered;
import loverduck.clover.entity.UserDetail;
import loverduck.clover.entity.Users;
import loverduck.clover.repository.OrderedRepository;
import loverduck.clover.repository.UserDetailRepository;

import loverduck.clover.repository.UsersRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersServiceImpl implements UsersService {

	
	@Autowired
	private UsersRepository usersRep;
	
	@Autowired
	private UserDetailRepository userDetailRep;
	
	@Autowired
	private OrderedRepository orderedRepository;


	@Override
	public int register(Users users) {
		
		Users dbUser = usersRep.save(users);
		
		return 1;
	}

	
	@Override
	public Users logincheck(String email, String password) {
		
		Users dbUser = usersRep.findByEmail(email);
		
		
		if (dbUser == null) {
	        throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
	    }
	    
	    if (!dbUser.getPassword().equals(password)) {
	        throw new IllegalArgumentException("비밀번호를 다시 확인해주세요.");
	    }
	    
	    return dbUser;
	}

	
	/**
	 * 진짜 - 이메일 중복 체크
	 */
	@Override
	public boolean checkEmailExists(String email) {
		Users users = usersRep.findByEmail(email);
		
		return users!= null;
	}


	/**
	 * 마이페이지 - 내가 투자한 펀딩 목록 출력 
	 */
	@Override
	public List<Ordered> findOrderdByUser(Long id) {
		List<Ordered> myFunds = orderedRepository.findFundingsByUserId(id);
	    return myFunds;
	}



	@Override
	public Users update(String password, String nickname, String phone, String postalCode, String address,
			String detailAddress, String email) {
		
		usersRep.updateUser(nickname, password, phone, postalCode, address, detailAddress, email);
		
		Users dbUser = usersRep.findByEmail(email);
		
		return dbUser;
		
	}





}
