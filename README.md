# Instructions

With maven installed...

To get a compile, unit-test, package, start-server, deploy, soapui-test, undeploy and stop-server in one cmdline session use....

mvn clean verify

----------------------------

To get a build and deployment to an already running Glassfish use...

mvn clean package -P itest 

(uses the Ant-config contained in the deploy2glassfish.xml to locate the server autodeploy directory)
