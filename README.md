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
  