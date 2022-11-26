[1. API](#API)


## API
### API란

API(Application Programming Interface, 응용 프로그램 프로그래밍 인터페이스)는 응용 프로그램에서 사용할 수 있도록, 운영 체제나 프로그래밍 언어가 제공하는 기능을 제어할 수 있게 만든 인터페이스를 뜻한다.

## Rest (Representational State Transfer)
### Rest란
 HTTP URI (Uniform Resource Identifier) 를 통해 자원 (Resource)을 명시하고, HTTP Method(Post, Get, Put, Delete) 를 통해 해당 자원에 대한 CRUD Operation 을 적용하는 것을 의미한다.


## MSA
### MSA란
한 개의 시스템을 서비스별로 독립적으로 분리하여 배포하는 방식으로 각각의 서비스는 api를 통해서만 데이터를 교환하며, 1개의 큰 서비스를 구성합니다. 모든 시스템의 구성요소가 한 프로젝트에 통합되어 있는 모놀리식 아키텍쳐의 한계점을 극복하고자 설계되었습니다. MSA를 사용하면 일부 서비스에 장애가 발생하여도, 다른 서비스를 문제없이 사용할 수 있고, 각 백단을 다양한 언어와 프레임워크로 사용할 수 있고, 서비스 확장이 용이합니다. 
