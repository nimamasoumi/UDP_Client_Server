# UDP Client-Server Application
Clients and a server may communicate using the Transmission Control Protocol (TCP) or the User Datagram Protocol (UDP). TCP communication is more reliable than UDP, while, UDP communication is faster and suitable for send large data. In UDP communication, the user does not require to receive the entire data, and the data may be lost during the transmission. However, the received data are sufficient to convey the information. 

![alt-text](https://github.com/nimamasoumi/UDP_Client_Server/blob/main/udp.jpeg?raw=true)

Herein, the client and server Java applications connect and communicate using UDP. 
## Usage and run the program ##

- Step 1:
  Compile the source codes into bytecode class files using Java Compiler (javac). For instance:
  
  `javac *.java`
  
  
- Step 2:
  Run the server and then the client codes by starting the Java Runtime Environment (JRE). For instance:
  
  `java UdpServer && java UdpClient`