# Process Synchronization 1

Critical Section에 동시에 접근하면 문제가 발생할 수 있기 때문에 공유 데이터 접근 코드 이전에
entry section을 넣어 Lock을 걸어서 여러 프로세스가 동시에 크리티컬 섹션에 들어가는 것을 막고 끝나면 Lock을 풀어서 크리티컬 섹션에 다른 프로세스가 들어갈 수 있게 한다.
## Initial Attemps to Solve Problem
- 두 개의 프로세스가 있다고 가정 P0 P1
- 프로세스들의 일반적인구조
``` C
do{
    entry section
    critical section
    exit section
    remainder section
}while(1);
````
- 공유 데이터 접근하는 critical section 전에 entry section에서 락을 건다. 여러 프로세스가 크리티컬 섹션에 들어가는 것을 막는다.
- 또 critical section에서 나올 때는 exit section에서 락을 풀어주어 다른 프로세스도 접근 가능하도록 한다.

## 프로그램적 해결법의 충족 조건

- 크리티컬 섹션 문제를 해결하기 위해서 **충족해야할 조건**
- Mutual Exclusion (상호 배제)
    - 하나의 프로세스가 critical section 부분을 수행 중이면 다른 모든 프로세스들은 그들의 critical section에 들어가면 안 된다.
- Progress(진행)
    - 아무도 critical section에 있지 않은 상태에서 critical section에 들어가고자 하는 프로세스가 있다면 critical section에 들어가게 해주어야 한다.(
    - 당연한 이야기 같지만 둘이 동시에 들어가 있는 것을 막고자 하는 것이 둘 다 못들어가게 할 때가 있다.(코드 잘 못 짰을 때)
- Bounded Waiting
    - 프로세스가 critical section에 들어가려고 요청한 후부터 그 요청이 허용될 때까지 다른 프로세스들이 critical section에 들어가는 횟수에 한계가 있어야 한다.
    - 특정 프로세스 입장에서 크리티컬 섹션을 너무 오래 못들어가는 starvation 현상이 생기지 않도록 해야 한다. 
    - critical section에 들어 가고자 하는 프로세스가 3개 일때 2개만 번갈아 가며 들어가는 경우가 이 조건을 위반하는 경우이다.

## Algorithm1
- 단일 인스트럭션으로 수행된다면 문제가 발생하지 않지만 고급 언어의 코드들이 여러 줄의 인스트럭션으로 이루어져 있고 도중에 CPU를 빼앗기게 되어서 문제가 발생
```c
//P0의 경우
do{
    while(turn !=0) //자기 차례인 turn == 인지 확인 0이면 들어감
    critical section
    turn=1;//나올 때 다음 차례로 바꾸어줌
    remainder section
}while(1)

```
- critical section에 들어가기 전에 while문을 돌면서 체크 
- turn을 배정 받아서 자신의 turn이 올 때까지 들어가지 않고 대기한다.
- 자기 차례에만 들어가기 때문에 첫 조건 성립한다.
- mutual exclusion은 만족 
- 아무도 섹션에 없는데도 불구하고 섹션에 들어가지 못하는 문제 발생 한다.(progress 조건 만족 x)
    - 상대방이 들어가서 나와야만 들어갈 수 있다.
    - 프로세스 마다 critical section에 들어가고 싶은 빈도가 다르기 마련인데 다른 프로세스가 들어갔다가 나와야 들어갈 수 있기에 비효율적

## Algorithm2
- 프로세스마다 flag를 가지고 있다.
    - 처음에는 모두 false
- flag를 통해 본인이 critical section에 들어가고자 하는 의사를 표시한다.
- critical section에 들어가려면 flag를 true로 하고 상대방도 flag를 확인한다.
- 상대방의 flag가 true이면 대기한다 .
```c
do{
    flag[i]=true;     //들어가고 싶은 의사 표현
    while(flag[j]);   //다른 들어가고 싶은 프로세스가 없는지 확인하고 없다면
    critical section  //들어감 
    flag[i]=false;    //나올 때 의사 false로
    remainder section
}while(1)
```
- 상대방의 flag가 false이면 그 때 들어가고 나올 때 자신의 flag를 false로 바꾸어주어 다른 프로세스가 critical section에 들어갈 수 있도록 한다.
- flag = true로 바꾼 상태에서 cpu 뺏기고 다른 프로세스도 flag=true 만들면 아무도 못 들어감.(Progress(진행) 만족 x)
- critical section에 들어가서 cpu 뺏긴 경우 서로 눈치만 살피다가 아무도 못 들어가는 문제가 발생한다.

## Algorithm3

- Peterson’s Algorithm
- turn과 flag를 모두 사용한다.
- 들어가고자 할 때 flag를 true로 표시한 후 turn을 상대방 turn으로 바꾸어 놓는다.
- 그 후 상대방의 flag가 true 이고 동시에 turn이 상대방 turn일 때만 대기 한다.
- 위의 경우를 제외한 경우에는 critical section에 들어간다.
- 나올 때 flag를 false로 바꿔서 다른 프로세스가 critical section에 들어갈 수 있도록 한다.
- 중간에 cpu를 뺏겨도 위의 3 조건을 모두 만족한다.
```c
do{
    flag[i]=true;     //들어가고 싶은 의사 표현
    turn = j;
    while(flag[j] && turn == j);   //상대방이 들어가고 싶다는 의사도 표현하고 또한 차례도 상대방 차례일 때만 대기한다.
    critical section  //들어감 
    flag[i]=false;    //나올 때 의사 false로
    remainder section
}while(1)
```
- 조건 확인에만 memory와 cpu를 사용하는 **Busy Waiting**문제가 있다.

위와 같은 복잡한 알고리즘이 나온 이유는 여러 개의 cpu 인스트럭션으로 구성된 고급 언어의 인스트럭션 수행 중 cpu를 뺏기는 경우를 고려하였기 때문이다.

## Synchronization Hardware(하드웨어적인 해결법)

- 하드웨어적으로 Test& modify(읽고, 쓰는)를 atomic하게 수행할 수 있도록 지원하는 경우 위의 복잡한 소프트웨어적 알고리즘 없이 간단하게 문제를 해결할 수 있다.

```c
do{
    while(Test_and_Set(lock));   //lock이 0이라면 lock을 1로 만들고 들어가는 것 모두 하나의 인스트럭션이므로 도중에 뺏길일 없어 안심하고 바로 들어가고,
    critical section             //그러나 lock이 1이라면 대기하면서 기다린다.
    lock = false;    //
    remainder section 
}while(1)
```

# Process Synchronization 2

## Semaphore S

- 추상 자료형이다.
- 추상 자료형은 컴퓨터에서 실제 구현되는 자료형과는 별개로 논리적으로 정의한 자료형이다.
- 앞의 Process Synchronization 해결 방식들을 추상화 시킨 것이다.
- 공유 자원을 획득하고 반납하는 과정을 처리한다.
- 정수 변수를 가진다.
- 이 자료형에는 **P연산**과 **V연산** 2가지 **atomic 연산**이 정의되어 있다.
    - P연산이 Lock을 걸고 공유 자원의 획득하고, V연산이 반납하고 Lock을 푸는 과정을 수행한다.
- Semaphore S가 가진 정수 변수는 자원의 수를 의미하고, S가 0이하이면 자원이 모두 사용 중이므로 while문을 돌며 대기한다.
    - 총 자원이 5개일 때, 어떤 프로세스가 P연산을 하면 S=4가 된다.
    - V연산을 하면 다시 5개로
- 변수가 양수가 되면 1을 빼고 critical section에 들어가서 자원을 획득하고, 자원 사용 완료 후 V연산을 통해서 변수 값을 1증가 시키고 자원을 반납한다.
    - 자원이 있으면 1을 빼고 자원을 획득하고, 없으면 while문을 돌며 대기한다.
    - Busy Waiting 문제는 여전히 존재
- 프로그래머가 일일이 코딩하지 않고 제공되는 추상 자료형인 Semaphore를 가지고 프로그래밍 하면 훨씬 간단하다.
``` c
semaphore mutex; // initially 1: 1개의 프로세스만이 critical section에 들어갈 수 있다.

do{
    P(mutex); //양수이면 자원 획득+lock 아니면 대기
    critical section
    V(mutex); //세마포어 변수 +1
    remainder section
}while(1)
```
- busy-wait는 효율적이지 못함(=spin lock)
- **block &  sleep** 방식의 구현(=sleep lock)
    - cpu 낭비하며 대기 하지 않고, blocked 상태로 두는 것 
- 추상적인 자료형으로 실제 P연산과 V연산을 구현하는 것은 각 시스템에 맞게 구현해야 한다. 
- semaphore를 다음과 같이 정의
``` c
typedef struct
{   int value;
    struct process *L;   //process wait queue
}semaphore;
```

## Critical Section of *n* Process

### Busy-wait

- 다른 프로세스가 자원을 사용하는 경우 while문을 돌면서 대기한다.
- spin lock 문제가 발생되어 cpu, 메모리의 낭비가 발생한다 .

### Block & Wakeup

- 공유 자원을 획득할 수 없다면 sleep상태로 바꾸어 자원의 낭비를 막는 방법이다.
- 커널은 자원을 획득하지 못하여 **block을 호출한 프로세스**를 **suspend** 시키고, **이 프로세스의 PCB**를 semaphore에 대한 **wait queue**에 넣는다.
- **block된 프로세스**를 **wakeup**시켜서 **프로세스의 PCB**를 **ready queue**로 옮긴다.
- 구체적으로,  p연산에서 s.value의 값을 1빼준 후 그 값이 0보다 작으면 자원을 다른 프로세스가 이미 획득한 것이기 때문에 s.L에 프로세스를 연결 시킨 후 block 시킨다.
- 자원을 다 쓴 프로세스가 v연산에서 s.value를 증가시킨다. 증가된 값이 0이하라는 것은 어떤 프로세스가 자신의 작업을 기다리며 block된 상태이기 때문에 S.L에 block된 프로세스를 wakeup 시켜준다.

- **Block & Wakeup**방식 구현방법
``` c
//P(s)
S.value--;
if(S.value<0) //자원이 없다면
{   add this process to S.L; //semaphore를 위한 wait queue로 
    block(); // 그리고 블락 상태로 
}

//V(S) 
S.value++;
if(S.value<=0) //프로세스가 이미 들어올 때 S.value-1 했기 때문에 자원을 반납했음에도 불고하고 0이하라면 wait queue에서 blocked된 상태에서 기다리는 프로세스가 있을 것 이므로
{   remove a process P from S.L; //잠들어 있는 프로세스를 wait queue에서 빼고
    wakeup(P); //깨워준다 
}

```

### Busy-wait vs Block/wakeup

- Critical section의 길이가 긴 경우 Block/wakeup이 적당하다.
- Critical section의 길이가 짧은 경우 Block/wakeup 오버헤드가 Busy-wait의 오버헤드보다 더 커질 수 있다.
    - 프로세스를 block시키고 wakeup하는 것도 부담이 크다. 
- 일반적으로는 Block/wakeup이 더 좋은 방법이다.(cpu를 효율적으로 이용) 

## Semaphore의 두 가지 타입

- Counting semaphore
    - 도메인이 0이상인 임의의 정수값이다.
    - 주로 resource counting에 사용된다.
    - 자원의 개수가 여러 개 여분이 있는 경우이다.

- Binary semaphore(자원이 1개인 경우) 
    - 0또는 1 값만 가질 수 있는 semaphore이다.
    - 주로 mutual exclusion에 사용한다.

## Deadlock & Starvation

- Deadlock
    - 둘 이상의 프로세스가 서로 상대방에 의해 충족될 수 있는 event를 무한히 기다리는 형상이다.
    - 자원 획득 순서를 맞춰 주면 문제를 해결할 수 있다. 둘 다 S먼저 Q다음에 얻게 하면 문제 해
![데드락](https://user-images.githubusercontent.com/62707891/181908337-180c6fae-75ca-43cd-86b3-983dde2b91fd.png)
    
- Starvation
    - 특정 프로세스만이 자원을 독점하여 어떤 프로세스는 자원을 가질 수 없을 때 생기는 문제이다.

    
# Process Synchronization 3

## Classical Problems of Synchronization

### 1. Bounded-Buffer Problem(Producer-Customer Problem)

- 생산자 프로세스
    - buffer에 데이터 저장하는 프로세스.
    - 빈 buffer가 있는지 확인하고 없으면 대기한다.
        - 비어있는 buffer가 없이 꽉 차있다면 다음 소비자 프로세스가 와서 buffer를 비울 때까지 대기한다.
    - 빈 buffer에 동시에 데이터 집어 넣을 때 문제가 생긴다. 
    - 공유 데이터에 Lock을 건 후 Empty buffer에 데이터 입력 및 buffer를 조작한다.
    - 데이터 집어 넣기 끝나면 Lock을 푼다.
    - 다음 비어있는 buffer위치 포인트 하기.
    - Full Buffer를 하나 증가시킨다.
   
- 소비자 프로세스
    - buffer에서 데이터를 꺼내오는 프로세스.
    - full buffer가 있는지 확인하고 없으면 대기한다.
        - full buffer가 없다면 생산자가 채워 줄 때까지 대기.
    - 공유데이터에 Lock을 건다.
    - Full buffer에서 데이터 꺼내고 buffer를 조작.
    - Lock을 푼다.
    - Empty buffer를 하나 증가시킨다.
- Shared data
    - buffer 자체 및 buffer 조작 변수(Empty /full buffer의 시작 위치)
    - lock을 걸고 푸는 과정 필요
- Synchronization variables
    - mutual exclusion, binary semaphore
        - lock을 걸고 푸는 과정 필요
    - 자원 세는 용도, 정수형 semaphore
        - 생산자 입장에서 빈 buffer 수
        - 소비자 입장에선 full buffer 수  
- Mutual Exclusion
    - binary semaphore를 사용하여 shared data의 mutual exclusion을 할 수 있다.
- Resource Count
    - integer semaphore를 사용하여 남은 full/empty buffer의 수 표시한다.
``` c
//producer
do{
    produce an item x
    
    P(empty); //빈 버퍼 수 없다면 대기
    P(mutex);//락 걸린 상태 확인
    
     add x to buffer  //버퍼 채우기
     
    V(mutex); // 락 해제
    V(full); // 가득 찬 버퍼 수 하나 올리기
    
}while(1);

//consumer
do{
    produce an item x
    
    P(full); //full 버퍼 수 없다면 대기
    P(mutex);//락 걸린 상태 확인
    ...
    remove an item from buffer to y  //데이터 꺼내가기
    ...
    V(mutex); // 락 해제
    V(empty); // 빈 버퍼 수 하나 올리기
    ...
    consume the item y
    ...
}while(1);
    
```

### 2. Readers - Writers Problems

- DB에 write하는 프로세스와 read하는 프로세스가 있다.
- 한 프로세스가 DB에 write 중일 때는 다른 프로세스가 접근하면 안된다.
- read는 동시에 여럿이 해도 된다.
- Solution
    - Writer가 DB에 접근 허가를 아직 얻지 못한 상태에서는 모든 대기 중인 Reader들을 다 DB에 접근하게 해준다.
    - Writer는 대기 중인 Reader가 하나도 없을 대 DB 접근이 허용된다.
    - 일단 Writer가 DB에 접근 중이면 Reader의 접근이 금지된다.
    - Writer가 DB에서 빠져나가야만 Reader의 접근이 허용된다.
    - 모든 Reader가 작업하는 것을 기다려야 하기 때문에 starvation문제 발생 가능하다.
- Shared data
    - DB 자체 (db라는 binary semaphore 사용 )
    - int readcount : 현재 DB에 접근 중인 Reader의 수(mutex라는 binary semaphore 사용)
- Synchronization variables
    - mutex : 공유 변수 readcount를 접근하는 코드(critical section)의 mutual exclusion 보장을 위해 사용한다.
    - db : Reader와 writer가 공유 DB자체를 올바르게 접근하도록 하는 역할을 수행한다.
- writer가 기다리는 시간이 매우 길어지는 starvation 문제가 발생할 수 있다.
    - writer를 aging 기법 이용해서 해결 가능할 듯. 

``` c
//writer
P(db);

writing DB is performed

V(db);


//Reader
P(mutex); //readcount도 공유 변수 이미르 lock 걸어줌 
readcount++; //몇 명이 읽고 있나?
if(readcount == 1) P(db); // writer block, 1 이상이여서 누군가 읽고 있다면 이미 lock걸린 상태, 읽기만 하면 됨
V(mutex);

reading DB is performes

P(mutex) //readcount도 공유 변수 이미르 lock 걸어줌 
readcount--;
if (readcount ==0) V(db); //마지막으로 읽고 나간 프로세스라면 lock을 풀어줘서 쓰기 가능하게 만든다. 
V(mutex) 
```

### 3. Dining-Philosophers Problem(식사하는 철학자 문제)
![dinig](https://user-images.githubusercontent.com/62707891/181920674-7f6190ad-1548-473b-90c5-8fe1dd09f25f.png)
``` c
//데드락 밯생하는 잘 못된 코드
do{
    P(chopstick[i]); //왼 젓가락 확보
    P(chopstick[(i+1) % 5]); //오른쪽 젓가락 확보 
    ...
    eat();
    ...
    V(chopstick[i]);
    V(chopstick[(i+1) % 5]);
    ...
    think();
    ...
}while(1);
```

- 위 코드의 문제점
    - 모두 왼쪽 젓가락을 먼저 들어서 Deadlock 가능성이 있다.
- 해결 방안
    - 4명의 철학자만이 테이블에 동시에 앉을 수 있도록 한다.
    - 젓가락을 두 개 모두 집을 수 있을 때에만 젓가락을 집을 수 있게 한다.
    ```c
    enum {thinking, hungry, eating} state[5]; //철학자의 상태
    semaphore self[5]=0; //두 젓가락 들 수 있는 권한 1이면 ok
    semaphore mutex=1; //철학자의 상태(공유 변수)에 접근을 제한하기 위한  Synchronization variables
    
    
    // 철학자 1
    do{ pickup(i); 
        eat();
        putdown(i);
        think();
    }while(1)
    
    // putdown
    void putdown(int i){
        P(mutex); 
        state[i]] = thinking;
        test((i+4) % 5); //내 좌우 철학자가 배고픈데 못 먹고 있는지 테스트 이 테스트로 아래에서 대기 중인 P(self[i]) 실행 가능 
        test((i+1) % 5);
        V(mutex);
    }
    
    // pickup
    void pickup(int i){
        P(mutex); //철학자의 상태 락
        state[i]] = hungry;
        test(i); // 젓가락 둘 다 잡을 수 있는지 테스트
        V(mutex); 
        P(self[i]); //젓가락 권한 못 얻으면 0이기 때문에 기다려야함 , 이는 인접한 철학자가 밥을 다 먹고 내려 놓았을 때 1로 바꿔줌
    }
    
    
    //test
    void test(int i){
        if(state[(i+4) % 5]!=eating && state[i] == hungry && state[(i+1) % 5]!=eating){ //양옆 젓가락 사용하지 않고 내가 배고픈 상태일 때
            state[i]= eating;
            V(self[i]); // 젓가락 잡을 수 있는 권한 
        }
    }
    
    
    
    ```

    - 비대칭 : 짝수 철학자는 왼쪽 젓가락부터 집도록 하고 홀수 철학자는 오른쪽 젓가락부터 집게 한다.
    - 철학자 i 가 젓가락을 잡는 함수를 호출하면 자신의 상태를 hungry로 바꾸어 놓은 후 철학자 i가 젓가락 두 개를 다 잡을 수 있는 상태인지 확인하고, 확인이 되면 그 철학자에게 젓가락 잡을 수 있는 권한을 준다.
    - 권한이 없다면 인접한 철학자가 내려놓을 때 까지 기다린다.


## Monitor

- Semaphore의 문제점
    - 코딩하기 어렵다.
    - 정확성의 입증이 어렵다.
    - 버그 찾기가 어렵다.
    - 자발적 협력이 필요하다.
    - 한 번의 실수가 모든 시스템에 치명적인 영향을 준다.
- Monitor
    - Monitor는 동시 수행중인 프로세스 사이에서 abstract data type의 안전한 공유를 보장하기 위한 high-level synchronization construct
    - 공유 데이터 접근 위해서는 모니터 내부의 프로시져를 통해서만 접근 가능 
    - 기본적으로 동시 접근을 허용하지 않기 때문에 따로 Lock을 걸지 않고 공유 자원에 접근 가능하다.
    - 공유하기 위한 자원을 선언하고 공유 데이터 접근 위한 절차는 모니터 내부 함수로 구현해 놓았다.
    - 모니터 내에서는 한번에 하나의 프로세스만이 활동 가능하다.
    - 프로그래머가 동기화 제약 조건을 명시적으로 코딩할 필요가 없다.
    - 프로세스가 모니터 안에서 기다릴 수 있도록 하기 위해 condition variable을 사용한다.
        -  condition variable은 wait과 signal 연산에 의해서만 접근이 가능 ex) x.wait(). x.signal()
        - x.wait()을 invoke한 프로세스는 다른 프로세스가 x.signal() invoke할 때까지 suspend
        - x.signal()은 정확하게 하나의 suspend된 프로세스를 resume한다. suspend된 프로세스가 없으면 아무 일도 일어나지 않는다.
        
    - 사용 가능한 자원이 없으면 큐에 들어가서 대기한다.

# Process Synchronization 4

Process Synchronization는 다른 말로 Concurrency Control라고도 한다. 프로세스가 동시 실행 될 때 문제가 생기지 않도록 컨트롤한다는 것이다. 

## Monitor

- 동시 수행중인 프로세스 사이에서 abstract data type의 안전한 공유를 보장하기 위한 high-level synchronization construct
- 공유 데이터 접근과 관련된 문제를 모니터가 자동으로 해결하도록 해서 프로그래머의 부담이 적어진다.
- 모니터 안에 공유 데이터와 접근 코드를 정의해서 프로세스가 공유 데이터에 접근하려면 모니터 안에 정의된 코드를 통해서만 접근할 수 있게 해서 액티브한 프로세스 하나만이 모니터 안의 코드를 실행할 수 있도록 제어한다.
- 프로세스가 모니터 안에서 기다릴 수 있도록 하기 위해 condition variable을 사용한다.
- condition x, y;
- Condition variable은 wait와 signal연산에 의해서만 접근이 가능하다.
- 조건을 충족하지 못해 오랜 시간 프로세스가 기다려야 할 때 프로세스를 잠들게 하기 위해서  x.wait()을 하면 프로세스가 x라는 조건을 만족하지 못해서 x라는 줄에 가서 서게 된다.
- x.signal()은 잠들어 있는 프로세스를 깨워주는 역할을 수행한다.
- suspend된 프로세스가 없다면 하는 일이 없다고 할 수 있다.
- Monitor와 semaphore로 작성한 코드는 쉽게 변환이 가능하다.
- 생산자 소비자 문제 with monitor


- 식사하는 철학자 문제 with monitor
