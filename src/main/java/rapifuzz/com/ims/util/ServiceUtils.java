package rapifuzz.com.ims.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rapifuzz.com.ims.service.UserDetailService;


/**
 * Utility class to provide static access to Spring-managed services.
 * This is useful in situations where injecting a service directly is not feasible,
 * such as in custom validators that are not managed by the Spring container.
 */
@Component
public class ServiceUtils {

    // Static instance to hold the reference to this utility class.
    // This instance will be used to provide access to the services.
    private static ServiceUtils instance;

    @Autowired
    public UserDetailService userDetailService;

    /**
     * Initializes the static instance.
     * This method is called by Spring after constructing the bean,
     * ensuring that the static instance is set before any static methods are called.
     */
    @PostConstruct
    public void fillInstance() {
        instance = this;
    }

    public static UserDetailService getUserDetailService() {
        return instance.userDetailService;
    }

}
