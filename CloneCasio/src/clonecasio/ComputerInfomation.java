package clonecasio;

import java.io.InputStream;
import java.net.InetAddress;
import static java.net.InetAddress.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ComputerInfomation {
    public static void main(String[] args) throws UnknownHostException {
        Bai1();
        System.out.println("--------------------------------");
        Bai2();
        System.out.println("--------------------------------");
        Bai3();
        System.out.println("--------------------------------");
        Bai4();
    }
    
    public static void Bai1() throws UnknownHostException{
        System.out.println(getLocalHost());
        System.out.println(getByName(null));
        System.out.println(getByName("ute.udn.vn"));
    }
    
    public static void Bai2() throws UnknownHostException{
        InetAddress[] arr = getAllByName("ute.udn.vn");
        for (InetAddress inetAddress : arr) {
            System.out.println(inetAddress.getHostName());
            System.out.println(inetAddress.getHostAddress());
            System.out.println(inetAddress.getCanonicalHostName());
        }
    }
    
    public static void Bai3() {
        try {
            System.out.println("Nhap dia chi can ket noi: ");
            Scanner sc = new Scanner(System.in);
            String urlStr = sc.nextLine();
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            System.out.println("\nThông tin Header:");
            int i = 1;
            String headerKey;
            while ((headerKey = connection.getHeaderFieldKey(i)) != null) {
                String headerValue = connection.getHeaderField(i);
                System.out.println(headerKey + ": " + headerValue);
                i++;
            }
        } catch (Exception e) {
            System.out.println("Throws Error in Bai3: "+ e.getMessage());
        }
    }
    
    public static void Bai4() {
        try {
            System.out.println("Nhap dia chi can ket noi: ");
            Scanner sc = new Scanner(System.in);
            String urlStr = sc.nextLine();
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            System.out.println("\nThông tin content website:");
            InputStream inputStream = (InputStream) connection.getContent();
            Scanner contentScanner = new Scanner(inputStream);
            StringBuilder content = new StringBuilder();

            while (contentScanner.hasNextLine()) {
                content.append(contentScanner.nextLine()).append("\n");
            }
            contentScanner.close();

            // Hiển thị nội dung trang web
            System.out.println("\nNội dung trang web:\n");
            System.out.println(content.toString());
            
        } catch (Exception e) {
            System.out.println("Throws Error in Bai3: "+ e.getMessage());
        }
    }
}
