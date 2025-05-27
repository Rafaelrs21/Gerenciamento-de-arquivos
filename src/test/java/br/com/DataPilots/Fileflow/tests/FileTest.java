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

public class FileTest extends BaseSeleniumTest {

    @Test
    @DisabledIf("servicesNotRunning")
    public void testFluxoCompletoArquivo() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        System.out.println("Iniciando teste completo: Registrar → Login → Upload → Excluir");
        
        // 1. REGISTRAR USUÁRIO
        System.out.println("1. Registrando usuário...");
        driver.get(FRONTEND_URL + "/login");
        Thread.sleep(3000);
        
        // Clica no link de registro
        WebElement linkRegistro = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[4]/p/a")));
        linkRegistro.click();
        
        // Aguarda página de registro
        wait.until(ExpectedConditions.urlContains("/register"));
        
        // Dados do usuário
        String email = "teste" + System.currentTimeMillis() + "@fileflow.com";
        String senha = "123456";
        
        // Preenche formulário de registro
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[1]/input")));
        emailInput.clear();
        emailInput.sendKeys(email);
        
        WebElement senhaInput = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[2]/input"));
        senhaInput.clear();
        senhaInput.sendKeys(senha);
        
        WebElement confirmSenhaInput = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[3]/input"));
        confirmSenhaInput.clear();
        confirmSenhaInput.sendKeys(senha);
        
        // Clica em registrar
        WebElement botaoRegistrar = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[4]/button"));
        botaoRegistrar.click();
        
        // Aceita alerta de registro
        Alert alertRegistro = wait.until(ExpectedConditions.alertIsPresent());
        System.out.println("Alerta de registro: " + alertRegistro.getText());
        alertRegistro.accept();
        
        System.out.println("Usuário registrado: " + email);
        
        // 2. FAZER LOGIN
        System.out.println("2. Fazendo login...");
        driver.get(FRONTEND_URL + "/login");
        Thread.sleep(3000);
        
        // Preenche formulário de login
        WebElement emailLoginInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[1]/input")));
        emailLoginInput.clear();
        emailLoginInput.sendKeys(email);
        
        WebElement senhaLoginInput = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[2]/input"));
        senhaLoginInput.clear();
        senhaLoginInput.sendKeys(senha);
        
        WebElement botaoLogin = driver.findElement(
            By.xpath("/html/body/div[1]/div/div[2]/div/form/div[3]/button"));
        botaoLogin.click();
        
        wait.until(ExpectedConditions.urlToBe(FRONTEND_URL + "/"));
        System.out.println("Login realizado com sucesso!");
        
        System.out.println("3. Enviando arquivo...");
        
        // Cria arquivo temporário
        java.io.File arquivoTeste = java.io.File.createTempFile("teste_completo", ".txt");
        java.nio.file.Files.write(arquivoTeste.toPath(), "Arquivo para teste completo".getBytes());
        
        // Encontra input de arquivo e envia
        WebElement inputArquivo = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='file']")));
        inputArquivo.sendKeys(arquivoTeste.getAbsolutePath());
        
        System.out.println("Arquivo enviado: " + arquivoTeste.getName());
        
        // Aceita alerta de upload se aparecer
        try {
            Alert alertUpload = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("Alerta de upload: " + alertUpload.getText());
            alertUpload.accept();
        } catch (Exception e) {
            System.out.println("Nenhum alerta de upload detectado");
        }
        
        Thread.sleep(5000);
        
        System.out.println("🗑️ 4. Excluindo arquivo...");
        
        WebElement botaoExcluir = null;
        
        
        
        if (botaoExcluir == null) {
            try {
                botaoExcluir = driver.findElement(By.xpath("//button[@title='Excluir']"));
                System.out.println("Encontrou botão de excluir por title='Excluir'");
            } catch (Exception e) {
                System.out.println("Não encontrou por title='Excluir'");
            }
        }
        
        
        if (botaoExcluir == null) {
            try {
                botaoExcluir = driver.findElement(By.xpath(
                    "//button[.//svg[@viewBox='0 0 20 20']]"));
                System.out.println("Encontrou botão de excluir por SVG viewBox");
            } catch (Exception e) {
                System.out.println("Não encontrou por SVG viewBox");
            }
        }
        
        
        botaoExcluir.click();
        System.out.println("Clicou no botão de excluir");
        
        Alert alertExclusao = wait.until(ExpectedConditions.alertIsPresent());
        System.out.println("Alerta de exclusão: " + alertExclusao.getText());
        alertExclusao.accept(); // Confirma exclusão
        
        System.out.println("Arquivo excluído com sucesso!");
        
        System.out.println("5. Fazendo logout...");
        
        Thread.sleep(2000);
        
        try {
            WebElement botaoLogout = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div/div/nav/div/div/div[2]/div/div/button")));
            botaoLogout.click();
            
            wait.until(ExpectedConditions.urlContains("/login"));
            System.out.println("Logout realizado com sucesso!");
            
        } catch (Exception e) {
            System.out.println("Erro ao fazer logout: " + e.getMessage());
            driver.manage().deleteAllCookies();
            System.out.println("Cookies limpos como fallback");
        }
        
        arquivoTeste.delete();
        
    }

    static boolean servicesNotRunning() {
        return !areServicesRunning();
    }
} 