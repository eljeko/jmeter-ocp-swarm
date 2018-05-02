FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift

# Jmeter Controller image for OpenShift
#
# This image provides a Jmeter Controller to run distributed tests.
# Jmeter 4.0
# OpenShift v3.

MAINTAINER Stefano Linguerri <slinguer@redhat.com>

ENV LANG=en_US.UTF-8 \
    LC_ALL=en_US.UTF-8

LABEL io.k8s.description="Jmeter is a tool for testing and stress testing" \
      io.k8s.display-name="Jmeter controller" \
      io.openshift.tags="Jmeter,test,ci" \
      io.openshift.expose-services="1099:http" \
      io.openshift.s2i.scripts-url="image:///usr/libexec/s2i"

# Labels consumed by Red Hat build service
LABEL com.redhat.component="openshift-jmeter-4-controller-docker" \
      name="openshift3/jmeter-4-controller-rhel7" \
      version="4.0" \
      architecture="x86_64" \
      release="7"

# 60000 for slave agents to callback controller
EXPOSE 60000

COPY ./contrib/s2i/bin /usr/libexec/s2i

USER root
RUN mkdir /home/jmeter
ADD ./contrib/apache-jmeter-4.0 /home/jmeter

RUN ["/bin/bash", "-c", "chown -R 1001 /home/jmeter/ && \
                         chgrp -R 0 /home/jmeter/ && \
                         chmod -R g=u /home/jmeter/"]
USER 1001
ENTRYPOINT ["/usr/libexec/s2i/run"]
