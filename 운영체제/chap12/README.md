# Disk Management & Scheduling 1

## Disk Structure

### logical block(외부에서 디스크 접근하는 단위)
 -  디스크의 외부에서 보는 디스크의 단위 정보 저장 공간들
 -  주소를 가진 **1차원 배열**처럼 취급
 -  정보를 전송하는 최소 단위
### Sector(디스크 관리 최소 단위)
- Logical block이 물리적인 디스크에 매핑된 위치
- Sector 0은 최외곽 실런더의 첫 트랙에 있는 첫번째 섹터.

### 디스크 구조
![디스크 구조](https://user-images.githubusercontent.com/62707891/194739040-deb601a1-120c-4f1d-8aac-dde786d5d77a.png)
- Access time의 구성
  - **Seek time**
    - 헤드를 해당 실린더롤 움직이는데 걸리는 시간
    - 안쪽 트랙 바깥쪽 트랙 
  - **Rotational latency**(seek time보다 상대적으로 적다)
    - 헤드가 원하는 섹터에 도달하기까지 걸리는 회전지연시간
  - **Transfer time**(굉장히 적은 시간을 차지)
    - 실제 데이터 전송 시간
  - 한번 seek해서 많은 량을 전송하면 이득 
- Disk bandwidth
  - 단위 시간 당 전송된 바이트의 수
- Disk Scheduling 
  - **seek time**을 최소화 하는 것이 목표
  - seek time =~ seek distance 
  
### Disk Menagement
- Physical formatting(Low-level formatting)
    - 디스크를 컨트롤러가 읽고 쓸 수 있도록 섹터를 나누는 과정 
    - 각 섹터는 header + 실제 data(보통 512bytes) + trailer로 구성
    - header와 trailer는 sector number, ECC(Error-Correcting Code)(error 찾기 가능) 등의 정보가 저장되면 controller가 직접 접근 및 운영
- partitioning 
   - 디스크를 하나 이상의 실린더 그룹으로 나누는 과정
   - OS는 이것을 독립적 Disk로 취급(logical disk, 파일 시스템 만들 수도 있음)
- Logical formatting
    - 파일시스템을 만드는 것
    - FAT, inode, free space 등의 구조 포함
- Booting
    - ROM(전력 나가도 데이터가 날아가지 않는 부분)에 있는 "small bootstrap loader"의 실행
      - 전원을 키면 cpu 제어권의 주소가 ROM의 주소를 가리킴("small bootstrap loader"의 실행)
      - 하드 디스크의 O  sector부분을 메모리에 올리고 실행 
    - sector 0(boot block)을 load하여 실행
    - sector 0은 "full Bootstrap loader program"
    - OS를 디스크에서 load 하여 실행.

### Disk Scheduling Algorithm
- 큐에 다음과 같은 실린더 위치의 요청이 존재하는 경우 디스크 헤드 53번에서 시작한 각 알고리즘의 수행 결과는?(실린더 위치는 0-199)<br/>98,183,37,122,14,124,65,67


#### FCFS(First Come First Service)
- 헤드의 이동거리가 길어져서 비 효율적
#### SSTF(Shortest Seek Time First)
- 현재의 헤드 위치에서 가장 가까운 것 부터 처리
 - 헤드의 이동거리가 짧아짐.
- starvation문제 발생 가능.

#### SCAN(기본적으로 이를 기반으로 스케쥴링함)
- 가장 간단하면서도 획기적인 스케쥴링
  - 엘리베이터 스케쥴링이라고도 함
- disk arm이 디스크의 한쪽 끝에서 다른쪽 끝으로 이동하며 가는 길목에 있는 모든 요청을 처리한다.
- 다른 한쪽 끝에 도달하면 역방향으로 이동하며 오는 길목에 있는 모든 요청을 처리하며 다시 반대쪽 끄트로 이동
- 문제점 : 실린더 위치에 따라 대기 시간이 다르다.
  - 가장 자리는 더 오래 걸린다.  
- 지그재그

#### C-SCAN
- 헤드가 한쪽 끝에서 다른쪽 끝으로 이동하며 가는 길목에 있는 모든 요청을 처리
- 다른쪽 끝에 도달했으면 요청을 처리하지 않고 곧바로 출발점으로 다시 이동
- SCAN보다 균일한 대기 시간을 제공한다.

#### N-SCAN
- 들어올 때 들어온 요청은 처리하지 않는다.

#### LOOK, C-LOOK
- 더 이상 그 방향에서 요청이 없으면 다시 돌아가는 방법 (각각 SCAN, C-SCAN 기반)

### Disk-Scheduling Algorithm의 결정
- SCAN, C-SCAN 및 그 응용 알고리즘은 LOOK, C-LOOK 등이 일반적으로 디스크 입출력이 많은 시스템에서 효율적인 것으로 알려져 있음
- File의 할당 방법에 따라 디스크 요청이 영향을 받음.
- 디스크 스케쥴링 알고리즘은 필요할 경우 다른 알고리즘으로 쉽게 교체할 수 있도록 OS와 별도의 모듈로 작성하는 것이 바람직.


### Swap-Space Management
- Disk를 사용하는 두 가지 이유
  - memory의 volatile(휘발)한 특징 -> file system
  - 프로그램 실행을 위한 memory 공간 부족 -> swap space(swap area)
- Swap-space
  - Virtual memory system에서는 디스크를 memory의 연장 공간으로 사용
  - 파일시스템 내부에 둘 수도 있으나 별도 position 사용이 일반적 
    - 공간의 효율성보다는 속도의 효율성이 우선
    - 일반 파일보다 훨씬 짧은 시간만 존재하고 자주 참조됨.
    - 따라서, block의 크기 및 저장 방식이 일반 파일시스템과 다름.
### RAId
- RAID(Redundant Array of Independent Disks)
  - 여러 개의 디스크를 묶어서 사용
- RAID의 사용목적
  - 디스크 처리 속도 향상
    -  여러 디스크에 block 내용을 분산 저장.
    -  병렬적으로 읽어 옴(interleaving, striping)
  - 신뢰성(reliability) 향상
    - 동일 정보를 여러 디스크에 중복 저장
    - 하나의 디스크가 고장 시 다른 디스크에서 읽어옴
    - 단순한 중복 저장이 아니라 일부 디스크에 parity(오류가 생긴것을 알아내고 복구할 수 있을 정도의 중복저장만)를 저장하여 공간의 효율 성을 높일 수 있다.    
