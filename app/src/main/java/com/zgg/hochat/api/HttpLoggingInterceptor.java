package com.zgg.hochat.api;


import android.os.SystemClock;

import com.orhanobut.logger.Logger;
import com.zgg.hochat.utils.Constant;
import com.zgg.hochat.utils.ShaUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static java.net.HttpURLConnection.HTTP_NOT_MODIFIED;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static okhttp3.internal.http.StatusLine.HTTP_CONTINUE;

/**
 * Created by niujingtong on 2018/7/13.
 * 模块：
 */
public class HttpLoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();

        String body = null;

        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = Charset.forName("UTF8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            body = buffer.readString(charset);
        }

        Logger.d("发送请求\n method：%s\n url：%s\n headers: %s\n body：%s",
                request.method(), request.url(), request.headers(), body);


        String appKey = Constant.APP_KEY;
        String appSecret = Constant.APP_SECRET;
        String nonce = String.valueOf(Math.floor(Math.random() * 1000000));
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

//        request.newBuilder()
//                .addHeader("App-Key", appKey)
//                .addHeader("Nonce", nonce)
//                .addHeader("Timestamp", timestamp)
//                .addHeader("Signature", ShaUtils.sha1(appSecret + nonce + timestamp))
//                .build();

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        String rBody = null;

        if (hasBody(request, response)) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = Charset.forName("UTF8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(charset);
                } catch (UnsupportedCharsetException e) {
                    e.printStackTrace();
                }
            }
            rBody = buffer.clone().readString(charset);
        }

        Logger.d("收到响应\n responseCode:%s\n message:%s\n time:%s\n 请求url：%s\n 请求body：%s\n 响应body：%s",
                response.code(), response.message(), tookMs, response.request().url(), body, rBody);

        String cookies = null;
        if (!response.headers("Set-Cookie").isEmpty()) {

            for (String header : response.headers("Set-Cookie")) {
                if (header.contains("rong_auth_cookie")) {
                    cookies = header;
                }
            }
        }
        Logger.d(cookies);

        return response;
    }


    private boolean hasBody(Request request, Response response) {
        if (response.request().method().equals("HEAD")) {
            return false;
        }

        int responseCode = response.code();
        if ((responseCode < HTTP_CONTINUE || responseCode >= 200)
                && responseCode != HTTP_NO_CONTENT
                && responseCode != HTTP_NOT_MODIFIED) {
            return true;
        }
        if (stringToLong(request.headers().get("Content-Length")) != -1
                || "chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
            return true;
        }

        return false;
    }

    private long stringToLong(String s) {
        if (s == null) return -1;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
