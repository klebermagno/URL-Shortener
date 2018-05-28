#!/bin/bash
# Installing JAVA
# Authored by Filipe Goes
# Tested in Ubuntu Server 14.05
# Tested in CentOS 7.3


JAVA_URL=http://download.oracle.com/otn-pub/java/jdk/8u121-b13/e9e7ea248e2c4826b92b3f075a80e441/jdk-8u121-linux-x64.tar.gz
JAVA_FOLDERNAME=jdk1.8.0_121

if [[ $EUID -ne 0 ]]; then
   echo "This script must be run as root."
   exit 1
fi

echo "Downloading: $JAVA_URL..."
[ -e "$JAVA_FILENAME" ] && echo 'Java archive already exists.'
if [ ! -e "$JAVA_FILENAME" ]; then
  wget -O jdk.tar.gz --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" $JAVA_URL
  if [ $? -ne 0 ]; then
    echo "Not possible to download Java."
    exit 1
  fi
fi

echo "Installation..."
tar -xvf jdk.tar.gz

echo "Cleaning up..."
rm -f jdk.tar.gz
 
mkdir -p /usr/lib/jvm
mv ./$JAVA_FOLDERNAME /usr/lib/jvm/  
update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/$JAVA_FOLDERNAME/bin/java" 1 
update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/$JAVA_FOLDERNAME/bin/javac" 1
update-alternatives --install "/usr/bin/javaws" "javaws" "/usr/lib/jvm/$JAVA_FOLDERNAME/bin/javaws" 1
update-alternatives --install "/usr/bin/keytool" "keytool" "/usr/lib/jvm/$JAVA_FOLDERNAME/jre/bin/keytool" 1
chmod a+x /usr/bin/java
chmod a+x /usr/bin/javac
chmod a+x /usr/bin/javaws
chmod a+x /usr/bin/keytool
chown -R root:root /usr/lib/jvm/$JAVA_FOLDERNAME
chmod +x start.sh
ln -s /home/ubuntu/Encurtador/target/klebermagno-0.1.0.jar /etc/init.d/encurtador

echo "Done."