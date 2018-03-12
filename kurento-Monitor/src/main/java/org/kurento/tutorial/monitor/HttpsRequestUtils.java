package org.kurento.tutorial.monitor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https通用接口工具类
 * @author zhao
 *
 */
public class HttpsRequestUtils {
	
	private static Logger logger = LoggerFactory.getLogger(HttpsRequestUtils.class);   
	
	/**
	 * 发起https请求并获取结果 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return
	 */
	public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {    
		String result = null;    
        
        StringBuffer buffer = new StringBuffer();    
        try {    
              
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化     
            TrustManager[] tm = { new MyX509TrustManager() };    
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");    
            sslContext.init(null, tm, new java.security.SecureRandom());    
              
            // 从上述SSLContext对象中得到SSLSocketFactory对象     
            SSLSocketFactory ssf = sslContext.getSocketFactory();    
    
            //打开连接  
            URL url = new URL(requestUrl);    
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();    
            httpUrlConn.setSSLSocketFactory(ssf);    
            httpUrlConn.setDoOutput(true);    
            httpUrlConn.setDoInput(true);    
            httpUrlConn.setUseCaches(false);    
              
            // 设置请求方式（GET/POST）     
            httpUrlConn.setRequestMethod(requestMethod);    
    
            if ("GET".equalsIgnoreCase(requestMethod)){    
                httpUrlConn.connect();    
            }  
            // 当有数据需要提交时     
            if (null != outputStr) {    
                OutputStream outputStream = httpUrlConn.getOutputStream();    
                // 注意编码格式，防止中文乱码     
                outputStream.write(outputStr.getBytes("UTF-8"));    
                outputStream.close();    
            }    
    
            // 将返回的输入流转换成字符串     
            InputStream inputStream = httpUrlConn.getInputStream();    
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");    
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
            String str = null;    
            while ((str = bufferedReader.readLine()) != null) {    
                buffer.append(str);    
            }    
            bufferedReader.close();    
            inputStreamReader.close();    
            // 释放资源     
            inputStream.close();    
            inputStream = null;    
            httpUrlConn.disconnect();    
            result = buffer.toString();    
        } catch (ConnectException ce) {    
            logger.error("server connection timed out.");    
        } catch (Exception e) {    
            logger.error("https request error:{}", e);    
        }    
        return result;    
    }   

}
