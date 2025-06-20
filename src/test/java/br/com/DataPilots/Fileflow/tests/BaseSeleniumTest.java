package br.com.DataPilots.Fileflow.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.condition.DisabledIf;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.net.Socket;
import java.io.IOException;

public abstract class BaseSeleniumTest {
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static String FRONTEND_URL = "http://localhost:5173";
    protected static String BACKEND_URL = "http://localhost:8080";


    private static boolean isPortOpen(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(host, port), 3000);
            return true;
        } catch (IOException e) {
            try (Socket socket = new Socket()) {
                socket.connect(new java.net.InetSocketAddress("::1", port), 3000);
                return true;
            } catch (IOException e2) {
                return false;
            }
        }
    }

    public static boolean areServicesRunning() {
        boolean backendRunning = isPortOpen("localhost", 8080);
        boolean frontendRunning = isPortOpen("localhost", 5173);
        
        System.out.println("Backend (8080) rodando: " + backendRunning);
        System.out.println("Frontend (5173) rodando: " + frontendRunning);
        
        return backendRunning && frontendRunning;
    }

    @BeforeAll
    public static void setUp() {
        if (!areServicesRunning()) {
            System.out.println("Serviços não estão rodando. Testes serão pulados.");
            System.out.println("   Para executar os testes:");
            System.out.println("   1. Inicie o backend na porta 8080");
            System.out.println("   2. Inicie o frontend na porta 5173");
            return;
        }

        System.out.println("Serviços detectados. Configurando WebDriver...");
        
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--headless");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        System.out.println("Frontend URL: " + FRONTEND_URL);
        System.out.println("Backend URL: " + BACKEND_URL);
        System.out.println("WebDriver configurado com sucesso!");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            System.out.println("Fechando WebDriver...");
            driver.quit();
            System.out.println("WebDriver fechado.");
        }
    }
} 