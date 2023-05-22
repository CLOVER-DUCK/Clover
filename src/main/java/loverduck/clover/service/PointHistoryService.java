package loverduck.clover.service;

import java.util.List;

import loverduck.clover.entity.PointHistory;

public interface PointHistoryService {
	
	/**
	 * 포인트 충전 내역 저장
	 */
	void pointChargeInsert(PointHistory pointHistory);
	

	/**
	 * wallet_id별 포인트 내역 상세 조회
	 */
	List<PointHistory> pointHistoryList(Long id);


	/**
	 * 포인트 충전/사용 내역에 따른 wallet amount 값 변경
	 */
	
	Integer updateWalletAmount(Long id);
	
	Integer minusWalletAmount(Long id);

}
