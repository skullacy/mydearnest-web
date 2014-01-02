package com.junglebird.webframe.common;

public class StringUtils {

	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static String replaceCrLf(String str) {
		return str.replaceAll("\\n", "<br />");
	}
	
	
	
	
	/**
	 * @brief
	 * 한글입력을 치환하기 위한 상수들. (자소분리, 영타전환 구현)
	 */
	private final static char[] arrChoSung = { 0x3131, 0x3132, 0x3134, 0x3137, 0x3138,
				0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148,
				0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };
	private final static char[] arrJungSung = { 0x314f, 0x3150, 0x3151, 0x3152,
				0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a,
				0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162,
				0x3163 };
	private final static char[] arrJongSung = { 0x0000, 0x3131, 0x3132, 0x3133,
				0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c,
				0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145,
				0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };
	
	
	private static final String[] arrChoSungEng = { "r", "R", "s", "e", "E",
				"f", "a", "q", "Q", "t", "T", "d", "w",
		        "W", "c", "z", "x", "v", "g" };
	private static final String[] arrJungSungEng = { "k", "o", "i", "O",
				"j", "p", "u", "P", "h", "hk", "ho", "hl",
				"y", "n", "nj", "np", "nl", "b", "m", "ml",
				"l" };
	private static final String[] arrJongSungEng = { "", "r", "R", "rt",
				"s", "sw", "sg", "e", "f", "fr", "fa", "fq",
				"ft", "fx", "fv", "fg", "a", "q", "qt", "t",
				"T", "d", "w", "c", "z", "x", "v", "g" };
	private static final String[] arrSingleJaumEng = { "r", "R", "rt",
				"s", "sw", "sg", "e","E" ,"f", "fr", "fa", "fq",
				"ft", "fx", "fv", "fg", "a", "q","Q", "qt", "t",
				"T", "d", "w", "W", "c", "z", "x", "v", "g" };
	
	/**
	 * @brief 한글입력값 자소분리하기.
	 * @param word
	 * @return word_splitted
	 */
	public static String jasoSplit(String word) {
		return KorHandler(word, "jaso");
	}
	
	public static String replaceEngPos(String word) {
		return KorHandler(word, "eng");
	}
	
	private static String KorHandler(String word, String type) {
		String resultSplitted = new String();
		String resultEng = new String();
		for(int i = 0; i < word.length(); i++) {
			char chars = (char) (word.charAt(i) - 0xAC00);
			
			//자음과 모음이 합쳐진 글자인 경우.
			if (chars >= 0 && chars <= 11172) {
				
				// 초,중,종성 분리하기
				int chosung			= chars / (21 * 28);
				int jungsung		= chars % (21 * 28) / 28;
				int jongsung		= chars % (21 * 28) % 28;
				
				//word_splitted에 저장
				resultSplitted = resultSplitted + arrChoSung[chosung] + arrJungSung[jungsung];
				
				//종성이 존재할 경우 추가로 담기
				if(jongsung != 0x0000) {
					resultSplitted = resultSplitted + arrJongSung[jongsung];
				}
				
				//알파벳으로 치환
				resultEng = resultEng + arrChoSungEng[chosung] + arrJungSungEng[jungsung];
				
				//종성이 존재할 경우
				if(jongsung != 0x0000) {
					resultEng = resultEng + arrJongSungEng[jongsung];
				}
			}
			//자음만 있거나 한글이 아닌경우.
			else {
				//자음분리하기
				resultSplitted = resultSplitted + ((char)(chars + 0xAC00));
				
				//알파벳으로 변환
				//단일자음인 경우
				if(chars >= 34097 && chars <= 34126) {
					int jaum = (chars - 34097);
					resultEng = resultEng + arrSingleJaumEng[jaum];
				} 
				//단일모음인 경우
				else if(chars >= 34127 && chars <= 34147) {
					int moum = (chars - 34127);
					resultEng = resultEng + arrJungSungEng[moum];
				}
				//알파뱃인경우
				else {
					resultSplitted = resultSplitted + ((char)(chars + 0xAC00)); 
					resultEng = resultEng + ((char)(chars + 0xAC00));
				}
			}
		}
		
		if (type.equals("jaso")) return resultSplitted;
		else if(type.equals("eng")) return resultEng;
		else return null;
	}
	
}
