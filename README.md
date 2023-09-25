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
<img alt="AWS" src ="https://img.shields.io/badge/AWS-232F3E.svg?&style=for-the-badge&logo=amazonaws&logoColor=white"/>
<img alt="Linux" src ="https://img.shields.io/badge/Linux-FCC624.svg?&style=for-the-badge&logo=linux&logoColor=white"/>
<img alt="Jenkins" src ="https://img.shields.io/badge/Jenkins-D24939.svg?&style=for-the-badge&logo=Jenkins&logoColor=white"/>
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
- [Solution](#Solution)


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

# :bulb: Solution <a name = "outline"></a>

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




