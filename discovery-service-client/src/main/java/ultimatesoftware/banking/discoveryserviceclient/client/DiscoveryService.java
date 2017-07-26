package ultimatesoftware.banking.discoveryserviceclient.client;

import java.util.List;

/**
 * Created by justinp on 7/21/17.
 */
public interface DiscoveryService {
    <T> T queryOne();
    List<?> queryMany();
    <T> T command();
}
