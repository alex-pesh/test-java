#!/bin/bash
#alpine/java:21-jre \

docker container run -it --user root \
-v /home/alexander/Downloads/jextract-21:/opt/jextract-21 \
alpine:3.17 \
sh -c '/opt/jextract-21/bin/jextract' #-c "/opt/jextract-21/bin/java"