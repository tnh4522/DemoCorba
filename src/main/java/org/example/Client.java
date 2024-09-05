package org.example;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;
import org.example.Example.*;

public class Client {
    public static void main(String[] args) {
        try {
            // Khởi tạo ORB
            ORB orb = ORB.init(args, null);

            // Lấy tham chiếu tới NameService
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Tìm đối tượng Hello trong NameService
            String name = "Hello";
            Hello helloRef = HelloHelper.narrow(ncRef.resolve_str(name));

            // Gọi phương thức trên đối tượng từ xa
            String response = helloRef.sayHello();
            System.out.println("Response from server: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
