# Mintoners

July 2024 Personal project, match table creation and management application

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
  - CalendarView 추가
    - 대회일자를 선택할 수 있는 캘린더 뷰 추가
    - 캘린더 뷰를 통해 대회일자를 선택하면 MatchInfoFragment 에서 대회일자 자동 입력
    - 변경되는 대회 일자에 따라 Title hint 변경
    - MaterialCalendarView 는 충돌이 발생하여 일반 CalendarView 사용
    - Dialog 형식으로 출력하고 ViewModel을 통해 데이터 주고받음