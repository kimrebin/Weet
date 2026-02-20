Weet
팀원 구성 :

| 이름 | 역할 및 담당 업무 |

| kimrebin (팀장) | 전체 개발 과정 총괄, Room DB 구축, 최종 checklist ui 및 팝업 기능 구현 |

| gyulin719 | 보고서 제작, repository 제작 및 연동, 최종 mainview ui와 relationshipMap 구현 |

| 2hwajin | figma 제작, 전반적인 ui 담당, dao구현, profileview 제작, checklistview 제작 |

주제 :

인간 관계 관리에 어려움과 염증을 느끼는 현대인들을 위해 사용자 중심으로 사용자와 상호작용하는 사람들의 친밀도를 분석해 명시적인 지표로 나타내 주는 앱을 개발하고자 하였다.

관계 점수, 감정 일기, 체크리스트 등 다양한 도구를 통해 정서적 유대 강화를 지원한다.

타겟층 :

20대 ~ 40대 직장인, 대학생, 사회초년생 등 다양한 인간관계를 가진 사용자
친구, 연인, 가족, 직장 관계 속에서 소통 빈도, 감정 거리, 중요도 등을 관리하고 싶은 사용자
사람들과의 관계를 정리하고 싶거나, 단절된 관계를 복구하고 싶은 사용자
기능 목록 및 설명
핵심 기능 1 : 인간관계도 시각화 [김규린]
사용자를 중심으로, 사용자와 상호작용하는 인물들이 원형으로 배치되도록 했다. 체크리스트로 산출한 관계 점수를 바탕으로 노드의 거리가 바뀌며 명시적으로 관계와 거리감을 보여 준다.

관계 태그에 따라 원하는 태그에 해당하는 사람만 볼 수도 있다.

[RelationshipMap.kt]

friends.forEachIndexed를 통해 각 친구에 대한 위치를 계산한다. 360도를 친구 노드 수로 나누어 균등한 각도를 설정하며 toRadians 함수로 라디안 값으로 변환한다.

관계 점수가 높은 친구일수록 가까이 위치하게 하기 위해 각 친구 노드는 score 값이 51 이상이면 중심에 가까운 반지름에, score 값이 그 미만이면 바깥 반지름에 위치한다.

그리고 cos, sin 함수를 이용해 노드의 x, y 좌표를 구하고 Offset 객체로 묶어 친구의 위치를 구한다.

[RelationshipScreen.kt]

사용자가 tagList의 태그 중 하나를 클릭하면 TagSelector 함수를 이용해 선택된 태그를 실시간으로 갱신한다. RelationshipMap 함수는 실제 친구들을 원형으로 배치해서 태그에 맞는 친구 노드들만 보여준다.

[PersonEntity.kt]

relationshipMap UI를 위해 persons 테이블에서 이름, 태그, 점수만 가져온 간소화한 클래스 Friend를 따로 만들어 데이터를 전달한다.

핵심 기능 2 : 인물 별 프로필, 감정 일기 기록 [이화진]
인물 별로 프로필을 작성할 수 있고, 사용자의 감정 일기 (간단한 Memo)도 기록할 수 있다. 이 데이터들은 Room DB에 저장된다.

[ProfileViewModel.kt]

ProfileViewModel을 통해 확인할 수 있듯이 사용자의 정보 저장과 DB에 저장된 점수를 불러오는 함수를 제작하였다. 특히나 입력된 사용자에서 변환할 수 있는 값으로 photourl과 historyMessage의 변수를 저장하였고, 다시 이를 불러오도록 설계하였다.

loadPerson에서는 저장된 PersonId를 바탕으로 기본 정보를 로드, 체크리스트를 입력하고 저장한 최신 값을 반영, 이후 위에 변수로 한 photoUrl과 historyMessage (=memo)또한 저장해두고 불러오도록 하였다.

[insertionDao.kt]

저장에 관한 점 또한 insertionDao를 추가 생성하여 관리할 수 있도록 하였다.

기타 기능
추가되는 사용자 별 Tag 분리 기능 [김레빈]
추가되는 사용자의 프로필을 Tag 별로 나누어 표시되게 한다.

[MainScreen.kt]

이 코드는 Weet의 메인 화면 역할을 하는 코드로, ViewModel로부터 태그 별 인물 데이터를 수신하여 사용자가 선택한 태그에 따라 해당 목록을 표시한다. 상단에는 태그 선택 UI(TagSelector)가 제공되고, 하단에는 인물 추가(ADD) 및 관계도 확인(relationship map) 버튼이 배치되어 있다.

[MainViewModel.kt]

이 ViewModel은 저장된 인물 데이터를 태그 별로 불러올 수 있도록 하였다. personByTag함수로 tag에 따라 인물 목록을 그룹화 해서 Map<String, List>을 전달한다.

관계 점수 측정 [이화진]
사용자가 작성한 체크리스트를 바탕으로 인물 별 관계 점수를 측정한다.

[checklistpopup.kt]

popup 기능을 통해 체크리스트를 띄운 후 인간관계점수 RQS를 기반의 문항에 응답하도록하여 관계 공식을 바탕으로 네트워크의 중심성(centrality), 밀도(density), 결속력(cohesion) 등을 분석해 점수를 산출한다.

각 문항마다 관계 점수를 설정하여 체크리스트를 save하면 이를 person ID별로 구분하여 다시 room DB에 저장되게 한다.

[checklistviewModel]

그 뒤 viewModel을 통하여 점수를 정수로 변화하는 작업을 거친다.

[checklistRepository.kt]

점수 계산한 부분을 통하여 personDao를 설정 이후 updatePersonScore를 통하여 checklistRepository에 저장한다. 이는 이후 Hlit 의존성 주입을 통하여 안전하게 personprofile에 반영하도록 한다.

Architecture 및 코드 구조
주요 패키지 구성
com.example.weet.data.local.entity : Room DB 테이블 구조 정의

com.example.weet.data.local.dao : Room의 DAO 인터페이스 모음

com.example.weet.data.local.model : Room Entity를 ViewModel/Composable에서 사용하기 쉬운 형태로 변환한 데이터 모델 정의

com.example.weet.repository : ViewModel이 사용할 수 있게 데이터 통합 관리

com.example.weet.ui.* : UI 구성 요소 파일

com.example.weet.viewmodel : 각 화면에서 사용하는 상태와 로직 관리

com.example.weet.ui.navigation : 앱 내 화면 간 이동 담당

계층 설명 (주요 파일 위주)
Weet ├── .gradle ├── .idea ├── app └── src └── main └── java └── com.example.weet ├── data.local │ ├── dao │ │ ├── ChecklistDao │ │ ├── InteractionDao │ │ └── PersonDao │ ├── entity │ │ ├── CheckListResultEntity.kt │ │ └── PersonEntity.kt │ ├── model │ │ └── InteractionEntity │ ├── AppDatabase │ └── SettingsDataStore.kt ├── di │ └── DatabaseModule ├── repository │ ├── ChecklistRepository │ ├── InteractionRepository.kt │ ├── PersonRepository │ └── RepositoryModule ├── ui │ ├── components │ ├── navigation │ ├── screen │ └── theme ├── util │ ├── Constants │ └── TimeUtils └── viewmodel ├── AddPersonViewModel ├── ChecklistViewModel ├── MainViewModel └── ProfileViewModel

테스트 및 성능
테스트 방법
각자의 브랜치(feature/rebin, feature/gyulin, feature/hwajin)에서 서로 맡은 부분을 구현해 develop 브랜치에 merge하는 걸 반복하고 AVD를 실행해보면서 테스트했다.

버그 및 수정 내역 요약
최종 build 과정에서 버전이 안 맞거나, 따로 작업하는 것을 합치는 과정에서 merge 문제 등 여러 오류가 발생하였고, 이를 수정하는 데에 오랜 시간이 걸렸다.

버전과 같은 경우 AGP의 버전을 한 단계 높게 사용하고 있던 것이라 build.gradle의 버전 수정과 dependency를 추가하여 주요 코드 수행에 필요한 환경을 재설정했으며, 이전에 build한 캐시들을 삭제하면서 이전 결과와의 충돌을 막을 수 있었다.

또한, AVD를 실행할 때 앱을 누르자마자 꺼지는 현상이 발생하였는데, logcat과 오류 메시지를 확인한 결과 DI(의존성 주입)가 필요한 것을 알 수 있었다.

특히 ViewModel의 checklist 부분에서 score를 받아와 계산하고 화면에 결과를 띄워야 하므로, Repository 같은 인자를 전달하거나 특정한 초기화 로직이 필요한 경우가 많았다. 이때 의존성을 따로 주입하지 않으면 화면이 꺼지거나 기본 생성자만 불러와서 충돌이 생겼던 것이라고 추측하였다.

따라서 DI 주입을 위해 Hilt를 사용했다. Hilt를 필요한 viewModel에 삽입하고, Repository 클래스에서 생성자 주입을 활용하는 방식으로 각 연동과 의존 파일들에 대한 상속을 처리하여 오류를 해결할 수 있었다.

배운 점 & 회고
기술적으로 배운 점
이번 프로젝트를 진행하며 수업 시간에 배운 내용들을 직접 코드로 작성하며 실습해볼 수 있었다. 또한 작성 중 모르는 부분이 생기면 추가로 학습했고 수업 자료도 다시 복습하며 이해도를 높였다.

특히 다양한 모듈이 어떻게 유기적으로 작동하는지 확인하며 프로젝트 구조에 대한 감각을 키울 수 있었다. GitHub를 사용하며 협업 및 코드 관리 방법을 익혔고, 이를 통해 Kotlin 개발에 대한 이해도가 크게 향상되었다. 앞으로 실시간 동기화, 로그인 기능 등을 추가하며 앱을 더 발전시키고 싶다.

아쉬웠던 점
시간 관계상 초기 Figma 설계와 100% 동일하게 구현하지 못한 점이 아쉽다. 테스트 과정에서 인물 삭제나 프로필 변경 등 초기 설계에서 놓친 기능적 부족함을 깨달았고, 향후 Firebase 연동이나 API 추가를 통한 온라인 지원 등 확장 가능성을 발견했다. 학습을 통해 Android Studio를 더욱 익혀 확장성 있는 개발을 하고자 한다.

팀워크 & 시간 관리 회고
[팀워크] 초기 RoomDB와 Firebase 간의 혼선 및 파일 경로 설계 실수 등 비효율적인 사고가 있었으나, 이를 통해 소통의 중요성을 깨달았다. 이후 확실한 역할 분담, Git 커밋 보고, 상시 진행 상황 공유 등 적극적인 피드백을 통해 원활하게 협력했다. GitHub 브랜치 전략을 통해 충돌을 최소화했고, 팀원 모두가 서로 도움을 주고받으며 개인과 팀 모두 성장할 수 있었다.

[시간 관리] 시험 기간 전 2회의 회의를 통해 역할을 분담하고 중간 결과를 보고했으며, 마무리를 위해 추가 회의를 진행하여 효율적으로 시간을 분배했다. 오프라인에서도 4차례 이상 만나 피드백을 주고받았다. 다만 미숙한 지식으로 인해 코드 취합 후 빌드 오류 해결에 시간을 많이 소요한 점이 아쉬우며, 추후 더 향상된 개발 능력으로 효율적인 개발을 하고자 한다.

About
