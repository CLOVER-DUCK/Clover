package loverduck.clover.service;

import loverduck.clover.entity.Wallet;

public interface WalletService {
    //Long getWalletAmount();
    
    public Wallet findById(Long walletId);
}
