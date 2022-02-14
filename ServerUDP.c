#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <arpa/inet.h>
#include <sys/socket.h>

#define BUFLEN 512
#define PORT 8888

void die(char *s){
    perror(s);
    exit(1);
}

int main(void) {
    struct sockaddr_in si_me, si_other;

    int s, i, slen = sizeof(si_other), recv_len;
    char buf[BUFLEN];

    //UDP Socket
    if((s=socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)) == -1){
        die("socket");
    }

    //zero out the structure
    memset((char *)&si_me, 0, sizeof(si_me));

    si_me.sin_family = AF_INET;
    si_me.sin_port = htons(PORT);
    si_me.sin_addr.s_addr = htonl(INADDR_ANY);

    //bind socket to port
    if (bind (s, (struct sockaddr*) &si_me, sizeof(si_me)) == -1){
        die("bind");
    }

    memset(buf,'\0',BUFLEN);

    //listen for data
    while (1) {
        printf ("Waiting for data...");
        fflush(stdout);

        //blocking call to try and receive some data
        if ((recv_len = recvfrom(s, buf, BUFLEN, 0, (struct sockadder *) &si_other, &slen)) == -1){
            die("recvfrom()");
        }

        //print details and data received
        printf("Received packet from %s:%d\n", inet_ntoa(si_other.sin_addr), ntohs(si_other.sin_port));
        printf("Data: %s\n", buf);

        if (sendto(s, buf, recv_len, 0, (struct sockaddr*) &si_other, slen) == -1){
            die("sendto()");
        }

        memset(buf,'\0',BUFLEN);
    }

    close(s);
    return 0;
}