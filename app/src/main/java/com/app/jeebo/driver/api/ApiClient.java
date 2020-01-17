package com.app.jeebo.driver.api;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.app.jeebo.driver.BuildConfig;
import com.app.jeebo.driver.R;
import com.app.jeebo.driver.application.JeeboApplication;
import com.app.jeebo.driver.utils.AppUtils;
import com.app.jeebo.driver.utils.Tls12SocketFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static IApiRequest apiRequest;
    private static TrustManager[] trustAllCerts;

    public static void init(Class<IApiRequest> requestClass, Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // setup timeout vale offset
        /*OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new ApiInterceptor()).addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                .build();*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create()).client(checkCertificates(context))
                .build();

        Log.d("url" , BuildConfig.HOST);
        apiRequest = retrofit.create(requestClass);

    }
    public static IApiRequest getRequest() {
        if(AppUtils.isNetworkAvailable(JeeboApplication.getContext()))
            return apiRequest;
        else{
            Toast.makeText(JeeboApplication.getContext(),JeeboApplication.getContext().getResources().getString(R.string.network_error) , Toast.LENGTH_LONG).show();
        }
        return apiRequest;
        //return apiRequest;
    }

    private static OkHttpClient checkCertificates(Context context){
        try {
            // Create a trust manager that does not validate certificate chains
            trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {

                            return new X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttp = null;


            SSLSocketFactory pinnedCertSslSocketFactory = getPinnedCertSslSocketFactory(context);
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .sslSocketFactory(pinnedCertSslSocketFactory)
                    .addNetworkInterceptor(new ApiInterceptor()).addInterceptor(interceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS);

            okHttp = enableTls12OnPreLollipop(builder).build();


            return okHttp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }

    private static SSLSocketFactory getPinnedCertSslSocketFactory(Context context) {
        try {
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore trusted = KeyStore.getInstance(keyStoreType);
            //  InputStream in = context.getResources().openRawResource(R.raw.certificate);
//            trusted.load(in, null);
            // Loading CAs from an InputStream
            CertificateFactory cf = null;
            cf = CertificateFactory.getInstance("X.509");

            Certificate ca;
            // I'm using Java7. If you used Java6 close it manually with finally.
            InputStream cert = new BufferedInputStream(context
                    .getResources().openRawResource(R.raw.jeeboapp_dz));
            try {
                ca = cf.generateCertificate(cert);
                System.out.println("ApiCallback ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                cert.close();
            }
//            trusted.load(cert, "mysecret".toCharArray());
            trusted.load(null, null);
            trusted.setCertificateEntry("ca", ca);
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
            trustManagerFactory.init(trusted);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            Log.e("MyApp", e.getMessage(), e);
        }
        return null;
    }


}
