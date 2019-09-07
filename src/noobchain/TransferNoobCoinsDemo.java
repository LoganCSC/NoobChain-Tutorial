package noobchain;

import noobchain.model.Wallet;

/**
 * Illustrates making transaction involving NoobCoins between block chain based Wallets.
 * Transfers coins from one wallet to another and then back again.
 */
class TransferNoobCoinsDemo {

    private CoinBase coinBase = new CoinBase();
    private Wallet walletA = coinBase.createWallet();
    private Wallet walletB = coinBase.createWallet();


    void run() {

        coinBase.createInitialTransaction(100f, walletA);

        // testing
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        coinBase.transfer(walletA, walletB, 40f);
        showBalances();

        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        coinBase.transfer(walletA, walletB, 1000f);
        showBalances();

        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        coinBase.transfer(walletB, walletA, 20);
        showBalances();
    }


    private void showBalances() {
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());
    }
}
