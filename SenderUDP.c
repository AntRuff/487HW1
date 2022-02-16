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

struct sockaddr_in si_other;
int s, i, slen=sizeof(si_other);
char *buffer;


void die(char *s){
    perror(s);
}

int sendUDP(beacon b){

    if ((s = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)) == -1){
        die("socket");
        return -1;
    }

    memset((char *) &si_other, 0, sizeof(si_other));
    si_other.sin_family = AF_INET;
    si_other.sin_port = htons(b.CmdPort);
    //printf(IP);

    if (inet_pton(AF_INET, b.IP, &si_other.sin_addr) == 0){
        fprintf(stderr, "inet_aton() failed\n");
        return -1;
    }

    
    while(1) {
        printf("%d\n", b.StartUpTime);
        buffer = (char*) malloc(sizeof(beacon));
        int i;
        memcpy(buffer, (const unsigned char*)&b, sizeof(b));

        for(i = 0; i < sizeof(b); i++){
            printf("%02X ", buffer[i]);
        }
        printf("\n");

        if (sendto(s, buffer, sizeof(beacon), 0 , (struct sockaddr*)&si_other, sizeof(si_other)) < 0){
            die("SendTo()");
            return -1;
        }
        memset(buffer, '\0', sizeof(beacon));
        sleep(60);
    }
}