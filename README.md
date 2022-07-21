# find_gas_station
내 주변 주유소를 쉽게 찾아주는 어플
내 반경 1,3,5km 등 반경, 
기름 종류(경유,휘발유,자동차 부탄등) 
정렬 순서(가격순,거리순) 설정 가능

설정한 정보를 바탕으로 RecyclerView로 보여준다.

# 어려웠던 점

1. 좌표 변환 문제

# 업데이트 내용

1. recyclerView 맨 위로 올리기(구현완료)

-> 이전 버전은 recyclerView pointer가 중간에 있어 정렬된 첫번째 순서를 보려면 일일이 스크롤을 올려야하는 불편함이 있었는데
   어플을 실행하면 자동적으로 recyclerView를 첫번째 pointer로 지정하여 스크롤을 자동으로 올라가는 동작을 구현
   
2. 카카오 네비로 네비게이션 api를 이용하여 주유소까지 갈 수 있게 하기(구현완료)
 
 -> 이전 버전은 주변 주유소의 정보만 알 수 있었다면, 지금 버전은 카카오 내비 api를 추가하여 목표 주유소까지 갈 수 있도록 구현

3. 자신의 setting 정보 local db에 저장하기(구현 완료)

-> 이전 버전은 자신이 설정한 정보 ex) 기름 종류, 반경 범위, 가격순/거리순을 설정하면, 어플을 재실행시 다시 설정을 해줘야 하는 불편함이 있었지만
     현재 버전은 자신이 설정한 정보를 RoomDB를 이용하여 어플을 재실행해도 이전 설정이 유지될 수 있도록 구현
     
4. 스플래쉬 화면 유지시간 최소화 


# 버그 수정

1. 어플을 설치하면 초기에 구글 맵과 정보가 뜨지 않는 버그가 있었는데 초기 설정 코드에서 메소드 호출을 수정하고, 추가함으로써
   버그 수정 
 
2. xml에서 textView나 Button이 분산되는 경우가 있었는데 constraintlayout -> RelativeLayout으로 변경함으로써 제자리에 있도록 수정


# 스플래쉬 화면
![아이콘](https://user-images.githubusercontent.com/50404123/157656737-53b150dc-d932-473c-9466-0f1d727a4959.PNG)



# 내 주변 주유소 정보
![아이콘4](https://user-images.githubusercontent.com/50404123/157656906-aae509ea-442a-44d1-9dd8-cba09cd4f8b3.PNG)

opinet이라는 주유소 공식 사이트에서 무료 API를 호출하여 그 정보를 가공하여
RecyclerView로 나타내줌.


# 옵션 설정 화면
![아이콘2](https://user-images.githubusercontent.com/50404123/157656955-dc12e2e8-9750-4c89-b5a9-df01e2fce48a.PNG)




