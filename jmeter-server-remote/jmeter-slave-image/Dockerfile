FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift

# Jmeter Server/Slave image for OpenShift
#
# This image provides a Jmeter Server Slave to run distributed tests.
# Jmeter 4.0
# OpenShift v3.

MAINTAINER Stefano Linguerri <slinguer@redhat.com>

ENV LANG=en_US.UTF-8 \
    LC_ALL=en_US.UTF-8

LABEL io.k8s.description="Jmeter is a tool for testing and stress testing" \
      io.k8s.display-name="Jmeter server slave" \
      io.openshift.tags="Jmeter,test,ci" \
      io.openshift.expose-services="1099:http" \
      io.openshift.s2i.scripts-url="image:///usr/libexec/s2i"

# Labels consumed by Red Hat build service
LABEL com.redhat.component="openshift-jmeter-4-slave-docker" \
      name="openshift3/jmeter-4-slave-rhel7" \
      version="4.0" \
      architecture="x86_64" \
      release="7"

# Port 1099 for remote method invocation, 50000 for slave agents
EXPOSE 1099 50000

COPY ./contrib/s2i/bin /usr/libexec/s2i

ADD ./contrib/apache-jmeter-4.0 /home/jboss/jmeter

ENTRYPOINT ["/usr/libexec/s2i/run"]
