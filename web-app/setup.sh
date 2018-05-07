oc delete all -l app=cool-app

oc new-build --image-stream=openshift/jboss-webserver31-tomcat8-openshift:1.1 --name=cool-app --binary=true

oc start-build cool-app --from-file=./target/ROOT.war

oc new-app cool-app

oc expose svc/cool-app
