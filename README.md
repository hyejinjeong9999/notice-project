# 개발환경

* swagger 3
* h2
  * Spring jpa
  * Query dsl
* Spring 2.6.4
* Junit5
* Redis 6.2.6

# 실행 방법

* **h2와 redis가 필요합니다**
  * H2
    * localhost
    * db name : testdb
    * username : sa (password 없음)
  * redis
    * localhost
    * Port 6379
* **파일 경로 수정이 필요합니다**
  * application.yml `file_dir`수정
  
* **qfile 생성 오류가 나면**
  * queyrdsl qfile이 생성되지 않아서 나는 오류입니다
  * gradle - `bootJarMainClassName`을 실행해 큐파일을 생성하면 정상적으로 동작됩니다.



# 실행 절차

1. Swagger를 실행 시킵니다
   * [swagger page](http://localhost:8080/swagger-ui/index.html#)

2. [유저 회원가입](http://localhost:8080/swagger-ui/index.html#/users/createUser)을 합니다

   * 관리저 유저를 가입시키기 위한 용도로 이미 저장되어 있는 유저 5명을 저장하게 됩니다
     * testUser1만 관리자로 공지사항을 등록할 권한이 있습니다.

   * <img width="688" alt="1" src="https://user-images.githubusercontent.com/58923740/156924099-1655c07a-bce4-4554-a3e9-2effe529fbbc.png">

3. [공지사항 리스트](http://localhost:8080/swagger-ui/index.html#/notices/getNoticeList)를 출력합니다
   * 현재 저장된 데이터가 없기 때문에 빈 리스트가 결과로 나옵니다
   * <img width="236" alt="2" src="https://user-images.githubusercontent.com/58923740/156924103-eafc37a6-0cd2-4d0c-b1bc-6ed9f6072307.png">

4. [공지사항 추가](http://localhost:8080/swagger-ui/index.html#/notices/createNotice) 로 공지사항을 등록합니다

   > **참고사항**
   > 현재 소스상으로는 `JsonFormat` 을 이용해 String 데이터를 LocalDateTime으로 변환하게 되어있지만
   > swagger상에서는 LocaldateTime 형태로 인식되어  `noticeStartDate`와 `noticeEndDate`에 제대로된 테스트 데이터가 들어가 있지 않습니다
   > **yyyy-MM-dd HH:mm:ss** 형태로 넣어야 합니다

   * 등록 예시데이터

   ```json
   {
     "userId": "testUser1",
     "title": "공지사항입니다",
     "contents": "내용입니다",
     "noticeStartDate": "2022-03-04 11:00:00",
     "noticeEndDate": "2022-04-04 11:00:00"
   }
   ```

   * 첨부파일 첨부 가능

     <img width="522" alt="3" src="https://user-images.githubusercontent.com/58923740/156924105-db84723f-84d5-4e31-8049-fc839e4378f7.png">

   * 등록 결과

     * notice와 file에 정상적으로 저장됩니다.
     * <img width="998" alt="4" src="https://user-images.githubusercontent.com/58923740/156924106-5adac202-2157-4938-b49f-39fe41d1a495.png">

     * <img width="589" alt="5" src="https://user-images.githubusercontent.com/58923740/156924108-f365105a-83a2-41c6-a28f-c602fcd32112.png">

   * Admin이 아닌 유저는 등록 권한이 없습니다

     * 예시 데이터

       ```json
       {
         "userId": "testUser2",
         "title": "공지사항입니다",
         "contents": "내용입니다",
         "noticeStartDate": "2022-03-04 11:00:00",
         "noticeEndDate": "2022-04-04 11:00:00"
       }
       ```

     * 결과

       <img width="1148" alt="6" src="https://user-images.githubusercontent.com/58923740/156924109-e31831a8-b379-4854-8a31-3d4d9c2f8962.png">

5. [공지사항 리스트](http://localhost:8080/swagger-ui/index.html#/notices/getNoticeList)를 출력합니다

   * 저장된 데이터가 성공적으로 출력됩니다

   * 결과

     ```json
     {
       "items": [
         {
           "noticeNo": 6,
           "title": "공지사항입니다",
           "contents": "내용입니다",
           "createdAt": "2022-03-06T21:05:47.163462",
           "views": 0,
           "userName": "관리자",
           "fileResponse": [
             {
               "fileNo": 7,
               "fileName": "test.md"
             }
           ]
         }
       ],
       "meta": {
         "result": "ok",
         "errorCode": null,
         "errorMessage": null
       },
       "paging": {
         "totalCount": 1,
         "currentPage": 0,
         "perPage": 10,
         "totalPage": 1
       }
     }
     ```

6. [공지사항 상세](http://localhost:8080/swagger-ui/index.html#/notices/getNoticeDetail) 화면을 출력합니다

   * NoticeNo를 이용해 출력합니다

   * 결과

     ```json
     {
       "noticeNo": 6,
       "title": "공지사항입니다",
       "contents": "내용입니다",
       "createdAt": "2022-03-06T21:05:47.163462",
       "views": 1,
       "userName": "관리자",
       "fileResponse": [
         {
           "fileNo": 7,
           "fileName": "test.md"
         }
       ]
     }
     ```

   * 성공적으로 views가 증가한 것을 확인할 수 있습니다.

   * redis에 데이터가 저장된 것을 확인할 수 있습니다.

     <img width="786" alt="7" src="https://user-images.githubusercontent.com/58923740/156924110-70407cf0-aa72-4ade-b67e-fe8cfa9e5457.png">

> **데이터 조회에 REDIS 사용 이용**
>
> * 데이터 조회시 증가하는 조회수를 매번 db에 저장하는것은 비효율적입니다.
> * 증가된 조회수는 redis에만 저장하고 일정주기로 db에 업데이트 합니다. 
>
> * redis를 사용해 조회 결과를 캐싱해두면 조회시 db를 조회할 필요가 없기 때문에 대용량 트래픽때 유용하게 사용할 수 있습니다. 
> * 레이지 로딩(Lazy Loading) 전략을 이용해 조회된 공지사항만 redis에 저장됩니다.
> * 공지사항 업데이트와, 공지사항 삭제시에는 캐시를 삭제해 캐시 최신화가 가능하도록 했습니다.

> **Request Type에 `Patch` 를 사용한 이유**
>
> * 조회수가 매번 증가하는 로직이 포함되어 멱등하지 않기 때문에 get을 사용하지 않고 patch를 사용했습니다.



7. [첨부파일](http://localhost:8080/swagger-ui/index.html#/notices/downloadAttach)을 가져옵니다

   * fileNo를 이용해 저장된 파일을 받을 수 있습니다.
   * 결과

   ![8](https://user-images.githubusercontent.com/58923740/156924112-bdae6a68-14f5-4cf7-a066-9726bb348330.png)



8. [공지사항수정](http://localhost:8080/swagger-ui/index.html#/notices/updateNotice) 을 합니다

   * noticeNo를 이용해 공지사항을 수정합니다

     >  **참고사항**
     > 현재 소스상으로는 `JsonFormat` 을 이용해 String 데이터를 LocalDateTime으로 변환하게 되어있지만
     > swagger상에서는 LocaldateTime 형태로 인식되어  `noticeStartDate`와 `noticeEndDate`에 제대로된 테스트 데이터가 들어가 있지 않습니다
     > **yyyy-MM-dd HH:mm:ss** 형태로 넣어야 합니다

     * 예시 데이터

       * ***(noticeStartDate 와 noticeEndDate를 현재 날짜 범위 밖으로 설정합니다)***

       ```json
       {
         "userId": "testUser1",
         "title": "수정된 공지사항입니다",
         "contents": "수정된 내용입니다",
         "noticeStartDate": "2022-03-04 11:00:00",
         "noticeEndDate": "2022-03-04 11:00:00",
         "noticeNo": 6
       }
       ```

     * 파일 수정 가능

       ![9](https://user-images.githubusercontent.com/58923740/156924113-b66e4d9e-bef6-4c68-a163-ebe5e3fb8f9c.png)

   				* 최신데이터 유지를 위해 데이터를 수정하면 redis의 데이터가 삭제됩니다.

     * 데이터베이스에는 그동안 redis에 저장된조회수와 함께 수정된 결과가 저장됩니다

       ![10](https://user-images.githubusercontent.com/58923740/156924115-0403a5ca-42a2-4213-9978-602dcd9791ba.png)

9.[공지사항 리스트](http://localhost:8080/swagger-ui/index.html#/notices/getNoticeList)를 출력합니다

* 현재날짜가 공지 시작날짜와 공지 종료날짜에 포함되지 않기 때문에 데이터가 출력되지 않습니다

* 결과

  ![11](https://user-images.githubusercontent.com/58923740/156924117-a60d1598-d2d7-4f12-8c27-96367a3f991b.png)



10. [공지사항 삭제](http://localhost:8080/swagger-ui/index.html#/notices/delNotice)를 합니다
    * noticeNo를 이용해 공지사항을 삭제합니다
    * redis에서도 데이터가 삭제됩니다
    * 데이터베이스에서도 데이터가 삭제됩니다
    * ![12](https://user-images.githubusercontent.com/58923740/156924118-ec61c1f2-966f-4b95-9139-79a6808e8f61.png)