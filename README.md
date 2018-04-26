#Jmeter on OCP

This project is a collection of samples file to run jmeter in remote/controller mode.

You can read the official jmeter [documentation](http://jmeter.apache.org/usermanual/remote-test.html) to run jemter in remote/controller mode.

## Create server slave image

Point to dir 

```[PATH-TO]/jmeter-ocp-swarm/jmeter-server-remote/jmeter-slave-image```

Create the new build:

```oc new-build --strategy docker --binary --name jmeter-server-remote```

Build our image:

```oc start-build jmeter-server-remote --from-dir . --follow```

Create at least one running pod:

``` oc new-app jmeter-server-remote```

The above command will start a new pod from the previously create build 

*NOTE*: all the instances are started with ```-Dserver.rmi.ssl.disable=true``` to turn off SSL.

## Create jmeter controller image

Point to dir 

```[PATH-TO]/jmeter-swarm/meter-controller/jmeter-controller-image```

Create the new build:

```oc new-build --strategy docker --binary  --name jmeter-controller```

Build our image:

```oc start-build jmeter-controller --from-dir . --follow```

## Running controller as job

Create the job from yaml file









------------------------------

--docker-image registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift


## Start server slave nodes with a test plan

unpack jmeter on your project dir

add

s2i/bin/run file

```
#!/bin/bash
echo "Before running application"
exec /usr/libexec/s2i/run
```

Create the builder

```oc new-build --binary=true --image-stream=redhat-openjdk18-openshift:1.2  --name=jmeter-remote```

build the image:

```oc start-build jmeter-remote --from-dir=./deployment --follow```

Create new app for the image built

```oc new-app jmeter-remote```


```jmeter -n -t script.jmx -R server1,server2,serverN```

You can read the official [documentation](http://jmeter.apache.org/usermanual/remote-test.html)

## Start the controller

