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

# 1099 for rmi, 50000 for slave agents
EXPOSE 1099 50000

#RUN yum-config-manager --disable epel >/dev/null || : && \
#    yum-config-manager --enable rhel-7-server-ose-onlineint-rpms || : && \
    #INSTALL_PKGS="dejavu-sans-fonts wget rsync gettext git tar zip unzip openssl bzip2 dumb-init  java-1.8.0-openjdk java-1.8.0-openjdk.i686 java-1.8.0-openjdk-devel java-1.8.0-openjdk-devel.i686 atomic-openshift-clients" && \
#    INSTALL_PKGS="python wget openssl bzip2 dumb-init  java-1.8.0-openjdk java-1.8.0-openjdk.i686 java-1.8.0-openjdk-devel java-1.8.0-openjdk-devel.i686 atomic-openshift-clients" && \    
#    yum install -y $INSTALL_PKGS && \
    # have temporarily removed the validation for java to work aroun known rhel 7.5 problem fixed in fedora; jupierce and gmontero are working with
    # the requisit folks to get that addressed ... will switch back to rpm -V $INSTALL_PKGS when that occurs
    #rpm -V dejavu-sans-fonts wget rsync gettext git tar zip unzip openssl bzip2 dumb-init  atomic-openshift-clients && \
#    yum clean all  && \
#    localedef -f UTF-8 -i en_US en_US.UTF-8

#COPY ./contrib/openshift /opt/openshift
#COPY ./contrib/jenkins /usr/local/bin
COPY ./contrib/s2i/bin /usr/libexec/s2i

#ADD release.version /tmp/release.version

# Remove the base-plugins.txt file because it's only used for Centos
# and its presence in the rhel image is confusing.
USER root
RUN mkdir /home/jmeter 
ADD ./contrib/apache-jmeter-4.0 /home/jmeter    

RUN ["/bin/bash", "-c", "chown -R 1001 /home/jmeter/ && \
                         chgrp -R 0 /home/jmeter/ && \
                         chmod -R g=u /home/jmeter/"]   

USER 1001
#ENTRYPOINT ["/usr/bin/dumb-init", "--"]
#CMD ["/usr/libexec/s2i/run"]
ENTRYPOINT ["/usr/libexec/s2i/run"]