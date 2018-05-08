This project lets you build a Docker image for the jmeter controller node.

# What you need

* oc client

# How to build
Create a build with strategy docker:
`oc new-build --strategy docker --binary  --name jmeter-controller`

Start the build from dir:
`oc start-build jmeter-controller --from-dir . --follow`

# Build with scripts
There are two sh scripts:
* ./delete: cleanup the current namespace
* ./setup.sh: deploy do the build stuff for you
