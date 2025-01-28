import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {

    // Метод для создания курьера
    public Response createCourier(Body json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
    }
    public Response createOrder(Body json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/orders");
    }

    public Response loginCourierForResponse(Body json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
    }

    public AnswerId loginCourier(Body json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login")
              .as(AnswerId.class);

    }
    public void deleteCourier(int id) {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + id);
    }

}