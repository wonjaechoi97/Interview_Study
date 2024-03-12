# 리눅스 기본 명령어
## 마운트

- man 명령어 : 도움말
```python
man {명령어}
# {명령어}에 대한 도움말 출력
```

- mkdir
```
///media/cdrom 디렉토리 생성
mkdir /media/cdrom

// aaa/bbb/ccc 생성
[root@localhost ~]# mkdir -p aaa/bbb/ccc
[root@localhost ~]# ls

//  a, b, c 3 폴더 생성
[root@localhost ~]# mkdir a b c

// 

```
- pwd
```python
pwd
#현재 위치한 디렉토리 보기
```

### 마운트
- 마운트란 물리적인 장치를 특정한 위치(대게 디렉토리)에 연결시켜 주는 과정
  - 윈도우에서는 cd 나 usb 연결하면 d 드라이브 생성되면서 바로 마운트 되나, 리눅스는 자동으로 되지 않음


- mount
```python
mount
#mount 목록 보여줌
```

- mount {드라이브} {마운트할 디렉토리}
```
mount /dev/cdrom /media/cdrom
# /dev/cdrom(연결한 CD)를 /media/cdrom(디렉토리)로 마운트 연
```
## 리눅스 기본 명령어(1)
### ls
```
[root@localhost ~]# ls
anaconda-ks.cfg  공개  다운로드  문서  바탕화면  비디오  사진  서식  음악

[root@localhost ~]# ls -l
합계 4
-rw-------. 1 root root 939  7월 16  2022 anaconda-ks.cfg
drwxr-xr-x. 2 root root   6  7월 16  2022 공개
drwxr-xr-x. 2 root root   6  7월 16  2022 다운로드
drwxr-xr-x. 2 root root   6  7월 16  2022 문서
drwxr-xr-x. 2 root root   6  7월 16  2022 바탕화면
drwxr-xr-x. 2 root root   6  7월 16  2022 비디오
drwxr-xr-x. 2 root root   6  7월 16  2022 사진
drwxr-xr-x. 2 root root   6  7월 16  2022 서식
drwxr-xr-x. 2 root root   6  7월 16  2022 음악
// - 시작 일반 파일
// d 시작 디렉토리

[root@localhost ~]# ls /etc/sysconfig/
// 해당 디렉토리의 파일 목록 나열

[root@localhost ~]# ls -a
//숨김 파일도 보여준다
[root@localhost ~]# ls -aㅣ
// ls -a , ls -l 합치기

[root@localhost ~]# ls *.cfg
//확장명이 cfg인 것을 보여준다.
```
### cd
```
[root@localhost ~]# cd /
// / 디렉토리로 이동, / 란 윈도우의 c 드라이브와 비슷 

[root@localhost /]# cd
[root@localhost ~]# pwd
/root
// 현재 사용자의 홈 디렉토리, root 사용자라면 /root로 이동

```
  - cd 후엔 pwd 명령어로 작업중인 디렉토리를 확인하자!
```
//cd .. 앞에 경로(상위 경로)로 이동
[root@localhost sysconfig]# cd ..
[root@localhost etc]#

//cd . -> 현재 경로

```

### rm

- touch
```
// touch - 크기가 0인 파일 생성 
root@localhost ~]# touch abc.txt
[root@localhost ~]# ls
abc.txt          공개      문서      비디오  서식
anaconda-ks.cfg  다운로드  바탕화면  사진    음악

// 여러 파일 한 번에 만들기도 가능
touch abc.txt bcd.txt
```
- rm
```
// 삭제 명령어, 확인을 묻는다면 y 엔터
rm 파일명

// 묻는 거 없이 바로 지우기
rm -f 파일명

//폴더와 안에 있는 것 싹다 지우기
rm -rf 폴더명

//폴더 속 비어 있는 경우 폴더 지우기
rmdir 폴더명
```
### cp
```
// cp a b --> a 파일을 b(새로 만들어짐)로 복사 
root@localhost ~]# cp abc.txt bbb.txt
[root@localhost ~]# ls -l
합계 4
-rw-r--r--  1 root root   0  3월 12 21:36 abc.txt
-rw-------. 1 root root 939  7월 16  2022 anaconda-ks.cfg
-rw-r--r--  1 root root   0  3월 12 21:37 bbb.txt
-rw-r--r--  1 root root   0  3월 12 21:36 bcd.txt

//폴더 복사 /etc/sysconfig를 현재(.) 위치에 복사 
[root@localhost ~]# cp -r /etc/sysconfig .
[root@localhost ~]# ls
abc.txt          bbb.txt  sysconfig  다운로드  바탕화면  사진  음악
anaconda-ks.cfg  bcd.txt  공개       문서      비디오    서식

```

### mv
```
//abc.txt를 상위 디렉토리로 옮겨라 
[root@localhost ~]# mv abc.txt ../
[root@localhost ~]# ls
anaconda-ks.cfg  bcd.txt  다운로드  바탕화면  사진  음악
bbb.txt          공개     문서      비디오    서식
[root@localhost ~]# cd ..
[root@localhost /]# ls
abc.txt  bin   dev  home  lib64  mnt  proc  run   srv  tmp  var
afs      boot  etc  lib   media  opt  root  sbin  sys  usr

//상위 폴더에서 abc.txt를 이름을 aaaa.txt로 바꾸며 이동시키기
[root@localhost ~]# mv ../abc.txt ./aaaa.txt
[root@localhost ~]# ls
aaaa.txt         bbb.txt  공개      문서      비디오  서식
anaconda-ks.cfg  bcd.txt  다운로드  바탕화면  사진    음악
```
### cat
텍스트로 작성된 파일을 화면에 출력

### head, tail
텍스트로 작성된 파일 앞 10행 또는 마지막 10행 보여줌
- 옵션으로 -5 붙이면 5개 행 만 보여줌

### more
텍스트로 작성된 파일 화면에 페이지 단위로 출력
- 스페이스 누르면 다음페이지
- 그만두려면 q
### less
- more보다 확장된 기능 방향키, 스페이스 다 먹음

### file
어떤 종류의 파일인지 표시
```
[root@localhost ~]# file anaconda-ks.cfg 
anaconda-ks.cfg: ASCII text
[root@localhost ~]# file /dev/sr0
/dev/sr0: block special (11/0)

```
