Configuring Eclipse for development:
====================================
The supported IDE for NCI OCR development is Eclipse IDE for Java EE Developers version 3.7.1.

In Preferences, open Java -> Compiler -> Errors/Warnings and set Unused '@SuppressWarnings' token to "Ignore".

You will need to add to Eclipse the following plug-in versions (install site URLs are given):
    - PMD for Eclipse 3 Plug-in, version 3.2.x (http://pmd.sf.net/eclipse)
    - Eclipse Checkstyle Plug-in, version 5.5.x (http://eclipse-cs.sf.net/update)
    - Subclipse GIT Plug-in
    - m2eclipse Maven Plug-in. This needs to be installed manually. 
	- Unzip the plugin found in firebird/tools/eclipse/maven_eclipse_plugin.zip
	- Copy the contents of the unzipped folder into the Eclipse plugins folder
        
Checkstyle, PMD, and Java code formatter XML configuration can be found in the software/resources folder
   
To complete configuration of the PMD checking for NCI OCR, create a Working Set (Java type) named 
"FIREBIRD code" (the name must be exactly that) including the following selections:
    - services/src/main/java
    - web/src/main/java

Doing this will only flag PMD issues in the production src (not tests). If you see warnings/errors flagged in test 
classes right-click on the FIREBIRD project and select PMD > Clear PMD Violations.

How to run Selenium tests under a dedicated firefox profile:
============================================================================
1.) Create a new profile in firefox named Selenium 
    (see http://girliemangalo.wordpress.com/2009/02/05/creating-firefox-profile-for-your-selenium-rc-tests/)
2.) If desired, navigate to the profiles directory and edit the profile directory name as well as the profiles.ini file.
3.) Add a new property to your local profiles.xml file
    <test.selenium.server.profile.location>/Users/<<USERNAME>>/Library/Application Support/Firefox/Profiles/<<PROFILE_DIR>> </test.selenium.server.profile.location>
4.) Setup profile as desired with any extensions or property changes  

How to set up your own Mail Server information to send email using non-NIH mail server:
======================================================================================
When in test and production environments we will be using the mail server information set up on test JBoss
server, but when performing development you probably don't want to always be connected to the VPN to access
the NIH mail server so just follow these instructions to set up an external server for NCI OCR to use.

[Note: by default sending email is disabled. This is usually preferable for development, so the following steps
are optional].

1.) Open the mail-service.xml file in the $JBOSS_HOME/server/default/deploy directory.
2.) Replace the Contents of the file with the XML at the bottom of this file.
3.) Replace the User and Password properties of the Dev_Mail mbean configuration with your 5AM / gmail email and password
4.) Save
4.) Open your profiles.xml file at the root of your source directory.
5.) Make sure you have a property named "mail.service.jndi" with the value of the JNDI name in the mail-service.xml (i.e. Dev_Mail)
6.) You can also add a property "send.mail" which dictates whether emails will or will not be sent. (Boolean true or false)
7.) You're Done! Don't check in the profiles.xml

Using dumbster.war to check generated emails:
====================================================================
Dumbster is an open source utility used to intercept SMTP email. The NCI OCR team has created a web application that employs Dumbster that 
supports reviewing NCI OCR-generated emails via the web. To enable this functionality:

- If setting this up for a local developer environment, ensure that your profiles.xml is up-to-date based on the profiles.xml.example
  that's checked into the repository. You will need the mail.smtp.server, mail.smtp.port and send.mail properties set as they are in
  profiles.xml.example. If this is for DEV or QA, this is unnecessary as these properties are provided in the existing build configurations.
  
- From the root of the NCI OCR trunk, copy tools/dumbster/dumbster.war to the $JBOSS_HOME/server/default/deploy directory.

- To access the Dumbster email web application, access the URL /dumbster on the JBoss server (e.g. http://localhost:8080/dumbster).

- To start intercepting emails, click the "Start" button on the page. Click the "Sent Mail" link when you want to view any generated emails.

Note that functionality that needs to send email will fail if Dumbster has not been started, so ensure that Dumbster is running when
manually using NCI OCR. Also note that the functional/integration tests need to start a Dumbster instance to run, so be sure to stop the 
Dumbster web application before running the integration tests.

Maven commands to know during development:
====================================================================
These commands are useful when using the maven build.

mvn -Plocal,nes-integration-tier install -Dskip-liquibase=true  (the skip liquibase flag is needed anytime
    you don't want the latest db upgrades to run)
cd services  followed by mvn compile hibernate3:hbm2ddl (produce a fresh hibernate schema
         which is useful when writing db update scripts)
mvn -Plocal,nes-integration-tier install (compiles code, builds packages, tests and verifies coding
            metrics - the most commonly used target)
mvn clean (deletes all built artifacts)
mvn compile (just compiles the source)
mvn test (compiles and executes tests)
mvn site (compiles, packages, and builds a site of reports, but does not fail build if metrics do not pass)
mvn -Pnuke-db sql:execute  (completely deletes and recreates the db, only needs to be run in the services project)
mvn process-resources (process application resources, which includes running all of the main and client-specific
    db update scripts the current db has not had executed against it.  Only needs to be run in the services project)
mvn cargo:deploy (depoys to your local jboss)
mvn nci-commons:copy-web-files (copys htmml, jsp, css,images, etc to therunning container)
mvn nci-commons:jboss-undeploy (undeploys from the local jboss)
mvn -f ear/pom.xml -Pci,local,nes-integration-tier integration-test (launches a test container and runs the Selenium and integration tests).
mvn -f ear/pom.xml -Pci-nostart-nodeploy,local,nes-integration-tier integration-test (runs the Selenium and integration tests using an already running JBoss with NCI OCR deployed).