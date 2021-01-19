#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include<time.h>
#include <pthread.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/utsname.h>


#define TCPPORT 4321

int OScmdLen;

int receive_one_byte(int client_socket, char *cur_char)
{
    ssize_t bytes_received = 0;
    while (bytes_received != 1)
    {
        bytes_received = recv(client_socket, cur_char, 1, 0);
    }
    
    return 1;
}

int receiveFully(int client_socket, char *buffer, int length)
{
    char *cur_char = buffer;
    ssize_t bytes_received = 0;
    while (bytes_received != length)
    {
        receive_one_byte(client_socket, cur_char);
        cur_char++;
        bytes_received++;
    }
    
    return 1;
}

int toInteger32(char *bytes)
{
    int tmp = (bytes[0] << 24) +
    (bytes[1] << 16) +
    (bytes[2] << 8) +
    bytes[3];
    
    return tmp;
}

void GetLocalTime(char localTime[8], int *valid){


    if(*valid==0){
        localTime[6]='n'; //n== valid
        return;
       }
         
    time_t timenow = time(NULL);
        //srand(time(NULL));
         char* temp= ctime(&timenow);
        temp[strlen(temp)-1]= '\0';
        
       // char real[8];
        int j=11;
        for (int i=0; i<8; i++){
            if (j==13 || j==16){
                j++;
            }
            localTime[i]=temp[j];
            j++;
        }
       if(*valid==1){
        localTime[6]='y'; //y== valid
       }
         else{
        localTime[6]='n'; //n=invalid
        }

        localTime[7]='\0';
}


void GetLocalOS(char OS[30], int *valid){

     if(*valid==0){
          OS[13]='n'; //n== valid
          return;
         }

           struct utsname details;
           int ret = uname(&details);
                strcat(details.sysname, " ");
                strcat (details.sysname,details.release);
                strcat(OS,details.sysname);
                OScmdLen=strlen(OS);

      if(*valid==1){
          OS[13]='y'; //y== valid
         }
      else{
          OS[13]='n'; //n=invalid
         }
}

void *RPC(void* arg){

 int server_socket = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
    printf("server_socket = %d\n", server_socket);
    struct sockaddr_in sin;
    memset(&sin, 0, sizeof(sin));
    sin.sin_family = AF_INET;
    sin.sin_port = htons(4321);
    sin.sin_addr.s_addr= inet_addr("127.0.0.1");

    if (bind(server_socket, (struct sockaddr *)&sin, sizeof(sin)) < 0){
        printf("bind error\n");
    }
 listen(server_socket, 5);
    int counter = 0;

    while (1){

        struct sockaddr client_addr;
        unsigned int client_len;
        
        printf("TCP accepting ....\n");
        int client_socket = accept(server_socket, &client_addr, &client_len);
        printf("request %d comes ...\n", counter++);

//Receive a command
    char header [104];
    receiveFully(client_socket, header, 104);

    char checkCmd[13];

    for(int i=0; i<12;i++){
        checkCmd[i]=header[i];
    }
    checkCmd[12]='\0';

    int validCmd;
   // printf(" Result is %d \n ", strcmp("GetLocalTime", checkCmd));
    if(strcmp("GetLocalTime", checkCmd)!=0 && strcmp("GetLocalOS", checkCmd)!=0){
        validCmd=0;
    }
    else{
        validCmd=1;
    }

    char buf[4];

    char tmp[109];
    for(int i=0; i<104;i++){
        tmp[i]=header[i];
    }
   
//Execute the command

if(strcmp("GetLocalTime", checkCmd)==0){
   char localTime[8];

   GetLocalTime(localTime, &validCmd);

if(validCmd==1){
   tmp[12]=localTime[0]; //H
   tmp[13]=localTime[1]; // H
   tmp[14]=localTime[2]; //M
   tmp[15]=localTime[3];//M
   tmp[16]=localTime[4];//S
   tmp[17]=localTime[5];//S
}
   tmp[18]=localTime[6]; //Valid


    tmp[104]=buf[0];
    tmp[105]=buf[1];
    tmp[106]=buf[2];
    tmp[107]=buf[3];

    send (client_socket,tmp, 109,0);
}


else if (strcmp("GetLocalOS", checkCmd)==0){

    char localOS [30];
    GetLocalOS(localOS,&validCmd);

    int j=10;
if(validCmd==1){

    
    for(int i=0; i<OScmdLen;i++){
        tmp[j]=localOS[i];
        j++;
    }
  
}
    tmp[j]=localOS[OScmdLen];

    tmp[104]=buf[0];
    tmp[105]=buf[1];
    tmp[106]=buf[2];
    tmp[107]=buf[3];


//Send the command back
   send (client_socket,tmp, 109,0);
   
}

else{
    char localTime[8];

   GetLocalTime(localTime, &validCmd);
   tmp[18]=localTime[6]; //Valid


    tmp[104]=buf[0];
    tmp[105]=buf[1];
    tmp[106]=buf[2];
    tmp[107]=buf[3];

//Send the command back
    send (client_socket,tmp, 109,0);
}
    }
    return NULL;
}

int main() {
    
    //When a connection arrived, launch a thread to process a command as follows
    pthread_t CmdProcessor;
    int launchFirstT=pthread_create(&CmdProcessor,NULL,RPC, NULL);
    
    pthread_join( CmdProcessor, NULL);

//Wait for a connection
    for(;;){

    }
 
    exit(0);
    
    return 0;
}
