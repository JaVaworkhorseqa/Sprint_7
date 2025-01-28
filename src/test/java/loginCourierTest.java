import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class loginCourierTest {

    private String login = "aweffsd";
    private String password = "1234";
    private String firstName = "simba";
    private CourierApi createApi;
    private Integer courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        createApi = new CourierApi();
        Body json = new Body(login, password, firstName);
        createCourier(json);
    }

    @Test
    public void loginCurierTest() {
        Body json = new Body(login, password);
        Response response = loginCourier(json);
        verifyLoginResponse(response);
    }

    @Test
    public void loginCurierWithEmptyTest() {
        Body json = new Body(login, "");
        Response response = loginCourier(json);
        verifyLoginWithEmptyPassword(response);
    }

    @Test
    public void loginWithNonexistentUserReturnsError() {
        Body json = new Body("asdfdds", "23232651");
        Response response = loginCourier(json);
        verifyNonexistentUserLogin(response);
    }

    @After
    public void cleanData() {
        AnswerId idResponse = loginCourierAndGetId(new Body(login, password));
        courierId = idResponse.getId();
        if (courierId != null) {
            deleteCourier(courierId);
        }
    }

    @Step("Создание курьера")
    private void createCourier(Body json) {
        createApi.createCourier(json);
    }

    @Step("Логин курьера")
    private Response loginCourier(Body json) {
        return createApi.loginCourierForResponse(json);
    }

    @Step("Проверка успешного логина")
    private void verifyLoginResponse(Response response) {
        response.then().assertThat()
                .body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Проверка логина с пустым паролем")
    private void verifyLoginWithEmptyPassword(Response response) {
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Step("Проверка логина несуществующего пользователя")
    private void verifyNonexistentUserLogin(Response response) {
        response.then().assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Step("Логин и получение ID курьера")
    private AnswerId loginCourierAndGetId(Body json) {
        return createApi.loginCourier(json);
    }

    @Step("Удаление курьера с ID {courierId}")
    private void deleteCourier(Integer courierId) {
        createApi.deleteCourier(courierId);
    }
}
