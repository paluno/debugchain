package due.debugchain.web3j;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static due.debugchain.web3j.Web3jProperties.WEB3J_PREFIX;

/**
 * web3j property container.
 */
@ConfigurationProperties(prefix = WEB3J_PREFIX)
public class Web3jProperties {

    public static final String WEB3J_PREFIX = "web3j";

    private String clientAddress;

    private Boolean adminClient;

    private String networkId;

    private Long httpTimeoutSeconds;

    private Long pollingInterval;

    public Long getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(Long pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Boolean isAdminClient() {
        return adminClient;
    }

    public void setAdminClient(Boolean adminClient) {
        this.adminClient = adminClient;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public Long getHttpTimeoutSeconds() {
        return httpTimeoutSeconds;
    }

    public void setHttpTimeoutSeconds(Long httpTimeoutSeconds) {
        this.httpTimeoutSeconds = httpTimeoutSeconds;
    }

}
