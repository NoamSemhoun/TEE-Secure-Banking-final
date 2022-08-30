# Finalbzh_Banking7Host
Project objective :
The objective of the project is to enable a user/customer to open a bank account in complete security in a trusted environment and to use it with greater serenity when authenticating or carrying out operations while his account is open. For this, all operations take place in a secure and reliable environment. The data will then be provided to the user if the operations have gone well. In addition to this, there will be generation and use of asymmetric keys to encrypt the password which complicates authentication for a malicious third party.


Problem resolved :
The problem ? that her generation has never seen bank robberies? embezzlement scam and theft of money...
The banking system must be ultra secure and must not allow any failure of the system in order to prevent anyone from entering an account and stealing money or even confidential data.
An attacker could, for example, infiltrate this type of application to recover account data or the various transfers of a user, Or embed in a transfer operation to modify its amount or even its destination...
The issue here is creating a banking system in a secure environment with cybersecurity related functions for a safer and more serene experience for the user. He can thus create an account, authenticate himself and carry out banking operations such as a withdrawal of money or a deposit. The user also has the option of modifying his password if he wishes. All of this can be done through a clear graphical interface in order to facilitate the user's experience when choosing the various operations he would like to perform in order to be able to fully exploit his account. 

Conception and Architecture :

The advantage of our solution will therefore be that the applet makes the intrusion almost impenetrable and therefore the data and interactions are secure in our application. 
The project is composed of sever!


al layers. The base of the project is in visual studio which creates and opens sessions in the applet. It is also in visual studio that all the graphics are located. All the operations, when to them, are in the applet, that is to say in eclipse in a java file. The visual studio part is written in C#. The WPF creates sessions each time the user wants to perform an operation and will display a menu with the available options. The user will send the necessary data for the operation to run. If the data is not good, the operation will not take place and the user will be informed and can start again if he wishes. Operations are performed in the applet without anyone having access to it. Once the operation has been completed, the user is sent back what was requested. Thus, this structure allows for increased security because everything is hidden and also very complicated to penetrate.


Program execution : 
in the smooth running of the application, we first invite the customer who has registered as a new customer by completing the following form (mot de passe sécurisé, double authetification,... ) :
 
Tthen he will receive these identifiers and will be able to connect to his platform through this fairly realistic graphical interface :    
He then found himself in his client interface which displays the balance of his bank account (which he obviously filled in when he registered) and which also displays certain bank functions such as different transactions (deposit, withdraw; make a transfer, etc.) 

[Uploading Image1.png…]()
 
Our application makes it possible to manage them all with security thanks to the communication of these functions with the applet.
Code snippets  :
 
Future work : 

This project is designed for a single client. It would be necessary to be able to allow several customers to register and suddenly connect the project to a server so that they can interact with each other. This means making transactions to other customers. We could add an IBAN to customers in order to allow transactions between users. This would offer even more security with additional personal data but also a more complete project. We could also add a savings or interest option to improve the model already established. These ideas were planned but due to the many problems encountered and the passage of time, we implemented an effective system for a single user.
Our challenge in the next stage will be to complete the security features that we have started (thinking that we will have the possibility of completing them of course) which among other things use the RSA algorithm to generate keys and control the strength of the pass of password and signature of our transactions by each user.

To conclude this experience was for us a real chain of twists and turns in the development of this project with in the end a lot of concept skills and notions learned in this exciting field of security
Thank you !

