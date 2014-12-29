/*
 ============================================================================
 Name        : TcpClient.c
 Author      : daemon
 Version     : 1.0
 Copyright   : daemon
 Description : tcp client
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <unistd.h>
#include <netdb.h>
#include <errno.h>

int main(int argc, char* argv[])
{
	struct sockaddr_in addr;
	char buffer[200], read_buffer[1024];
	int bufferLen;
	ssize_t write_len, read_len;
	int ret = -1;

	int fd;
	struct hostent *host;
	int port;

	if(3 != argc)
	{
		fprintf(stderr, "Usage: %s hostname portnumber\a\n", argv[0]);
		exit(EXIT_FAILURE);
	}

	host = gethostbyname(argv[1]);
	printf("gethostbyname:%s\n", host->h_name);
	if(NULL == host)
	{
		fprintf(stderr, "gethostbyname error!\n");
		exit(EXIT_FAILURE);
	}

	port = atoi(argv[2]);
	printf("port:%s atoi:%d\n", argv[2], port);
	if(port < 0)
	{
		fprintf(stderr, "Usage: %s hostname portnumber\a\n", argv[0]);
		exit(EXIT_FAILURE);
	}

	fd	= socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
	printf("socket:%d\n", fd);
	if(-1 == fd)
	{
		perror("cannot create socket");
		exit(EXIT_FAILURE);
	}

	memset(&addr, 0, sizeof(struct sockaddr_in));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(port);
	addr.sin_addr = *((struct in_addr *)host->h_addr_list[0]);

	// connect
	ret = connect(fd, (const struct sockaddr *) &addr, sizeof(struct sockaddr_in));
	printf("connect:%d\n", ret);
	if(-1 == ret)
	{
		perror("connect failed");
		close(fd);
		exit(EXIT_FAILURE);
	}

	read_len = read(fd, read_buffer, 1024);
	printf("tcp client read len: %ld\n", read_len);
	if(-1 == read_len)
	{
		fprintf(stderr, "read error: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}
	read_buffer[read_len] = '\0';
	printf("tcp client read:%s\n", read_buffer);

	bufferLen = snprintf(buffer, sizeof(buffer), "Hello  tcp, bitch!\n");
	write_len = write(fd, buffer, bufferLen);
	printf("tcp client write len: %ld\n", write_len);

	shutdown(fd, SHUT_RDWR);

	// close
	close(fd);

	return EXIT_SUCCESS;
}
