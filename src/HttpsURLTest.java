/*
 * Copyright (c) Multichoice Technical Operations. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Multichoice Technical Operations. ("Confidential Information"). You
 * shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of the license agreement you
 * entered into with Multichoice Technical Operations.
 *
 * MULTICHOICE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. MULTICHOICE
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * @author Navaneeth Sen
 * @since 2015/04/22
 */

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class HttpsURLTest
{
    public static void main(String[] args) throws Exception
    {

        HttpsURLConnection.setDefaultSSLSocketFactory(createSslSocketFactory());

        long t_1 = System.currentTimeMillis(), t_2, t_3, t_4, t_5;
        String urlString = "https://google.com";
        URL url = new URL(urlString);
//        URLConnection urlConnection = url.openConnection();

        t_2 = System.currentTimeMillis();
        System.out.printf("Creating URLConnection %dms%n", t_2-t_1);

        HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) url.openConnection();
//        httpsUrlConnection.setRequestProperty("Connection", "close");
//        HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) urlConnection;
//        SSLSocketFactory sslSocketFactory = createSslSocketFactory();
//        httpsUrlConnection.setSSLSocketFactory(sslSocketFactory);

        t_3 = System.currentTimeMillis();
        System.out.printf("Preparation for input took %dms%n", t_3 - t_2);

        InputStream inputStream = httpsUrlConnection.getInputStream();

        t_4 = System.currentTimeMillis();
        System.out.printf("GetInputStream took %dms%n", t_4 - t_3);
        StringBuffer response = new StringBuffer();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null)
            {
                response.append(line);
            }
        }
//        try
//        {
//            final Reader reader = new InputStreamReader(inputStream);
//            final char[] buf = new char[16384];
//            int read;
//            final StringBuffer sb = new StringBuffer();
//            while ((read = reader.read(buf)) > 0)
//            {
//                sb.append(buf, 0, read);
//            }
//        }
        finally
        {
//            inputStream.close();
        }

        t_5 = System.currentTimeMillis();
        System.out.printf("Parsing the InputStream %dms%n", t_5 - t_4);
        System.out.printf("Total duration %dms%n" , t_5 - t_1);
        System.out.println("response = " + response);
    }

    private static SSLSocketFactory createSslSocketFactory() throws Exception
    {
        TrustManager[] byPassTrustManagers = new TrustManager[] {new X509TrustManager()
        {
            public X509Certificate[] getAcceptedIssuers()
            {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType)
            {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType)
            {
            }
        }};
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, byPassTrustManagers, new SecureRandom());
        return sslContext.getSocketFactory();
    }
}
