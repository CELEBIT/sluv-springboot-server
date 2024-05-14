# Sluv-springboot-server
<div align="center">
<img width="300" alt="sluvMain" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/a3799048-0fe7-4096-ae8c-0b64a8b7b48a">
</div>
<br/><br/>

## Workers
<div align="center">

|김준기|김보인|
|---|---|
| Backend <br>  [GitHub](https://github.com/KJBig)| DevOps <br>  [GitHub](https://github.com/Boin-Kau)|

</div>
<br/><br/>

## Detailed Roles
[김준기](https://github.com/KJBig)
- Backend
- 전체적인 API 서버 구조 구축
- 유저, 셀럽, 브랜드, 옷장, 아이템, 질문, 댓글, 공지, 검색 구현
- DB 설계
- JPA Entity 설계 및 구축
- Swagger 문서화

[김보인](https://github.com/Boin-Kau)
- DevOps
- DB 설계
- 전체적인 AWS 환경 구축
- Jenkins CI/CD 구축

<br/><br/>



## Language
<img alt="Java" src ="https://img.shields.io/badge/Java-007396.svg?&style=for-the-badge&logo=Java&logoColor=white"/>

<br/><br/>

## Skills
<img alt="Git" src ="https://img.shields.io/badge/Git-F05032.svg?&style=for-the-badge&logo=Git&logoColor=white"/><img alt="Jira" src ="https://img.shields.io/badge/Jira-0052CC.svg?&style=for-the-badge&logo=jira&logoColor=white"/>
<img alt="Spring Boot" src ="https://img.shields.io/badge/Spring Boot-6DB33F.svg?&style=for-the-badge&logo=springboot&logoColor=white"/>
<img alt="JPA" src ="https://img.shields.io/badge/jpa-6DB33F.svg?&style=for-the-badge&logo=jpa&logoColor=white"/>
<img alt="queryDsl" src ="https://img.shields.io/badge/querydsl-4479A1.svg?&style=for-the-badge&logo=querydsl&logoColor=white"/>
<img alt="mysql" src ="https://img.shields.io/badge/mysql-4479A1.svg?&style=for-the-badge&logo=mysql&logoColor=white"/>
<img alt="Redis" src ="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"/>
<img alt="AWS" src ="https://img.shields.io/badge/AWS-232F3E.svg?&style=for-the-badge&logo=amazonaws&logoColor=white"/>
<img alt="Linux" src ="https://img.shields.io/badge/Linux-FCC624.svg?&style=for-the-badge&logo=linux&logoColor=white"/>
<img alt="Docker" src ="https://img.shields.io/badge/Docker-4479A1.svg?&style=for-the-badge&logo=Docker&logoColor=white"/>
<img alt="FCM" src ="https://img.shields.io/badge/FCM-FFCA28.svg?&style=for-the-badge&logo=firebase&logoColor=white"/>

<br/><br/>

## Directory construction
### 도메인형 디렉토리 구조 채택
<img width="256" alt="Sluv 디렉토리 구조" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/755f64d8-a017-4280-8c43-d8f779528424">

- 11개의 Domain과 약 50개의 Entity. 
- 다량의 Entity를 기준으로 구분하기 쉬운 도메인형 디렉토리 구조를 채택.

<br/><br/>

---

# :memo: [목차](#index) <a name = "index"></a>

- [개요](#outline)
- [Solution](#solution)
- [Architecture](#architecture)
- [결과물](#result)
- [이 기술을 쓰는 이유](#why)
- [ERD](#erd)


<br>

# :bookmark: 개요 <a name = "outline"></a>

<details>
   <summary> 본문 확인 (👈 Click)</summary>
<br />
 
  - 셀럽이 사용한 아이템을 따라 구매하는 일이 증가하였습니다. 이에 따라 "손민수 아이템"이라는 단어까지 등장하며 인기는 꾸준히 증가하였습니다.
  - 인기가 증가함에 따라 트위터를 중심으로 다양한 SNS에서 손민수 아이템의 정보를 공유하는 계정들이 등장하였습니다.
  - 하지만 SNS로 공유하다 보니, 검색의 범위도 너무 광범위하며 공유자의 입장에서도 불편함이 발생하였습니다.
  - 검색 속도와 공유 속도 및 편의성을 개선하기 위해 서비스 운영을 하고자 합니다.
  <div align="center">
    <img width="700" alt="sluv_intro1" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/d6bac75b-475d-4054-afa7-582cf1c56009">
    <img width="700" alt="sluv_intro2" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/fd0efd3e-73f1-470f-9dd7-7e177ceee072">
    <img width="700" alt="sluv_intro3" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/53c979f6-665e-4aa5-9b9c-0ebee460f067">
  </div>
  

</details>

<br>

# :bulb: Solution <a name = "solution"></a>

<details>
   <summary> 본문 확인 (👈 Click)</summary>
<br />

### 획일화된 정보 형식 및 입력폼
- 검색자의 입장에서 다량의 정보를 획일화된 형식으로 얻을 수 있다.
- 공유자의 입장에서 필요한 정보만 입력할 수 있다.
<br> (많은 정보를 하나하나 텍스트로 입력하기 번거롭다는 점 해결)

### 관심 셀럽 및 필터링 기능
  - 검색자의 입장에서 원하는 셀럽의 정보를 중심으로 검색할 수 있다.
  <br>(방탄소년단 진의 티셔츠 -> 다른 셀럽의 티셔츠가 검색됨을 방지)
  - 검색자의 입장에서 필터링을 통해 원하는 조건의 정보만 검색할 수 있다.
  <br>(방탄소년단 진의 티셔츠 -> 다른 셀럽의 티셔츠가 검색됨을 방지)

### 질문 커뮤니티 및 댓글 기능
- 공유자의 입장에서 정보 공유 시 생기는 부담을 줄일 수 있다.
    <br>(혼자 정보를 공유하다 보니 정보 오전달 및 요청사항 수리에 대한 부담을 해결)
- 검색자의 입장에서 여러명에게 답을 들을 수 있으니, 정제된 정보를 얻을 수 있다.
<br><br/>
</details>

<br>

# :building_construction: Architecture <a name = "architecture"></a>

<details>
   <summary> 본문 확인 (👈 Click)</summary>
<br />
<img width="700" alt="arch" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/f59b81e8-5557-4da0-9200-585e66d93000">

</details>

<br>

# :tada: 결과물 <a name = "result"></a>

<details>
   <summary> 본문 확인 (👈 Click)</summary>
<br/>

### 앱 도입
<div align="center">
<img width="700" alt="intro_result" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/63e9bac9-f139-4940-8cc8-b9f07a8f83c1">
</div>
<br><br/>

- 3개의 소셜 로그인 제공합니다. (카카오, 구글, 애플)
- 약관 등록합니다.
- 프로필 사진, 닉네임을 등록합니다.
- 관심셀럽 선택합니다.

  각 단계에서 앱 종료하는 것을 대비하여 UserStatus를 통해 재접속 시에 첫 단계부터 다시 시작하는 상황을 방지하였습니다.

<br><br/>

### 관심셀럽
<div align="center">
<img width="700" alt="celeb_result" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/8c0c19ae-bae7-4974-aea6-02773b80cfb8">
</div>
<br><br/>

- 관심셀럽 설정을 통해 관심 있는 셀럽의 정보를 위주로 전달합니다.
- 동명이인 셀럽은 프로필을 통해 구분할 수 있습니다.
- 최대 50명까지 설정 가능합니다.

<br><br/>


### 마이페이지
<div align="center">
<img width="700" alt="mypage_result" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/bdac5fde-87a3-4385-95e8-b79c0c7fb546">
</div>
<br><br/>

- 유저별 마이페이지 운영.
- 내 마이페이지와 타인의 마이페이지 정보를 다르게 전달해 줍니다.
- 유저가 작성한 아이템, 유저의 옷장, 관심셀럽, 팔로워, 팔로잉 확인할 수 있습니다.
- 자신의 마이페이지에서 자신이 작성한 커뮤니티와 최근 본컨텐츠, 좋아요 한 게시글들을 확인할 수 있습니다.

<br><br/>

   
### 홈
<div align="center">
<img width="700" alt="home_result" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/f6c9b6f2-d71f-4ba2-9b18-972f6801ea35">
</div>
<br><br/>

- 앱의 홈 화면.
- 여러 셀럽의 아이템을 추천해 줍니다.
- 인기 아이템과 스러버 등을 추천해 줍니다.
- 설정한 관심셀럽들과 관련된 아이템 추천해 줍니다.

<br><br/>

### 커뮤니티 홈
<div align="center">
<img width="700" alt="community_home_result" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/ffae8e50-f221-4dee-a89d-44f21dcc3fb1">
</div>
<br><br/>

- 커뮤니티의 홈 화면.
- 일간, 주간 인기 커뮤니티 게시글 추천해 줍니다.
- 커뮤니티 게시글 타입별 검색을 할 수 있습니다.
   - 찾아주세요 : 셀럽별 필터링
   - 이 중에 뭐 살까 : 투표 상태별 필터링
   - 추천해 줘 : 해시태그별 필터링

<br><br/>

### 커뮤니티

<br><br/>

- 커뮤니티는 4개의 타입으로 구성되어 있습니다.

<br><br/>

#### 찾아주세요
<div align="center">
<img width="500" alt="question_find_result" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/25946a9a-71e1-44d6-af44-d4bda2da9e0d">
</div>
<br><br/>

- 셀럽을 기준으로 아이템을 찾아주는 게시글.
- 아이템 및 사진 첨부할 수 있습니다.


#### 이 중에 뭐 살까
<div align="center">
<img width="500" alt="question_buy_result" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/b77e9456-6b2a-48cb-a4fa-ca8f5c28ab72">
</div>

- 투표를 통해 고민되는 아이템을 추천받는 게시글.
- 아이템, 사진 첨부할 수 있습니다.
- 투표 마감시간 설정할 수 있습니다.

<br><br/>


#### 이거 어때
<div align="center">
<img width="500" alt="question_how_result" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/1a0275a7-9202-4684-9688-5152381cec14">
</div>

- 자유롭게 아이템과 사진을 올려 질문하는 게시글.
- 아이템, 사진 첨부할 수 있습니다.

<br><br/>


#### 추천해 줘
<div align="center">
<img width="500" alt="question_recommend_result" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/55f61e58-8ef9-41b8-9a97-54569e785fe9">
</div>

- 해시태그를 설정하여 주제를 기준으로 질문하고 추천받는 게시글.
- 해시태그 설정을 할 수 있습니다.
- 아이템, 사진 첨부할 수 있습니다.


<br><br/>

### 커뮤니티 댓글
<div align="center">
<img width="500" alt="comment_result" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/6f7c2433-10f6-4dc9-a8ef-975b5ce0d8fb">
</div>

- 커뮤니티 게시글에 아이템/사진을 첨부하여 댓글을 남길 수 있습니다.
- 댓글에 대댓글을 추가할 수 있습니다. 
- 좋아요 기능이 있습니다.

<br><br/>


### 정보공유
<div align="center">
<img width="700" alt="item_result1" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/34789c09-5e2f-42e0-be14-9c9f39f17320">
</div>
<br><br/>

- 필수 정보만 입력해도 되는 입력폼 제공합니다.
- 임시 보관함 기능을 통해 작성을 이어갈 수 있습니다.

<br><br/>

<div align="center">
<img width="700" alt="item_result2" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/65ccec99-8b02-424f-9c4d-8a48cd5ca63e">
</div>
<br><br/>

- 획일화된 정보 게시글을 제공합니다.
- 게시글과 같은 셀럽, 같은 브랜드, 함께 보관한 아이템을 추천해 줍니다.

<br><br/>


### 옷장
<div align="center">
<img width="700" alt="closet_result" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/6709bd2a-1cd1-45d3-84a3-ff46a6ded441">
</div>
<br><br/>

- 관심 있는 아이템 게시글을 옷장에 스크랩할 수 있습니다.
- 기본 옷장은 회원가입 시 제공됩니다.
- 배경 사진과 색, 이름을 변경할 수 있습니다.

<br><br/>



### 검색
<div align="center">
<img width="700" alt="search_result1" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/8e2803aa-d54a-47b0-b4ce-bbbab3ed53f8">
</div>
<br><br/>

- 통합검색을 통해 검색어와 관련된 아이템, 커뮤니티, 사용자를 검색할 수 있습니다.
- 일간 인기 검색어를 노출합니다.
- 필터를 통해 검색할 수 있습니다.

<br><br/>


<div align="center">
<img width="700" alt="search_result2" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/297ff13f-81fa-4867-8887-b718353c9c42">
</div>
<br><br/>

- 통합검색뿐만 아니라 아이템, 커뮤니티, 사용자로 묶어 검색할 수 있습니다.

<br><br/>

</details>

<br>

# :monocle_face: 이 기술을 쓰는 이유 <a name = "why"></a>

<details>
   <summary> 본문 확인 (👈 Click)</summary>
<br />

### SpringBoot 버전

<div align="center">
<img width="500" alt="SpringBoot_version" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/0cb3d0bc-b223-42ae-a5bd-94cb84e1daed")
">
</div>
<br><br/>

- SpringBoot의 버전을 SpringBoot 2와 SpringBoot 3중에서 고민하였습니다.
- 최종적으로 SpringBoot 3을 채택하였습니다.

**[이유1] 최신 버전의 기술로 구현해 보고 싶다는 생각이 가장 컸습니다.**
<div align="center">
<img width="400" alt="startIO" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/62e90d09-3c43-447c-b6bc-91421c1fa9ae")
">
</div>

- Sluv의 Spring 서버 개발을 시작할 당시, [start.spring.io](https://start.spring.io/)에서 추천하는 버전이 SpringBoot 3.0.2였습니다.
- 때문에 해당 버전이 최신 기술이라고 판단하였습니다.
- SpringBoot 2가 관련 레퍼런스가 많았음에도 **최신 기술로 개발해 보고 싶다는 생각**이 들어 SpringBoot 3를 선택하였습니다.

**[이유2] SpringBoot 2에서 SpringBoot 3으로 Migration 시 공수가 클 것이라고 판단했습니다.**
- Sluv이라는 앱을 취업 이후에도 운영할 생각을 가지고 참여했습니다.
- 때문에 추후 SpringBoot 2가 끝나고 SpringBoot 3으로의 Migration이 불가피할 경우 `WebSecurityConfigurerAdapter` 와 같이 Deprecated된 기능과 JAVA 17이 강제되며 발생하는 문법 변화 때문에 공수가 클 것이라고 판단하여 SpringBoot 3로의 개발을 선택하였습니다.


### API 문서화

<div align="center">
<img width="700" alt="API_Doc" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/d4918edf-03a3-4eba-ac1c-f75d007ed6f3">
</div>

- API 문서화를 Swagger와 Spring Rest Doc중에서 고민하였습니다.
- 최종적으로 Swagger를 채택하였습니다.

**[이유1]**

- **Swagger 문서상에서 API를 테스트**할 수 있다는 점이 굉장한 장점으로 다가왔다.
- 해당 기능을 통해 프론트 개발자가 API 문서를 보며 테스트할 수 있고, 실제 개발과 Swagger API 테스트로 2중 테스트가 가능하기 때문에 좋다고 생각했다.
    
    (실제로 프론트 개발자가 API 연동중 에러 발생 시 바로 말하는 것이 아닌, Swagger API 테스트 기능으로 한 번 더 확인해보고 서버 에러인지 여부를 판단할 수 있었다.)
    

**[이유2]**

- 또한 Spring REST Docs는 테스트 코드를 작성해야 문서 생성할 수 있다.
- 때문에 접근성이 떨어진다고 판단하였다.

<br><br/>


### Query DSL

<div align="center">
<img width="500" alt="query_dsl" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/317fd8bd-eecf-42e7-89a2-eed67d3985f1">
</div>
<br><br/>

간단한 기능은 Spring Data JPA가 제공해 주는 CRUD 메소드를 사용하더라도, 복잡한 기능의 경우 JPQL을 사용하여 구현하게 됩니다.

하지만 JPQL의 경우 문자열로 직접 작성하기 때문에 오타 혹은 문법적인 오류가 발생할 수 있습니다. 또한 SQL 문법을 작성하기 때문에 복잡한 구현 시 쿼리문이 길어지며 가독성이 떨어진다는 단점이 있습니다.

다음과 같은 Query DSL의 특징이 JPQL의 단점을 보완하기 때문에 도입하였습니다.

- 문자가 아닌 코드로 쿼리를 작성하여 문법 오류를 쉽게 확인할 수 있습니다.
- 객체를 사용하는 방식과 동일한 작성법을 이용하기 때문에 가독성이 높습니다.
- IDE의 도움을 받을 수 있습니다.

</details>
<br>

# :card_file_box: ERD <a name = "erd"></a>

<details>
   <summary> 본문 확인 (👈 Click)</summary>
<br />

<div align="center">
<img width="912" alt="rds" src="https://github.com/CELEBIT/sluv-springboot-server/assets/101792740/bb324c0c-c057-4e4e-9661-84b9099ae6e2">
</div>
<br><br/>

</details>
