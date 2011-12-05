#!/bin/bash

USER=`whoami`
QT_PATH=$(echo $(ls -d /tmp/QtJambi_*/lib))

cd $QT_PATH
chrpath -d *.so*
cd -

BASEDIR="$(pwd)/$(dirname $0)"
export P2PMPI_HOME="${BASEDIR}/lib/p2pmpi"
export PATH="${BASEDIR}/lib/p2pmpi/bin:${PATH}"
export CLASSPATH="${CLASSPATH}:${BASEDIR}/bin:"
export CLASSPATH="${CLASSPATH}:${BASEDIR}/lib/qtjambi-4.7.0.jar"
export CLASSPATH="${CLASSPATH}:${BASEDIR}/qtjambi-linux64-gcc-4.7.0.jar"
export CLASSPATH="${CLASSPATH}:${BASEDIR}/qtjambi-util-4.7.0.jar"
export CLASSPATH="${CLASSPATH}:${BASEDIR}/qtjambi-linux32-gcc-4.7.0.jar"
#export LD_LIBRARY_PATH="/tmp/QtJambi_${USER}_amd64_4.7.0_gcc-20100927-1542/lib/:${LD_LIBRARY_PATH}"
export LD_LIBRARY_PATH="${QT_PATH}:${LD_LIBRARY_PATH}"
"${BASEDIR}/lib/p2pmpi/bin/p2pmpirun" -n 3  air.Simulator

