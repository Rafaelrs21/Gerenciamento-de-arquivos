package br.com.DataPilots.Fileflow.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Alert;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest extends BaseSeleniumTest {

    @Test
    @DisabledIf("servicesNotRunning")
    public void testRegistroELoginCompleto() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // 1. Navega para a página de login
        driver.get(FRONTEND_URL + "/login");
        
        // 2. Clica no link para ir para a página de registro
        WebElement linkRegistro = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[4]/p/a")));
        linkRegistro.click();
        
        // 3. Aguarda estar na página de registro
        wait.until(ExpectedConditions.urlContains("/register"));
        
        // 4. Preenche o formulário de registro
        String email = "teste" + System.currentTimeMillis() + "@teste.com";
        String senha = "123456";
        
        // Email
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[1]/input")));
        emailInput.sendKeys(email);
        
        // Senha
        WebElement senhaInput = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[2]/input"));
        senhaInput.sendKeys(senha);
        
        // Confirmação de senha
        WebElement confirmSenhaInput = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[3]/input"));
        confirmSenhaInput.sendKeys(senha);
        
        // 5. Clica no botão de registrar
        WebElement botaoRegistrar = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[4]/button"));
        botaoRegistrar.click();
        
        // 6. Aguarda e aceita o alerta JavaScript
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        System.out.println("Alerta recebido: " + alertText);
        alert.accept();
        
        // 7. Navega manualmente para a página de login (não aguarda redirecionamento)
        driver.get(FRONTEND_URL + "/login");
        
        // 8. Faz login com as credenciais registradas
        WebElement emailLoginInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[1]/input")));
        emailLoginInput.sendKeys(email);
        
        WebElement senhaLoginInput = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[2]/input"));
        senhaLoginInput.sendKeys(senha);
        
        WebElement botaoLogin = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[3]/button"));
        botaoLogin.click();
        
        // 9. Verifica se o login foi bem-sucedido (redirecionamento para página inicial)
        wait.until(ExpectedConditions.urlToBe(FRONTEND_URL + "/"));
        assertEquals(FRONTEND_URL + "/", driver.getCurrentUrl());
        
        // 10. Faz logout clicando no botão de sair
        WebElement botaoLogout = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div/div/nav/div/div/div[2]/div/div/button")));
        botaoLogout.click();
        
        // 11. Verifica se retornou para a página de login após logout
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
        
        System.out.println("✅ Teste completo: Registro → Login → Logout realizado com sucesso!");
    }

    @Test
    @DisabledIf("servicesNotRunning")
    public void testLoginComCredenciaisInvalidas_TesteIndependente() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        System.out.println("🔄 Iniciando teste independente de login com credenciais inválidas...");
        
        // Navega para a página de login
        driver.get(FRONTEND_URL + "/login");
        
        // Preenche com credenciais inválidas
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[1]/input")));
        emailInput.sendKeys("invalido@teste.com");
        
        WebElement senhaInput = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[2]/input"));
        senhaInput.sendKeys("senhaerrada");
        
        WebElement botaoLogin = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[3]/button"));
        botaoLogin.click();
        
        // Verifica se permanece na página de login (login falhou)
        // Ou verifica se aparece mensagem de erro
        try {
            // Tenta aguardar um alerta de erro
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            System.out.println("Alerta de erro: " + alertText);
            alert.accept();
            assertTrue(alertText.toLowerCase().contains("erro") || 
                      alertText.toLowerCase().contains("inválid") ||
                      alertText.toLowerCase().contains("falhou"));
        } catch (Exception e) {
            // Se não houver alerta, verifica se ainda está na página de login
            assertTrue(driver.getCurrentUrl().contains("/login"));
        }
        
        System.out.println("✅ Teste de login inválido concluído!");
    }

    @Test
    @DisabledIf("servicesNotRunning")
    public void testRegistroComSenhasDiferentes() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        System.out.println("🔄 Iniciando teste de registro com senhas diferentes...");
        
        // Navega para a página de login
        driver.get(FRONTEND_URL + "/login");
        
        // Clica no link para ir para a página de registro
        WebElement linkRegistro = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[4]/p/a")));
        linkRegistro.click();
        
        // Aguarda estar na página de registro
        wait.until(ExpectedConditions.urlContains("/register"));
        
        // Preenche o formulário com senhas diferentes
        String email = "senhasDiferentes" + System.currentTimeMillis() + "@teste.com";
        String senha = "123456";
        String senhaConfirmacao = "654321"; // Senha diferente propositalmente
        
        // Email
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[1]/input")));
        emailInput.sendKeys(email);
        
        // Senha
        WebElement senhaInput = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[2]/input"));
        senhaInput.sendKeys(senha);
        
        // Confirmação de senha (diferente)
        WebElement confirmSenhaInput = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[3]/input"));
        confirmSenhaInput.sendKeys(senhaConfirmacao);
        
        // Clica no botão de registrar
        WebElement botaoRegistrar = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[4]/button"));
        botaoRegistrar.click();
        
        // Verifica se aparece erro de senhas diferentes
        try {
            // Tenta aguardar um alerta de erro
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            System.out.println("Alerta de erro de senhas: " + alertText);
            alert.accept();
            assertTrue(alertText.toLowerCase().contains("senha") || 
                      alertText.toLowerCase().contains("diferent") ||
                      alertText.toLowerCase().contains("não confere") ||
                      alertText.toLowerCase().contains("erro"));
        } catch (Exception e) {
            // Se não houver alerta, verifica se ainda está na página de registro
            assertTrue(driver.getCurrentUrl().contains("/register"));
            System.out.println("Permaneceu na página de registro (sem alerta)");
        }
        
        System.out.println("✅ Teste de registro com senhas diferentes concluído!");
    }

    @Test
    @DisabledIf("servicesNotRunning")
    public void testLoginIncorreto() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        System.out.println("🔄 Iniciando teste de login com credenciais incorretas...");
        
        // Navega para a página de login
        driver.get(FRONTEND_URL + "/login");
        
        // Tenta fazer login com email que não existe
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[1]/input")));
        emailInput.sendKeys("naoexiste" + System.currentTimeMillis() + "@teste.com");
        
        WebElement senhaInput = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[2]/input"));
        senhaInput.sendKeys("qualquersenha");
        
        WebElement botaoLogin = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[3]/button"));
        botaoLogin.click();
        
        // Verifica se aparece erro de login
        try {
            // Tenta aguardar um alerta de erro
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            System.out.println("Alerta de erro de login: " + alertText);
            alert.accept();
            assertTrue(alertText.toLowerCase().contains("erro") || 
                      alertText.toLowerCase().contains("inválid") ||
                      alertText.toLowerCase().contains("falhou") ||
                      alertText.toLowerCase().contains("incorret"));
        } catch (Exception e) {
            // Se não houver alerta, verifica se ainda está na página de login
            assertTrue(driver.getCurrentUrl().contains("/login"));
            System.out.println("Permaneceu na página de login (sem alerta)");
        }
        
        System.out.println("✅ Teste de login incorreto concluído!");
    }

    // Método auxiliar para verificar se os serviços NÃO estão rodando
    static boolean servicesNotRunning() {
        return !areServicesRunning();
    }
} 