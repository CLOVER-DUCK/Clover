package loverduck.clover.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import loverduck.clover.entity.Funding;
import loverduck.clover.entity.FundingReply;
import loverduck.clover.repository.FundingReplyRepository;
import loverduck.clover.repository.FundingRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class FundingServiceImpl implements FundingService{

	private final FundingRepository fundingRepository;
	private final FundingReplyRepository fundingReplyRepository;

	@Override
	public List<Funding> historyCorp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fundSubmit(Funding funding) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Funding> fundingList() {
		List<Funding> funds = fundingRepository.findAll();
		System.out.println("size ! -> " + funds.size());
		return funds;
	}

	@Override
	public Funding fundingDetail(Long id) {
		Funding fund = fundingRepository.findById(id).orElse(null);
		return fund;
	}

	@Override
	public Funding findById(Long id) {		
		Funding fund = fundingRepository.findById(id).orElse(null);
		return fund;
	}
	
	@Override
	public List<Funding> findByCompanyName(String name) {
		List<Funding> funds = fundingRepository.findByCompanyName(name);
		return funds;
	}

	@Override
	public void fundingComment(FundingReply fundingReply) {
		 fundingReplyRepository.save(fundingReply);

	}
	
	@Override
	public List<FundingReply> commentList(Long id) {
	    List<FundingReply> commentList = fundingReplyRepository.findByFundingId(id);
	    return commentList;
	}
	
	@Override
	public List<Funding> searchFundingByTitle(String keyword) {
        return fundingRepository.findByTitleContaining(keyword);
    }
	
	@Override
	public List<Funding> searchFundingByCompany(String keyword){
		return fundingRepository.findByCompanyName(keyword);
	}
}
