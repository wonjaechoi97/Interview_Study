# Vritual Memory

## virtual Memory 1
- 물리적인 메모리의 주소 변환은 운영체제가 관여 x
- virtual Memory 기법은 운영체제가 관여
- paging 기법을 사용한다고 가정, 실제로 대부분의 페이지는 paging기법 사용
  - 모든 page를 메모리에 올리는 것이 아니라 **Demand Paging** 기법 사용   

### Demand Paging
![pagetable](https://user-images.githubusercontent.com/62707891/188270358-791fbd9c-d6fe-4c65-b4aa-2d4693a94037.png)
  - 원통형은 backing store(디스크, swap area) : 당장 필요하지 않은 페이지가 내려가 있는 곳 
- 실제로 필요할 때 page를 메모리에 올리는 것
  - I/O 양의 감소
    - 이상한 사용자가 이상한 짓을 하더라도 문제가 생기지 않는 방어적인 코드가 대부분이고 이런 코드는 거의 대부분 사용이 안되는데 그럼에도 불구하고 그런 페이지를 한꺼번에 올려두면 낭비
  - Memry 사용량 감소
  - 빠른 응답 시간(여러 프로그램)
  - 더 많은 사용자 수용(multi programming에 유리)
- Valid/Invalid bit의 사용
  - Invalid의 의미
    - 사용되지 않는 주소 영역인 경우
      - 위 그림에서 G, H의 경우
    - 페이지가 물리적 메모리에 없는 경우
      - 위 그림에서 B, D, E의 경우
  - 처음에는 모든 page entry가 invalid로 초기화
    - 페이지가 메모리에 올라가면 invalid -> valid
  - address translation 시에 invalid bit이 set 되어 있으면 => "page fault"
    - **page fault** : 요청한 페이지가 메모리에 없는 경우 
    - 이 경우 cpu는 자동적으로 운영체제에게 넘어가게 된다. 일종의 소프트웨어 인터럽트
  
### Page Fault
- ![pagefaulthandling](https://user-images.githubusercontent.com/62707891/188271318-ae315116-683f-46e8-9946-50af5f286bea.png)
- invalid page를 접근하면 MMU(주소 변환 하드웨어)가 trap을 발생시킴 (page fault trap)
- kernel mode로 들어가서 page fault handler가 invoke(실행)됨
- 다음과 같은 순서로 page fault 처리
  > - Invalid reference(eg.bad address(프로세스가 사용하지 않는 주소 접근), protection(접근 권한, read only인데 수정하려고 한다던지))=>abort process.
  > -  정상적인 주소 접근이라면 페이지를 메모리에 올려야 하므로, Get an empty page frame(비어있는 메모리 page frame 가져오기, 없으면 뺏어온다. replace)
  > -  해당 페이지를 disk에서 memory로 읽어온다.(I/O이기 때문에 오래 걸림)
  >> 1. disk I/O가 끝나기 까지 이 프로세스는 CPU를 preempt 당함(block)
  >> 2. Disk read가 끝나면 page tables entry 기록, valid/invalid bit= "valid"
  >> 3. ready queue에 프로세스를 insert => dispatch later
  > - 이 프로세스가 cpu를 잡고 다시 running
  > - 아까 중단되었던 instruction 재개
  
 ### Performance of Demand Paging
 - page default 비율에 따라 퍼포먼스가 좌우된다.
 - page default 비율 0<= p <= 1.0
  - 만약 p = 0 -> page default 발생 없음
  - 만약 p = 1 -> 모든 메모리 접근이 page default 
  - 대부분 p = 0.0xx로 page default 나지 않지만 발생하면 시간 소모됨
 - Effective Access Time = (1-p) x memory access + p(OS & HW page default overhead + [swap page out if needed] + swap page in+OS&HW restart overhead)
    - page default가 안나서 메모리 접근 시간
    - page default가 발생하면 OS로 cpu가 넘어가서 하드웨어가 page default 처리 오버헤드
    - 메모리에 빈 공간이 없어서 page swap out 시간
    - swap in 시간
    - cpu를 얻으면 restart하는 시간
 ### Free frame이 없는 경우(빈 페이지 frame이 없는 경우)
 - ![pagereplacement](https://user-images.githubusercontent.com/62707891/188271788-d4d7ddc3-cc73-4e31-bfe8-0a84e0c19808.png)
 - page replacement
    - 어떤 frame을 빼앗아올지 결정해야 함
    - 곧바로 사용되지 않을 page를 쫓아내는 것이 좋음
    - 동일한 페이지가 여러 번 메모리에서 쫓겨났다가 다시 돌아올 수 있음
    -  - swap out 할 때 변경 된 것이 있다면 backing store에 변경 사항을 기록해두어야 함 
    - 없다면 그냥 메모리에서 지우기만 하면 ok
    - swap out한 page -> invalid , swap in page -> valid 
 - Replacement Algorithm
    - page-fault rate을 최소화하는 것이 목표
    - 알고리즘의 평가
        - 주어진 page reference string에 대해 page fault를 얼마나 내는지 조사
    - reference string(페이지가 참조된 순서)의 예
      - 1,2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5  
### Optimal Alogrithm
- 알고리즘 중 최적의 알고리즘
- MIN (OPT): 가장 먼 미래에 참조되는 page를 replace
- 4 frame exmaple
- ![4frameExample](https://user-images.githubusercontent.com/62707891/188271926-1342f9e0-2321-4848-94f9-9135ea501126.png)
- 미래의 참조를 어떻게 아는가?
    - Offline algorithm : reference string(미래 참조할 것들)을 알고 있다는 가정하에 오프라인에서 사용한는 것
- 다른 알고리즘의 성능에 대한 upper bound(상한선) 제공, 아무리 좋아도 이거 보다 좋을 순 없다.
    - Belady's optimal algorithm, MIN, OPT 등으로 불림
- 이 다음부터는 미래를 모르는 상태에서 사용하는 실제 사용하는 알고리즘
### FIFO Algorithm
- FIFO : 먼저 들어온 것을 먼저 내쫓음.
- ![fifo](https://user-images.githubusercontent.com/62707891/188272664-0306e40f-e5b7-45b0-8e67-68f8bffacb92.png)
- FIFO Anomaly(Belady's Anomaly)
    - 메모리에 page frame 늘어났는데 성능이 나빠지는 경우가 있음
- 잘 사용 안함

### LRU (Least Recently Used) Algorithm
- LRU : 가장 오래 전에 참조한 것을 지움
- ![LRU](https://user-images.githubusercontent.com/62707891/188272814-06a778ea-faea-4188-88e9-052f41591c31.png)

### LFU(Least Frequently Used) Algorithm
- LFU : 참조 횟수(reference count)가 가장 적은 페이지를 지움
    - 최저 참조 횟수인 page가 여럿일 경우
      - LFU 알고리즘 자체에서는 여러 page 중 임의로 선정한다.
      - 성능 향상을 위해 가장 오래 전에 참조된 page를 지우게 구현할 수도 있다
    - 장단점
      - LRU처럼 직전 참조 시점만 보는 것이 아니라 장기적인 시간 규모를 보기 때문에 page의 인기도를 좀 더 정확히 반영 가능
      - 참조 시점의 최근성을 반영하지 못함
      - LRU보다 구현이 복잡함
- ![LRULFU비교](https://user-images.githubusercontent.com/62707891/188275516-dca3ec69-5ab5-436c-beef-dfbd49049b5d.png)

### LRU와 LFU 알고리즘의 구현
- ![알고리즘 구현](https://user-images.githubusercontent.com/62707891/188276403-0a663d20-d17c-4f4e-a24f-a341cb779076.png)

- LRU
    - 참조 시점에 따라 줄세우기 (Linked List형태로)
      - 참조 되면 빼서 제일 아래에 삽입
      - swap out 시 가장 위에 있는 것을 삭제
      - O(1) complexity : 가장 위에 꺼 쫒아내면 되므로
- LFU
    - 비슷하게 참조 횟수에 따라 줄세우기
        - 하지만 힘들다 참조 횟수가 하나 늘어나면  가장 아래로 내려 가는 것이 아니라, 하나씩 비교하면서 참조 횟수 높은 것 바로 다음에 삽입해야 한다.
        - O(n) complexity : 복잡하므로 아래의 힙을 사용하여 구현
    - 힙을 사용하여 구현
        - 이진 트리로 구현
        - 부모보다 자식이 참조 횟수가 많도록
        - O(log n) complexity :자식들과만 비교하기 때문에

## virtual Memory 2

### 다양한 캐싱 환경
- 캐싱 기법
    - 한정된 빠른 공간(=캐시, 여기서는 물리적 메모리 느린 공간은 backing store(swap area))에 요청된 데이터를 저장해 두었다가 후속 요청 시 캐시로부터 직접 서비스하는 방식
    - paging system 외에도 cache memory, buffer caching(여기 까지 저장 매체간 읽어오는 속도 차이 때문에 사용), web caching(불러오는 시간이 길어져서) 등 다양한 분야에서 사용
- 캐시 운영의 시간 제약
    - 교체 알고리즘에서 삭제할 항목을 결정하는 일에 지나치게 많은 시간이 걸리는 경우 실제 시스템에서 사용할 수 없다
    - buffer caching이나 web caching의 경우
        - O(1)에서 O(log n) 정도까지 허용, O(n)은 너무 커서 사용 x
    - paging system인 경우
        - page fault인 경우에만 os가 관여
        - 페이지가 **이미 메모리에 존재**하는 경우 참조시각 등의 정보를 os가 알 수 없다. 즉 LRU, LFU 사용 불가능
            - page fault 나야만 사용 가능하다 
            - 다만 LRU, LFU가 buffer caching나 web caching에서 사용 가능.
        - O(1)인 LRU의 list 조작조차 불가능
        - **사실 사용 안함**.
        - 그래서 사용하는 것이 Clock Algorithm
### Clock Algorithm
- ![clockaltorithm](https://user-images.githubusercontent.com/62707891/188284808-8260980c-49ad-4ab9-9dcf-d4f4dc1041af.png)
- Clock algorithm
    - LRU의 근 사(approximation) 알고리즘
    - 여러 명청으로 불림
        - Second chance algorithm
        - NUR (Not Used Recently) 또는 NRU (Not Recenty Used)
    - Reference bit을 사용 해서 교체 대상 페이지 선정 (circular list)
      - 주소 변환 하드웨어가 어떤 페이지에 접근할 때 page table에 valid해서 cpu가 그 페이지를 읽어가야 한다면 그 페이지에 대한 reference bit을 1로 변경(하드웨어 적으로)
    - reference bit가 0인 것을 찾을 때까지 포인터를 하나씩 앞으로 이동
    - 포인터 이동하는 중에 reference bit 1은 모두 0으로 바꿈
    - Reference bit이 0인 것을 찾으면 그 페이지를 교세
    - 한 바퀴 되 돌아와서도(=second chance) 0이면 그때에는 replace 당 함
    - 자주 사용되는 페이지라 면 second chance가 올 때1
- Clock algorithm의 개 선
    - reference bit 과 modified bit (dirty bit)을 함께 사용
    - reference bit = 1 : 최근에 참조된 페이지
        - reference bit = 0 : 시계 바늘이 한 바퀴 도는 동안에 참조가 있었다는 의미
    - modified bit(dirty bit) = 1 : 최근에 변경된 페이지 (I/O를 동반하는 페이지)
        - 페이지 swipe out 할 때 modified bit이 0이면 이 페이지는 변경사항(write한 적이) 없으므로 그냥 쫓아낸다
        - 반면 1이면 backing store에 변경사항 저장 후 쫓아낸다
        - 가능한 0인 것을 쫓아내야 할 일이 적다
### Page Frame의 Allocation
- Allocation problem: 각 process에 얼마만큼의 page frame을 할당할 것인가?
- Allocation의 필요성
    - 메모리 참조 명령어 수행시 명령어, 데이터 등 여러 페이지 동시 참조
      - 명령어 수행을 위해 최소한 할당되어야 하는 frame의 수가 있음
    - Loop를 구성하는 page들은 한꺼번에 allocate 되는 것이 유리함
      - 최소한의 allocation이 없으면 매 loop 마다 page fault
      - for 문을 구성하는 페이지가 3개면 3개를 모두 올려줘야지 page default 매번 안 발생!
    - 하나의 프로그램이 모든 page frame을 독점해서는 안된다.
- Allocation Scheme
    - Equal allocation: 모든 프로세스에 똑 같은 갯수 할당
      - 프로세스마다 필요한 페이지가 다르므로 비효율적(어떤 프로세스는 많이 요구 어떤 프로세스는 적게 요구)   
    - Proportional allocation: 프로세스 크기에 비례하여 할당
      - 시간에 따라 다를 수 있기 때문에 부적절 할 수도 있다.   
    - Priority allocation: 프로세스의 priority에 따라 다르게 할당
### Global Vs Local Replacement
- Global replacement
    - Replace 시 다른 process에 할당된 frame 을 빼앗아 올 수 있다.
    - Process별 할당량을 조절하는 또 다른 방법임
    - FIFO, LRU, LFU 등의 알고리즘을 global replacement로 사용시에 해당
      - 이 알고리즘 사용하면 그때 그때 프로그램 별로 메모리 할당량 자동으로 조절되므로 위의 할당 방식 사용 x  
    - Working set, PFF 12
      -  LRU, LFU와 같은 알고리즘은 할당하는 효과는 없으나
      -  Working set, PFF과 같은 방법은 프로세스가 필요로하는 페이지들을 메모리에 올려 놓는 할당의 효과가 있다.
- Local replacement
    - 위의 할당 방법을 사용하고 할당량 내에서 쫒아냄
    - 자신에게 할당된 frame 내에서만 replacement
    - FIFO, LRU, LFU 등의 알고리즘을 process 별로 운영시
### Thrashing
- Thrashing
    - 프로세스의 원활한 수행에 필요한 최소한의 page frame 수를 할당 받지 못한 경우 발생
    - Page fault rate이 매우 높아짐
      - I/O하러 가야된다 
    - 이 경우를 **Thrashing**이라고 함
    - CPU utilization(pcu 이용률)이 낮아짐
    - OS는 MPD (Multiprogramming degree)를 높여야 한다고 판단
    - 또 다른 프로세스가 시스템에 추가됨 (higher MPD, MultiProgramming Degree)
    - 프로세스 당 할당된 frame의 수가 더욱 감소
    - 프로세스는 page의 swap in/swap out으로 매우 바쁨
    - 대부분의 시간에 CPU는 한가함
    - low throughput
- Thrashing Diagram
    -  ![Thrashingdiagram](https://user-images.githubusercontent.com/62707891/188294565-088ab507-6236-4426-b19e-def1507ce07e.png)
    -  프로그램 양이 적으면 i/o가 빈번하게 발생하여 cpu 사용률 낮다
    -  프로그램이 증가할 수록 i/o 시에도 놀지 않고 일하기 때문에 사용률이 높아진다.
    -  계속해서 증가하다가 어느 순간 뚝 떨어지는데 이 때가 Thrashing이 발생한 지점이다.
    -  이를 막기 위해  **MultiProgramming Degree**를 조절해야 한다.
### Working-Set Model
- 프로그램들이 원활하게 실행되기 위해서는 최소한의 페이지가 메모리에 올라와야 한다.
- Locality of reference
    - 프로세스는 특정 시간 동안 일정 장소만을 집중적으로 참조한다
      - for loop의 경우 특정 페이지만 집중적으로 참조한다   
    - 집중적으로 참조되는 해당 page들의 집합을 **locality set(working set)**이라 함
- Working-set Model
    - Locality에 기반하여 프로세스가 일정 시간 동안 원활하게 수행되기 위해 한꺼번에 메모리에 올라와 있어야 하는 page들의 집합을 Working Set이라 정의함
    - Working Set 모델에서는 process의 working set 전체가 메모리에 올라와있어야 수행되고 그렇지 않을 경우 모든 frame 을 반납한 후 swap out(suspend)
    - Thrashing을 방지함
    - Multiprogramming degree를 결정함

### Working-Set Algorithm
- Working set의 결정
    - 미리 알 수 없기 때문에 과거를 통해서 추정
    - Working set window를 통해 알아냄
    - window size가 △인 경우
      - 시각ti 에서의 working set WS (t )
          - Time interval [ti-△, ti] 사이에 참조된 서로 다른 페이지들의 집합
      - Working set에 속한 page는 메모리에 유지, 속하지 않은 것은 버림 (즉, 참조된 후 △ 시간 동안 해당 page를 메모리에 유지한 후 버림)
    - ![aset](https://user-images.githubusercontent.com/62707891/188295364-71be42ac-daad-43ad-a499-2c92fa9a50c6.png)
    - working set의 크기가 바뀐다.
- Working-Set Algorithm
    - Process들의 working set size의 함이 page frame의 수보다 큰 경우
      - 일부 process를 swap out시켜 남은 process의 working set을 우선적으로 충족시켜 준다 (MPD를 줄임)
    - Working set을 다 할당하고도 page frame이 남는 경우
      - Swap out 되었던 프로세스에게 working set을 할당 (MPD를 키움)
- Window size A
    - Working set을 제대로 탐지하기 위해서는 window size를 잘 결정해야 함
    - △ 값이 너무 작으면 locality set을 모두 수용하지 못할 우려
    - △ 값이 크면 여러 규모의 locality set 수용
    - △ 값이 이면 전체 프로그램을 구성하는 page working set으로 간주
### PFF(Page-Fault Frequency) Scheme
![pff](https://user-images.githubusercontent.com/62707891/188296394-b67300e2-df2a-41a2-8662-227bc2d3ab00.png)
- 특정 프로그램의 Page-fault rate의 상한값과 하한값을 둔다
    - page fault rate이 상한값을 넘으면 frame을 더 할당한다.
    - page fault rate이 하한값 이하이면 할당 frame 수를 줄인다.
- 빈 frame이 없으면 일부 프로세스를 swap out  
    - working-set 알고리즘과 같은 방식으로 

### Page Size의 결정
- 기존 4KB
- Page size를 감소시키면
    - 페이지 수 증가
    - 페이지의 테이블 크기 증가
    - Internal fragmentation 감소
        - 잘게 썰기 때문에 내부 조각이 적게 발생
    - Disk transfer의 효율성 감소
        - Seek/rotation vs transfer
        - 한번 디스크를 돌아서 많은 양의 내용 읽어 오는 것이 좋다. 오래 걸리기 때문에
    - 필요한 정보만 메모리에 올라와 메모리 이용이 효율적
        - Locality의 활용 측면에서는 좋지 않다
- Trend
    - 32비트 메모리 주소 체계에서 64비트 체계로 바뀜에 따라 큰 페이지 사용
    - Larger page size     
