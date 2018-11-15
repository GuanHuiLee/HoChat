package com.zgg.hochat.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Window;

import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseActivity;
import com.zgg.hochat.utils.DataUtil;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class SplashActivity extends BaseActivity {
    private android.os.Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initUI() {
        String token = DataUtil.getToken();
        if (!TextUtils.isEmpty(token)) {

            /**
             * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #init(Context)} 之后调用。</p>
             * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
             * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
             *
             * @param token    从服务端获取的用户身份令牌（Token）。
             * @param callback 连接回调。
             * @return RongIM  客户端核心类的实例。
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    showError("token错误");
                }

                /**
                 * 连接融云成功
                 * @param s 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String s) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToMain();
                        }
                    }, 800);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    showError("登录失败：" + errorCode);
                }
            });

        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToLogin();
                }
            }, 800);
        }
    }

    @Override
    protected void initData() {

    }


    private void goToMain() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    private void goToLogin() {
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }
}
