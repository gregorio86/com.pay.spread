# com.pay.spread

## 뿌리기 기능 구현

### 개발 환경
------------------------------
1. IDE
  - Spring Tool Suite 4 
2. 사용 언어 및 Library
  - JavaSE-1.8 / MongoDB / SpringBoot / JWT
3. 테스트
  - vscode-restclient: com.pay.spread/Kakao Spread API.http


### 문제 해결 전략
------------------------------
. 뿌리기, 받기, 조회 기능의 각 REST API 구현
. X-USER-ID, X-ROOM-ID 를 HTTP header 로 받게 됨
. 다수의 서버에 다수의 인스턴스로 동작하도록 jwt 기능을 추가
  - 단 db 구성 부족으로 아래 모든 api 는 permitAll 처리
. 각 기능 및 제약사항에 대한 단위 테스트는 별도로 unit test case 로 작성하지 않고 restclient 를 이용한 API 테스트 기능만 구현

### API 설명
------------------------------
1. 뿌리기
```
PUT {{protocol}}://{{host}}/spreadMoney?spreadTargetAmt={{SPREAD_AMT_TEST}}&spreadTargetCnt={{SPREAD_USER_CNT_TEST}} HTTP/1.1
X-USER-ID: {{X-USER-ID}}
X-ROOM-ID: {{X-ROOM-ID_REAL}}
Content-Type: {{contentType}}
Accept: {{accept}}
```

2. 받기
```
GET {{protocol}}://{{host}}/getRandomMoney?token={{token}} HTTP/1.1
X-USER-ID: {{X-USER-ID-POOR-2}}
X-ROOM-ID: {{X-ROOM-ID_REAL}}
Content-Type: {{contentType}}
Accept: {{accept}}
```

3. 조회
```
GET {{protocol}}://{{host}}/getRandomMoneyList?token={{token}} HTTP/1.1
X-USER-ID: {{X-USER-ID}}
X-ROOM-ID: {{X-ROOM-ID_REAL}}
Content-Type: {{contentType}}
Accept: {{accept}}
```
