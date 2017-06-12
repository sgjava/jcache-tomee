
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on July 3, 2016
 * sgoldsmith@codeferm.com
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "maxSessions", propertyValue
            = "100"),
    @ActivationConfigProperty(propertyName = "maxMessagesPerSessions",
            propertyValue = "100")})
@SuppressWarnings("checkstyle:designforextension") // CDI beans not allowed to have final methods
public class LogBean implements MessageListener {

    /**
     * Logger.
     */
    private static final Logger log = Logger.getLogger(LogBean.class.getName());

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        log.fine("PostConstruct");
    }

    /**
     * Destroy cache.
     */
    //CHECKSTYLE:OFF DesignForExtension - CDI beans cannot have final methods
    @PreDestroy
    //CHECKSTYLE:ON DesignForExtension
    public void destroy() {
        log.fine("PreDestroy");
    }

    /**
     * A MessageListener is used to receive asynchronously delivered messages.
     * Each session must insure that it passes messages serially to the
     * listener. This means that a listener assigned to one or more consumers of
     * the same session can assume that the onMessage method is not called with
     * the next message until the session has completed the last call.
     *
     * @param message Message.
     */
    @Override
    @SuppressWarnings({"PMD.AvoidCatchingGenericException"}) // Generic exception caught because of dependency
    public void onMessage(final Message message) {
        try {
            // Verify message is of type TextMessage
            if (message instanceof TextMessage) {
                final TextMessage textMsg = (TextMessage) message;
                final String text = textMsg.getText();
                log.info(text);
            } else {
                log.severe("Message is not type TextMessage");
            }
        } catch (JMSException e) {
            log.severe(e.getMessage());
        }
    }

}
