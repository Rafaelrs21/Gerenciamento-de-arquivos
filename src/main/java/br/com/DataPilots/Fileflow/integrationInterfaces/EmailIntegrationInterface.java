package br.com.DataPilots.Fileflow.integrationInterfaces;

import br.com.DataPilots.Fileflow.exceptions.EmailIntegrationException;

public interface EmailIntegrationInterface {

    public void sendRecovery(String email, String token) throws EmailIntegrationException;
}
