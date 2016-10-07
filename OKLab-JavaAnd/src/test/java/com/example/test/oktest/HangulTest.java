package com.example.test.oktest;

public class HangulTest{
    //한글 초성
    final char[] first = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ',
        'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
    //한글 중성
    final char[] middle = {'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 
        'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ',
        'ㅢ', 'ㅣ'};
    //한글 종성
    final char[] last = {' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 
        'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ',
        'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
    /**
    *한글 한 글자(char)를 받아 초성, 중성, 종성의 위치를 int[]로 반환 한다.
    *@param char : 한글 한 글자
    *@return int[] : 한글 초, 중, 종성의 위치( ex:가 0,0,0 )
    */
    public int[] split(char c){
        int sub[] = new int[3];
        sub[0] = (c - 0xAC00) / (21*28); //초성의 위치
        sub[1] = ((c - 0xAC00) % (21*28)) / 28; //중성의 위치
        sub[2] = (c -0xAC00) % (28);//종성의 위치
        return sub;
    } 

    /**
    *한글 한 글자를 구성 할 초성, 중성, 종성을 받아 조합 후 char[]로 반환 한다.
    *@param int[] : 한글 초, 중, 종성의 위치( ex:가 0,0,0 )
    *@return char[] : 한글 한 글자
    */
    public char[] combine(int[] sub){
        char[] ch = new char[1];
        ch[0] = (char) (0xAC00 + (sub[0]*21*28) + (sub[1]*28) + sub[2]);
        return ch;
    }
    
    /**
    *한글 초,중,종성 분리/조합 테스트 메소드
    */
    public void doSomething(){
        int[] x = null;
        String str = "my name is very stream ";
        int loop =  str.length();
        char c;
        System.out.println( "======"+loop+"======한글 분리============" );
        for( int i = 0; i < loop; i++ ){
            c = str.charAt( i );
            if( c >= 0xAC00 ){
                x = split( c );
                System.out.println( str.substring( i, i+1) + " : 초=" + first[x[0]] 
                        + "\t중="+middle[x[1]]
                        + "\t종="+last[x[2]] );
            }else{
                System.out.println( str.substring( i, i+1) );
            }
        }
        System.out.println( "\r\n============한글 조합============" );
        System.out.println( "0,0,0 : " +
                    new String( combine( new int[]{0,0,0} ) ) );
        System.out.println( "2,0,0 : " + 
                    new String( combine( new int[]{2,0,0} ) ) );
        System.out.println( "3,0,0 : " + 
                    new String( combine( new int[]{3,0,0} ) ) );
        System.out.println( "11,11,12 : " + 
                    new String( combine( new int[]{11,11,10} ) ) );
        System.out.println( "10,11,12 : " + 
                    new String( combine( new int[]{10,11,14} ) ) );
    }

    public static void main( String[] agrs ){
        new HangulTest().doSomething();
    }
}
