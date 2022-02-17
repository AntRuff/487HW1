#include <stdio.h>
#include <netdb.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include "SenderUDP.h"

int connectAndBind(beacon b);
int receiveMessage();
void respondToServer();
void closeSockets();