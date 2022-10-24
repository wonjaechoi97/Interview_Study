# Memory Management 1

## Logical Address == virtual address

- 프로세스마다 독립적으로 가지는 주소 공간
- 각 프로세스마다 0번지 부터 주소를 가지고 시작한다.
- CPU가 바라보는 주소도 Logical Address이다.

## Physical Address

- 메모리가 실제 올라가는 주소이다.
- 아랫부분에는 운영체제 커널 위에는 여러 프로그램이 섞여서 올라가게 된다.
- 주소 바인딩 : 프로그램의 logical Address가 실제 메모리의 어디에 올라갈지 결정하는 것.
    - Symbolic Address → Logical Address → Physical Address
    

이런 주소 변환이 언제 일어나는 가?

크게 3가지 시점으로 나누어 볼 수 있다.

## 주소 바인딩(Address Binding)

- Compile Time Binding
    - 컴파일 되는 시점에 physical address가 결정된다.
    - 컴파일 시점에 결정되기 때문에 비효율적이며, 현재는 사용되지 않는다.
    - 컴파일러는 절대 코드(absolute code)를 생성하게 된다.
    
- Load Time Binding
    - Loader의 책임 하에 물리적인 메모리 주소를 부여한다.
    - 실행파일 실행 시점에 주소가 결정된다.
    - 컴파일러가 재배치가능코드(relocatable code)를 생성한 경우에 가능하다.

- Execution Time Binding == Runtime Binding
    - 수행이 시작된 이후에도 프로세스의 메모리 상 위치를 옮길 수 있다.
    - CPU가 주소를 참조할 때마다 binding을 점검한다. 이를 위해 address mapping table이 필요하다.
    - 그때 그때마다 내용이 어디에 올라가 있는지 주소 변환을 새로 해주는 과정이 필요하기 때문에 주소 변환용 하드웨어적인 지원이 필요하다.
    

## Memory Management Unit

- logical address를 physical address로 매핑해 주는 하드웨어 디바이스
- MMU scheme
    - 사용자 프로세스가 CPU에서 수행되면서 생성하는 모든 주소 값에 대해 base register의 값을 더한다.
    - 논리 주소+시작 위치를 통해서 실제 주소를 얻게 된다.
    - Limit register(논리적 주소의 범위)를 통해서 접근할 수 있는 메모리 주소를 넘어선 접근을 막는다.
- user program
    - logical address만을 다룬다.
    - 실제 physical address를 볼 수 없으며 알 필요가 없다.

## 용어 설명

### Dynamic Loading

- 프로세스 전체를 메모리에 미리 다 올리지 않고 해당 루틴이 불려질 때 메모리에 load하는 것.
- memory utilization의 향상
- 가끔씩 사용되는 많은 양의 코드의 경우에 유용하다.
    - 예 : 오류 처리 루틴
    - 사용 않는 루틴을 메모리에 올려 놓으면 비효율적이다.
- 운영체제의 특별한 지원 없이 프로그램 자체에서 구현 가능함

### Overlays

- 메모리에 프로세스의 부분 중 실제 필요한 정보만을 올린다.
- 프로세스의 크기가 메모리보다 큰 경우에 유용한 방식이다.
- 운영체제의 지원 없이 사용자가 직접 구현 가능하지만 매우 복잡해진다.
- 작은 공간의 메모리를 사용하던 초창기 시스템에서 프로그래머가 수작업으로 구현하게 된다.

### Swapping

- 프로세스를 일시적으로 메모리에서 backing store로 쫓아내는 것.
- Backing store(=swap area)
    - 디스크
    - 많은 사용자의 프로세스 이미지를 담을 만큼 충분히 빠르고 큰 디스크 저장 공간
- Swap in / Swap out
    - 일반적으로 swapper(중기 스케줄러)에 의해서 swap out시킬 프로레스를 선정한다.
- Priority based CPU scheduling
    - priority가 낮은 프로세스를 swapped out 시킨다.
    - priority가 높은 프로세스는 메모리에 올려놓는다.
- Compile time 이나 Load time Binding에서는 원래의 메모리 위치로 swap in 해야 한다.
- Execution time binding에서는 추후 빈 메모리 영역 아무 곳에나 swap in 할 수 있다.
- swap time은 대부분 transfer time이다.

### Dynamic Linking

- Linking을 실행 시간까지 미루는 기법
- 여러 군데에 퍼져 있는 컴파일 된 파일을 묶어서 하나의 실행 파일을 만드는 과정이다.
- Static Linking
    - 라이브러리가 프로그램의 실행 파일 코드에 포함된다.
    - 실행 파일의 크기가 커진다.
    - 동일한 라이브러리를 각각의 프로세스가 메모리에 올리므로 메모리 낭비가 된다.
- Dynamic Linking
    - 라이브러리가 실행시 연결된다.
    - 라이브러리 호출 부분에 라이브러리의 루틴의 위치를 찾기 위한 stub이라는 작은 코드를 둔다.
    - 라이브러리가 이미 메모리에 있으면 그 루틴의 주소로 가고 없다면 디스크에서 읽어온다.
    - 운영체제의 도움이 필요하다.

## Allocation of Physical Memory

- 메모리는 일반적으로 두 영역으로 나누어서 사용한다.
    - OS 상주 영역
        - interrupt vector와 함께 낮은 주소 영역 사용
    - 사용자 프로세스 영역
        - 높은 주소 영역 사용
- 사용자 프로세스 영역의 할당방법
    - Contiguous allocation
        - 각각의 프로세스가 메모리의 연속적인 공간에 적재되도록 한다.
        - fixed partition allocation
        - Variable partition allocation
    - Noncontiguous allocation
        - 하나의 프로세스가 메모리의 여러 영역에 분산되어 올라갈 수 있다.
        - paging
        - segmantation
        - paged segmantation
        

### contiguous allocation

- 고정 분할 방식
    - 사용자가 들어갈 메모리 영역을 영구적 분할로 미리 나누어 놓는 방식
    - 분할의 크기가 모두 동일한 방식과 서로 다른 방식 존재한다.
    - 분할당 하나의 프로그램 적재할 수 있다.
    - 융통성이 없다
        - 동시에 메모리에 load되는 프로그램의 수가 고정된다.
        - 최대 수행 가능 프로그램 크기가 제한된다.
    - 외부 조각 : 메모리에 프로그램을 올리고 싶지만 올리려는 프로그램보다 조각이 작아서 사용되지 않은 조각
    - 내부 조각 : 프로그램 크기가 메모리 조각 크기보다 작아서 들어가고 남는 공간.
- 가변 분할 방식
    - 메모리 영역을 미리 나누어 놓지 않는 방식.
    - 프로그램의 크기를 고려해서 할당한다.
    - 분할의 크기, 개수가 동적으로 할당된다.
    - 기술적 관리 기법이 필요하다.
    - 프로그램 실행 시 프로그램을 메모리에 차곡 차곡 올리는 식
    - 프로그램이 실행되고 종료된 상태에서 다른 프로그램이 들어갈 때 종료되고 나온 조각이 너무 작아 사용되지 않은 영역이 존재할 수 있다. 외부 조각
    
- Hole
    - 가용 메모리 공간
    - 다양한 크기의 hole들이 메모리 여러 곳에 흩어져 있다.
    - 프로세스가 도착하면 수용가능한 hole을 할당한다.
    - 운영체제는 다음의 정보를 유지한다.
        - a) 할당 공간 b) 가용 공간(hole)
        
- Dynamic Storage-Allocation Problem
    - First-fit
        - Size가 프로그램 크기 n 이상인 것 중 최초로 찾아지는 hole에 할당
    - Best-fit
        - Size가 n 이상인 가장 작은 hole을 찾아서 할당
        - Hole들의 리스트가 크기순으로 정렬되지 않은 경우 모든 hole의 리스트를 탐색 해야 한다.
        - 만은 수의 아주 작은 hole들이 생성된다.
    - worst-fit
        - 가장 큰 hole에 할당
        - 역시 모든 리스트를 탐색해야 한다.
        - 상대적으로 아주 큰 hole들이 생성된다.
- Compaction
    - external fragmentation 문제를 해결하는 한 가지 방법이다.
    - 사용 중인 메모리 영역을 한군데로 몰고 hole들을 다른 한 곳으로 몰아 아주 큰 block을  만드는 것이다.
    - 매우 비용이 많이 드는 방법이다.
    - 최소한의 메모리 이동을 compaction하는 방법
    - Compaction은 프로세스의 주소가 실행 시간에 동적으로 재배치 가능한 경우에만 수행될 수 있다.

### Noncontiguous allocation

- Paging
    - Process의 virtual memory를 동일한 사이즈의 page 단위로 나눈다.
    - Virtual memory의 내용이 page 단위로 noncontiguous하게 저장된다.
    - 일부는 backstage storage에, 일부는 physical memory에 저장된다.
    - 페이지 테이블을 활용하여 페이지 별로 주소를 관리하는 기법이 필요하다.
    - 페이지 테이블에 각 페이지가 physical memory에 어디에 올라갔는지 담고 있다.
    - 내부의 상대적인 위치는 변함이 없다.

- **Implementation of Page Table**
    - Page Table은 main memory에 상주한다.
    - Page-table base register(PTBR)가 page table을 가리킨다.
    - Page-table length register(PTLR)가 테이블 크기를 보관한다.
    - 모든 메모리 접근 연산에는 두 번의 메모리 접근이 필요하다.
    - 페이지 테이블 접근 한 번, 실제 data/instruction 접근 한 번
    - 속도 향상을 위해 associative register 혹은 translation look-aside buffer (TLB)라 불리는 고속의 lookup hardware cache를 사용한다.
        - 주소 변환을 위한 캐시 메모리이다.
        - 메모리 페이지에서 빈번히 사용되는 일부 엔트리를 캐싱하고 있고, 메인 메모리의 페이지 테이블에 접근 전 TLB에 접근해서 페이지의 주소를 찾아본다.
    - Associative register(TLB) : parallel search가 가능하다.
        - TLB에는 page table 중 일부만 존재한다.
    - Address translation
        - page table 중 일부가 associative register에 보관되어 있다.
        - 만약 page #가 TLB에 있는 경우 곧바로 frame#을 얻을 수 있다.
        - 아니면 메인 메모리에 있는 page table로부터 frame#을 얻을 수 있다.
        - TLB는 context switch 때 flush된다. 모든 엔트리가 삭제된다.
- **Two-level Page Table**
    
    - 페이지 테이블을 두 단계 거쳐 주소 변환을 하고 그 다음에 메모리에 접근한다.
    - 현대의 컴퓨터는 address space가 매우 큰 프로그램 지원한다.
        - 32 bit address 사용 시 : 2^32 B(4GB)의 주소 공간이 필요하며
            - page size가 4K시 1M개의 page table entry가 필요하다.
            - 각 page entry가 4B시 프로세스 당 4M의 page table이 필요하지만, 대부분의 프로그램은 4G공간 중 지극히 일부분만 사용하기 때문에 공간의 낭비가 심하다.
        - page table 자체를 page로 구성한다.
        - 사용되지 않는 주소 공간에 대한 outer page table의 엔트리 값은 NULL이다.
    - 32 bit 주소 체제에서 logical address는 4K page size로 구성된다.
        - 20 bit의 page number
        - 12 bit의 page offset
    - page table 자체가 page로 구성되기 때문에 page number는 10 bit의 page number 10bit의 page offset으로 나뉜다.
    - p1(10 bit)-p2(10 bit)-page offset(12 bit)
        - P1: outer page table의 index
        - P2: outer page table의 page에서의 변위(displacement)
        
        
        
  ## 교준이 형 정리
  # Memory Management

## Logical vs Physical Address

* logical address (=virtual address)
  * 프로세스 마다 독립적으로 가지는 주소 공간
  * 각 프로세스마다 0번지부터 시작
  * CPU가 보는 주소는 logical address이다.

* physical address
  * 메모리에 실제 올라가는 위치

* 주소 바인딩 : 주소를 결정하는것
* ![image](https://user-images.githubusercontent.com/51043264/187057802-416f1a16-f00e-46c6-b122-940adad32bc2.png)
  * Logical address -> physical address
  * Compile time binding
    * 물리적 메모리 주소가 컴파일 시 알려진다.
    * 시작 위치 변경시 재컴파일
    * 컴파일러는 절대코드(absolute code)를 생성한다.
  * Load time binding
    * Loader의 책임 하에 물리적 메모리 주소를 부여한다.
    * 컴파일러가 재배치가능 코드 (relocatable code)를 생성한 경우 가능하다.
  * Excution time binding (= Run time binding)
    * 수행이 시작된 이후에도 프로세스의 메모리 상 위치를 옮길 수 있다.

* Memory Management Unit (MMU)
  * MMU : logical address를 physical address로 매핑해주는 Hardware device
  * MMU scheme : 사용자 프로세스가 CPU에 수행되며 생성해내는 모든 주소값에 대해 base register (=relocation register)의 값을 더한다.
  * user program
    * logical address만을 다룬다.
    * 실제 physical address를 볼 수 없으며 알 필요가 없다.
    
* Dynamic Loading
  * 프로세스 전체를 메모리에 미리 다 올리는 것이 아닌, 해당 루팅이 불려질 때 메모리에 load 하는 것
  * memory utilization 향상
  * 가끔씩 사용되는 많은 양의 코드의 경우 유용
  * 운영체제의 특별한 지원 없이 프로그램 자체에서 구현 가능
  
* Overlay
  * 메모리 프로세스 부분 중 실제 필요한 정보만을 올린다.
  * 프로세스의 크기가 메모리보다 클 때 유용하다.
  * 운영체제의 지원없이 사용자에 의해 구현된다.
  * Dynamic Loading 과 전체적으로 비슷하나, 발달 방식에서 차이가 있으며, 프로그래머가 적은 공간의 메모리에서 시스템 작동을 위해 수작업으로 구현한것이다.
  
* Swapping
* ![image](https://user-images.githubusercontent.com/51043264/187057972-45f4807e-de35-4b53-8bdf-c29b6854cb3f.png)
  * 프로세스를 일시적으로 메모리에서 backing store로 쫓아내는 것
  * Backing store (=swap area)
    * 디스크 - 많은 사용자의 프로세스 이미지를 담을 만큼 충분히 빠르고 큰 저장공간
  * swap in/ swap out
    * 일반적으로 중기 스케줄러에 의해 swap out 시킬 프로세스 선정
    * priority-based CPU scheduling algorithm
      * priority가 낮은 프로세스를 swapped out시킨다.
      * priority가 높은 프로세스를 메모리에 올린다.
    * compile time 혹은 load time binding에서는 원래 메모리 위치로 swap in 해야 한다.
    * execution time binding에서는 추후 빈 메모리 영역 아무 곳에나 올릴 수 있다.
    * swap time은 대부분 transfer time( swap되는 양에 비례하는 시간 )이다.

* Dynamic Linking
  * Linking을 실행시간까지 미루는 기법
  * Static linking
    * 라이브러리가 프로그램의 실행 파일 코드에 포함
    * 실행 파일의 크기가 커진다.
    * 동일한 라이브러리를 각각의 프로세스가 메모리에 올리므로 메모리가 낭비된다.
  * Dynamic linking
    * 라이브러리가 실행 시 연결된다.
    * 라이브러리 호출 부분에 라이브러리 루틴의 위치를 찾기 위한 stub 이라는 작은 코드를 둔다.
    * 라이브러리가 이미 메모리에 있으면 그 루틴의 주소로 가고, 없으면 디스크에서 읽어온다.
    * 운영체제의 도움이 필요하다.

* Allocation of Physical Memory
  * 메모리는 일반적으로 두 영역으로 나뉘어 사용된다.
    1) OS 상주 영역 : Interrupt vector와 함께 낮은 주소영역 사용
    2) 사용자 프로세스 영역 : 높은 주소 영역 사용
  * 사용자 프로세스 영역의 할당방법
    1) Configuous allocation : 각각의 프로세스가 메모리의 연속적인 공간에 적재되도록 하는 것
    2) Noncontignous allocation : 하나의 프로세스가 메모리의 여러 영역에 분산되어 올라갈 수 있음

# Paging
* Process의 virtual memory를 동일한 사이즈의 page 단위로 나눔
* virtual memory의 내용이 page 단위로 noncontignous하게 저장된다.
* 일부는 backing storage에, 일부는 physical memory에 저장된다.
* Basic Method
  * physical memory를 동일한 크기의 frame으로 나눈다.
  * logical memory를 동일한 크기의 page로 나눔(frame과 같은 크기)
  * 모든 가용 frame들을 관리한다.
  * page table을 사용하여 logical address를 physical address로 변환
  * Internal fragmentation 발생가능

## Implementation of Page Table 
* page table은 main memory에 상주한다.
* Page-table-base-register (PTBR)가 page table을 가리킨다.
* Page-table-length-register (PTLR)가 테이블 크기를 보관한다.
* 모든 메모리 접근 연산에는 2번의 memory access가 필요하다.
* page table 접근 1번, 실제 data/instruction 접근 1번
* 속도 향상을 위해 associative register 혹은 translation look-aside buffer (TLB)라 불리는 고속의 lookup hardware cache를 사용한다.

## Associative Register
![image](https://user-images.githubusercontent.com/51043264/187058389-428000f5-6933-4efa-b9fc-ce4bd5b0dcc7.png)
* Associative register(TLB) : parrel search가 가능하다.
  * 주소변경을 도와주는 일종의 캐시 메모리
  * TLB에는 page table 중 일부만 존재
* Address translation
  * page table 중 일부가 associative register에 보관되어 있음
  * 만약 해당 page가 associative register에 있는 경우, 곧바로 frame을 얻음.
  * 그렇지 않은 경우 main memory에 있는 page table로부터 frame을 얻음
  * TLB는 context switch때 flush (remove old entries).

## Two-Level Page table
* 현대의 컴퓨터는 address space가 매우 큰 프로그램 자원이다.
* 32bit address 사용 시 4G(2^32)의 주소공간을 가진다.
  * page size가 4K시 1M 개의 page table entry 필요
  * 각 page entry가 4B시 프로세스당 4M의 page table를 필요로 한다.
  * 그러나 대부분의 프로그램은 4G의 주소공간 중 지극히 일부분만 사용하므로 page table 공간이 심하게 낭비된다.
  * ![image](https://user-images.githubusercontent.com/51043264/187058619-bfb09177-9636-4b51-a945-450cc41fdbce.png)

* Two-Level Paging Example
* ![image](https://user-images.githubusercontent.com/51043264/187059743-220be56e-ea6c-4cb4-ad28-7f63b3eaef5e.png)
  * logical address (on 32-bit machine with 4K page size)의 구성
    * 20-bit의 page number
    * 12-bit의 page offset
  * page table 자체가 page로 구성되기 때문에 page number는 다음과 같이 나뉜다.
    * 10-bit의 page number
    * 10-bit의 page offset

## Multilevel Paging and Performance
* Address space가 더 커지면 다단계 페이지 테이블이 필요하다.
* 각 단계의 페이지 테이블이 메모리에 존재하므로 logical address의 physical address 변환에 더 많은 메모리 접근이 필요하다.
* TLB를 통해 메모리 접근 시간을 줄일 수 있다
  * 예시 : 4단계 페이지 테이블을 사용하는 경우
    * 메모리 접근시간이 100ns라고 가정할 경우 500ns가 소요된다.
    * 이때, TLB 접근 시간이 20ns이고 TLB hit ratio가 98% 라 가정한다면
      * (TLB에서 바로 값을 꺼내는 시간 + 메모리 접근시간) * 0.98 = 120 * 0.98
      * TLB에 값이 없어 메모리 테이블을 사용하는 시간 = 520 * 0.02
      * 위 두가지 값을 합치면 128ns가 걸리게 된다.
      * 이때, 메모리 접근시간이 100ns 이므로 실질적인 주소변환에 소요된 시간은 28ns 정도이다.
      
## 페이지 테이블
![image](https://user-images.githubusercontent.com/51043264/186410368-b0a36502-3364-4559-aede-1d1333823b34.png)
* 각 페이지는 페이지 번호를 가지고 있다.
* 페이지 테이블은 이 페이지 번호와, frame number, valid-invalid bit 를 가지고 있다.
  * frame number : page에 대한 지점
  * valid-invalid bit : page가 사용되는 페이지인지 아닌지를 표시하는 비트
* 이러한 방식을 통해 각 페이지는 다른 페이지의 간섭을 받지 않게 되며, 이를 Protection이라 표현하였다.

## Memory Protection
* Page table의 각 entry마다 아래의 bit를 둔다.
  1) Protection bit : Page에 대한 접근 권한 (read/write/read-only)
  2) valid-invalid bit
      * valid는 해당 주소의 frame에 그 프로세스를 구성하는 유효한 내용이 있음을 뜻한다. (접근 허용)
      * invalid는 해당 주소의 frame에 유효한 내용이 없음을 뜻한다.
      
## Inverted Page Table
  * page table이 매우 큰 이유
     1) 모든 process 별로 그 logical address에 대응하는 모든 page에 대하여 각각의 page table entry가 존재한다.
     2) 대응하는 page가 메모리에 없어도 page table에는 entry로 존재하게 된다.
  * Inverted page table
  ![image](https://user-images.githubusercontent.com/51043264/187055909-7ab0583a-782f-49ee-9513-e887cdf28d07.png)
     * page frame 하나당 page table에 하나의 entry를 둔 것 (system-wide)
     * 각 page table entry는 각각의 물리적 메모리의 page frame이 담고있는 내용을 표시한다. (process-id , process의 logical address)
     * 테이블 전체를 탐색해야 한다는 단점이 존재한다.
       * associative register를 사용해 이러한 단점을 해결할 수 있으나, 비용이 많이 든다 (expensive)

## Shared Page
  * ![image](https://user-images.githubusercontent.com/51043264/187061706-9780a8f0-1618-4263-8599-979f3a7d1e12.png)
  * Shared code
    * Re-entrant Code (= Pure Code)
    * read-only로 하여 프로세스 간 하나의 code만 메모리에 올림 (eg, text editors, window systems).
    * Shared code는 모든 프로세스의 logical address space에서 동일한 위치에 있어야 함
  * Private code and data
    * 각 프로세스들은 독자적으로 메모리에 올림
    * Private data는 logical address space의 아무곳에 와도 무방하다.
 
# Segmentation
* 프로그램은 의미 단위인 여러개의 segment로 구성되어 있다.
  * 작게는 프로그램을 구성하는 함수 하나하나를 세그먼트로 정의
  * 크게는 프로그램 전체를 하나의 세그먼트로 정의 가능
  * 일반적으로는 code, data, stack 부분이 하나씩의 세그먼트로 정의됨.

* ![image](https://user-images.githubusercontent.com/51043264/187056792-dd6ee762-7d52-4706-b168-f6a004190779.png)
- Segment entry point는 프로그램이 사용하는 segment의 수와 같다(반면 paging 기법에서는 미리 정해져있음)
- segment table에는 limit, base가 있다.
    - limit는 segment의 크기, base는 물리 메모리에서의 시작 위치
    - 위의 그림에서 d가 limit보다 크다면(d>limit) 적절하지 않은 메모리 참조이다.
    - 미리 체크해보고 적절하지 않다면 트랩을 발생시킨다.
* Segment는 다음과 같은 logical unit들이다.
  * main()
  * function
  * global variables
  * stack
  * symbol table, arrays
  
* Segment Architecture
  * Logical address는 다음의 두 가지로 구성
  ```
     < segment number, offset >
  ```
  * Segment table : each table entry has
    * base-starting physical address of the segment
    * limit-length of the segment
  * Segment-table base register (STBR)
    * 물리적 메모리에서의 segment table의 위치(그림에서의 S)
  * Segment-table length register (STLR)
    * 프로그램이 사용하는 segment의 수
      * segment number s is legal if s < STLR.
      * 잘못된 메모리 참조하면 trap 
   
## Segmentation Architecture (Cont.)
* ![image](https://user-images.githubusercontent.com/51043264/187056918-a6f14acf-1931-45da-9637-5718bc6f5770.png)
* Protection
  * 각 세그먼트 별로 protection bit가 존재
  * Each entry:
    * valid bit = 0 -> illegal segment
    * Read/Write/Execution 권한 bit
* Sharing
  * ![image](https://user-images.githubusercontent.com/51043264/187056914-63bf2d26-94b9-45c5-b7fb-d91a9d11abbd.png)
  * sharing segment
  * same segment number
  * 같은 워드프로세스 프로그램이라면 굳이 code 부분을 두 개 생성할 필요없이 공유하면 효율적이다.
* 의미 단위이기 때문에 **공유와 보안**에 있어 paging보다 훨씬 효과적이다.
* Allocation
  * segment보다 page의 개수가 훨씬 많기 때문에 테이블을 위한 메모리가 더 적게 든다.
  * segment 길이가 동일하지 않으므로 가변분할 방식과 동일한 문제점 발생
  * first fit / best fit
  * 중간에 whole들이 생긴다.
  * 전체적인 용량은 존재함에도 불구하고 빈 공간들 중 segment 공간에 맞는 공간이 없어 적재할 수 없는 external fragment, 외부단편화가 발생한다.

## Segmentation with Paging
* Segment를 page 단위로 나누어 메모리에 적재한다. 이를 통해 외부 단편화 현상을 해결한다.
* 공유나 보안 같은 문제는 segmentation의 장점을 가져오고 allocation의 단점은 보완한다.
    * 실제로 segment만 쓰는 경우는 없고 내부적으로 paging 기법을 사용한다. 
* ![image](https://user-images.githubusercontent.com/51043264/187057605-e3cf44ce-7358-4996-95a8-da58f0dbbd89.png)
* segment 번호와 offset을 가지고 있는다.
    * segmen length와 d(offset)을 비교
    * 적절한 메모리 참조라면 d를 페이지 번호(p)와 페이지 안에서 얼마나 떨어져 있는지 나타내는 page offset(p')로 쪼갠다.
* register는 segment의 페이지 번호와 segment 길이를 가지고 있으며 segment 번호를 통해 접근 가능하다.
* segment 길이와 offset을 비교해 올바른지 확인하고, page-table base를 통해 segment 내의 페이지 테이블에서 프레임 번호를 찾는다.
* 해당 페이지 테이블 내의 프레임 번호와 페이지 offset이 합쳐져 physical address가 된다.
## 이번 챕터에서 운영체제의 역할은 없다. 주소 변환은 무조건 하드웨어적으로 해결해야한다.


