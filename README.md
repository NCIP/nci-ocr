Introduction
===========================

The NCI Online Credentialing Repository (NCI OCR, previously FIREBIRD) is a software application that supports electronic submission of clinical trial documentation to trial sponsors and regulatory bodies. NCI OCR automates the investigator registration process that currently requires submission of a series of paper forms including the Form FDA 1572, Curriculum Vitae, Financial Disclosure Form, and supporting documentation. Through a single web-based interface to a secure central repository, investigators will be able to maintain their profile containing the accreditation information required for their participation in drug trials.

License
===========================
This product includes software developed by 5AM, Essex Management and the National Cancer Institute.

THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC. 
OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
THE POSSIBILITY OF SUCH DAMAGE. 

Note
===========================
This software package is now branded as NCI OCR (Online Credentialing Repository) but during its initial development was
named FIREBIRD. For this reason you will see the name FIREBIRD used in SVN paths, Java package namespaces,
class names, etc. We are retaining use of the name FIREBIRD for these internal constructs, but any user visible
references to the application must be to NCI OCR.

Setting up the environment:
===========================
These are the pre-requisites for deploying NCI OCR in a development environment:
1.  Download and install Java 6

	After installing Java 6, download the Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction 
	Policy Files 6 and install them following the README.txt file downloaded.
	Link: http://www.oracle.com/technetwork/java/javase/downloads/index.html
	
2.  Download and Install Maven 2.2.x
3.  Download and install JBoss 5.1.0 for JDK 6 and Set JBOSS_HOME env variable to your JBoss 
    installation directory. Copy bouncycastle jars to $JBOSS_HOME/server/default/lib.
4.  Download and install postgres 8.4.x from http://www.postgresql.org/download/.  During the 
    installation, take note of the password for the postgres user.
5.  Make sure your JAVA_HOME and PATH variables are set to use Java 6 and maven 2.2.x.
6.  Install svn (client) version 1.6.x if not already present and add to your PATH.
7.  Checkout the NCI OCR code base from the git repo at https://github.com/NCIP/nci-ocr

8.  Initialize the database:

    cd software/services
    mvn -Pnuke-db,local sql:execute

9. Synchronize with the Staging grid trust fabric:
 
    If not already installed, download and install caGrid 1.4 from cagrid.org. 
    In the root of the caGrid installation run the following command:
    ant -Dtarget.grid=nci_stage-1.4 configure

10. Configure your profiles.xml by copying profiles.xml.example to profiles.xml and changing any necessary properties.

    Note that jboss.home in the local profile needs to be set

Building and deploying the application:
===================================

To deploy the application:

    cd software
    mvn -Plocal,nes-integration-tier clean install cargo:deploy
    cp server/target/classes/*.xml $JBOSS_HOME/server/default/deploy

    NOTE: If you get a java.lang.OutOfMemoryError: Java heap space error,  run "export MAVEN_OPTS=-Xmx512m"

JBoss and NCI OCR access:
============================
Use the standard JBOSS_HOME/bin/run.sh (run.bat for Windows) to start the application.

For either build system JBOSS_HOME/bin/shutdown.sh (shutdown.bat for Windows) -S will stop JBoss.

Access the application at http://localhost:8080/ocr/

The test user account recommended for initial access is:
Username: fbciinv1
Password: F1reb1rd!!
    
