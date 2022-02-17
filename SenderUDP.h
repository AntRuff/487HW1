#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <fcntl.h>
#include <errno.h>

typedef struct BEACON {
    int ID; // randomly generated during setup
    int StartUpTime; //Time when client starts
    int timeInterval; // Time period this beacon will be repeated
    char IP[16]; //IP address of the client
    int CmdPort; //the client listens to this port form cmd
} beacon;

int sendUDP(beacon b);