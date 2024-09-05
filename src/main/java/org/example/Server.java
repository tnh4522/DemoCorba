package org.example;

import org.example.Example.*;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.CosNaming.*;

public class Server {
    public static void main(String[] args) {
        try {
            // Khởi tạo ORB
            ORB orb = ORB.init(args, null);

            // Lấy tham chiếu POA và kích hoạt POA Manager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Tạo đối tượng HelloImpl
            HelloImpl helloImpl = new HelloImpl();

            // Lấy tham chiếu đối tượng từ HelloImpl và đăng ký nó với ORB
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
            Hello href = HelloHelper.narrow(ref);

            // Đăng ký đối tượng với NameService
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            String name = "Hello";
            NameComponent[] path = ncRef.to_name(name);
            ncRef.rebind(path, href);

            System.out.println("CORBA Server ready and waiting...");

            // Chạy ORB để đợi yêu cầu từ client
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
