import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Step;

import static org.hamcrest.Matchers.*;


public class CreateCurierTest {

    private String login = "aweffsd";
    private String password = "1234";
    private String firstName = "simba";
    private CourierApi createApi;
    private Integer courierId;



    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        createApi  = new CourierApi();
    }

    @Test
    public void checkCreateNewCurier() {
        Body json = new Body(login, password, firstName);
        Response response = createApi.createCourier(json);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);

    }


    @Test
    public void checkCreateDoubleCurier() {
        Body json = new Body(login, password, firstName);

        //первый курьер
        createApi.createCourier(json);
        //второй курьер с теми же кредами
        Response response = createApi.createCourier(json);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);

    }
    @Test
    public void createCurierWithoutKey() {
        Body json = new Body(login);
        Response response = createApi.createCourier(json);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
    @After
    public void cleanData() {
        AnswerId idResponse = createApi.loginCourier(new Body(login, password));
        courierId = idResponse.getId();

        if (courierId != null) {
            createApi.deleteCourier(courierId);
            courierId = null;
        }
    }
    @Step("Создание курьера с логином: {json.login}")
    public Response createCourier(Body json) {
        return createApi.createCourier(json);
    }

    @Step("Логин курьера с логином: {json.login}")
    public AnswerId loginCourier(Body json) {
        return createApi.loginCourier(json);
    }

    @Step("Удаление курьера с ID: {id}")
    public void deleteCourier(Integer id) {
        createApi.deleteCourier(id);
    }
}



