# Mintoners

July 2024 Personal project, match table creation and management application

# 프로젝트 개요

### 프로젝트 진행 기간

- 2024.06.25 ~ 진행중

### 프로젝트 목표

<b>배드민턴 또는 테니스 동호인들이 사용하기에 편리한 대진표 생성 및 관리 어플리케이션을 제공하는 것</b>이 목표입니다.
<br>기존에 존재하는 몇몇 앱들은 출시된지 오래되거나 업데이트 지원이 끊긴 어플들 뿐이기에
<br>기존 어플 대비 <b>직관적인 UI와 문제점들이 개선</b>된 어플을 제작해보고자 하였습니다.

### 역할 및 책임

개인 프로젝트로 진행하였기 때문에 기획부터 디자인, 개발 QA, 배포, 운영 등 모든 과정을 직접 진행하였습니다.

# 기획 및 UI, Database 디자인

### 👉🏻 [FIGMA](https://www.figma.com/design/UdR7poMiFqJ9mWknKeSNg2/MIntoners?node-id=0-1&t=d1U0RAl7U4o8rFvT-1)

# Portfolio Page

### 👉🏻 [Notion](https://kimkwonsu.notion.site/KDK-9aaaa8c7aa914d8e80a8b49367b6ca99?pvs=4)

# 개발 일지

## 2024.07.08

- 기본 폰트 변경 (PyeongChang -> Pretendard)
- 스타일 일부 수정
- Profile 프래그먼트 추가
- Navigation 연결
- 모든 프래그먼트 최상단 뷰를 스크롤 뷰로 변경 (화면이 작을 경우 뷰가 찌그러지는 것을 방지)
- Navigation 오류 해결 (선택된 네비게이션 메뉴를 클릭 시 화면이 종료되는 오류 -> 현재 아이템을 체크)
- dp,sp 규격화 (dimens.xml 작성)
- Offset Decoration 추가 (RecyclerView 아이템 간격 조정)
- 뒤로가기 이벤트 추가 (두 번 누르면 종료, navigation pop stack)
- MatchActivity 추가 (MatchProcess 는 MatchActivity 에서 작업)
- MatchInfoFragment 개발중 (상단 바, 대회명 까지 완료)

## 2024.07.09

- **MatchInfoFragment 개발 완료**
    - 대회일자, 승점 입력, 1인당 게임수, 복식/단식 선택
    - 다음 버튼, MatchPlayerFragment와 연결
- **MatchPlayerFragment 개발 완료**
    - 플레이어 추가, 삭제, 플레이어 이름 입력
    - 이전 버튼, MatchInfoFragment와 연결
    - RecyclerView 세부 기능 일부 개발
        - 아이템 클릭 시 수정 가능 (TextView -> EditText 전환)
        - 삭제 버튼을 통해 아이템 삭제 가능
        - 아이템 추가 버튼이 자동으로 생성
        - 뒤로가기 이벤트 (포커스 해제, 키보드 숨김, navigation pop stack)
        - index 자동화
        - 포커스 이벤트 (TextView 터치 시 바로 키패드 오픈, 키패드에서 완료 클릭 시 포커스 해제)
    - Hilt 연결 완료
    - MatchViewmodel 개발 (향후 수정 필요. 현재는 플레이어 관련 정보만 다루지만 향후 대회 정보와 유저 정보를 총괄할 수 있는 뷰모델로 개선 필요)
    - MatchRepository 개발 (향후 수정 필요. 현재는 플레이어 관련 정보만 다루지만 향후 대회 정보와 유저 정보를 총괄할 수 있는 레포지토리로 개선 필요)
    - RecyclerView 아이템 개수에 따른 레이아웃 업데이트 (개수 출력)
- **CustomConstraint 개발**
    - ConstraintLayout을 상속받아서 레이아웃을 변경할 수 있는 클래스 개발
    - 터치 이벤트를 받아오려고 하니 perform() 을 오버라이드 해야하는 문제가 발생해서 커스텀 뷰 생성
    - 큰 의미가 있나 싶어서 삭제도 고려중
- **MatchListFragment 개발 시작**
    - 프래그먼트 생성 및 바인딩
    - UI 개발 완료
    - 리사이클러뷰 아이템 개발 완료
    - Navigation 연결 완료
    - 리사이클러뷰 세부 기능 개발 필요(어댑터, 레포지토리, 뷰모델)
- **MatchInfoFragment 기능 일부 개발**
    - 대회명 힌트 기능 구현 (오늘 날짜 기반 대회명 자동 생성)
    - 대회일자 오늘 날짜 자동 입력

## 2024.07.10

- **MatchMainFragment UI 개발 완료**
    - TabItem : MatchMainListFragment, MatchMainRankFragment UI 개발 완료
    - RecyclerItem : match_main_list_item, match_main_rank_item UI 개발 완료
    - Navigation 연결 완료
- **Profile - Detail Fragments UI 개발 완료**
    - ProfileRegisterFragment UI 개발 완료
    - ProfilePersonalFragment UI 개발 완료
    - ProfileWithdrawFragment UI 개발 완료
    - Navigation 연결 완료
    - 비밀번호 변경 삭제 고려. (KAKAKO, NAVER API를 연동하여 로그인 하기 때문에 별도 비밀번호 없음)
- **Room Database 연동을 위한 준비**
    - 기존 Adapter, Repository, ViewModel 삭제
    - 새로운 데이터 클래스 생성
        - MatchPlayer,RecentGame -> Match, Player, Game, Member, Converters(데이터베이스 사용을 위한 타입 변환)
        - 데이터베이스에 사용하기 위해 전부 Entity로 생성
    - Database, Dao 생성
        - DatabaseModule을 사용해서 Database와 Dao 모듈화
    - Hilt, Dao, Repository, Viewmodel 흐름에 대한 학습
- **Branch를 나눠서 작업 후 Merge하는 것에 대한 경험**
    - 기능 구현에 따라 브랜치를 나눠서 작업 (ui/match-main, ui/profile-detail, feature/setting-roomdb)
    - 작업 완료 후 git push origin <Branch 이름> 을 통해 깃허브에 push
    - pull request를 통해 main 브랜치에 merge

## 2024.07.11

- **Room Database 연동**
    - RoomDatabase 사용 전 오류 해결 (Kapt 관련 Error)
        - Kapt 제거 -> KSP 로 교체
        - DataBinding 제거 -> ViewBinding 으로 교체
        - Kotlin Version up 1.9.0 -> 2.0.0
    - 새로운 MatchViewModel 생성
        - 하나의 Match 와 여러 명의 Player, 여러 개의 Game 을 관리하는 ViewModel
        - 아래는 향후 구현할 데이터 관리 흐름 (아직 미구현)
            - MatchInfoFragment = 대회명, 대회일자, 승점, 경기수, 경기 타입을 입력받아서 ViewModel로 전달 -> 데이터베이스에 Match
              Insert
            - MatchPlayerFragment = 플레이어 객체를 넘겨받아서 ViewModel로 전달 -> 데이터베이스 X ViewModel 에서 players
              관리 -> Match 에 전달
            - MatchListFragment = ViewModel 에서 Players를 전달받아 대진표 생성(Game[]) -> 데이터베이스 X ViewModel 에서
              games 관리 -> Match 에 전달
            - MatchListFragment -> MatchMainFragment 로 넘어가면서 데이터베이스에 Match 최종 Insert, 데이터베이스에서 Match
              가져와서 정보 출력
            - MatchMainListFargment = 승점을 입력받아서 ViewModel로 전달 -> Match.Game.score 에 저장 -> 데이터베이스에
              Insert
            - MatchMainRankFragment = 데이터베이스에서 Score 합산 기준 Player 이름 가져와서 순서대로 출력
    - ViewModel 완성 후 리사이클러뷰 어댑터 제작할 것.
        - MatchPlayerAdapter는 현재 제작중.

## 2024.07.15

- **Room Database 연동**
    - MatchPlayer 데이터 처리 개발 완료
        - MatchPlayerAdapter 개발 완료
        - MatchPlayerFragment 개발 완료
        - MatchViewModel - Player 관련 개발 완료
        - Player 추가, 삭제, 수정 가능
        - Player 삭제 시 Index 초기화 (1부터 정렬)
    - MatchGame 데이터 처리 개발
        - MatchGameAdapter 개발 완료
        - MatchGameFragment 개발 완료
            - Fragment 최초 실행 시 뷰모델의 players 데이터를 통해 Game List 생성
        - MatchViewModel - Game 관련 개발 완료
        - Game 추가, 삭제, 수정 가능
            - 리사이클러뷰 Swipe를 통해 삭제
        - Game 삭제 시 Index 초기화 (1부터 정렬)
    - Match 데이터 처리
        - 입력이 완료된 player와 games 데이터는 화면 이동 버튼 클릭 시 match에 저장
        - match 데이터는 MatchMainFragment에서 확인 가능
    - 향후 개발
        - Swipe or Drag & Drop을 통한 Player, Game 순서 변경, 아이템 삭제, 아이템 수정 등 구현
        - MatchMainFragment에서 Match 데이터를 확인하고 수정 가능하도록 구현
        - MatchMainListFragment에서 승점을 입력받아서 Match 데이터에 저장
        - MatchMainRankFragment에서 Match 데이터를 가져와서 승점 순으로 정렬하여 출력

## 2024.07.16

- **MatchInfoDialog 개발**
    - Custom Dialog 를 만들어서 대회 정보를 출력하는 다이얼로그 개발
    - MatchMainFragment 에서 대회 정보를 출력하는 버튼 클릭 시 다이얼로그 출력
- **Match Process 디테일 개발**
    - MatchMain 정보 출력
        - 상단바에 제목 출력
        - 서브 타이틀에 경기 수와 참가자 수 출력
    - Process 화면들 ScrollView 전체로 변경

## 2024.07.17

- **Match Process 디테일 개발**
    - CalendarView 추가
        - 대회일자를 선택할 수 있는 캘린더 뷰 추가
        - 캘린더 뷰를 통해 대회일자를 선택하면 MatchInfoFragment 에서 대회일자 자동 입력
        - 변경되는 대회 일자에 따라 Title hint 변경
        - MaterialCalendarView 는 충돌이 발생하여 일반 CalendarView 사용
        - Dialog 형식으로 출력하고 ViewModel을 통해 데이터 주고받음
    - Player, Game, Main Fragment 코드 정리
    - Game, Main Fragment에서 네비게이션이 꼬이던 문제 해결
    - MatchMainFragment 개발 완료
        - ViewPager2 를 사용한 Tab 전환
        - MatchMainListFragment, MatchMainRankFragment 연결
    - MatchMainFragment 에서 Match 데이터를 가져와서 출력
        - Match.matchList 를 가져와서 대진표 출력 (MatchMainListFragment)
        - MatchMainListItem 디자인 수정
        - Match.players 를 가져와서 플레이어 목록 출력 (MatchMainRankFragment)
            - 각 플레이어 별 승점에 따라 정렬 기능 구현 필요.
    - HomeFragment 리사이+클러뷰 아이템 출력
        - Database에서 matches 테이블 데이터를 조회하여 출력

## 2024.07.18

- **Match Number를 통한 데이터 저장 및 불러오기**
    - MatchNumber 변수를 ViewModel에 별도로 선언하여 현재 선택된 Match의 MatchNumber를 식별
    - 식별된 MatchNumber를 통해 데이터를 불러오거나 저장
    - MatchNumber가 0이라면 새로운 Match, 아니라면 기존 Match를 불러옴
    - MatchGameFragment에서 Game명 변경 시 대진표에 반영되도록 수정
    - 새로운 Match의 경우 현재 존재하는 MatchNumber 중 가장 높은 MatchNumber를 찾아서 사용
      (자동으로 생성된 MatchNumber = 가장 높은 MatchNumber)
- **MatchProcess Detail**
    - KDK 대진표 알고리즘 함수 분리(5~16명 까지)
    - 최근 경기 출력 순서 변경 (Order by matchNumber DESC)
    - Match Process 각 페이지 별 Backpress Callback 추가
    - MatchMain 에서 뒤로가기로 종료 해도 변경 사항 저장
    - MatchMainListFragment 에서 승점 입력 시 Match 데이터에 저장
    - 편의성 일부 수정
        - MatchMainListFragment 에서 승점 입력 시 승점이 0일 경우 빈 칸으로 자동 변경
        - MatchMainListFragment 에서 승점 입력 시 승점이 빈 칸일 경우 0으로 자동 변경
- **Match State 추가**
    - Match 데이터 수정, matchState 추가
    - MatchInfoFragment 에서 matchState에 따라 Fragment 이동
    - Fragment 전환 시 matchState 업데이트
    - MatchNumber와 동일하게 기존 카드 클릭 시 intent로 matchState 전달

## 2024.07.22

- **현재 순위 출력 기능 추가**
    - Player 데이터 클래스에 playerWin, playerDraw, playerLose, playerScore 추가
    - ~~대진표에서 점수 입력 시 playerScore(득실) 업데이트~~
        - 현재 순위로 이동해서 Sync 버튼 클릭 시 playerScore, playerWin, playerDraw, playerLose 업데이트
    - MatchMainRankFragment 에 이름, 승점, 득실 RadioGroup 추가
    - RadioGroup 선택 시 해당하는 항목의 순서대로 정렬
    - matchViewModel에 updateCount 변수 추가.
        - playerScore 업데이트 시 updateCount 증가 -> viewPagerAdapter Update = 득실 실시간 업데이트
        - observer 가 players 의 playerScore 의 변화를 감지하지 못해서 차선책으로 updateCount 도입
    - playerWin, playerDraw, playerLose, match.matchPoint 기반으로 승점 계산
    - Sync 버튼 클릭 시 순위 최신화 및 정렬 순서 업데이트
    - Game 데이터 클래스에 gameState 변수 추가.(true,false)
        - false = 경기 진행 중. 점수 수정 가능
        - true = 경기 종료. 점수 수정 불가능
    - 대진표의 경기 순서를 클릭하여 해당 게임의 gameState 변경 가능
- **몇가지 작은 업데이트**
    - Player 추가 Fragment 디자인 일부 변경
    - Player 수정 시 기존 이름 비움.
    - Player 수정 시 빈 이름일 경우 P+(position+1) 로 기본 이름 설정

## 2024.07.23

- **대진표 및 순위 캡쳐/공유 기능 추가**
    - 대진표 및 순위를 캡쳐하여 공유할 수 있는 기능 추가
    - ScrollView의 전체를 찍어서 공유하기 때문에 화면을 넘어가는 영역도 캡쳐
    - 캡쳐시 불필요한 버튼은 디스플레이에서 제거
    - 캡쳐와 동시에 공유까지 바로 가능
- **MatchGameFragment Drag&Drop 기능 추가**
    - fromPosition, toPosition을 통해 아이템의 위치를 변경
- **MatchMainListFragment Drag&Drop, Swipe 기능 추가**
    - Drag&Drop을 통해 아이템의 위치를 변경
    - Swipe를 이벤트는 구현 하였으나 ItemTouchHelper.RIGHT를 막아서 이벤트 발생이 안되도록 해둠(기능 구상중)

- **Release 1.0.0-beta.1**
    - 미구현 기능(로그인, 경기 수 조정, 복식/단식) 접근 제어. 베타 텍스트 출력
    - 최근 경기 아이템 디자인 변경 (info -> trash bin)
    - 최근 경기 타이틀 우측에 새로고침 아이콘 삽입 (가시성 향상)
    - 선수 추가 시 Focus 변경이 자연스럽게 흘러가도록 변경

- **선수 여러명 동시 입력 기능 추가** branch : (feature/player-add-dialog)
    - MatchPlayerAddDialog 추가
    - MatchPlayerFragment 에서 여러명의 선수를 한번에 추가 가능
    - MatchPlayerAddDialog 에서 이름을 띄어쓰기로 구분해서 입력받아 동시에 선수 추가

- **Small Update**
    - ViewPager 화면 전환 시 RecyclerView 의 내용이 짤리는 문제 해결 (화면 전환 시 ViewPager 높이 재설정)
    - MatchMainRankFragment 이름 순 출력 정규식 도입
    - _games.value.shuffle() 을 통해 게임 순서 랜덤화 기능 추가
    - MatchMainRankFragment 에 있던 Sync Button 을 MatchMainFragment 의 TopInfo 로 이동
    - InfoDialog 는 MatchMainTitle 클릭 시 출력

## 2024.07.24

- **Release 1.0.0-beta.2**
    - MatchPlayerFragment 와 MatchGameFragment 에서 Item 이 Recycle 되면서 잘못 출력되던 오류 해결
        - ScrollView 를 NestedScrollView 로 바꾸고 RecyclerView 에 속성을 부여 하여 Recycle 방지
    - HomeRecentGame Card 의 클릭 이벤트 발생 범위 변경
        - Title 영역을 클릭해야 해당 Match Activity 로 이동
        - Delete 버튼 영역 확장

- **1인당 경기 수에 따른 대진표 생성 알고리즘 추가**
    - MatchInfoFragment 에서 입력할 수 있는 1인당 경기 수를 3,4 로 제한
    - 1인당 경기 수 입력 제목 우측에 주의사항 버튼 추가
    - MatchPlayerFragment 에서 1인당 경기 수 및 참가 인원 수 판별 알고리즘 추가
    - 3경기 알고리즘 추가

- **복식/단식 선택 기능 추가**
    - MatchInfoFragment 에서 복식/단식 선택 가능
    - 단식 선택 시 1인당 경기 수 입력란 제거(사용안함)
    - 복식/단식 변경 시 존재하는 GameList 는 초기화됨.
    - 단식 대진은 모든 사람과 한 번씩 매칭되는 방식으로 설정
    - 단식은 최소 2명, 최대 8명 설정 가능
    - 복식은 최소 5명, 최대 16명 설정 가능
    - 단식용 list_item, adapter 코드 추가

- **Small Update**
    - Sync 버튼 클릭 시 Sync 이미지가 360도 회전하도록 설정
    - HomeFragment, BottomNavigationView Ripple 효과 삭제
    - HomeFragment, 최근 경기 Sync 버튼 영역 확장
    - PlayerFragment Empty 버튼 제거, 입력 시에도 제거할 수 있도록 제거 버튼 제공

## 2024.07.25
- **Recycler View 아이템 추가 시 Scroll 이동**
  - HomeFragment 에서 Sync 버튼을 누를 시 아이템이 추가 되고 리사이클러뷰 스크롤이 가장 앞으로 이동
  - MatchPlayerFragment 에서 선수 추가 시 전체 스크롤이 가장 아래로 이동
  - MatchGameFragment 에서 게임 추가 시 전체 스크롤이 가장 아래로 이동
  - 선수 추가 시에만 스크롤이 이동하도록 설정
- **Lottie Animation View 추가**
  - HomeFragment 에서 KDK 대진표 생성 로고 이미지를 Lottie 로 교체
  - PlayerFragment 에서 인원 수가 잘못 되었을 경우 경고 Toast 메시지를 Dialog 로 교체.(Lottie 사용)
- **자유 대진 생성 메뉴 추가**
  - KDK 형식에 구애받지 않고 자유롭게 대진표를 생성할 수 있는 메뉴 카드 추가\
  - isFreeMatch Intent 를 통해 MatchActivity 에서 경로 판별
  - isFreeMatch 를 ViewModel 에 저장하고 Fragment 에서 사용
  - isFreeMatch 가 true 일 경우 경기수 입력 X, 인원 수 제한 X, 자동 게임 생성 X
  - isFreeMatch 가 true 여도 생성 버튼을 통해 대진표 생성은 가능.
- **GameFragment 선수 변경 목록화**
  - MatchGameItem 의 EditText 를 모두 TextView 로 변경
  - MatchGameItem 의 TextView 를 클릭 시 팝업 메뉴로 선수 변경 가능
  - 선수 변경 시 해당 선수로 대진표 및 순위 업데이트
  
- **Small Update**
  - MatchInfoFragment 의 1인당 경기 수 주의 사항을 Toast 메시지에서 Dialog 로 변경
  - MatchPlayerFragment 에서 선수 삭제 후 포커스가 두 단계씩 뛰는 문제 해결 (position->adapterPosition)
  - MatchMainListFragment 에서 잠금 시 잠금 아이콘 표시
  - EditText 커서 색상 변경
  - TabLayout Tab 이 변경될 때마다 ViewPager 높이 계산