package br.com.DataPilots.Fileflow.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest extends BaseSeleniumTest {

    @Test
    @DisabledIf("isServerNotAvailable")
    public void testLoginSucesso() {
        // Navega para a página de login
        driver.get(FRONTEND_URL + "/login");
        
        // Encontra os campos de login
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
        WebElement senhaInput = driver.findElement(By.id("senha"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        
        // Preenche os campos
        emailInput.sendKeys("seu-email@exemplo.com");
        senhaInput.sendKeys("sua-senha");
        
        // Clica no botão de login
        loginButton.click();
        
        // Verifica se foi redirecionado para a página principal
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        
        // Verifica se o nome do usuário está visível
        WebElement userName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        assertTrue(userName.isDisplayed(), "Nome do usuário não está visível após o login");
    }

    @Test
    @DisabledIf("isServerNotAvailable")
    public void testLoginFalha() {
        // Navega para a página de login
        driver.get(FRONTEND_URL + "/login");
        
        // Encontra os campos de login
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
        WebElement senhaInput = driver.findElement(By.id("senha"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        
        // Preenche os campos com credenciais inválidas
        emailInput.sendKeys("email-invalido@exemplo.com");
        senhaInput.sendKeys("senha-invalida");
        
        // Clica no botão de login
        loginButton.click();
        
        // Verifica se a mensagem de erro aparece
        WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error-message")));
        assertTrue(errorMessage.isDisplayed(), "Mensagem de erro não está visível");
        assertTrue(errorMessage.getText().contains("credenciais inválidas"), 
                  "Mensagem de erro não contém o texto esperado");
    }

    @Test
    @DisabledIf("isServerNotAvailable")
    public void testRegistroELogin() {
        // Credenciais de teste
        String email = "teste" + System.currentTimeMillis() + "@exemplo.com";
        String senha = "Senha@123";

        // Navega para a página de login
        driver.get(FRONTEND_URL + "/login");
        System.out.println("Navegando para página de login...");

        // Espera a página carregar completamente
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        System.out.println("Página de login carregada...");

        // Clica no link de registro
        WebElement linkRegistro = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[4]/p/a")));
        linkRegistro.click();
        System.out.println("Clicou no link de registro...");

        // Espera a página de registro carregar
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        System.out.println("Página de registro carregada...");

        // Preenche o formulário de registro
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[1]/input")));
        WebElement senhaInput = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/form/div[2]/input"));
        WebElement confirmaSenhaInput = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/form/div[3]/input"));
        WebElement botaoRegistrar = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/form/div[4]/button"));

        emailInput.sendKeys(email);
        senhaInput.sendKeys(senha);
        confirmaSenhaInput.sendKeys(senha);
        System.out.println("Preencheu formulário de registro...");

        // Clica no botão de registrar
        botaoRegistrar.click();
        System.out.println("Clicou no botão de registrar...");

        // Espera o alerta de sucesso
        WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        alertWait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        System.out.println("Aceitou alerta de sucesso...");

        // Espera redirecionar para a página de login
        wait.until(ExpectedConditions.urlContains("/login"));
        System.out.println("Redirecionado para página de login...");

        // Espera a página de login carregar novamente
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        System.out.println("Página de login recarregada...");

        // Preenche o formulário de login
        emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[1]/input")));
        senhaInput = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/form/div[2]/input"));
        WebElement botaoLogin = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/form/div[3]/button"));

        emailInput.sendKeys(email);
        senhaInput.sendKeys(senha);
        System.out.println("Preencheu formulário de login...");

        // Clica no botão de login
        botaoLogin.click();
        System.out.println("Clicou no botão de login...");

        // Verifica se foi redirecionado para a página principal
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        System.out.println("Login realizado com sucesso!");
    }
} 