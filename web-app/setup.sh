oc delete all -l app=cool-app

./build-with-version.sh 1234

oc start-build cool-app --from-file=./target/ROOT.war

oc new-app cool-app

oc expose svc/cool-app