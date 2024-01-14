## 🌐 테스트 프로젝트 저장소

### 프로젝트 개요

다양한 주제의 학습 내용 및 프로젝트 개발 진행

### Chapter 1

- 주제: tus 프로토콜을 이용한 동영상 업로드
- 패키지: com.network.network_project.video

빌드 & 실행 방법

```
1. 프로젝트 경로 이동
2. 환경에 맞게 아래 명령어 실행  
  - 윈도우: gradlew.bat 
  - linux: ./gradlew
3. 아래 명령어 수행 
    - gradlew clean bootWar
    
만약, intellij, eclipse 등의 IDE를 사용하는 경우, 
해당 ide 내에서 프로젝트 로드 및 UI 내에서 gradle task (clean, bootWar) 수행    

4. build/libs/network_project-0.0.1-SNAPSHOT.war 파일을 아래 java 명령어를 통해 실행 
    - (주의) java 17 이상 버전에서 실행 가능 (설치 필요)
    - java -jar network_project-0.0.1-SNAPSHOT.war
    - ctrl + c 커맨드를 통해 종료 가능 
    - 만약, 백그라운드에서 실행을 원할 경우, 다음 명령어 수행, 종료 시에는 kill -9 ${PID}를 통해 직접 종료
      - java -jar network_project-0.0.1-SNAPSHOT.war &

5. 아래와 같이, URL을 입력 후에, 화면에 접속 한다. 
    - ex) http://localhost:8585 | http://192.168.35.98:8585/
    - ex) http://localhost:8585/upload_video, http://192.168.35.98:8585/upload_video
    - 디폴트 port 넘버는 8585 이지만, 각자 환경에 맞게 커스터마이징
```

<br>

환경 변수 설정 방법

```
application.yml 파일 내에 아래 값을 설정해주도록 한다. 

파일 업로드 서버는 직접 구축 필요 (aws ec2 | azure | vm 등등)
위 프로젝트 빌드 결과물(*.war)은 파일 업로드 서버 내에서 실행하도록 한다. (* tus 프로토콜 특성 및 장점(힙메모리, 성능 ..)을 최대화 하기 위해)

server_protocol 값의 경우, http | https 중에 선택 
server_address: 파일 업로드 서버 주소 
upload_type: 디폴트 값은 videos, but 필요에 따라 커스터마이징 
upload_directory: 업로드 된 데이터들이 저장될 디스크 디렉토리 입력 
save_directory: 최종 업로드 결과물이 저장 될 디스크 디렉토리 입력
expiration: 만료될 업로드 데이터(upload_directory에 저장 된)가 만료 후에 스케쥴러를 통해 삭제 되기까지의 만료 유효 기간 설정 (디폴트: 1분) 

tus_util:
  server_protocol: {}
  server_address: {} 
  upload_type: {}
  upload_directory: {}
  save_directory: {}
  expiration: {}
```




