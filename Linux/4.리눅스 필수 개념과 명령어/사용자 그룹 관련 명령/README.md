# 사용자 그룹 관련 명령

## 사용자와 그룹
- 리눅스는 다중 사용자 시스템(Multi-User System)
  - 여러명 접속 가능
- 기본적으로 root -> super user, 모든 권한 가짐
- 모든 사용자를 하나 이상의 그룹에 소속됨
  - 한 사용자가 여러 그룹에 소속되어 있을 수도 있음
- 사용자 /etc/passwd 파일에 정의
```
rocky:x:1000:1000:rocky:/home/rocky:/bin/bash

// 사용자 이름 : 암호 : 사용자 ID : 사용자 소속 그룹 ID : 전체 이름: 홈 디렉토리: 기본 셸

```
- /etc/shadow에 비밀번호 정의됨(암호화)
- /etc/group 파일에 **그룹** 
```
[root@localhost ~]# cat /etc/group
중략
rocky:x:1000:
// 그룹명 : 비밀번호 : 그룹 id : 그룹에 속한 사용자명
// 그룹 딱히 지정 않고 계정 생성하면 계정명과 동일한 그룹 생성
```
