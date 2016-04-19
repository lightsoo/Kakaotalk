package com.sample.lightsoo.kakaotalk.Manager;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by LG on 2016-02-18.
 */

public class NetworkManager {
    // where my server lives.
//    private static final String serverURL ="http://172.30.2.30:3000/";
    private static final String serverURL ="http://52.192.180.38:3000/";
    //    private static final String serverURL ="http://127.0.0.1:3000/";
    Retrofit client;

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    private NetworkManager(){
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        //Retrofit설정, req를 인터셉트한다
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response;
            }
        });


        client = new Retrofit.Builder()
                .baseUrl(serverURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


   /* // add custom interceptor to manipulate the cookie value in header
    okHttpClient.interceptors().add(new RequestInterceptor());
    okHttpClient.interceptors().add(new ResponseInterceptor());*/

    //싱글톤 패턴, 프로그램 종료시점까지 하나의 인스턴스만을 생성해서 관리한다.
    public static class InstanceHolder{
        public static final NetworkManager INSTANCE = new NetworkManager();
    }
    public static NetworkManager getInstance(){return InstanceHolder.INSTANCE;}
    //나의 restAPI를 호출
    public <T> T getAPI(Class<T> serviceClass){
        return client.create(serviceClass);
    }


    /*// custom req, res interceptors
    public class ResponseInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            if(!response.headers("Set-Cookie").isEmpty()){
                HashSet cookies = new HashSet();
                for (String header : response.headers("Set-Cookie")) {
                    cookies.add(header);
                }
                PropertyManager.getInstance().setCookie(cookies);
            }
            return response;
        }
    }
    public class RequestInterceptor implements  Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            HashSet<String> preferences = PropertyManager.getInstance().getCookie();
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                Log.v("NetworkManager", "Cookie : " + cookie);
            }
            return chain.proceed(builder.build());
        }
    }*/


}
