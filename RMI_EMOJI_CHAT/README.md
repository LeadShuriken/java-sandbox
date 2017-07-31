# RMI Client/Server Chat Application

## Introduction.

„RMI Client/Server Chat Application“ is an aplication based on RMI (JAVA Remote Method Invocation). This application gives the posibility for many clients to communicat between each other while still using server resources (in this case downloadable emoji images). The application has a basic user interface made with java swing and is made as a delivarable for a university course from the masters of Software Dev. for the Internet in "New Bulgarian University" Sofia.

## Program flow

The program flow for the application is as follows.

* Enter the root project folder: **bin**
* We start the server with **java server.Server**
* The server registers itself in the local **rmi registry** as **rmi://<machine_ip_address>*//Chat_Server**
* The server hangs on waything for clients
* Each client is started with: **java client.ClientGUI**
The above also start the client GUI which in turn register the client.
* The client (client.Client) looks for the server in the rmi registry and if the server is there the client sends a hello echo to the chat and registers itself in the registry.
* The server storest the client in it's own data structure (ArrayList<UserPOJO> clients). 
* Comunication is handled entirely by RMI with the client being registered as a messege sender at the server and the server being registered as a messege sender at the client.

## Assets sharing/Data transfer (emoji)

With each client having a local emoji database the practical data sharing happens like so:

**1.** The client sends a char array to the server which might or might not contain emoji symbols ( :), etc..) which are beforehand associated with their respectful emojis at the server. 
**2.** The server associates the asset and prepares it for request from the sending client or any other which does not have it locally.
**3.** The server then adds a token to the start of the string of emoji so that the clients, besides the one intending to use the emoji in a messege, can associate the string to an asset.
**4.** The client recognise the emoticon string as something then need to make an association and request the filename for that association from the server (in the case that it's not already made (serialized states check the todo)).
**5.** If the client happens to have the file locally it creates an association otherwise it request the assets from the serve and just after creates it.

## TODO
While most of the bellow are outside of the scope of RMI they need to be pointed out as they are to be DONE and are now a serious hinder to the use of the application (commercilly)

•	Serialisiation and save of local application state (associations and such)
•	Panel for reference of whom is in the chat.
•	Personal messege to user. 
•	Posibility to save a group of emoji/image and their association(if they use any).
•	Panel for adding images other then emoji (gif/png).
•	Video and audio chat (real easy with RMI).
•	Codebase URI for full remote functionality of the RMI.

