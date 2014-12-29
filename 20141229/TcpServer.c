/*
 ============================================================================
 Name        : TcpServer.c
 Author      : daemon
 Version     : 1.0
 Copyright   : daemon
 Description : tcp server
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <unistd.h>
//opt
#include <netinet/tcp.h>

#define PORT 3333
#define BACKLOG 10

int main()
{
	int ret = -1;
	struct sockaddr_in addr, remote_addr;
	char buffer[1024];
	ssize_t recSize = -1;
	unsigned int sin_size;

	//opt
	int opt;
	unsigned int optLen;

	char send_buffer[] ="Hello, you are connected to server!\n";

	// socket
	int fd = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
	printf("socket:%d\n", fd);
	if(-1 == fd)
	{
		perror("can not create socket");
		exit(EXIT_FAILURE);
	}

	// get option
	optLen = sizeof(opt);
	ret = getsockopt(fd, IPPROTO_TCP, TCP_MAXSEG, (char*)&opt, &optLen);
	printf("TCP_MAXSEG TCP最大段大小:%d\n", opt);

	ret = getsockopt(fd, SOL_SOCKET, SO_SNDBUF, (char*)&opt, &optLen);
	printf("SO_SNDBUF 发送缓冲区大小:%d\n", opt);

	ret = getsockopt(fd, SOL_SOCKET, SO_RCVBUF, (char*)&opt, &optLen);
	printf("SO_RCVBUF 接收缓冲区大小:%d\n", opt);

	memset(&addr, 0, sizeof(struct sockaddr_in));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(PORT);
	addr.sin_addr.s_addr = INADDR_ANY;

	// bind
	ret = bind(fd, (const struct sockaddr*) &addr, sizeof(struct sockaddr_in));
	printf("bind:%d\n", ret);
	if(-1 == ret)
	{
		perror("error: bind failed");
		close(fd);
		exit(EXIT_FAILURE);
	}

	// listen
	ret = listen(fd, BACKLOG);
	printf("listen:%d\n", ret);
	if(-1 == ret)
	{
		perror("error: listen failed");
		close(fd);
		exit(EXIT_FAILURE);
	}

	for(;;)
	{
		// accept
		int connect_fd;
		memset(&remote_addr, 0, sizeof(remote_addr));

		connect_fd= accept(fd, (struct sockaddr *)&remote_addr, &sin_size);
		printf("accept:%d\n", connect_fd);
		if(connect_fd < 0)
		{
			perror("error accept failed");
			continue;
		}

		printf("received a connection from %s\n", inet_ntoa(remote_addr.sin_addr));

		if(!fork())
		{
			ret =send(connect_fd, send_buffer, strlen(send_buffer), 0);
			printf("send:%d\n", ret);
			if(-1 == ret)
			{
				perror("send error!\n");
				close(connect_fd);
				exit(EXIT_SUCCESS);
			}

			while(1)
			{
				recSize = read(connect_fd, buffer, sizeof(buffer));
				printf("tcp server read len: %ld\n", recSize);
				if(recSize <= 0)
				{
					break;
				}
				else if(recSize > 0)
				{
					buffer[recSize] = '\0';
					printf("tcp server read:%s\n", buffer);
				}
			}
		}

//		shutdown(connect_fd, SHUT_RDWR);
		close(connect_fd);
	}

	// close
	close(fd);
	return EXIT_SUCCESS;
}
