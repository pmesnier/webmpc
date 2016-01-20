#!/bin/bash


sudo hostname "$(hostname | cut -c1-63)"
sed -e "s/^\\(127\\.0\\.0\\.1.*\\)/\\1 $(hostname | cut -c1-63)/" /etc/hosts | sudo tee /etc/hosts

/usr/bin/update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java 1
/usr/bin/update-alternatives --set java /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java

# check for Java installation
if type java; then
  echo $(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
 else
    echo "Java not found"
    exit 1
fi
