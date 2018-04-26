# Start jmeter in remote mode


Step 1: Start the servers

To run JMeter in remote node, start the JMeter server component on all machines you wish to run on by running the JMETER_HOME/bin/jmeter-server (unix) or JMETER_HOME/bin/jmeter-server.bat (windows) script.

We can start the server and disable the SSL

```JMETER_HOME/bin/jmeter-server.bat -Dserver.rmi.ssl.disable=true```

## Create server slave image

oc new-build --strategy docker --binary --name jmeter-server-remote

Point to dir 

[PATH-TO]/jmeter-swarm/jmeter-server-remote/jmeter-slave-image

```oc start-build jmeter-server-remote --from-dir . --follow```

Create at least one running pod:

``` oc new-app jmeter-server-remote```

The above command will start a new pod from the previously create build 

## Create jmeter controller image

oc new-build --strategy docker --binary  --name jmeter-controller

Point to dir 

[PATH-TO]/jmeter-swarm/meter-controller/jmeter-controller-image

```oc start-build jmeter-controller --from-dir . --follow```

NOTE TBD DELETED:

Create at least one running pod:

```oc new-app jmeter-controller```

The above command will start a new pod from the previously create build 

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

