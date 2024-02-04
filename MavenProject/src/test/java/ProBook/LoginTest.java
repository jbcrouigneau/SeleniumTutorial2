package ProBook;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;

@Listeners (ProBook.Listener.class)

public class LoginTest {
	// Déclaration des variables que nous allons utiliser dans ce script.
    String url = "https://probook.progideo.com";
    String expectedTitle1 = "Dashboard - ProBook";
    String actualTitle1 = null;
    String username = "jbcrouigneau";
    String password = "samira;camilyes!";
    String expectedTitle2 = "(3) Dashboard - ProBook";
    String actualTitle2 = null;
    WebDriver driver;

	@Test
	public void test() {
        // On clique sur le lien "Se connecter / s'inscrire".
		driver.findElement(By.className("btn-enter")).click();
        // On récupère le titre de la page actuelle
        actualTitle1 = driver.getTitle();
        // On affiche dans le log un message d'information
        System.out.println("Login page title : expected value is "+expectedTitle1+" actual value is "+actualTitle1);
        // Si le test échoue, on affiche un message d'erreur 
        if (!actualTitle1.contentEquals(expectedTitle1)) {
            System.out.println("Test Failed");
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login_username")));

        // On saisit le username et le password
        driver.findElement(By.id("login_username")).sendKeys(username);
        driver.findElement(By.id("login_password")).sendKeys(password);
        // On clique sur le bouton "Sign in"
        driver.findElement(By.id("login-button")).click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("account-dropdown-link")));
        
        // On vérifie le titre de la page suite à la tentative de connexion
        actualTitle2 = driver.getTitle();
        // On affiche dans le log un message d'information
        System.out.println("Login result : expected value is "+expectedTitle2+" actual value is "+actualTitle2);
        // Si le test échoue, on affiche un message d'erreur 
        if (!actualTitle2.contentEquals(expectedTitle2)) {
            System.out.println("Test Failed");
        }
	}
	
	@BeforeMethod
	public void beforeMethod() {
        // Chemin vers le driver Gecko (pour Firefox uniquement)
        //System.setProperty("webdriver.gecko.driver","/Users/jbcrouigneau/eclipse/drivers/geckodriver");
        System.setProperty("webdriver.gecko.driver","/snap/bin/geckodriver");
        // Invocation du navigateur Firefox, qui sera identifié avec le nom "driver".
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        options.addArguments("--lang=fr-FR");
        driver = new FirefoxDriver(options);
        // Ouvrir la page "http://probook.progideo.com".
        driver.get(url);
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException {
		int status = result.getStatus();
		// Si le test est un échec, on prend une capture d'écra
		if (status == ITestResult.FAILURE) {
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			Date date = new Date();
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			FileHandler.copy(scrFile, new File("./screenshots/" + result.getName() + "_" + dateFormat.format(date) + ".png" ));
		}
        // On ferme Firefox
        driver.close();
	}

}
