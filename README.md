안드로이드 스튜디오 설치안내 https://developer.android.com/studio/install?hl=ko
안드로이드 설치 파일 URL  https://developer.android.com/studio/?hl=ko

Gradle 추가시에도 Import 되지 않을경우 https://wimir-dev.tistory.com/7
(Sync Now를 누르면 동기화 됨)

ListView 설명 : https://jwandroid.tistory.com/230

ListView 설명 : https://recipes4dev.tistory.com/79
- 어댑터 상속후 구현내용 포함
- LayoutInflater 설명 https://arabiannight.tistory.com/entry/340
  └ 직접정의하기 애매하면 안드로이드 기본 자원사용(android.R.layout.simple_list_item_multiple_choice)
  
안드로이드 토스트 설명 : https://lktprogrammer.tistory.com/152

RecyclerView 설명 : 
https://developer88.tistory.com/3
https://developer88.tistory.com/102 ( 내가 구현하고자 하는 기능에 가장 근접 )

app레벌에 추가할 코드
 implementation 'androidx.recyclerview:recyclerview:1.0.0'
 
파일입출력 : https://recipes4dev.tistory.com/113
외부저장소(공용파일) : https://underground2.tistory.com/39
 └ 앱내부가 아닌 공용공간에 저장하고 싶을때 확인하면 좋은 내용
내부저장소 접근 : https://choidev-1.tistory.com/70
 
// 20191027
[에러발생시] Error inflating class android.support.constraint.ConstraintLayout
참고 : https://stackoverflow.com/questions/42501868/error-inflating-constraintlayout-in-android-studio
// 이문제가 아닐지 맞을지는 확인해봐야 알거같다.
implementation 'com.android.support.constraint:constraint-layout:1.1.2'
/*
 그런문제가 아니다. 뷰 자원 생성시 애초에 
 android.support.constraint.ConstraintLayout 클래스 자체를 찾지 못해서 생긴 문제, 직접 xml 수정시 
 위 클래스가 있든 없든 상관없이 수정되므로 에러는 런타임에 확인이 가능하다.
 그래서, 그냥 툴의 도움을 빌려 다시 만들었다. 다시만든 constraintLayout은 아래와 가탇.
 <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
 ...
 </androidx.constraintlayout.widget.ConstraintLayout>
 
 근데, 이제는 ViewHolder에서 에러가 난다. 
*/


[에러발생시] android.content.res.Resources$NotFoundException: String resource ID #0x0
TextView.setText(int형) 시 발생하는 오류.
int형은 resId를 찾는 메소드로 오버로딩이 되어있다. 그래서, 자원ID에 해당하는 객체가 없으면 Exception발생.
난 당연히 int형은 숫자값을 텍스트로 설정하기 위해 오버로딩한줄 알았는데...
/**
 * Sets the text to be displayed using a string resource identifier.
 *
 * @param resid the resource identifier of the string resource to be displayed
 *
 * @see #setText(CharSequence)
 *
 * @attr ref android.R.styleable#TextView_text
 */
@android.view.RemotableViewMethod
public final void setText(@StringRes int resid) {
	setText(getContext().getResources().getText(resid));
	mTextSetFromXmlOrResourceId = true;
	mTextId = resid;
}


[런타임 예상치 못한 상황]
바코드 스캔을 위해 화면 전환이 이루어지면서, 액티비티가 가끔 죽다 살아난다.
그로인해, 지금까지 입력했던 데이터가 말끔히 사라지는 현상을 목격.
이에 대한 대처방안이 필요하다. 
액티비티의 생명주기를 확인하고, 뷰 컴포넌트가 담고있는 데이터가 아닌, 변수 데이터를 살릴 수 있는 방법을 찾아본다.
https://snowdeer.github.io/android/2017/08/16/android-on-save-instance-state/

[이벤트 처리중 에러사항]
스와이프나 드래그 이벤트시 dataList가 없는 상황이 발생 왜 이런지 확인 필요.
==>(20191028) 이벤트가 두번 바인딩 되어서 생긴 오류로 추정된다. (https://geunsuheo.github.io/android/android-lifecycle/)
    안드로이드 생명주기에 따라 다시 시작시 onCreate() -> onStart() -> onRestoreInstanceState()-> onResume()
	순으로 호출되는데, onCreate와 onRestoreInstanceState 두 군데에서 객체를 만들고 있었다. 
	해결책으로는 qrcode==null 일경우에만 객체를 만들도록 허용했다. (정상동작한다.)
	
	

[Git에 반영]  
…or create a new repository on the command line  
echo "# QrCodeTest" >> README.md  
git init  
git add README.md  
git commit -m "first commit"  
git remote add origin https://github.com/sonsangjun/QrCodeTest.git  
git push -u origin master  

…or push an existing repository from the command line  
git remote add origin https://github.com/sonsangjun/QrCodeTest.git  
git push -u origin master  

…or import code from another repository  
You can initialize this repository with code from a Subversion, Mercurial, or TFS project.  
