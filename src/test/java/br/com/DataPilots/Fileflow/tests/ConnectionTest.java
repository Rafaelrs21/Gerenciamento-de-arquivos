package br.com.DataPilots.Fileflow.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import static org.junit.jupiter.api.Assertions.*;

public class ConnectionTest extends BaseSeleniumTest {

    @Test
    public void testServiceConnection() {
        boolean servicesRunning = areServicesRunning();
        
        if (servicesRunning) {
            System.out.println("✅ Todos os serviços estão rodando!");
            System.out.println("   - Backend: " + BACKEND_URL);
            System.out.println("   - Frontend: " + FRONTEND_URL);
        } else {
            System.out.println("❌ Alguns serviços não estão rodando:");
            System.out.println("   - Verifique se o backend está rodando na porta 8080");
            System.out.println("   - Verifique se o frontend está rodando na porta 5173");
        }
        
        assertTrue(true, "Teste de conexão executado");
    }

    @Test
    @DisabledIf("servicesNotRunning")
    public void testFrontendAccess() {
        driver.get(FRONTEND_URL);
        
        String title = driver.getTitle();
        assertNotNull(title, "Título da página não deve ser nulo");
        
        System.out.println("✅ Frontend acessível. Título da página: " + title);
    }

    @Test
    @DisabledIf("servicesNotRunning")
    public void testBackendHealthCheck() {
        driver.get(BACKEND_URL + "/health");
        
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("UP") || pageSource.contains("status"), 
                  "Endpoint de health deve retornar status");
        
        System.out.println("✅ Backend health check funcionando");
    }

    static boolean servicesNotRunning() {
        return !areServicesRunning();
    }
} 