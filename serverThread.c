#include <arpa/inet.h>

#include <pthread.h>
#include <semaphore.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>

sem_t x, y;
pthread_t tid;
pthread_t writerthreads[100];
pthread_t readerthreads[100];
int readercount = 0;

void* reader(void* param){
    // Lock the semaphore
    sem_wait(&x);
    readercount++;

    if (readercount == 1) {
        sem_wait(&y);
    }

    sem_post(&x);

    printf("\n%d reader is inside", readercount);

    sleep(5);

    // Lock the semaphore
    sem_wait(&x);
    readercount--;

    if (readercount == 0) {
        sem_post(&y);
    }

    sem_post(&x);

    printf("\n%d Reader is leaving", readercount + 1);

    pthread_exit(NULL);
}

void* writer(void* param) {
    printf("\nWriter is trying to enter.");

    //Lock the semaphore
    sem_wait(&y);

    printf("\nWriter has entered");

    //Unlock the semaphore
    sem_post(&y);
    printf("\nWriter is leaving");
    pthread_exit(NULL);
}

int main() {
    int serverSocket, newSocket;
    struct sockaddr_in serverAddr;
    struct sockaddr_storage serverStorage;

    socklen_t addr_size;
    sem_init(&x, 0, 1);
    sem_init(&y, 0, 1);

    serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    serverAddr.sin_addr.s_addr = INADDR_ANY;
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(8888);

    //Bind socket
    bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr));

    //Listen at socket with 40 max connections
    if(listen(serverSocket, 50) == 0){
        printf("Listening\n");
    }
    else {
        printf("Error\n");
    }

    //Array for thread
    pthread_t tid[60];

    int i = 0;

    while (1) {
        addr_size = sizeof(serverStorage);

        //Extract the first connection of the queue
        newSocket = accept(serverSocket, (struct sockaddr*)&serverStorage, &addr_size);
        int choice = 0;
        recv(newSocket, &choice, sizeof(choice), 0);

        if (choice == 1) {
            //Create reader thread
            if (pthread_create(&readerthreads[i++], NULL, reader, &newSocket) != 0) {
                printf("Failed to create thread");
            }
        } else if (choice == 2) {
            //Create writer thread
            if (pthread_create(&writerthreads[i++], NULL, writer, &newSocket) != 0) {
                printf("Failed to create thread");
            }
        }

        if (i >= 50) {
            i = 0;

            while (i < 50) {
                //Suspend execution of the calling thread until the target thread terminates
                pthread_join(writerthreads[i++], NULL);
                pthread_join(readerthreads[i++], NULL);
            }

            i = 0;
        }
    }
    return 0;
}