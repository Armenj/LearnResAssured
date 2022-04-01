package api;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTest {

    /**
     * Тест без использования Pojo-класса
     */
    @Test
    public void checkAvatarsAndIdNoPojoTest(){
        Specifications.InstallSpecification(Specifications.requestSpecificationGet(), Specifications.responseSpecification());
        Response response = given().log().uri()
                .get("")
                .then().log().all()
                .body("page", equalTo(2))
                .body("data.id", notNullValue())
                .body("data.email", notNullValue())         // проверяем, что данные поля не приходят пустыми
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatars", notNullValue())
                .extract().response();
        JsonPath jsonPath = response.jsonPath(); // превращаем ответ с сервера в json для дальнейшего взаимодействия с ним
        List<String> emails = jsonPath.get("data.email");
        List<Integer> ids = jsonPath.get("data.id");            // сформировали списки с нужными значениями
        List<String> avatars = jsonPath.get("data.avatar");

        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i).toString())); // с помощью цикла перебрали аватары и
            // айди и сравнили на содержание одного другим
        }

        Assert.assertTrue(emails.stream().allMatch(x->x.endsWith("reqres.in"))); // перебираем через стримапи список
        // email-ов и проверяем, что каждый из них заканчивается на требуемое значение
    }


    /**
     * Тест с использованием Pojo-класса
     */
    @Test
    public void checkAvatarsAndIdPojoTest(){
        Specifications.InstallSpecification(Specifications.requestSpecificationGet(), Specifications.responseSpecification());
        List<UserDataPojo> users = given()
                .get("")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserDataPojo.class);
        users.forEach(x-> Assert.assertTrue(x.getAvatar().contains(x.getId().toString()))); // спомощью цикла проверяем
        // что Аватары содержат в себе айди
        Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("reqres.in"))); // с помощью стрима проверяем
        // что почта заканчивается на нужное значение.

        /**
         * Альтернативная проверка содержания в Аватарах айдишников
         */
        List<String> avatars = users.stream().map(UserDataPojo::getAvatar).collect(Collectors.toList()); // извлекаем в список все аватары
        List<String> ids = users.stream().map(x->x.getId().toString()).collect(Collectors.toList()); // извлекаем в список все айди, а
        // так айди это int, поэтому достаем их через лямбду
        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i))); // перебираем 2 списка и производим проверку
        }
    }

}
