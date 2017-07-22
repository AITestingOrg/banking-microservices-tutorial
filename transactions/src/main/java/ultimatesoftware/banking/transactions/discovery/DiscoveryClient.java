package ultimatesoftware.banking.transactions.discovery;

/**
 * Created by justinp on 7/21/17.
 */
public interface DiscoveryClient {
    void setUp();
    void setDown();
    String getHostForApp(String appName);
}
