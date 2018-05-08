# Jmeter on OCP

This project is a collection of samples file to run jmeter in remote/controller mode.
You can read the official jmeter [documentation](http://jmeter.apache.org/usermanual/remote-test.html) to run jmeter in remote/controller mode.

The samples use 4 components:
* Demo web application 'cool-app': Used as performance test target application.
* Jmeter Server Remote (Slave): Worker image, is used by a controller to delegate performance test execution.
* Jmeter Controller: Is the standard image to use for controlling a pipeline.
* Openshift Job: OCP Job use the Jmeter Controller image to lunch a specific test.


## Create the demo app

First you need to build the app, then:

```oc new-build --image-stream=openshift/jboss-webserver31-tomcat8-openshift:1.1 --name=cool-app --binary=true```

You should see this from the web console:

Empty project

We can now start the binary deploy (build the cool-app with maven from the web-app dir):

```oc start-build cool-app --from-file=<PATH_TO>/ROOT.war```

Deploy a new app

```oc new-app cool-app```

Create the route:

```oc expose svc/cool-app```

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

## Create Jmeter Controller image

Go to dir

```[PATH-TO]/jmeter-ocp-swarm/jmeter-controller/jmeter-controller-image```

Create the new build:

```oc new-build --strategy docker --binary  --name jmeter-controller```

Build our image:

```oc start-build jmeter-controller --from-dir . --follow```

## Setup the Jmeter project

In this example we use a config map to store the jmeter projet to run.
The config map is mounted by the Jmeter controller job.

Go to dir

```[PATH-TO]/jmeter-ocp-swarm/jmeter-controller/demo```

In this folder you'll find two files:
* cool-app-jmeter.jmx: is the test plan generated from Jmeter tools
* cool-app-heavy-jmeter.jmx: is the a more heavy test plan generated from Jmeter tools (http://jmeter.apache.org/usermanual/)
* jmeter-job-tempalte.yaml: This is a template useful for create the runtime environment for testing.

#JenkinsPipeline
Use this command to install the jmeter pipeline:
``` oc process -f openshift_templates/pipeline-load-testing-template.yaml -p JMETER_SLAVE_NODES_NUMBER=2  -p JENKINS_USER=demo-admin -p JENKINS_PWD_TOKEN=28a8fc209210fc6f4174e0f2c5d502c6 -p JMETER_TEST_FILE_URL=https://raw.githubusercontent.com/eljeko/jmeter-ocp-swarm/master/web-app/cool-app-jmeter.jmx  -p JMETER_JOB_TEMPLATE_URL=https://raw.githubusercontent.com/eljeko/jmeter-ocp-swarm/master/openshift_templates/jmeter-job-template.yaml -o yaml |oc create -f -```

* JMETER_SLAVE_NODES_NUMBER: How many test executor you need for testing the parallelism
* JENKINS_USER: You need to pass a jenkins username
* JENKINS_PWD_TOKEN: You need to pass a valid jenkins token for API Calls refer to (http://jmeter.apache.org/usermanual/)

## The Pipeline Runs jmeter controller as job
So you need to pass parameters:

* JMX_FILE_URL: the url of the jmx where the jmeter test is stored (also a github raw content)
* APPLICATION_NAME: the application name

The pipeline will get the ips of remote jmeter server running

```oc describe pods -l app=jmeter-server-remote|grep IP|awk '{print $2'}| tr '\n' ','| sed 's/,$//'```

eg (note that the server list must be changed according to your environment):

```oc process -f jmeter-job-template.yaml -p TIMESTAMP=1234 -p TEST_PLAN_URL=http://example.url/jmeter-test.jmx -p REMOTE_SERVERS_LIST=172.17.0.2,172.17.0.3 -o yaml --local=true```

You get the yaml processed template.

If you want you can pipe the process command to an ```oc create -f```

eg:

```oc process -f jmeter-job-template.yam [...] |oc create -f -```

To get the status of your job you can run:

```oc get pods -l job-name=jmeter-controller-job-<TIMESTAMP>```

eg:

```oc get pods -l job-name=jmeter-controller-job-1234```

The output should be something like this:

```
NAME                               READY     STATUS      RESTARTS   AGE
jmeter-controller-job-1234-gznfw   0/1       Completed   0          1h
```

Improvements:
* Remove the curl that retrieve the job controller template
* Move the performanceReport into the Collecting results phase
* Cleanup the Collecting results phase from grep
* Remove the JMX_FILE_URL parameter and the relative curl and execute a git clone, and a find of all jmx files
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
