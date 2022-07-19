# find_gas_station
내 주변 주유소를 쉽게 찾아주는 어플
내 반경 1,3,5km 등 반경, 
기름 종류(경유,휘발유,자동차 부탄등) 
정렬 순서(가격순,거리순) 설정 가능

설정한 정보를 바탕으로 RecyclerView로 보여준다.

# 어려웠던 점

1. 좌표 변환 문제

# 보완해야 할점

1. recyclerView 맨 위로 올리기(구현완료)
2. 카카오 네비로 네비게이션 api를 이용하여 주유소까지 갈 수 있게 하기(구현완료)
3. 자신의 setting 정보 local db에 저장하기(구현 완료)

# 스플래쉬 화면
![아이콘](https://user-images.githubusercontent.com/50404123/157656737-53b150dc-d932-473c-9466-0f1d727a4959.PNG)



# 내 주변 주유소 정보
![아이콘4](https://user-images.githubusercontent.com/50404123/157656906-aae509ea-442a-44d1-9dd8-cba09cd4f8b3.PNG)

opinet이라는 주유소 공식 사이트에서 무료 API를 호출하여 그 정보를 가공하여
RecyclerView로 나타내줌.


# 옵션 설정 화면
![아이콘2](https://user-images.githubusercontent.com/50404123/157656955-dc12e2e8-9750-4c89-b5a9-df01e2fce48a.PNG)




