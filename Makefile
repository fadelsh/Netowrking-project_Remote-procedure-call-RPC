server: server.c 
	gcc server.c -o server -lpthread

clean:
	rm -f server server.o	