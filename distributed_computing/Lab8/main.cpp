#include <iostream>

#include <windows.h>
#include "mpi/mpi.h"

using namespace::std;
int myRank;
int numProcs;
char host[256];

int main(int argc, char* argv[]) {
  MPI_Init(&argc, &argv);
  MPI_Comm_size(MPI_COMM_WORLD, &numProcs); // number of processes involved in run
  MPI_Comm_rank(MPI_COMM_WORLD, &myRank); // my process id: 0 <= myRank < numProcs
  cout << "Process " << myRank << " " << numProcs << " is running" << endl;
  MPI_Finalize();

  return 0;

}