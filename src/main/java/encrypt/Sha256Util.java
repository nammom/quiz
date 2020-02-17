package encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Sha256Util {
	

	
	//data와 salt를 합쳐 해시값 생성하는 메서드
	public static String getEncrypt(String source, byte[] salt) {
		String resultValue = "";
		
		byte[] src = source.getBytes();
		byte[] bytes = new byte[src.length + salt.length];
		
		System.arraycopy(src, 0, bytes, 0, src.length);
		//src의 0번째부터 src.length만큼  bytes배열에 0번째 부터 복사하겠다 
		System.arraycopy(salt, 0, bytes, src.length, salt.length);
		//bytes배열에 src+salt 합침
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			//SHA-256 해싱을 해주는 기능
			
			md.update(bytes);	//배열올리고
			byte[] byteData = md.digest();//해싱실행
			
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xFF)+ 256, 16).substring(1));
											//bit비트를 양수로 만드는 공식
			}
			resultValue = sb.toString();
		}
		catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return resultValue;
	}
	
	
	//오버로딩
	public static String getEncrypt(String source, String salt) {
		return getEncrypt(source, salt.getBytes());//String을 Byte배열로 반환
	}
	
	
	//salt만드는 메서드
	public static String genSalt(){
		Random random = new Random();
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		//배열에 1바이트짜리 하나씩 총 8개 => 8바이트짜리 랜덤값을 만듬
		System.out.println("salt :" + salt);
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < salt.length; i++) {
			sb.append(String.format("%02x", salt[i]));
		}
		System.out.println("salt ret :" + sb.toString());
		return sb.toString();	
		
	}


}
