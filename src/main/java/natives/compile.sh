#!/bin/sh

cd lib
g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux ./*.cpp && \
g++ -shared -fPIC -o libnatives.so ./*.o -lc && \
rm ./*.o
