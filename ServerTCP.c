#include <stdio.h>
#include <netdb.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>

#include "SenderUDP.h"

#define MAX 2000
#define PORT 8888

int socket_desc, client_sock, client_size;
struct sockaddr_in server_addr, client_addr;
char server_message[MAX], client_message[MAX];

int createAndBind(beacon b) {
    // Clean Buffers
    memset(server_message, '\0', sizeof(server_message));
    memset(client_message, '\0', sizeof(client_message));

    // Create socket
    socket_desc = socket(AF_INET, SOCK_STREAM, 0);

    if (socket_desc < 0) {
        printf("Error while creating socket\n");
        return -1;
    }
    printf("Socket created successfully");

    //Set port and IP
    printf("%d\n", b.CmdPort);
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(b.CmdPort);
    server_addr.sin_addr.s_addr = inet_addr(b.IP);

    //Bind port and IP
    if(bind(socket_desc, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        printf("Couldn't bind to the port\n");
        return -1;
    }
    printf("Successfully bound\n");

    //Listen for clients
    if(listen(socket_desc, 1) < 0) {
        printf("Error while listening\n");
        return -1;
    }
    printf("\nListening for incomming connections......\n");

    //Accept a connection
    client_size = sizeof(client_addr);
    client_sock = accept(socket_desc, (struct sockaddr*)&client_addr, &client_size);

    if (client_sock < 0) {
        printf("Can't accept\n");
        return -1;
    }

    return 0;
}

int receiveMessage() {
    //Receive client message
    if (recv(client_sock, client_message, sizeof(client_message), 0) < 0){
        printf("Couldn't receive\n");
        return -1;
    }
    printf("%s\n", client_message);
    char* comp1 = "void GetLocalOS(char OS[16], int *valid)";
    char* comp2 = "void GetLocalTime(int *time, int *valid)";
    if (strncmp(client_message, comp1, 40) == 0){
        return 1;
    }
    if (strncmp(client_message, comp2, 40) == 0) {
        return 2;
    }
    ;
    return 0;
}

void respondToServer(char* buffer) {
    if (send(client_sock, buffer, strlen(buffer), 0) < 0){
        printf("Can't send\n");
        exit(1);
    }
}

void closeSockets() {
    close(client_sock);
    close(socket_desc);
}
