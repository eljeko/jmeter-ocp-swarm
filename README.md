# Jmeter on OCP

This project is a collection of samples file to run jmeter in remote/controller mode.
You can read the official jmeter [documentation](http://jmeter.apache.org/usermanual/remote-test.html) to run jmeter in remote/controller mode.

The samples use 4 components:
* Demo web application 'cool-app': Used as performance test target application.
* Jmeter Server Remote (Slave): Worker image, is used by a controller to delegate performance test execution.
* Jmeter Controller: Is the standard image to use for controlling a pipeline.
* Openshift Job: OCP Job use the Jmeter Controller image to lunch a specific test.

## Create the main OCP project

Login as normal user (eg: developer), run the command:

```oc new-project ci-cd```

## Create the demo app

For this example we will deploy the cool-app in the same project ```ci-cd``` of Jenkins.

Go to the project subfolder ```web-app```.

Build the application following the [building istructions](web-app/README.md)

Then we can create the relevant object in order to deploy in OCP:

```oc new-build --image-stream=openshift/jboss-webserver31-tomcat8-openshift:1.1 --name=cool-app --binary=true```

We can now start the binary deploy:

```oc start-build cool-app --from-file=./target/ROOT.war```

Deploy a new app:

```oc new-app cool-app```

Create the route:

```oc expose svc/cool-app```

In the ```web-app``` subfolder there is a ```setup.sh``` script with all the commands.

## Create Jmeter server remote (slave) image

Go to project sub folder ```jmeter-server-remote```

Create the new build:

```oc new-build --strategy docker --binary --name jmeter-server-remote```

Build our image:

```oc start-build jmeter-server-remote --from-dir . --follow```

In the ```jmeter-server-remote``` subfolder there is a ```setup.sh``` script with all the commands.

Just to test you can create at least one running pod:

``` oc new-app jmeter-server-remote```

The above command will start a new pod from the previously create build.

*NOTE*: all the instances are started with ```-Dserver.rmi.ssl.disable=true``` to turn off SSL.

## Create Jmeter Controller image

Go to project sub folder ```jmeter-controller```

Create the new build:

```oc new-build --strategy docker --binary  --name jmeter-controller```

Build our image:

```oc start-build jmeter-controller --from-dir ./jmeter-controller-image/ --follow```

In the ```jmeter-controller``` subfolder there is a ```setup.sh``` script with all the commands.

## Setup the Jmeter project

In this example the jmeter test file is in the [cool-app subfolder](cool_app/cool-app-jmeter.jmx)

```cool-app-jmeter.jmx```: is the test plan generated with Jmeter tool.

# Setup Jenkins for performance plugin

```oc new-app jenkins -e INSTALL_PLUGINS=performance:3.8```


To troubleshooting problems please refer to [OCP Jenkins documentation](https://docs.openshift.com/container-platform/3.9/using_images/other_images/jenkins.html)
# JenkinsPipeline
Use this command to install the jmeter pipeline:

```oc process -f pipelines/pipeline-load-testing-template.yaml -v CONCURRENT_LAUNCHER=2  JENKINS_USER=demo-admin JENKINS_PWD_TOKEN=78907bd92279b73aee2b16b8ef7e8757 -o yaml |oc create -f -```

* CONCURRENT_LAUNCHER: How many test executor you need for testing the parallelism
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