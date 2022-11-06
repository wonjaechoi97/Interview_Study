# 디자인 패턴 공부 정리

## SOLID

### SRP(Single Resposibility Principle) 단일 책임 원칙
- 클래스는 단 한개의 책임(기능)을 가져야 한다.
  - Add라는 클래스에 곱하기 나누기까지 들어오면 이 원칙에 위배된다.
- 하나의 책임이라는 것은 모호하다.
  - 클 수도 있고 작을 수도 있다.
  - 문맥과 상황에 따라 다르다   
- **중요한 기준은 변경**, 변경이 있을 때 파급 효과가 적으면 단일 책임 원칙을 잘 따른 것.
- 패키지로 계층을 잘 나눈 것은 이 원칙을 잘 지키기 위한 것 
- 클래스를 변경하는 이유는 단 하나여아 한다.
- 이를 지키지 않으면, 한 책임의 변경에 의해 다른 책임과 관련된 코드에 영향을 미칠 수 있다.
  - 유지보수에 매우 비효율적  
### OCP(Open-Closed Principle) 개방 폐쇄 원칙
- 소프트웨어 요소는 **확장에는 열려** 있으나 **변경에는 닫혀** 있어야 한다.
- 기능보완할 시 A.class를 고치지 않고 A--.class를 새로 만들어 사용
  -A.class<-- 변경에는 닫혀있고, A--.class <--확장에는 열려있다   
- 다형성을 활용하여, 역할과 구현의 분리를 통해 실현할 수 있다.
- 인터페이스를 구현한 새로운 클래스를 하나 만들어서 새로운 기능을 구현한다.
```java
  public class MemberService {
    
    private MemberRepository memberRepository = new MemoryMemberRepository();
    
  }
  
  
  public class MemberService {
    
  //  private MemberRepository memberRepository = new MemoryMemberRepository();
    private MemberRepository memberRepository = new JdbcMemberRepository();
    
  }
  
  
  ```
  - 구현 객체를 사용하기 위해서는 클라이언트 코드를 변경해야 한다.
  - 다형성을 잘 사용했지만 OCP 원칙을 지킬 수 없다.
  - 객체를 생성하고, 연관관계를 맺어주는 별도의 조립, 설정자가 필요하다 --> 스프링
  
#### LSP(Liskov Substitution Principle) 리스코프 치환 원칙
  -  프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀 수 있어야 한다.
  -  다형성에서 하위 클래스는 인터페이스 규약을 다 지켜야 한다는 것, 다형성을 지원하기 위한 원칙, 인터페이스를 구현한 구현체는 믿고 사용하려면, 이 원칙이 필요하다.
  -  단순히 컴파일 성공하는 것을 넘어서는 이야기
  -  예) 자동차 인터페이스의 엑셀은 앞으로 가라는 기능, 뒤로 가게 구현하면 LSP 위반, 느리더라도 앞으로 가야한다.


#### ISP(Interface Segregation Principle) 인터페이스 분리 원칙
  - 특정 클라이언를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다.
  - 자동차 인터페이스 -> 운전 인터페이스, 정비 인터페이스로 분리
  - 사용자 클라이언트-> 운전자 클라이언트, 정비사 클라이언트로 분리
  - 분리하면 정비 인터페이스 자체가 변해도 운전자 클라이언트에 영향을 주지 않는다.
  - 인터페이스가 명확해지고, 대체 가능성이 높아진다.
    - 인터페이스도 기능을 적당한 크기로 쪼개는 것이 좋다.  

### DIP(Dependency Inversion Principle) 의존성 역전 원칙
 - 프로그래머는 "추상화에 의존해야지, 구체화에 의존하면 안된다." 의존성 주입은 이 원칙을 따르는 방법 중 하나다.
 - 쉽게 이야기 해서 구현 클래스에 의존하지 말고, 인터페이스에 의존하라는 뜻.
 - **역할에 의존하게 해야 한다는 것과 같다.** 
    - **구체적인 것이 아닌 추상적인 것에 의존**
``` java
   public class MemberService {
    
  //  private MemberRepository memberRepository = new MemoryMemberRepository();
    private MemberRepository memberRepository = new JdbcMemberRepository();
    
  }
```
  - 이 경우 MemberService는 인터페이스 뿐만 아니라 MemoryMemberRepository라는 구현체에 대해서도 의존하고 있으므로, 다른 구현체로 변경할 때 코드의 변경이 발생한다.
  - DIP에 위반된다.

- 이 원칙을 가장 잘 지킨 패턴이 **전략패턴**

  
  ## 전략패턴
- 실행 중에 알고리즘을 선택할 수 있게 하는 행위 소프트웨어 디자인 패턴이다.
- 메서드 구현 방식의 변경이 일어났을 때 손쉽게 바꿔끼울 수 있게 도와준다.
- 도로로 다니던 버스가 있었는데 도중 선로로 이동하는 방법이 개발 되었다고 할 때 
  - 도로로 다니는 것을 의미하는 메서드를 선로로 이동하는 것으로 바꾸는 것은 SOLID 위배 
  - 이동 방법 자체를 추상화해서 이동방법에 도로이동을 빼고 선로 이동을 주입시켜주면 **전략패턴**을 잘 활용한 것


 
