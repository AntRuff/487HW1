OBJS	= agent.o SenderUDP.o ServerTCP.o
SOURCE	= agent.c SenderUDP.c ServerTCP.c
HEADER	= SenderUDP.h ServerTCP.h
OUT	= agent
CC	 = gcc
FLAGS	 = -g -c -Wall
LFLAGS	 = -lpthread

all: $(OBJS)
	$(CC) -g $(OBJS) -o $(OUT) $(LFLAGS)

agent.o: agent.c
	$(CC) $(FLAGS) agent.c -std=c99

SenderUDP.o: SenderUDP.c
	$(CC) $(FLAGS) SenderUDP.c -std=c99

ServerTCP.o: ServerTCP.c
	$(CC) $(FLAGS) ServerTCP.c -std=c99

clean:
	rm -f $(OBJS) $(OUT)
