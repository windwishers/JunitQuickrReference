

import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.closeTo
import org.junit.*
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.rules.ExpectedException
import java.lang.IllegalArgumentException
import org.hamcrest.CoreMatchers.`is` as Is // 코틀린의 경우 is 가 예약어라서 alias import 가 필요함.


/**
 *  출처 : 자바와 JUnit를 이용한 실용주의 단위 테스트.
 *  Arrange(준비) - Act(실행) - Assert(단언)을 이용하여 테스트 일관성을 유지 합니다.
 *  Arrange : 테스트전 시스템이 일관성을 유지 하는 지 확인함.
 *  Act :  테스트 코들르 실행함.
 *  Assert : 실행한 코드가 기대한 대로 동작 했는지 확인 합니다.
 *  After : 테스트 중 할당한 자원을 적절히 해제 합니다. (optional)
 *
 *  테스트이름
 *  whenDoingSomeBehaviorThenSomeResultOccurs
 *  doingSomeOperationGeneratesSomeResult
 *  //코틀린에서는 `(백틱) 을 이용하여 한글로 테스트명을 쓸수있음.
 *  `언제무엇을해서예상결과갑`
 *  `어떤명령이어떤결과기대`
 *  TODO : 한글 일때는 어떻게 정리해야 명확할지 고민해보자.
 *
 *
 */
class Basic {

    @Test
    fun assertTrueTest(){
        Assert.assertTrue(true)
        Assert.assertTrue("테스트 메시지를 넣을 수 있음",true)
    }

    @Ignore  //Ignore 어노테이션이 붙으면 해당 테스트가 무시 됨.
    @Test
    fun assertTrueFailTest(){
        Assert.assertTrue(false)
    }

    @Test
    fun assertThatHamcrestMatcherTest(){  // 햄크레스트 매처를 이용한 테스트 작성.
        assertThat(100, equalTo(100))  //객체 동등.
//        assertThat(100, equalTo(101))
        /* 테스트에 실패 하는 경우 아래 와 같이 좀더 읽기 편한 에러 메시지가 표시 됩니다.
        Expected: <101>
     but: was <100>
         */
        // 단일 테스트 원칙에 따라 원래는 여러개가 위치하면 안된 다고 함.
        assertThat(100, equalTo(100))  // 프리미티브 동등
        assertThat("String", equalTo("String")) // 객체 동등
        assertThat(intArrayOf(1,2), equalTo(intArrayOf(1,2)))  // array  동등

        assertThat(arrayOf("0","1"), equalTo(arrayOf("0","1")))  // arrayOf - arrayOf 동등
        assertThat( Array<String>(2){"$it"}, equalTo(arrayOf("0","1")))  // Array - arrayOf 동등
        assertThat(arrayOf("0","1"), equalTo( Array<String>(2){"$it"}))  // arrayOf - Array 동등
        assertThat( Array<String>(2){"$it"}, equalTo( Array<String>(2){"$it"}))  // Array - Array 동등.
        assertThat("string is equal to",Is(equalTo("string is equal to")))  // IS 는 아무일도 하지 않음 읽기 편하기 위한 보일러 플레이트. // 코틀린의 경우 is 가 예약어라서 alias import 가 필요함.
        val `null`: String? = null
        val notNull: String? = "notNull"
        assertThat(`null`, nullValue())  // 그냥 null 로 넣으면 햄그레스트 메처는 템플릿이라 타입추정에 실패하기때문에  nullValue() 가 따로 있음.
        assertThat(notNull, not(nullValue()))   // 값을 반전 시키는 not() 어노테이션이 존재함.

        assertThat("xyz123", startsWith("xyz"))
        assertThat("string startWith xyz.. ","xyz123", startsWith("xyz")) // 테스트 사유를 적을 수 있음.

        //http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/CoreMatchers.html 햄그레스트 문서 //TODO 기본 탑재 햄크레스트와 추가 디펜던시 추가시 사용할 수 있는 햄크레스트 확인 할 것.

        // 햄그레스트 디펜던시를 직접 추가 해야 쓸 수 있음.
        assertThat(2.32 * 3,closeTo(6.96,0.005))
    }

    fun throwIllegalArgumentException(msg : String = ""){
        throw IllegalArgumentException(msg)
    }

    @Test(expected = IllegalArgumentException::class)
    fun expectedExceptionTest(){
        throwIllegalArgumentException() //expected 에 지정 되어 있는 익셉션은 에러로 표시 되지 않으며, 익셉션이 발생하지 않으면 테스트가 실패한다.
    }

    @Test
    fun tryCatchExceptionTest(){  // 단순히 예외가 무시되는 것이 아니라 예외가 나지 않아도 에러가 나야함으로 expected 에 비해 추가 적인 보일러 플레이트가 필요합니다.
        try {
            throwIllegalArgumentException()
            fail()  // expected 와 같은 동작을 하기 위해서는 테스트 위치 밑에서 에러가 나야한다.
        } catch (e: Exception) {

        }
    }

    @Rule
    @JvmField  // The @Rule 'thrown' must be public. 이 발생함으로 꼭 넣어주자.
    val thrown = ExpectedException.none()!!

    @Test
    fun expectedExceptionExceptionTest(){

        thrown.expect(IllegalArgumentException::class.java)  // expect 와 는 다르게 java 까지 적어줘야함.
        thrown.expectMessage("message check available")

        throwIllegalArgumentException("message check available")

    }


    companion object{

        @JvmStatic
        @BeforeClass
        fun initializeTestClass(){
            println("테스트 클래스 시작 전 실행 됨 ")
        }

        @JvmStatic
        @AfterClass
        fun cleanUpTestClass(){
            println("각 테스트가 모두 종료 된 후 실행 됨")
        }
    }


    @Before
    fun prepareTest(){
        println("각 테스트 시작 전 실행됨")
    }


    @After
    fun cleanUpTest(){
        println("각 테스트 후 실행됨")
    }


}