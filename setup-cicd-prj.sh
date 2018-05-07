oc new-build --strategy docker --binary --name jmeter-server-remote
oc start-build jmeter-server-remote --from-dir ./jmeter-server-remote/jmeter-slave-image/ --follow
oc new-build --strategy docker --binary  --name jmeter-controller
oc start-build jmeter-controller --from-dir ./jmeter-controller/jmeter-controller-image/ --follow
