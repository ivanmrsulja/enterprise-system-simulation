package rs.enterprise.paymentserviceprovider.model;

import org.springframework.stereotype.Component;

@Component
public class BitcoinWallet {
//    private final String filePrefix = "my-wallet-service-testnet";
//    private final String btcFileLocation = "src/main/resources";
//    private final WalletAppKit walletAppKit;
//
//    private final NetworkParameters networkParameters;
//
//    public BitcoinWallet() {
//        BriefLogFormatter.init();
//        this.networkParameters = TestNet3Params.get();
//
//        this.walletAppKit = new WalletAppKit(networkParameters, new File(btcFileLocation), filePrefix) {
//            @Override
//            protected void onSetupCompleted() {
//                if (wallet().getKeyChainGroupSize() < 1) {
//                    wallet().importKey(new ECKey());
//                }
//            }
//        };
//    }
//
//    @PostConstruct
//    public void start() {
//        walletAppKit.startAsync();
//        walletAppKit.awaitRunning();
//
//        walletAppKit.wallet().addCoinsReceivedEventListener(
//                (wallet, tx, prevBalance, newBalance) -> {
//                    Coin value = tx.getValueSentToMe(wallet);
//                    System.out.println("Received coins " + value.toFriendlyString());
//                    Futures.addCallback(tx.getConfidence().getDepthFuture(1),
//                            new FutureCallback<TransactionConfidence>() {
//                                @Override
//                                public void onSuccess(TransactionConfidence result) {
//                                    System.out.println("Received coins " +
//                                            value.toFriendlyString() + " is confirmed. ");
//                                }
//
//                                @Override
//                                public void onFailure(Throwable t) {}
//                            }, MoreExecutors.directExecutor());
//                });
//
//        Address sendToAddress = LegacyAddress.fromKey(networkParameters,
//                walletAppKit.wallet().currentReceiveKey());
//        System.out.println("Send coins to: " + sendToAddress);
//    }
//
//    public void send(String value, String to) {
//        try {
//            Address toAddress = LegacyAddress.fromBase58(networkParameters, to);
//            SendRequest sendRequest = SendRequest.to(toAddress, Coin.parseCoin(value));
//            sendRequest.feePerKb = Coin.parseCoin("0.00005");
//            Wallet.SendResult sendResult = walletAppKit.wallet().sendCoins(walletAppKit.peerGroup(), sendRequest);
//            sendResult.broadcastComplete.addListener(() ->
//                            System.out.println("Transaction hash is " + sendResult.tx.getTxId()),
//                    MoreExecutors.directExecutor());
//        } catch (InsufficientMoneyException e) {
//            throw  new RuntimeException(e);
//        }
//    }

    public void send(String value, String to) {

    }
}
