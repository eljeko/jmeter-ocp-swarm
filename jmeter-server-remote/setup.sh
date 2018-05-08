oc new-build --strategy docker --binary --name jmeter-server-remote
oc start-build jmeter-server-remote --from-dir ./jmeter-slave-image/ --follow
