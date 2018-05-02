oc new-build --strategy docker --binary  --name jmeter-controller

oc start-build jmeter-controller --from-dir . --follow
