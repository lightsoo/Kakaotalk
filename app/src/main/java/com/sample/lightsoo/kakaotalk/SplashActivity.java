package com.sample.lightsoo.kakaotalk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.sample.lightsoo.kakaotalk.Data.User;
import com.sample.lightsoo.kakaotalk.Manager.NetworkManager;
import com.sample.lightsoo.kakaotalk.Manager.PropertyManager;
import com.sample.lightsoo.kakaotalk.RestAPI.LoginAPI;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    Handler mHandler = new Handler(Looper.getMainLooper());

    String loginType;
    String userLoginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        doRealStart();

    }

    private void doRealStart(){
        //일전에 로그인 했을때 어떤 타입으로 로그인했는지 받아옴, 아무값도 설정안되어있을 시 로그아웃하거나 로그인한 적 없는 상태
        loginType = PropertyManager.getInstance().getLoginType();
        userLoginId = PropertyManager.getInstance().getUserLoginId();
        //로그인 한적이 없을 경우 혹은 로그아웃했을 경우 → 로그인 액티비티로 이동
        if(TextUtils.isEmpty(loginType)){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "로그인 한적이 없어서 로그인페이지로 이동");
                    goLoginActivity();
                }
            }, 500);
        }else {
            switch (loginType){
                case PropertyManager.LOGIN_TYPE_KAKAO:
                   Log.d(TAG, "case PropertyManager.LOGIN_TYPE_KAKAO:");
                    //로그인 id가 존재할 경우
                    if(!TextUtils.isEmpty(userLoginId)){

                        Log.d(TAG, "id가 있는경우 :!TextUtils.isEmpty(userLoginId))");
                        loginType = PropertyManager.getInstance().getLoginType();
                        User user = new User(userLoginId, loginType);

                        Call call = NetworkManager.getInstance().getAPI(LoginAPI.class).login(user);
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Response response, Retrofit retrofit) {
                                if (response.isSuccess()) {//이전에 가입되었던 사람이라면 OK,
                                    Toast.makeText(SplashActivity.this, "페이스북 연동 로그인으로 입장 합니다.", Toast.LENGTH_SHORT).show();
                                    goMainActivity();
                                } else {
                                    //아니라면 not registered
                                    UserManagement.requestLogout(new LogoutResponseCallback() {
                                        @Override
                                        public void onCompleteLogout() {
                                            //기존에 카카오앱에 로그인 되어있던 id를 로그아웃한다.
                                            goLoginActivity();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(SplashActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                                goLoginActivity();
                            }
                        });
                        /*Session.getCurrentSession().addCallback(new ISessionCallback() {
                            @Override
                            public void onSessionOpened() {
                                Toast.makeText(SplashActivity.this, "accessToken : " + Session.getCurrentSession().getAccessToken(), Toast.LENGTH_SHORT).show();
                                String token = Session.getCurrentSession().getAccessToken();

                                if (token.equals(userLoginId)) {
                                    UserManagement.requestMe(new MeResponseCallback() {
                                        @Override
                                        public void onSessionClosed(ErrorResult errorResult) {

                                        }

                                        @Override
                                        public void onNotSignedUp() {

                                        }

                                        @Override
                                        public void onSuccess(UserProfile result) {
                                            //여기서 통신을 통해서 유저 아이디 값을 넘겨주자!!!
                                            Toast.makeText(SplashActivity.this, "User : " + result.getId(), Toast.LENGTH_SHORT).show();
//                                            Toast.makeText(SplashActivity.this, "id랑 ", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                } else {
                                    //페북 로그인 했는데 일전에 레몬클립에서 페북으로 로그인한 id와 다를 경우
                                    //즉, 이앱으로 페북로그인했다가 다른 페북id로 페북 앱을 로그인 했을 경우
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SplashActivity.this, "카카오 앱에 다른 계정으로 로그인 한적이 있어 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                            goLoginActivity();
                                            UserManagement.requestLogout(new LogoutResponseCallback() {
                                                @Override
                                                public void onCompleteLogout() {
                                                    //기존에 카카오앱에 로그인 되어있던 id를 로그아웃한다.
                                                    goLoginActivity();
                                                }
                                            });


                                        }
                                    }, 500);
                                }
                            }

                            @Override
                            public void onSessionOpenFailed(KakaoException exception) {
                                Toast.makeText(SplashActivity.this, "실패 ", Toast.LENGTH_SHORT).show();
                                if (exception != null) {
                                    Logger.e(exception);
                                }
                            }
                        });*/

                    }else{
                        Log.d(TAG, "id가 없는경우 : !TextUtils.isEmpty(userLoginId))");
                        //페북 로그인 했는데 일전에 레몬클립에서 페북으로 로그인한 id와 다를 경우
                        //즉, 이앱으로 페북로그인했다가 다른 페북id로 페북 앱을 로그인 했을 경우
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SplashActivity.this, "Welcome! please log-in!", Toast.LENGTH_SHORT).show();
                                UserManagement.requestLogout(new LogoutResponseCallback() {
                                    @Override
                                    public void onCompleteLogout() {
                                        goLoginActivity();
                                    }
                                });
                            }
                        }, 1500);
                    }



                    break;
            }
        }




    }


    private void goMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void goLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
