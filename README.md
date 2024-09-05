Here is the formatted README file based on the provided content:

```markdown
# CORBA Client-Server Demo

## Introduction
This project demonstrates a simple CORBA (Common Object Request Broker Architecture) Client-Server application. CORBA is a middleware that allows objects to communicate with each other across networks, using the Internet Inter-ORB Protocol (IIOP). This demo uses the GlassFish CORBA library and Java.

## Theory Overview

### CORBA Object on Server:
1. Define the object specification using CORBA's IDL (Interface Definition Language).
2. Convert the IDL into a specific programming language (e.g., Java).
3. Use the IDL specification to generate middleware classes (`_Skel`).
4. Implement the CORBA object in Java.

### Client-side Connection:
1. Use the IDL to generate middleware classes (`_Stub`) on the client.
2. The client calls the server object through the `Stub`, which sends requests to the ORB (Object Request Broker).
3. The client's ORB sends the request to the server's ORB using the IIOP protocol.
4. The server ORB forwards the call to the server-side `Skeleton`, which executes the corresponding method on the CORBA object.

## Prerequisites
- JDK 1.8 (Oracle)
- Maven
- IntelliJ IDE or another Java IDE

## Project Setup

### Step 1: CORBA Environment Setup in Java
1. Create a new Java project in IntelliJ IDE with Maven as the build system.
2. Add the GlassFish CORBA dependency to the `pom.xml` file:

```xml
<dependencies>
    <dependency>
        <groupId>org.glassfish.corba</groupId>
        <artifactId>glassfish-corba-orb</artifactId>
        <version>4.2.5</version>
    </dependency>
</dependencies>
```

### Step 2: Define CORBA Object using IDL
- Create an `idl` directory in your project and add a file named `Hello.idl` with the following content:

```idl
module Example {
    interface Hello {
        string sayHello();
    };
};
```

### Step 3: Compile the IDL File
- Use the IDL-to-Java compiler to generate the necessary classes (`Stub`, `Skeleton`, and helper classes):

```bash
idlj -fall Hello.idl
```

### Step 4: Implement CORBA Object on Server
- Implement the CORBA object by creating a `HelloImpl.java` file:

```java
package org.example;

import Example.HelloPOA;

public class HelloImpl extends HelloPOA {
    @Override
    public String sayHello() {
        return "Hello from CORBA server!";
    }
}
```

- Create a `Server.java` file to run the CORBA server:

```java
package org.example;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.CosNaming.*;

public class Server {
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);

            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            HelloImpl helloImpl = new HelloImpl();
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
            Hello href = HelloHelper.narrow(ref);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            String name = "Hello";
            NameComponent[] path = ncRef.to_name(name);
            ncRef.rebind(path, href);

            System.out.println("CORBA Server ready and waiting...");
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Step 5: Implement CORBA Client
- Create a `Client.java` file to call the CORBA object from a remote client:

```java
package org.example;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;
import Example.*;

public class Client {
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            String name = "Hello";
            Hello helloRef = HelloHelper.narrow(ncRef.resolve_str(name));

            String response = helloRef.sayHello();
            System.out.println("Response from server: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Step 6: Running the Application
1. Compile the Java files:

```bash
javac -classpath . *.java
```

2. Start the ORB Naming Service:

```bash
tnameserv -ORBInitialPort 1050
```

3. Run the server:

```bash
java -ORBInitialPort 1050 Server
```

4. Run the client:

```bash
java -ORBInitialPort 1050 Client
```

### Step 7: Result
- When the client runs, it connects to the server and calls the `sayHello()` method. The server returns the string "Hello from CORBA server!" which is displayed in the client's console.

## Running CORBA Client-Server on Separate Machines

### Server Configuration
1. On the server machine, start the ORB Naming Service with the server's IP:

```bash
tnameserv -ORBInitialPort 1050 -ORBInitialHost <Server-IP-Address>
```

2. Update the `Server.java` to use the server's IP:

```java
String[] orbArgs = {"-ORBInitialHost", "192.168.1.10", "-ORBInitialPort", "1050"};
ORB orb = ORB.init(orbArgs, null);
```

3. Run the server with the correct IP:

```bash
java -ORBInitialHost 192.168.1.10 -ORBInitialPort 1050 Server
```

### Client Configuration
1. Update the `Client.java` to use the server's IP:

```java
String[] orbArgs = {"-ORBInitialHost", "192.168.1.10", "-ORBInitialPort", "1050"};
ORB orb = ORB.init(orbArgs, null);
```

2. Run the client:

```bash
java -ORBInitialHost 192.168.1.10 -ORBInitialPort 1050 Client
```

### Additional Steps
- Open firewall ports if necessary to allow connections through port `1050`.

## Conclusion
This demo shows how to set up a CORBA client-server application in Java using GlassFish CORBA and run the client and server on different machines over a network.
```
