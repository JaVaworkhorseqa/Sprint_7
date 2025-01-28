import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CreateOrderTest {

    private String lastName = "aweffsd";
    private String address = "1234";
    private String firstName = "simba";
    private String metroStation = "dsadas";
    private String phone = "6568431681";
    private Number rentTime = 5;
    private String deliveryDate = "2020-06-06";
    private String comment = "simba";
    private String[] color;
    private CourierApi createApi;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        initializeApi();
    }

    @Test
    public void checkCreateNewOrder() {
        color = new String[]{"BLACK"};
        Body json = createRequestBody(lastName, address, firstName, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = createOrder(json);
        verifyOrderCreated(response);
    }

    @Test
    public void checkCreateOrderWithoutColor() {
        Body json = createRequestBody(lastName, address, firstName, metroStation, phone, rentTime, deliveryDate, comment);
        Response response = createOrder(json);
        verifyOrderCreated(response);
    }

    @Test
    public void checkCreateOrderWithGreyColor() {
        color = new String[]{"GREY"};
        Body json = createRequestBody(lastName, address, firstName, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = createOrder(json);
        verifyOrderCreated(response);
    }

    @Test
    public void checkCreateOrderWithTwoColor() {
        color = new String[]{"GREY", "BLACK"};
        Body json = createRequestBody(lastName, address, firstName, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = createOrder(json);
        verifyOrderCreated(response);
    }

    @Step("Инициализация API для взаимодействия с сервером")
    private void initializeApi() {
        createApi = new CourierApi();
    }

    @Step("Создание тела запроса на создание заказа")
    private Body createRequestBody(String lastName, String address, String firstName, String metroStation, String phone, Number rentTime, String deliveryDate, String comment, String... color) {
        return new Body(lastName, address, firstName, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

    @Step("Создание нового заказа")
    private Response createOrder(Body json) {
        return createApi.createOrder(json);
    }

    @Step("Проверка, что заказ успешно создан")
    private void verifyOrderCreated(Response response) {
        response.then().assertThat()
                .body("track", notNullValue())
                .and()
                .statusCode(201);
    }
}
