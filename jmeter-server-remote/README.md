This project lets you build a Docker image for the jmeter slave test executor node.

# What you need

* oc client

# How to build
Create a build with strategy docker:
`oc new-build --strategy docker --binary --name jmeter-server-remote`

Start the build from dir:
`oc start-build jmeter-server-remote --from-dir ./jmeter-slave-image/ --follow`


# Build with scripts
There are two sh scripts:
* ./delete: cleanup the current namespace
* ./setup.sh: deploy do the build stuff for you
