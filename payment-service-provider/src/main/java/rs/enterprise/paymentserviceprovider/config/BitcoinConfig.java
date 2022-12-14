package rs.enterprise.paymentserviceprovider.config;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.utils.BriefLogFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class BitcoinConfig {

    @Value("${bitcoin.network}")
    private String network;

    @Value("${bitcoin.file-prefix}")
    private String filePrefix;

    @Value("${bitcoin.file-location}")
    private String btcFileLocation;

    private NetworkParameters networkParameters;

    public BitcoinConfig() {
        BriefLogFormatter.init();
    }

    @Bean
    public NetworkParameters networkParameters() {
        return TestNet3Params.get();
    }

    @Bean
    public WalletAppKit walletAppKit(@Autowired NetworkParameters networkParameters) {
        return new WalletAppKit(networkParameters, new File(btcFileLocation), filePrefix) {
            @Override
            protected void onSetupCompleted() {
                if (wallet().getKeyChainGroupSize() < 1) {
                    wallet().importKey(new ECKey());
                }
            }
        };
    }
}
