# nteam24
---
# Trello 프로젝트 문서

## **목차**
1. [제품명](#제품명)
   - [개발기간](#개발기간)
   - [배포주소](#배포주소)
   - [프로젝트 소개](#프로젝트-소개)
   - [팀원 소개](#팀원-소개)
2. [사용된 기술](#사용된-기술)
3. [요구사항](#요구사항)
   - [회원가입/로그인](#회원가입로그인)
   - [멤버 및 역할 관리](#멤버-및-역할-관리)
   - [워크스페이스 관리](#워크스페이스-관리)
   - [보드 관리](#보드-관리)
   - [리스트 관리](#리스트-관리)
   - [카드 관리](#카드-관리)
   - [댓글 관리](#댓글-관리)
   - [첨부파일 관리](#첨부파일-관리)
   - [알림 시스템](#알림-시스템)
   - [검색 기능](#검색-기능)
4. [비기능적 요구사항](#비기능적-요구사항)
5. [API 명세서](#API-명세서)
6. [와이어 프레임](#와이어-프레임)
7. [ERD](#ERD)
8. [성능개선](#성능개선)
   - [최적화](#최적화)
   - [동시성처리](#동시성처리)
   - [캐싱](#캐싱)
   - [CICD](#CICD)
9. [트러블 슈팅](#트러블-슈팅)

---

# SpringDashBoard

---

## DashBoard v1.0

> 개발 기간 2024.10.14 ~ 2024.10.18

## 정보

이 프로젝트는 **Trello와 유사한 애플리케이션**으로, 보드, 리스트, 카드 등을 생성하여 프로젝트 및 작업 관리를 할 수 있는 기능을 제공합니다. 또한, 사용자 인증 및 권한 관리 시스템을 포함하고 있으며,
역할 기반으로 사용자 및 워크스페이스 멤버들의 접근을 제어할 수 있습니다. 사용자는 보드를 관리하고 다른 멤버들과 실시간으로 협업할 수 있으며, 주요 이벤트에 대해 알림 기능도 제공합니다.

![노션](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white) [nteam24노션](https://www.notion.so/teamsparta/76b18a5116e943279d0c291445d9972b)

## 팀원

| 이름  | 역할 | GitHub URL                                 | 담당 구현 도메인 및 기능          | 
|-----|----|--------------------------------------------|-------------------------|
| 김창민 | 팀장 | [김창민 깃허브](https://rlackdals.tistory.com/)  | 맴버 역할, 워크스페이스, 젠킨스 CICD | 
| 김민주 | 팀원 | [김민주 깃허브](https://minjooig.tistory.com/)   | 보드, 알림(슬랙), 동시성 처리      | 
| 이동휘 | 팀원 | [이동휘 깃허브](https://velog.io/@anlee/posts)   | 리스트, 검색, 최적화            | 
| 한대규 | 팀원 | [한대규 깃허브](https://hanstory33.tistory.com/) | 회원, 댓글, 알림(디스코드), 캐싱    |
| 송민준 | 팀원 | [송민준 깃허브](https://j34679.tistory.com/)     | 카드, 첨부파일 (S3)           |

---


# **사용된 기술**

Environment

![인텔리제이](   https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![](https://img.shields.io/badge/Gradle-02303a?style=for-the-badge&logo=gradle&logoColor=white)
![](https://img.shields.io/badge/Postman-ff6c37?style=for-the-badge&logo=postman&logoColor=white)
![깃허브](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![깃이그노어](https://img.shields.io/badge/gitignore.io-204ECF?style=for-the-badge&logo=gitignore.io&logoColor=white)
![깃](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)

---

Development

![스프링부트](https://img.shields.io/badge/SpringBoot-6db33f?style=for-the-badge&logo=springboot&logoColor=white)
![자바](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-ED8B00?style=for-the-badge&logo=openjdk&logoColor=yellow)

---

CI/CD

![Jenkis](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white)

___

Database

![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white)

___

Cloud

![AWS](https://img.shields.io/badge/Amazon_AWS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)


___

Communication

![슬랙](  https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![노션](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)

---
# **요구사항**
## **기능**

### **회원가입/로그인**
- **회원가입**
  - **유저 아이디**: 이메일 형식이어야 합니다.
  - **비밀번호**:
    - `Bcrypt`로 인코딩됩니다.
    - 최소 1개의 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.
    - 최소 길이는 8자 이상입니다.
  - **권한**: 일반 유저(`USER`) 또는 관리자(`ADMIN`)로 가입할 수 있습니다.
  - **예외 처리**:
    - 중복된 이메일로 가입할 경우.
    - 이메일 또는 비밀번호 형식이 올바르지 않은 경우.

- **회원탈퇴**
  - 비밀번호 확인 후 일치할 시 탈퇴 처리.
  - 탈퇴한 유저 아이디는 재사용 및 복구가 불가능합니다.
  - **예외 처리**:
    - 유저 아이디와 비밀번호가 일치하지 않는 경우.
    - 이미 탈퇴한 유저 아이디일 경우.

- **로그인**
  - 가입한 아이디와 비밀번호로 로그인.

### **멤버 및 역할 관리**

- **유저 권한**
  - **일반 유저 (`USER`)**
  - **관리자 (`ADMIN`)**
    - 워크스페이스를 생성할 수 있습니다.
    - 일반 유저를 워크스페이스 관리자로 설정할 수 있습니다.

- **멤버 역할**
  - **워크스페이스**
    - 관리자가 설정하며, 워크스페이스 생성을 제외한 모든 기능을 사용할 수 있습니다.
  - **보드**
    - 워크스페이스 관련 기능을 제외한 나머지 기능을 사용할 수 있습니다.
  - **읽기 전용**
    - 생성, 수정, 삭제가 불가능하며, 조회만 가능합니다.

### **워크스페이스 관리**

- **워크스페이스 생성**
  - `ADMIN` 권한을 가진 유저만 생성 가능합니다.
  - 워크스페이스에는 이름과 설명을 설정할 수 있으며, 여러 보드를 포함할 수 있습니다.

- **워크스페이스 멤버**
  - 멤버 초대를 통해 협업이 가능하며, 초대는 회원 가입된 이메일을 통해 이루어집니다.
  - **예외 처리**:
    - 존재하지 않는 이메일로 초대하는 경우.
    - 초대할 권한이 없는 멤버가 초대하는 경우.

- **워크스페이스 조회**
  - 유저가 멤버로 가입된 워크스페이스 목록을 조회할 수 있습니다.

- **워크스페이스 수정 및 삭제**
  - 수정 및 삭제는 해당 워크스페이스에 역할이 있는 멤버만 가능합니다.
  - 삭제 시 워크스페이스 내의 모든 보드 및 데이터를 함께 삭제합니다.

### **보드 관리**

- **보드 생성 및 수정**
  - 멤버는 워크스페이스 내에서 새로운 보드를 생성할 수 있습니다.
  - 보드에는 제목과 배경색 또는 이미지를 설정할 수 있습니다.
  - **예외 처리**:
    - 로그인하지 않은 멤버가 보드를 생성하려는 경우.
    - 제목이 비어 있는 경우.
    - 읽기 전용 역할의 멤버가 보드를 생성/수정하려는 경우.

- **보드 조회**
  - 멤버는 자신이 속한 워크스페이스의 보드를 조회할 수 있습니다.
  - 보드 단건 조회 시 해당 보드의 리스트와 카드도 함께 조회됩니다.

- **보드 삭제**
  - 보드를 삭제하면 해당 보드 내의 리스트와 데이터도 함께 삭제됩니다.
  - **예외 처리**:
    - 읽기 전용 역할의 멤버가 보드를 삭제하려는 경우.

### **리스트 관리**

- **리스트 생성 및 수정**
  - 보드 내에서 리스트를 생성하고 수정할 수 있습니다.
  - **예외 처리**:
    - 읽기 전용 역할의 멤버가 리스트를 생성/수정하려는 경우.

- **리스트 삭제**
  - 리스트 삭제 시 해당 리스트 내의 카드도 함께 삭제됩니다.
  - **예외 처리**:
    - 읽기 전용 역할의 멤버가 리스트를 삭제하려는 경우.

### **카드 관리**

- **카드 생성 및 수정**
  - 리스트 내에서 카드를 생성하고 수정할 수 있습니다.
  - 카드에는 제목, 설명, 마감일, 담당자 등을 추가할 수 있습니다.
  - **예외 처리**:
    - 읽기 전용 역할의 멤버가 카드를 생성/수정하려는 경우.

- **카드 삭제**
  - 카드 삭제 시 해당 카드 내의 모든 데이터도 삭제됩니다.
  - **예외 처리**:
    - 읽기 전용 역할의 멤버가 카드를 삭제하려는 경우.

### **댓글 관리**

- **댓글 작성**
  - 카드 내에서 댓글을 작성할 수 있습니다.
  - **예외 처리**:
    - 읽기 전용 역할의 멤버가 댓글을 작성하려는 경우.

- **댓글 수정/삭제**
  - 댓글 작성자는 자신의 댓글을 수정하거나 삭제할 수 있습니다.
  - **예외 처리**:
    - 작성자가 아닌 멤버가 댓글을 수정/삭제하려는 경우.

### **첨부파일 관리**

- **첨부파일 추가**
  - 카드에 이미지 또는 문서 파일을 첨부할 수 있습니다.
  - **예외 처리**:
    - 지원되지 않는 파일 형식 또는 파일 크기가 초과되는 경우.

- **첨부파일 조회 및 삭제**
  - 첨부된 파일을 조회하고 삭제할 수 있습니다.
  - **예외 처리**:
    - 읽기 전용 역할의 멤버가 파일을 추가/삭제하려는 경우.

### **알림 시스템**

- **알림 설정**
  - 슬랙 또는 디스코드와 연동된 실시간 알림 기능 제공.
  - 슬렉과 디스코드 동시에 알람이 송출됩니다.

- **실시간 알림**
  - 멤버 추가, 카드 변경, 댓글 작성 등 주요 이벤트에 대한 알림 제공.
 
  Slack과 Discord 양방향으로 알림 전송 메서드를 하나의 메서드로 넣고 가변인자를 사용해 메세지를 통일하였습니다.
 
 <img width="544" alt="스크린샷 2024-10-18 오후 7 16 06" src="https://github.com/user-attachments/assets/b2bbc19a-a8e0-4056-82ad-4482cfd80678">

 <img width="425" alt="스크린샷 2024-10-18 오후 7 15 14" src="https://github.com/user-attachments/assets/b5d105ed-f179-4fca-a387-0752c3b37826">

<img width="393" alt="스크린샷 2024-10-18 오후 7 15 28" src="https://github.com/user-attachments/assets/6ca9eb5c-ee78-4e54-9682-7c5689ab3465">

### **검색 기능**

- **카드 검색**
  - 카드의 제목, 내용, 마감일, 담당자 등을 기준으로 검색할 수 있습니다.
  - 특정 보드에 속한 모든 카드를 검색할 수 있습니다.

 

 
## 비기능

1. 조회가 빨랐으면 좋겠어요
2. 많은 유저가 동시에 접근이 되면 좋겠어요
3. 조회수 어뷰징을 막아주세요

---
# API 명세서
## 회원가입 / 로그인 API

| 메서드 | 기능       | URL         | HEADER                        | REQUEST                                               | RESPONSE                                                                                         |
|--------|------------|--------------|------------------------------|----------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| POST   | 회원가입   | /auth/signup  | Content-Type: application/json| ```{ "email": "이메일", "password": "비밀번호", "userRole": "유저 권한" }```                      | ```{ "status": "SUCCESS", "data": { "bearerToken": "Bearer Token" }, "message": "요청 성공" }``` |
| POST   | 로그인     | /auth/signin  | Content-Type: application/json| ```{ "email": "이메일", "password": "비밀번호" }```                                             | ```{ "status": "SUCCESS", "data": { "bearerToken": "Bearer Token" }, "message": "요청 성공" }``` |

## 유저 API

| 메서드  | 기능           | URL                         | HEADER                                  | REQUEST                                                                                               | RESPONSE                                                                                                             |
|---------|----------------|------------------------------|---------------------------------------|--------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------|
| GET     | 사용자 조회     | /user/{userId}               | Authorization: Bearer <JWT Token>     | -                                                                                                                  | ```{ "status": "SUCCESS", "data": { "id": "유저 아이디", "email": "유저 이메일" }, "message": "요청이 성공적으로 처리되었습니다" }``` |
| POST    | 비밀번호 변경   | /user                        | Authorization: Bearer <JWT Token>     | ```{ "oldpassword": "이전 비밀번호", "newpassword": "새 비밀번호" }```                                             | ```{ "status": "SUCCESS", "message": "비밀번호가 성공적으로 변경되었습니다" }```                                        |
| DELETE  | 회원 탈퇴       | /user                        | Authorization: Bearer <JWT Token>     | ```{ "password": "비밀번호" }```                                                                                   | ```{ "status": "SUCCESS", "data": "회원탈퇴가 정상적으로 완료되었습니다.", "message": "요청이 성공적으로 처리되었습니다" }``` |
| POST    | 사용자 권한 변경| /admin/users/{userId}        | Authorization: Bearer <JWT Token>     | -                                                                                                                  | ```{ "status": "SUCCESS", "data": "유저 권한이 정상적으로 변경되었습니다.", "message": "요청이 성공적으로 처리되었습니다" }``` |
## 워크스페이스 API

| 메서드  | 기능                        | URL                                                          | HEADER                                   | REQUSET                                                                                                                                      | RESPONSE                                                                                                                          |
|---------|-----------------------------|----------------------------------------------------------------|----------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------|
| POST    | 워크스페이스 생성             | /workspaces                                                     | Authorization: Bearer <JWT Token>      | ```{ "name": "워크스페이스 이름", "description": "워크스페이스 설명" }```                                                                                  | ```{ "status": "SUCCESS", "message": "요청이 성공적으로 처리되었습니다", "data": { "id": "워크스페이스 id", "name": "워크스페이스 name" } }``` |
| POST    | 워크스페이스 관리자 설정      | /workspaces/{workspaceId}/users/{userId}/admin                  | Authorization: Bearer <JWT Token>      | -                                                                                                                                                        | ```{ "status": "SUCCESS", "data": "관리자 설정 완료", "message": "요청이 성공적으로 처리되었습니다" }```                                   |
| POST    | 워크스페이스에 사용자 초대    | /workspaces/{workspaceId}/users/{userId}/invitations            | Authorization: Bearer <JWT Token>      | -                                                                                                                                                        | ```{ "status": "SUCCESS", "message": "요청이 성공적으로 처리되었습니다", "data": { "email": "초대한 멤버 이메일" } }```                      |
| POST    | 워크스페이스 초대 수락        | /workspaces/{workspaceId}/invitations/accept                    | Authorization: Bearer <JWT Token>      | -                                                                                                                                                        | ```{ "status": "SUCCESS", "data": "초대 수락 완료", "message": "요청이 성공적으로 처리되었습니다" }```                                   |
| POST    | 워크스페이스 초대 거절        | /workspaces/{workspaceId}/invitations/reject                    | Authorization: Bearer <JWT Token>      | -                                                                                                                                                        | ```{ "status": "SUCCESS", "data": "초대 거절 완료", "message": "요청이 성공적으로 처리되었습니다" }```                                   |
| GET     | 본인이 속한 워크스페이스 조회 | /users/me/workspaces                                            | Authorization: Bearer <JWT Token>      | -                                                                                                                                                        | ```{ "status": "SUCCESS", "message": "요청이 성공적으로 처리되었습니다", "data": [ { "id": "워크스페이스 id", "name": "워크스페이스 name" } ] }``` |
| PUT     | 워크스페이스 수정             | /workspaces/{workspaceid}                                       | Authorization: Bearer <JWT Token>      | ```{ "name": "수정 후 이름", "description": "수정 후 설명" }```                                                                                           | ```{ "status": "SUCCESS", "message": "요청이 성공적으로 처리되었습니다", "data": { "id": "워크스페이스 id", "name": "워크스페이스 name", "description": "워크스페이스 설명" } }``` |
| DELETE  | 워크스페이스 삭제             | /workspaces/{workspaceid}                                       | Authorization: Bearer <JWT Token>      | -                                                                                                                                                        | ```{ "status": "SUCCESS", "message": "요청이 성공적으로 처리되었습니다", "data": { "id": "워크스페이스 id", "name": "워크스페이스 name" } }``` |

## 보드 API

| 메서드  | 기능              | URL                                                    | HEADER                                   | REQUEST                                                                                              | RESPONSE                                                                                                                                                                                                                 |
|---------|-------------------|---------------------------------------------------------|----------------------------------------|--------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST    | 보드 등록          | /board/workspace/{workspaceId}                          | Authorization: Bearer <JWT Token>      | ```{ "workspaceId": "{workspaceId}", "titile": "{board_title}", "background": "{new_background_color_or_image_url}" }``` | ```{ "status": "SUCCESS", "message": "보드 등록 성공", "data": { "board_id": "{new_board_id}", "title": "{board_title}", "background": "{new_background_color_or_image_url}", "createdAt": "2024-10-14T12:34:56" } }```       |
| UPDATE  | 보드 수정          | /board/{boardId}                                        | Authorization: Bearer <JWT Token>      | ```{ "titile": "String title", "background": "{new_background_color_or_image_url}" }```                           | ```{ "status": "SUCCESS", "message": "보드 수정 성공", "data": { "title": "String title", "background": "{new_background_color_or_image_url}", "modifiedAt": "2024-10-14T17:34:56" } }```                                    |
| GET     | 보드 목록 조회      | /workspace/{workspaceId}/board                          | Authorization: Bearer <JWT Token>      | -                                                                                                                  | ```{ "status": "SUCCESS", "message": "보드 목록 조회 성공", "data": [ { "boardId": "{boardId1}", "title": "{board_title1}", "background": "{background_color_or_image_url_1}" }, { "boardId": "{boardId2}", "title": "{board_title2}", "background": "{background_color_or_image_url_2}" } ] }``` |
| GET     | 보드 단건 조회      | /board/{boardId}                                        | Authorization: Bearer <JWT Token>      | -                                                                                                                  | ```{ "status": "SUCCESS", "message": "보드 단건 조회 성공", "data": { "boardId": "{boardId}", "title": "{board_title}", "background": "{background_color_or_image_url}", "lists": [ { "listId": "{listId1}", "name": "{list_name_1}", "cards": [ { "cardId": "{cardId1}", "title": "{card_title_1}", "description": "{card_description_1}" } ] } ] } }``` |
| DELETE  | 보드 삭제          | /board/{boardId}                                        | Authorization: Bearer <JWT Token>      | -                                                                                                                  | ```{ "status": "SUCCESS", "message": "보드 삭제 성공" }```                                                                                                                                                                   |
## 리스트 API

| 메서드  | 기능              | URL                                                    | HEADER                                   | REQUEST                                                                                             | RESPONSE                                                                                                                                                                                                                 |
|---------|-------------------|---------------------------------------------------------|----------------------------------------|--------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST    | 리스트 생성        | /boards/{boardId}/lists?pos=2                          | Authorization: Bearer <JWT Token>      | ```{ "title": "" }```                                                                                             | ```{ "status": "SUCCESS", "data": { "title": "test", "pos": "2" }, "message": "요청이 성공적으로 처리되었습니다" }```                                                              |
| PATCH   | 리스트 수정        | /boards/{boardId}/lists/{listId}                       | Authorization: Bearer <JWT Token>      | ```{ "title": "" }```                                                                                             | ```{ "status": "SUCCESS", "data": { "title": "test", "pos": "2", "cardList": { "title": "test", "deadline": "0000-00-00" } }, "message": "요청이 성공적으로 처리되었습니다" }```                            |
| PATCH   | 리스트 순서 변경    | /boards/{boardId}/lists/{listId}/pos?pos=3            | Authorization: Bearer <JWT Token>      | -                                                                                                                  | ```{ "status": "SUCCESS", "data": { "title": "test", "pos": "3", "cardList": { "title": "test", "deadline": "0000-00-00" } }, "message": "요청이 성공적으로 처리되었습니다" }```                            |
| GET     | 리스트 단건 조회    | /boards/{boardId}/lists/{listId}                       | Authorization: Bearer <JWT Token>      | -                                                                                                                  | ```{ "status": "SUCCESS", "data": { "title": "test", "pos": "2", "cardList": { "title": "title", "contents": "contents", "deadline": "0000-00-00", "manager": "xxx", "activityLog": "——-", "comments": {} } }, "message": "요청이 성공적으로 처리되었습니다" }``` |
| DELETE  | 리스트 삭제        | /boards/{boardId}/lists/{listId}                       | Authorization: Bearer <JWT Token>      | -                                                                                                                  | ```{ "status": "SUCCESS", "data": { "message": "삭제가 완료되었습니다." }, "message": "요청이 성공적으로 처리되었습니다" }```                                                                                                                                     |
## 카드 API

| 메서드  | 기능              | URL                         | HEADER                                  | REQUEST                                                                                    | RESPONSE                                                                                                                                                                                              |
|---------|-------------------|------------------------------|---------------------------------------|-------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST    | 카드 생성         | /cards                       | Authorization: Bearer <JWT Token>     | ```{ "managerId": "1", "listId": "1", "title": "title", "contents": "contents", "deadline": "0000-00-00" }```                      | 카드 생성이 완료되었습니다.                                                                                                                                                                            |
| GET     | 카드 단건 조회      | /cards/{id}                  | Authorization: Bearer <JWT Token>     | -                                                                                                           | ```{ "title": "title", "contents": "contents", "deadline": "0000-00-00", "manager": "xxx", "activityLog": "——-", "comments": {} }```                                                                                         |
| PATCH   | 카드 수정         | /cards/{id}                  | Authorization: Bearer <JWT Token>     | ```{ "managerId": "xxx", "listId": "1", "title": "title", "contents": "contents", "deadline": "0000-00-00" }``` | 카드 수정이 완료되었습니다.                                                                                                                                                                            |
| DELETE  | 카드 삭제         | /cards/{cardId}              | Authorization: Bearer <JWT Token>     | -                                                                                                           | 카드 삭제가 완료되었습니다.                                                                                                                                                                            |
| POST    | 첨부파일 등록      | /cards/{cardId}/attach       | Authorization: Bearer <JWT Token>     | -                                                                                                           | -                                                                                                                                                                                                      |
| GET     | 첨부파일 조회      | /cards/{cardId}/attach       | Authorization: Bearer <JWT Token>     | -                                                                                                           | -                                                                                                                                                                                                      |
| DELETE  | 첨부파일 삭제      | /cards/{cardId}/attach       | Authorization: Bearer <JWT Token>     | ```{ "fileName": "" }```                                                                                  | -                                                                                                                                                                                                      |
| GET     | 카드 검색         | /cards/search?keyword=test    | Authorization: Bearer <JWT Token>     | -                                                                                                           | ```{ "status": "SUCCESS", "data": { "card1": { "title": "title", "contents": "contents", "deadline": "0000-00-00", "manager": "xxx" }, "card2": { "title": "title", "contents": "contents", "deadline": "0000-00-00", "manager": "xxx" } }, "message": "요청이 성공적으로 처리되었습니다" }``` |

## 댓글 API

| 메서드  | 기능              | URL                           | HEADER                                  | RESQUSET                                                                                                 | RESPONSE                                                                                                                                                                                               |
|---------|-------------------|---------------------------------|---------------------------------------|-------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST    | 댓글 생성         | /cards/{cardId}/comments        | Authorization: Bearer <JWT Token>    | ```{ "content": "{comment_text}", "emoji": "{emoji_1}" }```                                                               | ```{ "status": "SUCCESS", "message": "댓글 등록 성공", "data": { "commentId": "{new_comment_id}", "content": "{comment_text}", "emoji": "{emoji_1}", "userId": "{userId}", "cardId": "{cardId}", "createdAt": "2024-10-14T12:34:56" } }``` |
| GET     | 댓글 조회         | /comments/{commentId}           | Authorization: Bearer <JWT Token>    | -                                                                                                                       | -                                                                                                                                                                                                      |
| PATCH   | 댓글 수정         | /comments/{commentId}           | Authorization: Bearer <JWT Token>    | ```{ "content": "{comment_text}", "emoji": "{emoji}" }```                                                                 | ```{ "status": "SUCCESS", "message": "댓글 수정 성공", "data": { "commentId": "{new_comment_id}", "content": "{comment_text}", "emoji": "{emoji}", "userId": "{userId}", "cardId": "{cardId}", "updatedAt": "2024-10-14T12:34:56" } }``` |
| DELETE  | 댓글 삭제         | /comments/{commentId}           | Authorization: Bearer <JWT Token>    | -                                                                                                                       | -                                                                                                                                                                                                      |


# 와이어 프레임

![image (12)](https://github.com/user-attachments/assets/6e0ec47a-dae0-42bd-8c8c-9bacbf536fe7)

# ERD

![image](https://github.com/user-attachments/assets/d5859cf5-c8d6-4146-bebe-d6418ead2de5)

# 성능개선
---
## 최적화

### 개선 대상 쿼리와 해당 쿼리 선택 이유
1. 기존 title과 contents를 별도로 검색하여 포괄적인 검색 제공 불가능
   -> 통합 keyword로 더 직관적이고 필요한 데이터만 정확히 조회하도록 수정

2. 기존 LeftJoin을 사용해서 불필요한 Null 데이터 포함하여 성능 저하
   가능성 존재 -> InnerJoin으로 변경하여 필요한 데이터만 정확히 조회하도록 수정

3. 데이터 양이 많을 때 오프셋 기반 페이징은 성능 저하 및 중복/누락 
   데이터가 발생 가능성이 있음 -> 커서 기반 페이징으로 변경하여
   대용량 데이터에서도 일관된 성능 보장하도록 수정

### 인덱스 설정 DDL 쿼리
```sql
CREATE INDEX idx_card_title_contents ON cards (title, contents); -- 카드의 제목과 내용을 함께 검색할 때 성능 향상
CREATE INDEX idx_card_deadline ON cards (deadline); -- 마감일 기준 검색 및 정렬 성능 개선
CREATE INDEX idx_card_manager ON cards (user_id); -- 특정 관리자의 카드 검색 성능 향상
CREATE INDEX idx_lists_board ON lists (board_id); -- 특정 보드의 리스트 검색 성능 개선
CREATE INDEX idx_board_workspace ON board (workspace_id); -- 특정 워크스페이스의 보드 검색 최적화
```
### 쿼리 속도 비교 (before, after)
- Before: Response Time 360 ms
- After: Response Time 98 ms
  
![쿼리 최적화 적용 x](https://github.com/user-attachments/assets/dbac3a55-2e35-464c-b039-842a850bbd37)
![인덱스 적용 및 쿼리 최적화](https://github.com/user-attachments/assets/83d30863-8194-4e82-9454-acaa19849f33)


## 동시성처리

<details>
  <summary>표준 상태</summary>
  
- Number of Threads (users) : 1000

- Ramp-up period (seconds) : 10

- Lopp Count : 2

- 소요시간 : 10sec

**샘플 수**: patch_cards에 대한 총 샘플 수는 **2000**

### **응답 시간**:

**평균 응답 시간**: 4 **ms**

**중앙값 응답 시간**: **4 ms**

**90%** : 6 **ms**

**95%** : 7 **ms**

**99%** : 12 **ms**

**최소 응답 시간**: **2 ms**

**최대 응답 시간**: 41 **ms**

### **처리량**:

**처리량**: **200.0 요청/초**

**수신 KB/sec**: **94.90**

**전송 KB/sec**: **104.47**

### **오류율**:

**오류 비율**: **0.00%** (테스트 중 오류가 발생하지 않음)

**그래프 분석**

**평균과 중앙값**: 두 값 모두 낮아, 대부분의 요청이 빠르게 완료됨을 나타냅니다. 평균(5 ms)은 중앙값(4 ms)보다 높습니다.

**백분위수**:

90%의 요청이 6 ms 이하로 완료되었음을 나타냅니다.

95%의 요청이 7 ms 이하로 완료되었음을 나타내며, 

99%의 요청이 41 ms 이하로 완료되었음을 의미합니다.

### **결론**

전체적으로 patch_cards 작업은 빠른 응답 시간을 보이며, 오류가 없어서 안정성이 높습니다.

응답 시간의 분포는 대체로 양호하며, 특정 요청에서의 지연이 평균에 영향을 미쳤을 가능성이 있습니다. 이를 바탕으로 추가적인 최적화를 고려할 수 있습니다.

![스크린샷 2024-10-18 오전 1 57 50](https://github.com/user-attachments/assets/88ce9416-3f2f-47b4-ae4c-513a5e8f0515)

![스크린샷 2024-10-18 오전 1 57 58](https://github.com/user-attachments/assets/a3a2edf5-63b8-4a87-9660-db32e8aa595b)

</details>

<details>
  <summary>낙관적 락</summary>
  
- Number of Threads (users) : 1000

- Ramp-up period (seconds) : 10

- Lopp Count : 2

- 소요시간: 10sec

![스크린샷 2024-10-18 오전 12 23 33](https://github.com/user-attachments/assets/5f575f89-2407-483a-af9e-467c3ef0b81c)

**샘플 수**: patch_cards에 대한 총 샘플 수는 **2000**

### **응답 시간**:

**평균 응답 시간**: **5 ms**

**중앙값 응답 시간**: **4 ms**

**90%** : **7 ms**

**95%** : **14 ms**

**99%** : **28 ms**

**최소 응답 시간**: **2 ms**

**최대 응답 시간**: **37 ms**

### **처리량**:

**처리량**: **200.0 요청/초**

**수신 KB/sec**: **94.93**

**전송 KB/sec**: **104.50**

### **오류율**:

**오류 비율**: **0.00%** (테스트 중 오류가 발생하지 않음)

**그래프 분석**

**평균과 중앙값**: 두 값 모두 낮아, 대부분의 요청이 빠르게 완료됨을 나타냅니다. 평균(5 ms)은 중앙값(4 ms)보다 높습니다.

**백분위수**:

90%의 요청이 7 ms 이하로 완료되었음을 나타냅니다.

95%의 요청이 14 ms 이하로 완료되었음을 나타내며, 

99%의 요청이 28 ms 이하로 완료되었음을 의미합니다.

### **결론**

전체적으로 patch_cards 작업은 빠른 응답 시간을 보이며, 오류가 없어서 안정성이 높습니다.

응답 시간의 분포는 대체로 양호하며, 특정 요청에서의 지연이 평균에 영향을 미쳤을 가능성이 있습니다. 이를 바탕으로 추가적인 최적화를 고려할 수 있습니다.

![스크린샷 2024-10-18 오전 12 23 39](https://github.com/user-attachments/assets/57926060-2905-4d34-879f-39d5343fe0d9)

![스크린샷 2024-10-18 오전 12 24 46](https://github.com/user-attachments/assets/95bbecbc-1c5a-4e91-8d33-0932a8bca19b)

@Version을 통해 낙관락 구현

여러 사용자가 동시에 같은 카드를 수정할 경우 마지막으로 저장된 버전이 우선시되고, 다른 사용자는 충돌 알림을 받을 수 있습니다.
  
</details>

<details>
  <summary>비관적 락</summary>
  
  여기에 토글 안에 들어갈 내용을 작성합니다.

- Number of Threads (users) : 1000

- Ramp-up period (seconds) : 10

- Lopp Count : 2

- 소요시간: 10sec

  ![스크린샷 2024-10-18 오전 12 27 49](https://github.com/user-attachments/assets/40d7d2b5-205e-400d-af74-2fb346219a53)

  **샘플 수**: patch_cards에 대한 총 샘플 수는 **2000**

### **응답 시간**:

**평균 응답 시간**: 16 **ms**

**중앙값 응답 시간**: 3 **ms**

**90%** : 6 **ms**

**95%** : 62 **ms**

**99%** : 446 **ms**

**최소 응답 시간**: **2 ms**

**최대 응답 시간**: 613 **ms**

### **처리량**:

**처리량**: **200.1 요청/초**

**수신 KB/sec**: **94.93**

**전송 KB/sec**: **104.53**

### **오류율**:

**오류 비율**: **0.00%** (테스트 중 오류가 발생하지 않음)

**그래프 분석**

**평균과 중앙값**: 두 값 모두 낮아, 대부분의 요청이 빠르게 완료됨을 나타냅니다. 평균(16 ms)은 중앙값(3 ms)보다 높습니다.

**백분위수**:

90%의 요청이 3 ms 이하로 완료되었음을 나타냅니다.

95%의 요청이 62 ms 이하로 완료되었음을 나타내며, 

99%의 요청이 446 ms 이하로 완료되었음을 의미합니다.

### **결론**

전체적으로 patch_cards 작업은 빠른 응답 시간을 보이며, 오류가 없습니다. 하지만

응답 시간의 분포는 90%부분으로 편향되는 특징을 가지며 이는 서버가 불안정해지는 모습을 보이고 있습니다. 이를 바탕으로 추가적인 최적화를 고려할 수 있습니다.

![스크린샷 2024-10-18 오전 12 27 56](https://github.com/user-attachments/assets/80b56938-cc8a-4136-8c9e-90f71b269bc8)

![스크린샷 2024-10-17 오후 11 31 25](https://github.com/user-attachments/assets/f764d7b7-f51e-4b03-ba8d-28cf28efceea)

Lock 애너테이션을 사용하여 특정 조회 메소드에 락을 설정합니다. 일반적으로 PESSIMISTIC_WRITE 또는 PESSIMISTIC_READ 옵션을 사용합니다.

비관적 락은 데이터베이스에서 직접 잠금을 거는 것이므로, 데이터에 대한 접근을 조정할 수 있습니다.

## 주의사항

**성능 고려**: 비관적 락은 성능에 영향을 미칠 수 있습니다. 데이터베이스에서 락을 설정하고 해제하는 과정에서 지연이 발생할 수 있습니다.

**데드락**: 비관적 락을 사용할 때는 데드락이 발생할 수 있으므로, 트랜잭션의 순서를 잘 관리해야 합니다.

**락 해제**: 트랜잭션이 끝나면 자동으로 락이 해제되지만, 트랜잭션이 롤백되는 경우에도 락은 해제됩니다.

</details>

<details>
  <summary>분산 락</summary>
  
- Number of Threads (users) : 1000

- Ramp-up period (seconds) : 10

- Lopp Count : 2

- 소요시간: 10sec
  
![스크린샷 2024-10-18 오전 12 46 30](https://github.com/user-attachments/assets/376dfc1e-c46f-48d6-953f-fa94c9566d05)

**샘플 수**: patch_cards에 대한 총 샘플 수는 **2000**

### **응답 시간**:

**평균 응답 시간**: 9 **ms**

**중앙값 응답 시간**: 5 **ms**

**90%** : 20 **ms**

**95%** : 43 **ms**

**99%** : 62 **ms**

**최소 응답 시간**: **2 ms**

**최대 응답 시간**: 85 **ms**

### **처리량**:

**처리량**: 199.9 **요청/초**

**수신 KB/sec**: **94.89**

**전송 KB/sec**: **104.46**

### **오류율**:

**오류 비율**: **0.00%** (테스트 중 오류가 발생하지 않음)

**그래프 분석**

**평균과 중앙값**: 두 값 모두 낮아, 대부분의 요청이 빠르게 완료됨을 나타냅니다. 평균(9 ms)은 중앙값(5 ms)보다 높습니다.

**백분위수**:

90%의 요청이 20 ms 이하로 완료되었음을 나타냅니다.

95%의 요청이  ms 이하로 완료되었음을 나타내며, 

99%의 요청이 446 ms 이하로 완료되었음을 의미합니다.

### **결론**

전체적으로 patch_cards 작업은 빠른 응답 시간을 보이며, 오류가 없어서 안정성이 높습니다.

응답 시간의 분포는 대체로 양호하며, 특정 요청에서의 지연이 평균에 영향을 미쳤을 가능성이 있습니다. 이를 바탕으로 추가적인 최적화를 고려할 수 있습니다.

![스크린샷 2024-10-18 오전 12 46 37](https://github.com/user-attachments/assets/95f0e548-d4c7-4395-8600-894f298966b8)

분산 락(Distributed Lock)을 사용해야 하는 경우는 여러 서버 또는 인스턴스에서 동시에 접근하는 데이터에 대한 동기화가 필요할 때입니다.

**즉, 분산 락**은 여러 서버에서 동시에 데이터를 수정할 때 충돌을 방지하는 데 유용합니다.

**Redisson** 라이브러리를 사용하여 Redis를 기반으로 간단하게 분산 락을 구현하였습니다.

![스크린샷 2024-10-18 오전 1 15 17](https://github.com/user-attachments/assets/c546b551-eaa0-4845-b2ab-5d5cc9a3c21c)

## 코드 설명

**Lock Creation**:

RLock lock = redissonConfig.redissonClient().getLock("cardLock:" + cardId);

Redis의 분산 잠금을 사용하여 특정 카드에 대한 잠금을 생성합니다. cardLock:<cardId> 형태로 고유한 잠금 키를 생성하여 각 카드에 대해 개별적으로 잠금을 설정합니다.

**Lock Acquisition**:

if (lock.tryLock(10, TimeUnit.SECONDS)) { ... }tryLock 메서드는 지정된 시간(여기서는 10초) 내에 잠금을 획득하려고 시도합니다. 만약 잠금을 획득하지 못하면 예외를 발생시킵니다. 이 방법은 다른 사용자가 해당 카드를 수정 중일 때, 동시에 수정하지 않도록 하기 위해 사용됩니다.

**Business Logic**:

사용자가 카드의 리스트를 조회하여 존재하지 않을 경우 NotFoundListsException을 발생시킵니다.

사용자의 역할을 확인하여 읽기 권한만 있는 사용자가 카드를 수정하려 할 경우 BadAccessUserException을 발생시킵니다.

카드를 업데이트한 후, 카드의 변경 사항에 대한 알림을 전송합니다.

**Lock Release**:

lock.unlock();

작업이 완료되면 잠금을 해제하여 다른 사용자들이 해당 카드를 수정할 수 있도록 합니다.

![스크린샷 2024-10-18 오전 1 14 46](https://github.com/user-attachments/assets/e4260a75-4f88-4f89-abd5-f9f3a6422af6)

</details>

이렇게 3개의 동시성 제어를 Jmeter를 통해 서버 부하 테스트를 통해 결론적으로 낙관적 락이 더 적합하다는 판정이 나와 낙관 락을 사용하게 되었습니다.

<img width="574" alt="스크린샷 2024-10-18 오후 7 06 45" src="https://github.com/user-attachments/assets/2e945a48-cf9c-4523-bd85-a03f519194d0">

- 그리고 낙관적 락은 잠금을 사용하지 않기 때문에 트랜잭션 간의 동시성을 최대화할 수 있고, 여러 사용자가 동시에 같은 데이터를 읽는 것은 자유롭고, 수정이 일어나기 직전에만 검증을 하기 때문에 대기 시간이 줄어드는 점과.
- 비관적 락(Pessimistic Lock)은 데이터를 잠금 상태로 두어, 트랜잭션 간에 데드락(Deadlock)이 발생할 가능성이 있다. 하지만 낙관적 락은 잠금을 사용하지 않기 때문에 데드락 발생 위험이 없는 결론과
- 낙관적 락은 버전 번호나 타임스탬프 기반으로 데이터를 검증하므로, 락을 설정하고 관리할 필요가 없고, 그 결과 데이터베이스 부하가 적다.

그리고 무엇보다 동시에 게시물을 수정하는 상황은 드물기에 여러 의견과 테스트 결과물에 따라 낙관락 정도면 충분하다는 결론에 도달하게 되었습니다.

---

## 캐싱

---

## CICD

---
# 트러블 슈팅

### 깃허브 보안 오류로 인한 dev 브랜치 오류 발생 
- yml 파일의 커밋으로 인한, Github 보안 오류 발생
- git filter-branch -f —prune-empty —index-filter”git rm -r —cached —ignore-unmatch **/application.yml” HEAD 명령어로 인한 yml 파일이 포함된 커밋 모두 증발
- 최신 브랜치인 feature/comment를 dev 브랜치로 설정

### 잦은 디스코드 API 호출로 인해 일시적인 호출 차단

- 서버 부하 테스트를 할때는 외부 API는 주석처리후 실행할 것
![스크린샷 2024-10-18 오전 11 36 19](https://github.com/user-attachments/assets/9791289c-e9ec-45b0-874f-5c736eae7707)

## 처음 사용하는 Tool들의 설정 문제
### Jenkins 
권한 부여, env파일 처리, 젠킨스-도커 연결 등.. -> 구글링으로 해결
EC2프리티어 사용으로 인한 메모리 부족.. -> 노드 정리 자주해주기

![스크린샷 2024-10-16 오전 11 07 01](https://github.com/user-attachments/assets/1d2dd803-9662-47be-86e1-8f0da2d02cbb)

![스크린샷 2024-10-16 오전 11 17 42](https://github.com/user-attachments/assets/c9a04aa3-881e-40db-9e7a-389b672a59c6)

### Jmeter 
- Patch cards에서 request값에 맞추었지만 403에러가 발생하였다.
- 포스트맨처럼 Bearer Token을 받아와야 하지만 토큰 값을 주입 안했기에 발생하였다.
- config의 HTTP Header Manager에서 Authorization에 토큰값을 적용하니 403에러 문제를 해결하였다.

<img width="1170" alt="스크린샷 2024-10-18 오후 1 42 46" src="https://github.com/user-attachments/assets/b31c56e9-2672-4958-acc7-3267e939af7f">


  



