# nteam24
---
# Trello 프로젝트 문서

## **목차**
1. [소개](#소개)
2. [사용된 기술](#사용된-기술)
3. [기능](#기능)
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
4. [API 명세서](#API-명세서)
6. [와이어 프레임](#와이어-프레임)
7. [ERD](#ERD)

---

## **소개**

이 프로젝트는 **Trello와 유사한 애플리케이션**으로, 보드, 리스트, 카드 등을 생성하여 프로젝트 및 작업 관리를 할 수 있는 기능을 제공합니다. 또한, 사용자 인증 및 권한 관리 시스템을 포함하고 있으며, 역할 기반으로 사용자 및 워크스페이스 멤버들의 접근을 제어할 수 있습니다. 사용자는 보드를 관리하고 다른 멤버들과 실시간으로 협업할 수 있으며, 주요 이벤트에 대해 알림 기능도 제공합니다.

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

### **검색 기능**

- **카드 검색**
  - 카드의 제목, 내용, 마감일, 담당자 등을 기준으로 검색할 수 있습니다.
  - 특정 보드에 속한 모든 카드를 검색할 수 있습니다.
 

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

![화면 캡처 2024-10-17 161405](https://github.com/user-attachments/assets/44e9c5f0-05b3-4215-820b-d965b59ac531)

