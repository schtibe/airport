#!/bin/bash

BASEDIR="$(pwd)/$(dirname $0)"
export P2PMPI_HOME="${BASEDIR}/lib/p2pmpi"
export PATH="${BASEDIR}/lib/p2pmpi/bin:${PATH}"
export CLASSPATH="${CLASSPATH}:${BASEDIR}/bin:"
export CLASSPATH="${CLASSPATH}:${BASEDIR}/lib/qtjambi-4.7.0.jar"
export CLASSPATH="${CLASSPATH}:${BASEDIR}/qtjambi-linux64-gcc-4.7.0.jar"
export CLASSPATH="${CLASSPATH}:${BASEDIR}/qtjambi-util-4.7.0.jar"
export CLASSPATH="${CLASSPATH}:${BASEDIR}/qtjambi-linux32-gcc-4.7.0.jar"
"${BASEDIR}/lib/p2pmpi/bin/p2pmpirun" -n 3  air.Simulator
