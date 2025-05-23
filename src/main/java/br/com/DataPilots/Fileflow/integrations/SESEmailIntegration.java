package br.com.DataPilots.Fileflow.integrations;

import br.com.DataPilots.Fileflow.AppConsts;
import br.com.DataPilots.Fileflow.exceptions.EmailIntegrationException;
import br.com.DataPilots.Fileflow.integrationInterfaces.EmailIntegrationInterface;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.Objects;


@Component
public class SESEmailIntegration implements EmailIntegrationInterface {
    private final String enviroment;
    private final Region awsRegion;
    private final String senderEmail;
    private final String access_key;
    private final String secret_key;

    public SESEmailIntegration(Environment env) {
        enviroment = env.getProperty("environment");
        String configGroup = "integrations.ses.";
        awsRegion = Region.of(Objects.requireNonNull(env.getProperty(configGroup + "aws_region")));
        senderEmail = env.getProperty(configGroup + "sender_email");
        access_key = env.getProperty(configGroup + "access_key");
        secret_key = env.getProperty(configGroup + "secret_key");
    }

    @Override
    public void sendRecovery(String email, String recoveryToken) throws EmailIntegrationException {
        String link = "http://localhost:8081/password_recovery?token=" + recoveryToken;

        sendEmail(
            email,
            "Recuração de senha",
            "Link para recuração de senha: " + link
        );
    }

    private void sendEmail(String to, String subject, String body) throws EmailIntegrationException {
        if (!isProduction()) return;

        try {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(access_key, secret_key);

            SesClient sesClient = SesClient.builder()
                .region(awsRegion)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();

            sesClient.sendEmail(buildMessage(to, subject, body));
        } catch (SesException e) {
            throw new EmailIntegrationException();
        }
    }

    private boolean isProduction() {
        return Objects.equals(enviroment, AppConsts.ENVIRONMENT_PRODUCTION);
    }

    private SendEmailRequest buildMessage(String to, String subject, String body) {
        return SendEmailRequest.builder()
            .destination(Destination.builder().toAddresses(to).build())
            .message(Message.builder()
                .subject(Content.builder().data(subject).build())
                .body(Body.builder().text(Content.builder().data(body).build()).build())
                .build())
            .source(senderEmail)
            .build();
    }
}
