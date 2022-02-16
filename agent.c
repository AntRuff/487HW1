#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <time.h>

#include <arpa/inet.h>
#include <unistd.h>

#include <pthread.h>
#include <semaphore.h>

#include "SenderUDP.h"



/*typedef struct BEACON {
    int ID; // randomly generated during setup
    int StartUpTime; //Time when client starts
    int timeInterval; // Time period this beacon will be repeated
    char IP[4]; //IP address of the client
    int CmdPort; //the client listens to this port form cmd
} beacon;*/

void* clientthread(void* args){
    int client_request = *((int*) args);
    int network_socket;
    //Create socket stream
    network_socket = socket(AF_INET, SOCK_STREAM, 0);

    struct sockaddr_in server_address;
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = INADDR_ANY;
    server_address.sin_port = htons(8888);
    // Create socket connection
    int connection_status = connect(network_socket, (struct sockaddr*)&server_address, sizeof(server_address));

    if (connection_status < 0) {
        puts("Error\n");
        return 0;
    }

    printf("Connection established\n");

    //send data
    send(network_socket, &client_request, sizeof(client_request), 0);

    close(network_socket);
    pthread_exit(NULL);

    return 0;
}

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

}

void GetLocalTime(int *time, int *valid) {

}

void* CmdAgent() {
    
}

int main () {
    char* server = "127.0.0.1";
    void *a;
    char buffer[4];
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
    
    printf("%d\n", b.StartUpTime);
    pthread_t b_tid, c_tid;

    pthread_create(&b_tid, NULL, BeaconSender, &b);
    //pthread_create(&c_tid, NULL, CmdAgent, 0);
    pthread_join(b_tid, NULL);
    return 0;
}