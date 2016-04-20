package com.sample.lightsoo.kakaotalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.sample.lightsoo.kakaotalk.Data.User;
import com.sample.lightsoo.kakaotalk.Manager.NetworkManager;
import com.sample.lightsoo.kakaotalk.Manager.PropertyManager;
import com.sample.lightsoo.kakaotalk.RestAPI.LoginAPI;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends Activity  {
        private static final String TAG = "LoginActivity";
    //server response code
    private static final int CODE_ID_PASS_INCORRECT = 531;

    String userLoginId;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton loginButton = (LoginButton)findViewById(R.id.com_kakao_login);

        Session.getCurrentSession().addCallback(new ISessionCallback() {
            @Override
            public void onSessionOpened() {
                Toast.makeText(LoginActivity.this, "accessToken : " + Session.getCurrentSession().getAccessToken(), Toast.LENGTH_SHORT).show();
//                userLoginId = Session.getCurrentSession().getAccessToken();

                UserManagement.requestMe(new MeResponseCallback() {
                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {

                    }

                    @Override
                    public void onNotSignedUp() {

                    }

                    @Override
                    public void onSuccess(UserProfile result) {
                        Toast.makeText(LoginActivity.this, "User : " + result.getId(), Toast.LENGTH_SHORT).show();

                        userLoginId = ""+ result.getId();

                        user = new User(userLoginId, PropertyManager.LOGIN_TYPE_KAKAO);
                        Call call = NetworkManager.getInstance().getAPI(LoginAPI.class).login(user);
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Response response, Retrofit retrofit) {
                                if (response.isSuccess()) {
                                    Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                                    PropertyManager.getInstance().setUserLoginId(userLoginId);
                                    PropertyManager.getInstance().setLoginType(PropertyManager.LOGIN_TYPE_KAKAO);
                                    goMainActivity();
                                } else {
                                    if (response.code() == CODE_ID_PASS_INCORRECT) {
                                        Toast.makeText(LoginActivity.this, "ID or Password incorrect", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Server Failure.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });



                    }
                });

            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                Toast.makeText(LoginActivity.this, "실패 ", Toast.LENGTH_SHORT).show();
//                Log.i("SessionCallback", exception.toString());
                if (exception != null) {
                    Logger.e(exception);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //로그인 성공하면 메인으로 이동하고 이전액티비티는 종료한다.
    private void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

//    protected void redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
//        final Intent intent = new Intent(this, KakaoSignupActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(intent);
//        finish();
//    }

}
