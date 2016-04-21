# diabetes-registry
This diabetes registry source code can be used to build a system for saving and tracking health information on patients with diabetes.

##Diabetes Registry Setup and Run Instructions

##Sections

A - requirements

B - build MySQL database

C - build application

D - deploy application

E - notes


###Section A - requirements

**a)** Download and install MySQL Community Server and Workbench from 

http://dev.mysql.com/downloads/

**b)** Download and install JDK 8 from 

http://www.oracle.com/technetwork/java/javase/downloads/index.html

**c)** Download and install Apache Tomcat 7 from 

https://tomcat.apache.org

**d)** Download and install NetBeans IDE from

https://netbeans.org/downloads/

###Section B - build MySQL database

**a)** In Workbench, open and execute the file named "createdbscript.sql" found in the db directory.

**b)** In Workbench, open and execute the file named "data_dump.sql" found in the db directory.

###Section C - build application

**a)** Place the following jar files in the Tomcat/lib directory:

Go to 

<a href="https://jstl.java.net/download.html" target="_blank">https://jstl.java.net/download.html</a> and download 

		- javax.servlet.jsp.jstl-api-1.2.1.jar
		
		- javax.servlet.jsp.jstl-1.2.1.jar
Go to 

<a href="https://dev.mysql.com/downloads/connector/j/" target="_blank">https://dev.mysql.com/downloads/connector/j/</a> and download

		- MySQL Connector/J
		
Go to 

<a href="http://www.jfree.org/jfreechart/download.html" target="_blank">http://www.jfree.org/jfreechart/download.html</a> and download

	- jcommon-1.0.23
	
	- jfreechart-1.0.19

**b)** Open the project in NetBeans and verify the program compiles and runs.  Build the application to produce a WAR file in the dist folder.

###Section D - deploy application

**a)** Place the WAR file in the Tomcat/webapps directory.

**b)** Run the application by executing the startup file in the Tomcat/bin directory.

**c)** View the application by pointing the browser to https://localhost:8443/diabetesregistry for a connection with SSL/TLS implemented.  Otherwise, comment out the application's security constraint in diabetesregistry/WEB-INF/web.xml and point the browser to http://localhost:8080/diabetesregistry.

**d)** Stop the application by executing the shutdown file in the Tomcat/bin directory.
	

###Section E - notes

**a)** A default user is provided with the following credentials:

username = joeUser

password = Test1234?!

**b)** The default user is associated with a default clinic, named "Test Clinic".  The registration key for this clinic is

“#1registrationKey”.

**c)** SSL/TLS:

	To utilize SSL/TLS on localhost, a self-signed certificate is required.  With a self-signed certificate created, access the running application by SSL/TLS by pointing a web browser to https://localhost:8443/diabetesregistry

	Otherwise, comment out the application's security constraint in diabetesregistry/WEB-INF/web.xml and access the running application by pointing a web browser to http://localhost:8080/diabetesregistry

**d)** storage encryption:

	A default key used for database encryption of sensitive attributes is assigned in the WelcomeServlet.  Modify this key assignment according to user requirements.

