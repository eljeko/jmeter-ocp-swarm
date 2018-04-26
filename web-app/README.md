This project lets you build a war file with dynamic version and context without changing source code.

# What you need

* A jdk 1.7+

* mavevn 3+ 

# How to build

When you build the app you can change the *appversion* eg:

`mvn clean package -Dappversion=1.2.3 `
 
The resulting ROOT.war file can be deployed into tomcat
 
