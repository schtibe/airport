# Airport Simulator

## What is it ?

This is a project at the Berne University of Applied Sciences
in the module Parallel Computing. Its purpose is to explore the
concepts of a distributed simulation implemented with the 
p2p-mpi framework. It simulates several airports and aicrafts that
travel between these airports.

## Usage

You have to copy `lib/p2pmpi/P2P-MPI.conf.sample` to 
`lib/p2pmpi/P2P-MPI.conf` and set the variable `SUPERNODE` to your
the IP address of your supernode (which is usually the local IP 
address if you're just testing this software)

Then following steps have to be taken to execute the software.

	$ cd airport/
	$ source env.sh # Set the environment
	$ ant jar
	$ runSuperNode
	$ mpiboot
	$ ./run-mpi.sh

## Further Info

For further information see the doc directory.

## License

BSD 3-Clause. See LICENSE for further details
