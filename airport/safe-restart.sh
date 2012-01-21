#!/bin/sh
mpihalt
ant jar
stopSuperNode
sleep 2
runSuperNode
mpiboot
./run-mpi.sh

