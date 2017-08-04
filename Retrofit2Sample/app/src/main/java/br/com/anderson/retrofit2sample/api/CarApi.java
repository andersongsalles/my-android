package br.com.anderson.retrofit2sample.api;

import java.util.List;

import br.com.anderson.retrofit2sample.domain.Car;
import br.com.anderson.retrofit2sample.domain.WrapRequest;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by anderson on 03/08/2017.
 */

public interface CarApi {
    @GET("package/ctrl/{ctrlCar}")
    Call<Car> getLuxuryCar(@Path("ctrlCar") String ctrl);

    @FormUrlEncoded
    @POST("package/ctrl/CtrlCar.php")
    Call<Car> getOneCar( @Field("method") String method);


    @FormUrlEncoded
    @POST("package/ctrl/CtrlCar.php")
    Call<List<Car>> getManyCars(@Field("method") String method);

    @POST("package/ctrl/CtrlCar.php")
    Call<Car> saveCar( @Body WrapRequest wrapRequest);

    @Multipart
    @POST("package/ctrl/CtrlCar.php")
    Call<Car> sendImg(@Part("method") String method, @Part("name_image") String imageName, @Part("binary_image") RequestBody requestBody);

    @FormUrlEncoded
    @POST("package/ctrl/CtrlCar.php")
    Call<Car> sendHeader(@Header("mega-test") String megaTest, @Field("method") String method);

    @FormUrlEncoded
    @POST("package/ctrl/CtrlCar.php")
    Call<Car> saveCars( @Field("method") String method, @Field("cars") String carsJson);
}
