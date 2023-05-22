package loverduck.clover.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import loverduck.clover.entity.PointHistory;
import loverduck.clover.entity.QPointHistory;
import loverduck.clover.entity.QWallet;
import loverduck.clover.entity.Wallet;
import loverduck.clover.repository.PointHistoryRepository;
import loverduck.clover.repository.WalletRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistoryServiceImpl implements PointHistoryService {

	private final PointHistoryRepository phRepository;
	private final WalletRepository walletRepository;
	private final EntityManager entityManager;
	
	@Override
	@Transactional
	public void pointChargeInsert(PointHistory pointHistory) {
		phRepository.save(pointHistory);
	}

	/**
	 * 포인트 상세내역 조회(오름차순으로 정렬)
	 */
	@Override
	public List<PointHistory> pointHistoryList(Long id) {
		List<PointHistory> pointHistoryListById = phRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
		return pointHistoryListById;
	}

	
	/**
	 * 포인트 충전/사용 내역에 따른 wallet amount 값 변경
	 */
	public Integer updateWalletAmount(Long id) {
		
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);		
	    QPointHistory qpointHistory = QPointHistory.pointHistory;

	    //포인트 type이 1이면 충전이므로 +, 0이면 사용이므로 -
	    NumberExpression<Integer> amountSum = Expressions.numberTemplate(Integer.class,
	            "SUM(CASE WHEN {0} = 0 THEN {1} ELSE -{1} END)", qpointHistory.type, qpointHistory.amount);

	    // 해당 id에 해당하는 포인트 연산 결과 저장
	    Integer result = queryFactory
	            .select(amountSum)
	            .from(qpointHistory)
	            .where(qpointHistory.wallet.id.eq(id))
	            .fetchOne();

	    // null이 아닐 경우, Wallet의 amount 업데이트
	    if (result != null) {
	        Wallet wallet = entityManager.find(Wallet.class, id); // 해당 id에 해당하는 Wallet 조회
	        if (wallet != null) {
	            Long currentAmount = wallet.getAmount();
	            Long newAmount = Long.valueOf(result); 
	            wallet.setAmount(newAmount); // 연산 결과 새로운 amount 값 업데이트
	            entityManager.merge(wallet); // 업데이트된 Wallet 저장
	        }
	    }

	    return result;
	}

	@Override
	public Integer minusWalletAmount(Long id) {
		// TODO Auto-generated method stub
		return null;
	}



}
