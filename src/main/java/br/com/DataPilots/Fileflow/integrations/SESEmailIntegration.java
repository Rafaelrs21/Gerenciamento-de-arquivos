package br.com.DataPilots.Fileflow.integrations;

import br.com.DataPilots.Fileflow.exceptions.EmailIntegrationException;
import br.com.DataPilots.Fileflow.integrationInterfaces.EmailIntegrationInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;


@Component
public class SESEmailIntegration implements EmailIntegrationInterface {
    private static final Region AWS_REGION = Region.US_EAST_1;
    private static final String SENDER_EMAIL = "breno.nova@al.infnet.edu.br";
    private final String access_key;
    private final String secret_key;

    public SESEmailIntegration(Environment env) {
        access_key = env.getProperty("integrations.ses.access_key");
        secret_key = env.getProperty("integrations.ses.secret_key");
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
        try {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(access_key, secret_key);

            SesClient sesClient = SesClient.builder()
                .region(AWS_REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();

            sesClient.sendEmail(buildMessage(to, subject, body));
        } catch (SesException e) {
            throw new EmailIntegrationException();
        }
    }

    private SendEmailRequest buildMessage(String to, String subject, String body) {
        return SendEmailRequest.builder()
            .destination(Destination.builder().toAddresses(to).build())
            .message(Message.builder()
                .subject(Content.builder().data(subject).build())
                .body(Body.builder().text(Content.builder().data(body).build()).build())
                .build())
            .source(SENDER_EMAIL)
            .build();
    }
}
