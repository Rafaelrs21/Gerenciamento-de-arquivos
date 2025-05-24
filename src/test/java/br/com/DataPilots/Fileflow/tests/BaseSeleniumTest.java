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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
public abstract class BaseSeleniumTest {
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static final String FRONTEND_URL = "http://localhost:5173"; // Frontend Vite
    protected static final String BACKEND_URL = "http://localhost:8080"; // Backend Spring Boot
    protected static boolean serverAvailable = false;

    @LocalServerPort
    private int port;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    public static boolean isServerNotAvailable() {
        System.out.println("Verificando disponibilidade do servidor frontend...");
        
        // Primeiro tenta uma conexão TCP simples
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", 5173), 5000);
            System.out.println("Conexão TCP bem sucedida na porta 5173");
        } catch (Exception e) {
            System.out.println("Não foi possível estabelecer conexão TCP na porta 5173");
            System.out.println("Erro: " + e.getMessage());
            return true;
        }

        // Se a conexão TCP funcionou, tenta a conexão HTTP
        try {
            URL url = new URL(FRONTEND_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            
            int responseCode = connection.getResponseCode();
            serverAvailable = (responseCode >= 200 && responseCode < 300);
            
            if (!serverAvailable) {
                System.out.println("Servidor frontend não está disponível. Pulando testes...");
                System.out.println("URL testada: " + FRONTEND_URL);
                System.out.println("Código de resposta: " + responseCode);
                return true;
            }
            
            System.out.println("Servidor frontend está disponível!");
            System.out.println("URL: " + FRONTEND_URL);
            System.out.println("Código de resposta: " + responseCode);

            // Verifica se o backend está disponível
            System.out.println("Verificando disponibilidade do servidor backend...");
            url = new URL(BACKEND_URL + "/actuator/health");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            
            responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Servidor backend não está disponível!");
                System.out.println("URL testada: " + BACKEND_URL);
                System.out.println("Código de resposta: " + responseCode);
                return true;
            }
            
            System.out.println("Servidor backend está disponível!");
            return false;
            
        } catch (Exception e) {
            System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
            System.out.println("URL testada: " + FRONTEND_URL);
            e.printStackTrace();
            return true;
        }
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Configurando WebDriver...");
        
        // Configura o ChromeDriver primeiro
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");
        
        System.out.println("Iniciando ChromeDriver...");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("WebDriver configurado com sucesso!");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            System.out.println("Fechando WebDriver...");
            driver.quit();
        }
    }
} 