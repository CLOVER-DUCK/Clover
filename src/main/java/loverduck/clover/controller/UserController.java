package loverduck.clover.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

import loverduck.clover.entity.Users;
import loverduck.clover.service.KakaoServiceImpl;
import loverduck.clover.service.UsersService;

import loverduck.clover.entity.Ordered;
import loverduck.clover.entity.UserDetail;

/**
 * 회원가입 , 로그인 , 마이페이지 
 */
@Controller
@RequiredArgsConstructor
public class UserController {	
	
	private final UsersService usersService;
	
	@Autowired
	private KakaoServiceImpl kakaoService;
	
	/**
	 * 메인
	 */
	@GetMapping("/")
	public String mainPage() {
		
		return "mypage/index";
	}
	
	/**
	 * 회원가입 - 투자자 폼
	 */
	@GetMapping("/registerInvestor")
	public String registerInvestor() {
		System.out.println("투자자폼");
		return "registerInvestor";
	}
	
	/**
	 * 회원가입 투자자
	 */
	@PostMapping("/registerInvestor")
	public String register(String name, String nickname, String email, String userid, String password, String password2, String phone,String postalCode,String address, String detailAddress ) {

		Users dbUser = new Users(null, userid, password, email,name , nickname,  1, phone, postalCode, address, detailAddress, null, null);

		int userCreateForm = usersService.register(dbUser);
		
		return "redirect:/";
	}
	
	/**
	 * 회원가입 - 기업 폼
	 */
	@GetMapping("/registerCorp")
	public String registerCorp() {
		
		return "registerCorp";
	}

	/**
	 * 회원가입 기업
	 * 추가로 받는 정보 : 사업자 등록번호, 산업선택, 기업url
	 *                    no       sector    homepage
	 */
	@PostMapping("/registerCorp")
	public String register2(String name, String nickname, String email, String userid, String password, String password2, 
			String phone,String postalCode,String address, String detailAddress,
			String no, String sector, String homepage) {
		
		Users dbUser = new Users(null, userid, password, email , name , nickname, 0, phone, postalCode, address, detailAddress, null, null);

		//System.out.println("출력 "+ dbUser.getEmail()+" "+dbUser.getNickname()+" "+dbUser.getUserid() );
		
		int userCreateForm = usersService.register(dbUser);
		
		return "redirect:/";
	}
	
	/**
	 * 진짜 - 이메일 중복체크
	 */
	@GetMapping("/checkEmail")
    @ResponseBody
    public ResponseEntity<Object> checkEmail(@RequestParam("email") String email) {
        boolean emailExists = usersService.checkEmailExists(email);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", emailExists);
        return ResponseEntity.ok(response);
    }
	
	
	/**
	 * 로그인 폼
	 */
	@RequestMapping("/loginForm")
	public String login() {

		return "login";
	}
	
	/**
	 * 로그인하기
	 * post -> 페이지 전환 없이 값만 전달
	 */
	@PostMapping("/loginCheck")
	public String loginCheck(String email, String password, HttpSession session, Model model) {
		try {

	        Users dbUser = usersService.logincheck(email, password);
	        
	        session.setAttribute("loginUserId", dbUser.getUserid());
	        session.setAttribute("loginEmail", dbUser.getEmail());
	        
	        session.setAttribute("user", dbUser);
	        
	        
	        return "redirect:/";
	    } catch (IllegalArgumentException e) {
	        model.addAttribute("error", e.getMessage());
	        return "login";
	    }
	}
	
	
	/**
	 * 로그인하기 - 카카오톡 로그인
	 */
	@RequestMapping("/kakao")
	public String kakaoLogin(@RequestParam("code") String code, HttpSession session) {
		String access_Token = kakaoService.getAccessToken(code);

		HashMap<String, Object> userInfo = kakaoService.getUserInfo(access_Token);
	
//	    클라이언트의 이메일이 존재할 때 세션에 해당 이메일 등록 토큰은 x
		if(userInfo.get("email")!=null) {
//			session.setAttribute("loginEmail", userInfo.get("email"));
//			session.setAttribute("access_Token", access_Token);
			
			String email = (String) userInfo.get("email");
			String nickname = (String) userInfo.get("nickname");
			session.setAttribute("loginEmail", email);
						
			Users dbUser = new Users(nickname, access_Token, email, nickname, null);
			
			session.setAttribute("user", dbUser);
			
			boolean emailExists = usersService.checkEmailExists(email);
			if(emailExists == false) {
				//이메일 존재 안할때 저장하기
				usersService.register(dbUser);
			}
			
			
		}
		
		return "redirect:/";
	}
	
	/**
	 * 로그아웃 하기 (카톡 로그아웃 포함)
	 */
	@GetMapping("/logout")
	public String logout(HttpSession session) {

		session.invalidate();
			    
		return "redirect:/";
	}
	
	/**
	 * 마이페이지 - 투자자 (개인정보 수정폼)
	 */
	@GetMapping("/updateInvestor")
	public String updateInvestorForm() {

		return "mypage/updateInvestor";
	}
	
	/**
	 * 마이페이지 - 투자자 (개인정보 수정폼 수정하기)
	 */
	@PostMapping("/updateInvestor")
	public String updateInvestor( String nickname, String password,String phone,String postalCode,String address, String detailAddress , HttpSession session)  {
		Users dbUser = (Users) session.getAttribute("user");
		
		String email = dbUser.getEmail();
		System.out.println(email+" 이메일이다 ");
		if(nickname==null) {
			nickname = dbUser.getNickname();
		}
		if(phone==null) {
			phone = dbUser.getPhone();
		}
		if(postalCode==null) {
			postalCode = dbUser.getPostalCode();
		}
		if(address==null) {
			address = dbUser.getAddress();
		}
		if(detailAddress==null) {
			detailAddress = dbUser.getDetailAddress();
		}
		
		Users UpUser = usersService.update(password, nickname, phone, postalCode, address, detailAddress, email);
		
		session.setAttribute("user", UpUser);
		
		return "redirect:/";
	}
	
	/**
	 * 마이페이지 - 기업 (개인정보 수정폼)
	 */
	@GetMapping("/updateCorp")
	public String updateCorpForm() {
		return "mypage/updateCorp";
	}
	/**
	 * 마이페이지 - 기업 (개인정보 수정폼)
	 */
	@PostMapping("/updateCorp")
	public String updateCorp( String nickname, String password,String phone,String postalCode,String address, String detailAddress , HttpSession session)  {
		Users dbUser = (Users) session.getAttribute("user");
		
		String email = dbUser.getEmail();
	
		if(nickname==null) {
			nickname = dbUser.getNickname();
		}
		if(phone==null) {
			phone = dbUser.getPhone();
		}
		if(postalCode==null) {
			postalCode = dbUser.getPostalCode();
		}
		if(address==null) {
			address = dbUser.getAddress();
		}
		if(detailAddress==null) {
			detailAddress = dbUser.getDetailAddress();
		}
		
		Users UpUser = usersService.update(password, nickname, phone, postalCode, address, detailAddress, email);
		
		session.setAttribute("user", UpUser);
		
		return "redirect:/";
	}
	
	/**
	 * 마이페이지 - 투자자 (내 펀딩)
	 */
	@RequestMapping("mypage/mypageInvestor/{id}")
	public String mypageInvestor(@PathVariable Long id, Model model, HttpSession session) {
	    Users user = (Users) session.getAttribute("user");
	    if (user != null) {
	        List<Ordered> myfunds = usersService.findOrderdByUser(user.getId());
	        model.addAttribute("myfunds", myfunds);
	        return "mypage/mypageInvestor";
	    } else {
	        // 세션에 사용자 정보가 없는 경우 로그인 창으로 
	        return "redirect:/login";
	    }
	}
	
	/**
	 * 마이페이지 - 기업 펀딩 현황?
	 */
	@RequestMapping("/mypageCorp")
	public String mypageCorp() {
		
		return "mypage/mypageCorp";
	}
	
	///////////////////////////////////////////////////////////////

	/**
	 * 마이페이지 - 투자자 (거래 내역)
	 */
	@RequestMapping("/historyInvestor")
	public String historyInvestor() {
		
		return "mypage/historyInvestor";
	}


	/**
	 * 마이페이지 - 투자자 (정산) - 페이지 레이아웃 미완료
	 */

	/**
	 * 마이페이지 - 투자자 (포인트 충전)
	 */
	@RequestMapping("/charge")
	public String pointCharge() {
		
		return "mypage/pointCharge";
	}
	
	/**
	 * 마이페이지 - 투자자 (포인트 충전완료)
	 */
	@RequestMapping("/chargeFin")
	public String pointChargeFin() {
		
		return "mypage/pointChargeFin";
	}
	
	//////////////////////////////////////
	
	/**
	 * 마이페이지 - 투자자 & 기업 (문의 내역)
	 */
	@RequestMapping("/historyAsk")
	public String historyAsk() {
		
		return "mypage/historyAsk";
	}

	/**
	 * 마이페이지 - 투자자 & 기업 (문의 내역 > 문의하기)
	 */
	@RequestMapping("/ask")
	public String doAsk() {
		
		return "mypage/ask";
	}
	
	/**
	 * 마이페이지 - 기업 펀드 신청
	 */
	@RequestMapping("/fundSubmitForm")
	public String fundSubmitForm() {
		
		return "mypage/fundSubmitForm";
	}
	
	/**
	 * 마이페이지 - 기업 펀드 신청 (등록하기)
	 * insert 펀드를 등록시킨다. ->등록되면 내역으로 보내기
	 */
	@RequestMapping("/fundSubmit")
	public String fundSubmit() {
		
		return "mypage/historyCorp";
	}
	
	/**
	 * 마이페이지 - 기업 (펀딩 신청) - 펀딩 신청 내역
	 * 펀드 신청 조회
	 */
	@RequestMapping("/historyCorp")
	public String historyCorp() {
		
		return "mypage/historyCorp";
	}
	
	/**
	 * 마이페이지 - 기업 (정산) - 페이지 레이아웃 미완료
	 */
	
	/**
	 * 마이페이지 - 기업 (포인트)  - 페이지 레이아웃 미완료 (투자자랑 비슷)
	 */
	@RequestMapping("/pointCorp")
	public String pointCorp() {
		
		return "mypage/pointCharge";
	}
	
	
}
