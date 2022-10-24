# File and File System
## File
* A named collection of related information
* 일반적으로 비휘발성의 보조기억장치에 저장
* 운영체제는 다양한 저장 장치를 file이라는 동일한 논리적 단위로 볼 수 있게 해줌
* Operation
  * create, read, write, reposition(lseek), delete, open, close 등

## File attribute(혹은 파일의 metadata)
* 파일 자체의 내용이 아니라 파일을 관리하기 위한 각종 정보들
  * 파일 이름, 유형, 저장된 위치, 파일 사이즈
  * 접근 권한 (읽기/쓰기/실행), 시간(생성/변경/사용), 소유자 등

## File system
* 운영체제에서 파일을 관리하는 부분
* 파일 및 파일의 메타데이터, 디렉토리 정보 등을 관리
* 파일의 저장 방법 결정
* 파일 보호 등

# Directory and Logical Disk
## Directory
* 파일의 메타데이터 중 일부를 보관하고 있는 일종의 특별한 파일
* 디렉토리에 속한 파일들의 이름 및 attributes를 가진다.
* operation
  * search for a file, create a file, delete a file
  * list a directory, rename a file, traverse the file system

## Partition (= Logical Disk)
* 하나의 (물리적) 디스크 안에 여러 파티션을 두는게 일반적이다.
* 여러개의 물리적인 디스크를 하나의 파티션으로 구성하기도 함
* (물리적) 디스크를 파티션으로 구성한 뒤 각각의 파티션에 file system을 깔거나 swapping 등 다른 용도로 사용할 수 있음

# Open
## open('/a/b/c')
* 디스크로부터 파일 c의 메타데이터를 메모리로 가지고 온다.
* 이를 위해 directory path를 search 한다.
* ![image](https://user-images.githubusercontent.com/51043264/189559749-46f5da53-af6e-46f1-a93d-9a52afac885d.png)
  1) 루트 디렉토리 '\'를 open하고 그 안에서 파일 'a'의 위치를 얻는다.
  2) 루트 디렉토리 '\'를 open하고 그 안에서 파일 'b'의 위치를 얻는다.
  3) 루트 디렉토리 '\'를 open하고 그 안에서 파일 'c'의 위치를 얻는다.
  4) 파일 'c'를 open한다.
* Directory path의 search에 너무 많은 시간을 소요한다.
  * open을 read/write와 별도로 두는 이유
  * 한번 open한 파일은 read/write시 directory search가 필요하지 않다
    * 운영체제가 자신의 메모리영역에 copy 해놓고 바로 전달하기 때문

# File Protection
## File Protection
* 파일에 대해 누구에게, 어떤 유형의 접근(read/write/execution)을 허락할 것인가?
* Access Control 방법
  * Access Control Matrix
    * Access control list : 파일 별로 누구에게 어떤 접근 권한이 있는지를 표시한다.
    * Capability : 사용자 별로 자신이 접근 권한을 가진 파일 및 해당 권한을 표시한다.
  * Grouping
    * 전체 user를 owner, group, public의 세 그룹으로 구분
    * 각 파일에 대해 세 그룹의 접근 권한(rwx)을 3비트씩 표시한다. Ex) UNIX
  * Password
    * 파일마다 password를 두는 방법 (디렉토리 파일에 두는 방법도 가능하다.
    * all-or-nothing : 모든 접근권한에 대해 하나의 password를 사용한다.
    * 접근 권한 별 password를 사용 : 암기 및 관리문제가 생김

# Access Methods
## Access Methods
* 시스템이 제공하는 파일 정보의 접근 방식
  1) 순차적 접근
    * 카세트 테이프를 사용하는 방식처럼 접근
    * 읽거나 쓰면 offset은 자동으로 증가
  2) 직접 접근
    * LP레코드 판과 같이 접근하도록 함
    * 파일을 구성하는 레코드를 임의의 순서로 접근할 수 있음
* Allocation of File Data in Disk
  1) Contiguous Allocation
  2) Linked Allocation
  3) Indexed Allocation 

## Contiguous Allocation
![image](https://user-images.githubusercontent.com/51043264/189560429-8c608553-f119-488d-b900-a44778065389.png)
* 임의의 파일을 동일한 크기의 블록으로 나눈 뒤, 연속하여 적재한다.
* directory 에는 파일과 시작점, 길이를 저장하여 확인한다.
* 장/단점
  * 장점
    * Fast I/O
      * 한번의 seek/rotation으로 많은 바이트 transfer
      * Realtime file용으로, 또는 이미 run 중이던 process의 swapping 용도
    * Direct access (= random access) 가능
      * 직접 접근하여 빠르게 데이터 활용 가능
  * 단점
    * external fragmentation : 무작위 공간을 연속하여 적재하므로 외부 단편화 발생 가능 있음
    * File grow가 어려움
      * file 생성 시 얼마나 큰 hole을 배당할 것인가?
      * grow 가능 vs 낭비 (internal fragmentation)

## Linked Allocation
![image](https://user-images.githubusercontent.com/51043264/189560763-0f10c7a3-28cc-4cbc-b0dc-c5f9071a910a.png)
* directory에는 시작 sector와 끝부분의 sector의 주소만을 가진다.
* 각 sector마다 다음 sector가 존재하는 주소를 가리킨다.
* 끝부분의 sector는 -1을 가리키면서 마지막임을 알린다.
* 장/단점
  * 장점
    * External Fragmentation 발생 안함
  * 단점
    * No random access
      * 중간의 데이터를 찾기 위해선 차례대로 따라가야 하므로 직접 접근을 할 수 없다.
    * Reliability 문제
      * 한 sector가 고장나면 그 이후의 sector를 모두 잃어버린다.
    * Pointer를 위한 공간이 block의 일부가 되기때문에 공간의 낭비 발생
      * Ex) 512 byte/sector , 4byte/pointer -> 512 byte의 sector마다 4byte의 포인터가 붙어 낭비가 발생한다.
  * 변형 
    * File-Allocation table (FAT) 파일 시스템
      * 포인터를 별도의 위치에 보관하여 reliability와 공간 효율성 문제 해결     

## Indexed Allocation
![image](https://user-images.githubusercontent.com/51043264/189561171-449daff5-6706-4865-a8a5-2d745420c0ec.png)
* 디렉토리에 데이터들의 위치를 저장하는 것이 아닌, indexing block을 따로 운영하여 해당 block의 주소만을 가진다.
* Indexing block 내부에 있는 주소를 통해 데이터에 접근한다.
* 장/단점
  * 장점
    * External Fragmentation이 발생하지 않는다.
    * Direct access 가능
  * 단점
    * Small file의 경우 공간낭비 (실제로 많은 file들이 작다.)
    * Too Large file의 경우 하나의 block만으로는 index를 저장하기에 공간이 부족하다.
      * 해결 방안
        1) linked scheme
        2) multi-level index

# Directory Implementation
![image](https://user-images.githubusercontent.com/51043264/189569014-9d36c14d-8476-4d0c-b7d3-c0cc2dca3735.png)
## Linear list
* <file name, file의 metadata> 의 리스트
* 구현이 쉽고 간단하다.
* 디렉토리 내에 파일이 있는지 찾기 위해서는 linear search를 해야하므로, 시간의 소모가 생긴다. 
## Hash Table
* linear list + hashing
* Hash table은 file name을 이 파일의 linear list의 위치로 바꾸어 준다.
* search time을 없애서 빠르다.
* Collision 발생 가능

## File의 metadata의 보관 위치
* 디렉토리 내에 직접 보관
* 디렉토리에는 포인터를 두고 다른곳에 보관 Ex) inode, FAT 등

# Long file name
![image](https://user-images.githubusercontent.com/51043264/189569245-c5a1e39e-7e0b-4a9c-9b13-4bf4d1b0f655.png)
## Long file name의 지원
* <file name, file의 metadata>의 list에서 각 entry는 일반적으로 고정크기를 가진다.
* file name이 고정크기의 entry 길이보다 길어지는 경우 entry의 마지막 부분에 이름의 뒷부분이 위치한 곳의 포인터를 두는 방법으로 해결할 수 있다.
* 이름의 나머지 부분은 동일한 directory file의 일부에 존재한다.


# VFS and NFS
![image](https://user-images.githubusercontent.com/51043264/189569386-cf0424e9-4e5a-4d82-b0c1-f297ead4b215.png)
## Virtual File System (VFS)
* 서로 다른 다양한 file system에 대해 동일한 시스템 콜 인터페이스(API)를 통해 접근할 수 있게 해주는 OS의 layer
## Network File System (NFS)
* 분산 시스템에서는 네트워크를 통해 파일이 공유될 수 있음
* NFS는 분산 환경에서의 대표적인 파일 공유 방법임


# Page Cache and Buffer Cache
## Page Cache
* virtual memory의 paging system에서 사용하는 page frame을 caching의 관점에서 설명하는 용어
* Memory-Mapped I/O를 쓰는 경우 file의 I/O에서도 page cache 사용
## Memory-Mapped I/O
* File의 일부를 virtyal memory에 mapping 시킨다.
* 매핑시킨 영역에 대한 메모리 접근 연산은 파일의 입출력을 수행하게 함.
## Buffer Cache
![image](https://user-images.githubusercontent.com/51043264/189569585-b154bdad-e9af-4039-b453-e175df445f23.png)
* 운영체제가 file system을 대신 읽어 buffer cache에 저장
* 파일시스템을 통한 I/O연산은 메모리의 특정 영역인 buffer cache를 사용한다.
* File 사용의 locality 활용
  * 한번 읽어온 block에 대한 후속 요청시 buffer cache에서 즉시 전달
* 모든 프로세스가 공용으로 사용한다.
* Replacement algorithm이 필요하다 (LRU, LFU 등)
## Unified Buffer Cache
![image](https://user-images.githubusercontent.com/51043264/189569780-8ca423c4-ada9-4436-8ef0-82137142b7a8.png)
* 최근의 OS에서는 기존의 buffer cache가 page cache에 통합됨



# 프로그램의 실행
## 프로그램의 실행
![image](https://user-images.githubusercontent.com/51043264/189569817-8c22b41d-a5b5-448f-9bd5-babc429d2970.png)
* 실행파일로 저장되어 있는 프로그램이 실행되면 프로세스가 된다.
* 프로세스 실행 시 virtual memory에 독자적인 공간을 가진다.
* 실행도중 필요한 부분은 물리적 메모리에 부분적으로 올라가고, 내려갈 때는 swap area로 이동한다.
  * code 부분은 이미 실행파일의 형태로 저장되어 있으므로 swap 영역에 올라가지 않는다.
* 처음 virtual memory에서 물리적 메모리에 데이터를 올릴때는 운영체제의 도움을 받지만, 이후부터는 직접 접근이 가능하다.
* 데이터가 쫓겨날때는 file-mapped되어있기 때문에 swap area에 올리지 않고 파일 시스템에 올린뒤 쫓아낸다.
