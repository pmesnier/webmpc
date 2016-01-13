#!/bin/bash

sudo add-apt-repository -y ppa:openjdk-r/ppa
# deb repo should live
sudo apt-get update
sudo apt-get install -y openjdk-8-jdk

/usr/bin/update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java 1
/usr/bin/update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java

# check for Java installation
if type java; then
  echo $(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
 else
    echo "Java not found"
    exit 1
fi
