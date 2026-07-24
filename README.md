# BUTIM-BE

버팀 BUTIM 백엔드 레포지토리

---
## BUTIM

## 🚀 Project Introduction
> **BUTIM (버팀)**  
웹 기반 산재 신청 승인 기간 예측 및 소득 공백 대응 AI 서비스

---

## 🛠 Tech Stack

<div align="center">

### Frontend
<!-- Frontend -->
![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-FFD700?style=for-the-badge&logo=javascript&logoColor=black)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)

### Backend
<!-- Backend -->
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)

### Infra
<!-- Infra -->
![AWS](https://img.shields.io/badge/AWS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

### Tools
<!-- Tools -->
![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

</div>

---

## 📂 Repository Structure
- **Backend**: [`BUTIM-BE`](https://github.com/BUTIM-butim/BUTIM-BE.git)  
- **Frontend**: [`BUTIM-FE`](https://github.com/BUTIM-butim/BUTIM-FE.git)  
- **Client**: [`https://butim.vercel.app`](https://butim.vercel.app)  

---

## 🤝 Contributors
<br>

| 이름     | 개발분야  | 개인 레포                                         | 역할                    |
| -------- | --------- | ------------------------------------------------- | ------------------------- |
| 🦁이현정 | Back-end | [hyhy-j](https://github.com/hyhy-j)  | CICD (Docker-github action) |
| 🦁서문지 | Back-end | [SEOMUNJI](https://github.com/SEOMUNJI)  | CICD (Docker-github action) |

<br/>

---
## 🗂️ 프로젝트 구조

```
📦 
├─ .gitattributes
├─ .gitignore
├─ README.md
├─ build.gradle
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
├─ settings.gradle
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ tumbloom
   │  │        └─ tumblerin
   │  │           ├─ TumblerinApplication.java
   │  │           ├─ app
   │  │           │  ├─ controller
   │  │           │  │  ├─ AuthController.java
   │  │           │  │  ├─ CafeController.java
   │  │           │  │  ├─ CafeVerificationController.java
   │  │           │  │  ├─ CouponController.java
   │  │           │  │  ├─ FavoriteController.java
   │  │           │  │  └─ MyPageController.java
   │  │           │  ├─ domain
   │  │           │  │  ├─ Cafe.java
   │  │           │  │  ├─ Coupon.java
   │  │           │  │  ├─ CouponManager.java
   │  │           │  │  ├─ Favorite.java
   │  │           │  │  ├─ Menu.java
   │  │           │  │  ├─ Preference
   │  │           │  │  │  ├─ ExtraOption.java
   │  │           │  │  │  ├─ PreferredMenu.java
   │  │           │  │  │  └─ VisitPurpose.java
   │  │           │  │  ├─ RefreshToken.java
   │  │           │  │  ├─ RoleType.java
   │  │           │  │  ├─ Stamp.java
   │  │           │  │  ├─ User.java
   │  │           │  │  └─ UserPreference.java
   │  │           │  ├─ dto
   │  │           │  │  ├─ Authdto
   │  │           │  │  │  ├─ LoginRequestDTO.java
   │  │           │  │  │  ├─ RefreshRequestDTO.java
   │  │           │  │  │  ├─ SignupRequestDTO.java
   │  │           │  │  │  └─ TokenResponseDTO.java
   │  │           │  │  ├─ Cafedto
   │  │           │  │  │  ├─ CafeBatchCreateRequestDTO.java
   │  │           │  │  │  ├─ CafeCreateRequestDTO.java
   │  │           │  │  │  ├─ CafeDetailResponseDTO.java
   │  │           │  │  │  ├─ CafeListResponseDTO.java
   │  │           │  │  │  └─ CafeRecommendDTO.java
   │  │           │  │  ├─ Coupondto
   │  │           │  │  │  ├─ AvailableCafeCouponDto.java
   │  │           │  │  │  ├─ MyCouponDetailResponse.java
   │  │           │  │  │  ├─ MyCouponDto.java
   │  │           │  │  │  └─ MyCouponListResponse.java
   │  │           │  │  ├─ Userdto
   │  │           │  │  │  ├─ UserFavoriteCafeDTO.java
   │  │           │  │  │  ├─ UserHomeInfoDTO.java
   │  │           │  │  │  ├─ UserMyPageResponseDTO.java
   │  │           │  │  │  └─ UserPreferenceDTO.java
   │  │           │  │  └─ Verifydto
   │  │           │  │     ├─ VerificationCodeVerifyRequestDTO.java
   │  │           │  │     └─ VerificationCodeVerifyResponseDTO.java
   │  │           │  ├─ repository
   │  │           │  │  ├─ CafeRepository.java
   │  │           │  │  ├─ CouponManagerRepository.java
   │  │           │  │  ├─ CouponRepository.java
   │  │           │  │  ├─ FavoriteRepository.java
   │  │           │  │  ├─ MenuRepository.java
   │  │           │  │  ├─ RefreshTokenRepository.java
   │  │           │  │  ├─ StampRepository.java
   │  │           │  │  ├─ UserPreferenceRepository.java
   │  │           │  │  └─ UserRepository.java
   │  │           │  ├─ security
   │  │           │  │  ├─ CustomUserDetails.java
   │  │           │  │  └─ CustomUserDetailsService.java
   │  │           │  └─ service
   │  │           │     ├─ CafeRecommendationMappingService.java
   │  │           │     ├─ CafeRecommendationService.java
   │  │           │     ├─ CafeService.java
   │  │           │     ├─ CafeVerificationService.java
   │  │           │     ├─ CouponService.java
   │  │           │     ├─ FavoriteService.java
   │  │           │     ├─ MyPageService.java
   │  │           │     ├─ OpenAIEmbeddingService.java
   │  │           │     ├─ UserPreferenceService.java
   │  │           │     └─ UserService.java
   │  │           └─ global
   │  │              ├─ config
   │  │              │  ├─ SecurityConfig.java
   │  │              │  ├─ SwaggerConfig.java
   │  │              │  └─ WebConfig.java
   │  │              ├─ dto
   │  │              │  ├─ ApiResponseTemplate.java
   │  │              │  ├─ ErrorCode.java
   │  │              │  └─ SuccessCode.java
   │  │              ├─ exception
   │  │              │  ├─ BusinessException.java
   │  │              │  └─ GlobalExceptionHandler.java
   │  │              └─ security
   │  │                 ├─ JwtAuthenticationFilter.java
   │  │                 ├─ JwtTokenProvider.java
   │  │                 └─ SecurityConstants.java
   │  └─ resources
   │     ├─ application.properties
   │     └─ application.yml
   └─ test
      └─ java
         └─ com
            └─ tumbloom
               └─ tumblerin
                  └─ TumblerinApplicationTests.java
```
©generated by [Project Tree Generator](https://woochanleee.github.io/project-tree-generator)


---
## 프로젝트 구조
```
📦src
 ┣ 📂main
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂example
 ┃ ┃ ┃ ┃ ┗ 📂butim
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂accident
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AccidentInfoController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DiagnosisCodeAdminController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DiagnosisCodeController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂csv
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DiagnosisCodeCsvImporter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CreateAccidentInfoRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DiagnosisCodeSuggestRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UpdateAccidentInfoRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AccidentInfoResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DiagnosisCodeResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AccidentInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BusinessSize.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DiagnosisCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmploymentType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Gender.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AccidentInfoRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DiagnosisCodeRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AccidentInfoService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DiagnosisCodeGptService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DiagnosisCodeService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂auth
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PhoneController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PhoneSendRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PhoneVerifyRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RefreshRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SignupRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PhoneSendResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TokenResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PhoneVerificationService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂financial
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FinancialInfoController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FinancialInfoRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FinancialInfoResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FinancialInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂enums
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmploymentStatus.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜HouseholdType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜IncomeLevel.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FinancialInfoRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FinancialInfoService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂industry
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂client
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜IndustrialAccidentApiClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ExternalApiConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜IndustrialAccidentProperties.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜IndustryController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JobController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜IndustryResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JobResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Industry.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Job.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜IndustryRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JobRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜IndustryService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂prediction
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PredictionController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂csv
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DiagnosisTreatmentCsvReader.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜IndustryApprovalCsvReader.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PredictionResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Prediction.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Reliability.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PredictionRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PredictionGptService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PredictionService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂stats
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DiagnosisStats.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DiagnosisStatsService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂region
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RegionAdminController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RegionController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂csv
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RegionCsvImporter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SidoResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SigunguResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Region.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RegionRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RegionService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂strategy
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StrategyController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AiStrategyPlan.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AiStrategyResult.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CandidateSupportDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CashflowPointDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StrategyCardDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StrategyContext.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StrategyItemDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TimelineItemDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CashflowRecalculateRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StrategyConfirmRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StrategyRunRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CashflowResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StrategyMeResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StrategyRunResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CashflowSnapshot.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StrategyItem.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StrategyResult.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂enums
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CashflowEventType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StrategyItemType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StrategyType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂external
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CentralWelfareApiClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LocalWelfareApiClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MicroFinanceApiClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PublicDataXmlParser.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WelfareApiAggregator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂openai
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OpenAiChatRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OpenAiChatResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OpenAiClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CashflowSnapshotRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StrategyItemRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StrategyResultRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CashflowCalculator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StrategyAiService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StrategyService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂user
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UpdateUserRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserCurrentStepResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserMainResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserMeResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜User.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂external
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂openai
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜GptClient.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂global
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JacksonConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OpenAiConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OpenAiProperties.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RedisConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SwaggerConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WebClientConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BaseEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜GlobalExceptionHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂jwt
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtProvider.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BaseResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂security
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomUserDetails.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtAccessDeniedHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtAuthenticationEntryPoint.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtAuthenticationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ButimApplication.java
 ┃ ┗ 📂resources
 ┃ ┃ ┣ 📂data
 ┃ ┃ ┃ ┣ 📜diagnosis_code.csv
 ┃ ┃ ┃ ┣ 📜diagnosis_treatment.csv
 ┃ ┃ ┃ ┣ 📜industry_approval.csv
 ┃ ┃ ┃ ┗ 📜lawd_code.csv
 ┃ ┃ ┣ 📜application-local.yml
 ┃ ┃ ┣ 📜application-prod.yml
 ┃ ┃ ┣ 📜application.properties
 ┃ ┃ ┗ 📜application.yml
 ┗ 📂test
 ┃ ┗ 📂java
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂example
 ┃ ┃ ┃ ┃ ┗ 📂butim
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ButimApplicationTests.java
```
