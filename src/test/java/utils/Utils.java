package utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

public class Utils {
    public static void doScroll(WebDriver driver, int heightPixel) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("window.scrollBy(0," + heightPixel + ")");
    }

    public static JSONObject loadJSONFile(String fileLocation) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(new FileReader(fileLocation));
//        JSONObject jsonObject = (JSONObject) obj;
//        return jsonObject;
        return (JSONObject) obj;
    }
    public static JSONObject loadJSONFileContainingArray(String fileLocation, int index) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(new FileReader(fileLocation));
        JSONArray jsonArray = (JSONArray) obj;
//        JSONObject jsonObject = (JSONObject) jsonArray.get(index);
//        return jsonObject;
        return (JSONObject) jsonArray.get(index);
    }

    public static void main(String[] args) throws IOException, ParseException {
        JSONObject userObject = Utils.loadJSONFile("./src/test/resources/Admin.json");

        String username = (String) userObject.get("username");
        String password = (String) userObject.get("password");

        System.out.println(username);
        System.out.println(password);
    }

    public static void waitForElement(WebDriver driver, WebElement element, int TIME_UNIT_SECONDS) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIME_UNIT_SECONDS));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static int generateRandomNumber(int min, int max) {

        return (int) Math.round(Math.random() * (max - min) + min);
    }

    public static void addJsonArray(String firstName, String lastName, String employeeId, String username, String password) throws IOException, ParseException {
        String fileName = "./src/test/resources/Employee.json";
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(new FileReader(fileName));
        JSONObject userObj = new JSONObject();
        JSONArray jsonArray = (JSONArray) obj;

        userObj.put("firstname", firstName);
        userObj.put("lastname", lastName);
        userObj.put("username", username);
        userObj.put("password", password);
        userObj.put("employeeId", employeeId);
        jsonArray.add(userObj);

        FileWriter file = new FileWriter(fileName);
        file.write(jsonArray.toJSONString());
        file.flush();
        file.close();

    }
    public static void updateJSONObject(String fileName, String key, String value, int index) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Object object = jsonParser.parse(new FileReader(fileName));
        JSONArray jsonArray = (JSONArray) object;
        JSONObject jsonObject = (JSONObject) jsonArray.get(index);
        jsonObject.put(key, value);

        FileWriter file = new FileWriter(fileName);
        file.write(jsonArray.toJSONString());
        file.flush();
        file.close();
    }
}
