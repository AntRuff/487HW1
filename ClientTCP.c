#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#define MAX 2000
#define PORT 8888

int main(void) {
    int socket_desc;
    struct sockaddr_in server_addr;
    char server_message[MAX], client_message[MAX];

    //Clean Buffers
    memset(server_message, '\0', sizeof(server_message));
    memset(client_message, '\0', sizeof(client_message));

    //Create socket
    socket_desc = socket(AF_INET, SOCK_STREAM, 0);
    
    if (socket_desc < 0) {
        printf("Unable to create socket\n");
        return -1;
    }
    printf("Socket created successfully\n");

    //Set port and IP the same as serverside
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = inet_addr("127.0.0.1");

    //Send connection request to server
    if (connect(socket_desc, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0){
        printf("Unable to connect\n");
        return -1;
    }
    printf("Connected with server successfully\n");

    //User input
    printf("Enter message: ");
    gets(client_message);

    //Send message to server
    if (send(socket_desc, client_message, strlen(client_message), 0) < 0){
        printf("Unable to send message\n");
        return -1;
    }

    //Receive the server's response:
    if (recv(socket_desc, server_message, sizeof(server_message), 0) < 0){
        printf("Error while receiving the server's message\n");
        return -1;
    }
    printf("Server's response: %s\n", server_message);

    //close
    close(socket_desc);

    return 0;
}
