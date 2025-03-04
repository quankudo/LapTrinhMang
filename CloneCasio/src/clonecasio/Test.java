/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clonecasio;

/**
 *
 * @author HP
 */
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
public class Test {
    

    public static void main(String[] args) {
        try {
            // URL của trang web cần lấy header
            URL url = new URL("https://phimmoichill.blog");
            
            // Mở kết nối
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // Phương thức GET
            connection.setInstanceFollowRedirects(false); // Không tự động redirect

            // Kết nối và lấy response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            // Lấy và in tất cả header
            Map<String, List<String>> headers = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            // Đóng kết nối
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

