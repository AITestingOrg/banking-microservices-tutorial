package ultimatesoftware.banking.transactions.discovery;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by justinp on 7/21/17.
 */
@Service
public class EurekaDiscoveryClient implements DiscoveryClient {
    @Autowired
    private EurekaClient eurekaClient;

    @Override
    public void setUp() {

    }

    @Override
    public void setDown() {

    }

    @Override
    public String getHostForApp(String appName) {
        Application application = eurekaClient.getApplication(appName);
        InstanceInfo instanceInfo = application.getInstances().get(0);
        return instanceInfo.toString();
    }
}
