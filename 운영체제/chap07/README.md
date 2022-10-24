# 🌐 Deadlock 1

## Deadlock(교착 상태)

- 자원을 가진 상태에서 상대방의 자원을 요청하는데 상대방도 자원을 내놓지 않고 자원을 요구하는 상태
- 일련의 프로세스들이 서로가 가진 자원을 기다리며 block된 상태

## Resource(자원)

- 하드웨어, 소프트웨어를 등을 포함하는 개념
- 예) I/O device, CPU cycle, memory space, semaphore 등
- 프로세스가 자원을 사용하는 절차
    - Request(요청), Allocate(획득), Use(사용), Release(반납)

- Deadlock Example(하드웨어)
    - 시스템에 2개의 tape drive가 있고, 하나의 드라이브의 것을 다른 드라이브에 카피하는 작업을 원할 때
    - 프로세스 P1과 P2가 각각이 하나의 tape drive를 보유한 채 다른 하나를 기다리는 경우
- Deadlock Example 2(소프트웨어)
    - Binary semaphores A and B
   ``` c
   //P0
   P(A);
   P(B);
   
   //P1
   P(B);
   P(A);
   ```

    

## Deadlock 발생의 4가지 조건

이 네 가지 중 어느 하나가 만족하지 않으면 deadlock 발생하지 않는다. 

- Mutual exclusion(상호 배제)
    - 매 순간 하나의 프로세스만이 자원을 사용할 수 있다. 독점적으로 사용한다.
- No preemption(비선점)
    - 프로세스는 자원을 스스로 내어 놓을 뿐 강제로 빼앗기지 않는다.
- Hold and wait(보유 대기)
    - 자원을 가진 프로세스가 다른 자원을 기다릴 때 보유 자원을 놓지 않고 계속 가지고 있다.
- circular wait(순환 대기)
    - 자원을 기다리는 프로세스 간에 사이클이 형성되어야 한다.
        - P0는 P1이 가진 자원을 기다림
        - P1는 P2이 가진 자원을 기다림
        - P2는 P3이 가진 자원을 기다림
        - Pn-1는 Pn1이 가진 자원을 기다림
    

## Resource-Allocation Graph(자원 할당 그래프)
![할당그래프](https://user-images.githubusercontent.com/62707891/183253237-37e1d003-8a3a-4bc3-bf40-fafa581ae17d.png)

- deadlock 발생을 알아보기 위해 그리는 것이다.
    - 프로세스 -> 자원(요청)
    - 자원 -> 프로세스(획득)
- 그래프에 cycle이 없으면 deadlock이 아니다.
    - 그래프의 화살표를 따라가보기. 
- 그래프에 cycle이 있는 경우
    - 자원 당 인스턴스가 하나밖에 없으면 deadlock이다.
    - 자원 당 인스턴스가 여러 개면 deadlock일 수도 아닐 수도 있다.

## Deadlock의 처리 방법

- deadlock은 자주 생기는 문제가 아니므로 미연의 방지하는 방법은 오버헤드가 커서 현대의 운영체제들은 대부분 **무시하는 방법**을 사용한다.
- Deadlock Prevention
    - 자원 할당 시 Deadlock의 4가지 필요 조건 중 어느 하나가 만족 되지 않도록 하는 방법이다.
- Deadlock Avoidance
    - 자원 요청에 대한 부가적인 정보를 이용해 deadlock의 가능성이 없는 경우에만 자원을 할당하는 방법이다.
    - 시스템 state가 원래의 state로 돌아올 수 있는 경우에만 자원을 할당
- Deadlock Detection and Recovery
    - Deadlock 발생은 허용하되 그에 대한 detection 루틴을 두어서 deadlock 발견시 recover하는 방법이다.
- **Deadlock Ignorance**
    - Deadlock을 시스템이 책임지지 않는 방법이다.
    - UNIX를 포함한 현대의 운영체제 대부분이 사용하는 방법이다.

## Deadlock Prevention

자원에 대한 이용률이 낮아지고, 시스템의 성능이 나빠지는 문제, starvation 문제가 있다.

- Mutual exclusion(상호 배제)
    - 공유해서는 안되는 자원의 경우 반드시 성립해야 한다.
    - -배제할 수 있는 조건은 아니다.
- Hold and wait(보유 대기)
    - 프로세스가 자원을 요청할 때 다른 어떤 자원도 가지고 있지 않아야 한다.
    - 방법 1: 프로세스 시작 시 모든 필요한 자원을 할당 받게 하는 방법이다.
        - 자원 활용이 비효율적
    - 방법 2: 자원이 필요한 경우 보유 자원을 모두 놓고 다시 요청
- No preemption(비선점)
    - process가 어떤 자원을 기다려야 하는 경우 이미 보유한 자원이 선점된다.
    - 모든 필요한 자원을 얻을 수 있을 때 그 프로세스는 다시 시작된다.
    - 자원의 상태를 쉽게 save하고 restore할 수 있는 자원에서 주로 사용된다.(cpu, memory)- 뺏어도 되는 이유
        - 중간에 뺏는게 어려운 자원은 불가능
    - 자원을 빼앗아 올 수 있게 하는 방법 
- circular wait(순환 대기)
    - 모든 자원 유형에 할당 순서를 정하여 정해진 순서대로만 자원 할당
    - 3번 자원을 얻기 위해서는 1번, 2번 도 가지고 있어야 한다.
    - 또한 순서가 3인 자원을 보유 중인 프로세스가 순서가 1인 자원을 할당 받기 위해서는 우선 1번 자원을 반납해야 한다.

## Deadlock Avoidance

- 자원 요청에 대한 부가 정보를 이용해서 자원 할당이 deadlock으로 부터 안전한지를 동적으로 조사해서 안전한 경우에만  할당한다.
- 가장 단순하고 일반적인 모델은 프로세스들이 필요로 하는 각 자원 별 최대 사용량을 미리 선언하도록 하는 방법이다.
- 자원의 인스턴스가 하나 밖에 없는 경우
    - 자원 할당 그래프 알고리즘을 사용한다.
    - ![점선추가](https://user-images.githubusercontent.com/62707891/183255118-ae5a8d5e-1dd8-4066-9a7a-5e390d5606ec.png)
        - 점선 : 이 프로세스가 평생에 한 번 이 자원을 사용할 가능성이 있다.
        - 데드락은 아니다. 아직 평생에 한 번 요청한다고 했지 아직 요청을 안 했으므로 but 요청하면 데드락 가능
        - Deadlock Avoidance는 이 경우 R2 자원에서 데드락 가능성이 있으므로 요청을 받지 P2의 요청을 받지 않는다.
        - 하지만 P1의 경우에는 준다고 해도 데드락의 위험이 없기 때문에 자원 요청을 받아 들인다.
    - 프로세스가 자원을 미래에 요청할 수 있는 것을 점선으로 표시
    - 프로세스가 해당 자원 요청이 실선으로 바꿔서 표시
    - 자원이 반납되면 다시 점선으로 표시
    - 요청에서 할당으로 변경 시 cycle이 생기지 않는 경우에만 요청 자원을 할당한다.
    - cycle 생성 여부 조사 시 프로세스 수가 n일 때 O(n^2)시간이 걸린다.
- 자원 당 인스턴스가 여러 개일 경우
    - banker’s 알고리즘 사용한다.
    - ![뱅커스](https://user-images.githubusercontent.com/62707891/183255354-408df2d6-0724-43c0-9611-79543f9816f8.png)
        - Allocation : 현재 할당, Max : 평생 필요한 것 선언, Available : 현재 사용 가능한 자원 수, Need : 추가로 요청할 양
        - P1이 A1 C2 요청한 경우, 줄 수는 있다. 주기 전에 남은 자원으로 평생 이용할 자원을 모두 줄 수 있는 경우에만 준다.
        - 가용 자원만으로 max만족 가능한 경우, 이 경우에 가능
    - 프로세스가 추가로 요청하는 자원이 남은 것으로는 충족이 안되면 자원을 할당하지 않는다.
    - 프로세스가 최대로 요청할 수 있는 만큼 요청하였을 때 그 수가 Available한 자원 수를 넘어 간다면 자원을 할당하지 않는다.
    - 무조건 최악의 경우를 가정한다.
    - 각 프로세스의 최대 요청 자원을 모두 충족할 수 있는 safe sequence가 존재 한다면 시스템은 safe state이다.
    - deadlock은 안 생기지만 비효율적이다.

## Deadlock Detection and Recovery

- 자원당 인스턴스가 하나일 경우 자원 할당 그래프를 Corresponding wait-for graph로 교체하고 싸이클이 있는지 확인하여 deadlock이 있는지 확인한다.
- 낙관적이게 상황을 바라봐서 정말 deadlock인 경우를 찾는다.
- 낙관적이게 요청이 없는 프로세스가 자원을 반납한 경우에도 다른 프로세스의 요청을 충족할 수 없다면 deadlock이다.
- deadlock이 발견되면
    - process termination
        - deadlock에 연루된 모든 프로세스를 모두 죽인다.
        - deadlock이 종료될 때까지 프로세스를 하나씩 죽인다.
    - Resource Preemption
        - 비용을 최소화할 victim을 선정한다.
        - safe state로 rollback하여 process를 restart한다.
        - starvation 문제
            - 동일한 프로세스가 계속해서 victim으로 선정되는 경우
            - 비용을 고려할 때 rollback 횟수도 고려한다.

## Deadlock Ignorance

- deadlock이 일어나지 않는다고 생각하고 아무런 조치도 취하지 않는다.
    - daedlock이 매우 드물게 발생하므로 deadlock에 대한 조치 자체가 더 큰 overhead일 수 있다.
    - 만약, 시스템에 deadlock이 발생한 경우 시스템이 비정상적으로 작동하는 것을 사람이 느낀 후 process를 죽이는 방법 등의 방법으로 대처한다.
    - UNIX, Windows 등 대부분의 범용 OS가 채택.
