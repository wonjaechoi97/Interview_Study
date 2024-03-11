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

## 리눅스 기본 명령어(1)
- ls 
