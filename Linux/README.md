## 마운트

- man 명령어 : 도움말
```python
man {명령어}
# {명령어}에 대한 도움말 출력
```

- mkdir
``` python
mkdir /media/cdrom
#/media/cdrom 디렉토리 생성
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
- ls
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
- cd
```
[root@localhost ~]# cd /
// / 디렉토리로 이동, / 란 윈도우의 c 드라이브와 비슷 

[root@localhost /]# cd
[root@localhost ~]# pwd
/root
// 현재 사용자의 홈 디렉토리, root 사용자라면 /root로 이동

```
  - cd 후엔 pwd 명령어로 작업중인 디렉토리를 확인하자!
