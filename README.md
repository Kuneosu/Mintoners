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