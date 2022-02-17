#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/utsname.h>
#include <time.h>

#include <arpa/inet.h>
#include <unistd.h>

#include <pthread.h>
#include <semaphore.h>

//#include "SenderUDP.h"
#include "ServerTCP.h"


/*typedef struct BEACON {
    int ID; // randomly generated during setup
    int StartUpTime; //Time when client starts
    int timeInterval; // Time period this beacon will be repeated
    char IP[4]; //IP address of the client
    int CmdPort; //the client listens to this port form cmd
} beacon;*/

void* BeaconSender(void* args) {
    beacon b = *((beacon*) args);
    //printf(b.IP);

    //printf("%d\n", b.CmdPort);

    /*if(connectToServer(b.CmdPort, b.IP) < 0){
        printf("Error connecting to server.");
        pthread_exit(NULL);
    }*/
    while (1){
        if(sendUDP(b) < 0){
            printf("Error sending the beacon.");
            pthread_exit(NULL);
        }
        sleep(60);
    }
}

void GetLocalOS(char OS[16], int *valid) {
    struct utsname name;
    if (uname(&name)) {
        *valid = 0;
        return;
    }
    strcpy(OS, name.sysname);
    *valid = 1;
}

void GetLocalTime(int *localtime, int *valid) {
    *localtime = (int) time(0);
    *valid = 1;
}

void* CmdAgent(void* args) {
    beacon b = *((beacon*) args);
    
    if (createAndBind(b) < 0){
        printf("Errors with TCP socket.");
        exit(1);
    };
    while(1){
        int cmd = receiveMessage();
        printf("%d\n", cmd);
        char OS[16];
        int valid;
        int localtime;
        switch (cmd) {
            case 1: 
                GetLocalOS(OS, &valid);
                if (valid){
                    respondToServer(OS);
                }
                else { 
                    respondToServer("There was an error retreiving the OS of the agent.");
                    }
                break;
            case 2:
                GetLocalTime(&localtime, &valid);
                if (valid) {
                    char buffer[4];
                    respondToServer(memcpy(buffer, localtime, sizeof(localtime)));
                }
                else { respondToServer("There was an error retreiving the time of the agent.");}
                break;
            default:
                //Error
                break;
        }
    }
    closeSockets();
}

int main () {
    char* server = "127.0.0.1";
    void *a;
    inet_pton(AF_INET, server, a);
    srand(time(NULL));
    beacon b;
    b.ID = rand() % 10000;
    b.StartUpTime = (int) time(0);
    inet_ntop(AF_INET, a, b.IP, INET_ADDRSTRLEN);
    b.timeInterval = 60;
    b.CmdPort = 8888;

    //printf(b.IP);
    //memcpy(&b.IP, &buffer, sizeof(buffer));
    //printf(b.IP);
    pthread_t b_tid, c_tid;

    pthread_create(&b_tid, NULL, BeaconSender, &b);
    pthread_create(&c_tid, NULL, CmdAgent, &b);
    pthread_join(b_tid, NULL);
    pthread_join(c_tid, NULL);
    return 0;
}