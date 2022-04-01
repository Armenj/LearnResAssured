package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specifications {
        public static RequestSpecification requestSpecificationGet(){
            return new RequestSpecBuilder()
                    .setBaseUri("https://reqres.in/")
                    .setBasePath("api/users")
                    .addQueryParam("page", "2")
                    .setContentType(ContentType.JSON)
                    .build();
        }

        public static ResponseSpecification responseSpecification(){
            return new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .build();
        }

        public static void InstallSpecification(RequestSpecification request, ResponseSpecification response){
            RestAssured.requestSpecification = request;
            RestAssured.responseSpecification = response;
        }


}
