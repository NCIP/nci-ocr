package gov.nih.nci.firebird.proxy;

import gov.nih.nci.cagrid.introduce.security.client.ServiceSecurityClient;

import com.google.inject.Provider;

/**
 * Pool factory that checks the validity of the client connections.
 * @param <C> type of client to be validated.
 */
class ValidatingClientFactory <C> extends PoolingHandler.ProviderObjectFactory {

    private static final long MIN_VALID_SECONDS_LEFT = 1L;

    /**
     * ctor.
     * @param provider grid client provider.
     */
    ValidatingClientFactory(Provider<? extends C> provider) {
        super(provider);
    }

    @Override
    public boolean validateObject(Object obj) {
        ServiceSecurityClient client = (ServiceSecurityClient) obj;
        long timeLeft = client.getProxy().getTimeLeft();
        return timeLeft > MIN_VALID_SECONDS_LEFT && super.validateObject(obj);
    }

}