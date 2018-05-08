This project lets you build a war file with dynamic version and context without changing source code.

# What you need

* A jdk 1.7+

* maven 3+

# How to build
There are two different profile for build the application to test the thresholds:
* app-slow: adds a Thread sleep of 200 milliseconds to the page
* app-fast: no Thread sleep for page loading

When you build the app you can change the *appversion* and *profileName* eg:

`mvn clean package -Dappversion=1.2.3 -P{profileName}`

The resulting ROOT.war file can be deployed into tomcat

# Build with scripts
There are two sh scripts:
* ./build-with-version.sh 1.4 app-slow: enables you to build the application
* ./setup.sh: deploy on openshift end expose a route for the application
