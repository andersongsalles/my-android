package br.com.anderson.retrofit2sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.anderson.retrofit2sample.api.CarApi;
import br.com.anderson.retrofit2sample.domain.Brand;
import br.com.anderson.retrofit2sample.domain.Car;
import br.com.anderson.retrofit2sample.domain.Engine;
import br.com.anderson.retrofit2sample.domain.WrapRequest;
import br.com.anderson.retrofit2sample.util.BinaryBytes;
import br.com.anderson.retrofit2sample.util.CarDes;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "LOG";
    public static final String API = "http://192.168.138.111:8888/retrofit-example/";
    private TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvData = (TextView) findViewById(R.id.tv_data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Gson gson = new GsonBuilder().registerTypeAdapter(Car.class, new CarDes()).create();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CarApi carAPI = retrofit.create(CarApi.class);

        // GET LUXURY CAR - REQUEST
            Call<Car> callLuxury = carAPI.getLuxuryCar("CtrlLuxuryCar.php");
            callLuxury.enqueue(new Callback<Car>() {
                @Override
                public void onResponse(Call<Car> call, Response<Car> response) {
                    Car c = response.body();

                    if (c != null){
                        tvData.setText(Html.fromHtml( "Model: "+c.getName()+"<br>Brand: "+c.getBrand().getName()+"<br>Engine: "+c.getEngine().getStrength()) );
                    }
                }

                @Override
                public void onFailure(Call<Car> call, Throwable t) {
                    Log.i(TAG, "Error GET LUXURY CAR: "+t.getMessage());
                }
            });

        // ONE CAR - REQUEST
            Call<Car> call = carAPI.getOneCar("one-car");
            call.enqueue(new Callback<Car>() {
                @Override
                public void onResponse(Call<Car> call, Response<Car> response) {
                    Car c = response.body();
                    if( c != null ){
                        Log.i(TAG, "Car: " + c.getName());
                        //tvData.setText(Html.fromHtml( "Model: "+c.getName()+"<br>Brand: "+c.getBrand().getName()+"<br>Engine: "+c.getEngine().getStrength() ) );
                    }
                }

                @Override
                public void onFailure(Call<Car> call, Throwable t) {
                    Log.i(TAG, "Error ONE CAR: "+t.getMessage());
                }
            });

        // MANY CAR - REQUEST
        final Call<List<Car>> callManyCars = carAPI.getManyCars("many-cars");

        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    List<Car> listCars = callManyCars.execute().body();

                    if( listCars != null ){
                        for( Car c : listCars ){
                            Log.i(TAG, "Car: "+c.getName());
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "MANY CAR request Ok");
                    }
                });
            }
        }.start();

        // SAVE CAR - REQUEST
        Engine e = new Engine(325, 2015, "5.9");
        Brand b = new Brand("Ferrari", null);
        Car c = new Car("Spider", null, b, e);

        WrapRequest wrapRequest = new WrapRequest( "save-car", c );

        call = carAPI.saveCar(wrapRequest);
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {

            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Log.i(TAG, "Error SAVE CAR: " + t.getMessage());
            }
        });

        // SEND IMG - REQUEST
        String imageName = BinaryBytes.getResourceName(this, R.drawable.jeep);
        byte[] imageBinary = BinaryBytes.getResourceInBytes(this, R.drawable.jeep);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), imageBinary);

        call = carAPI.sendImg("send-img", imageName, requestBody);
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {

            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Log.i(TAG, "Error SEND IMG: " + t.getMessage());
            }
        });

        // SEND HEADER - REQUEST
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("username", "thiengo")
                        .addHeader("password", "calopsita")
                        .build();

                return chain.proceed( request );
            }
        };


        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(interceptor);


        retrofit = new Retrofit
                .Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        carAPI = retrofit.create(CarApi.class);

        call = carAPI.sendHeader("mega-test-header", "send-header");
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {

            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Log.i(TAG, "Error SEND HEADER: " + t.getMessage());
            }
        });


        // SAVE CAR - REQUEST
        ArrayList<Car> listCars = new ArrayList<>();

        e = new Engine(325, 2015, "5.9");
        b = new Brand("Ferrari", null);
        c = new Car("Spider", null, b, e);
        listCars.add(c);

        e = new Engine(325, 2015, "5.9");
        b = new Brand("Ferrari", null);
        c = new Car("F-150", null, b, e);
        listCars.add(c);

        call = carAPI.saveCars("save-cars", new Gson().toJson( listCars ));
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {

            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Log.i(TAG, "Error SAVE CARS: " + t.getMessage());
            }

        });

    }
}
