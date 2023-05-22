package loverduck.clover.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import loverduck.clover.repository.UsersRepository;
import loverduck.clover.repository.WalletRepository;
import loverduck.clover.service.UsersService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsersTest {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private WalletRepository walletRepository;


    @Autowired
    TestEntityManager testEntityManager;
    JPAQueryFactory qFactory;


    
    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        EntityManager em = testEntityManager.getEntityManager();
        qFactory = new JPAQueryFactory(em);
        // here initialize setup for each test
        System.out.println("Before Strat test >>>> " + testInfo.getTestMethod().get().getName());
    }

    @AfterEach
    void afterEach(TestInfo testInfo) {
        // here initialize setup for each test
        System.out.println("Afetr End test >>>> " + testInfo.getTestMethod().get().getName());
    }

    @Test
    @Rollback(false)
    @DisplayName("test 사용자 추가")
    void insertDatabaseUsers() {
        Users user = Users.builder()
                .userid("test")
                .email("example@example.com")
                .password("1234")
                .nickname("test")
                .type(0)
                .build();
        usersRepository.save(user);
    }

    @Test
    @Rollback(value = false)
    @DisplayName("test 이름을 가진 사용자에게 지갑 추가")
    void insertDatabaseWallet() {
        Users u = qFactory.selectFrom(QUsers.users)
                .where(QUsers.users.userid.eq("test"))
                .fetchOne();
        Wallet wallet = Wallet.builder()
                .user(u)
                .amount(1000000L)
                .build();
        walletRepository.save(wallet);
    }

    @Test
    @Rollback(value = false)
    @DisplayName("test 사용자의 세부정보 설정")
    void insertDatabaseUserDetail() {
        Users u = qFactory.selectFrom(QUsers.users)
                .where(QUsers.users.userid.eq("test"))
                .fetchOne();
       
//        userRepository.save(u);
    }
    
    @Test
    @Rollback(false)
    @DisplayName("User_Userdetail 사용자 추가")
    void insertUsers() {
        Users users = Users.builder()
        		.userid("jang")
                .email("jang@example.com")
                .password("1234")
                .nickname("jang")
                .type(1)
                .build();
        usersRepository.save(users);
        
        /*
        UserDetail ud = UserDetail.builder()
        		.phone("1111")
        		.address("선릉")
        		.detailAddress("123")
        		.build();
        
        Users dbUser = usersRepository.save(users);
		
	     ud.setUser(dbUser); //부모의 key(id)참조 
	     userDetailRepository.save(ud);	
		*/
		
		//System.out.println(dbUser.getEmail()+" 패스워드 "+dbUser.getPassword()+" 유저아이디 "+dbUser.getUserid());
		
		
       // userService.register(user, ud);
    }
}
