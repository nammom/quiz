


package spring;

import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;

import encrypt.Sha256Util;

public class MemberRegisterService {
	private MemberDao memberDao;
	
	public MemberRegisterService(MemberDao memberDao){
		this.memberDao = memberDao;
	}
	
	private PasswordEncoder encoder;
	
	public void setPassWordEncoder(PasswordEncoder encoder) {
		
	}
	
	
	

	public void regist(RegisterRequest req){
		Member member = memberDao.selectByEmail(req.getEmail());
		if(member != null){
			throw new AlreadyExistingMemberException(
						"dup email " + req.getEmail());
		}
		//암호화 과정 start
//		StringBuffer encryptPasword = new StringBuffer();
//		String password = req.getPassword();
//		String salt = Sha256Util.genSalt();
//	
//		encryptPasword.append(Sha256Util.getEncrypt(password, salt));
//		encryptPasword.append("\\$").append(salt);
//		System.out.println(encryptPasword.toString());
//		
//		req.setPassword(encryptPasword.toString()); // => (해시값\$salt) 이렇게 저장됨
//		//해시로 암호화후 set
//		
		String password = req.getPassword();
		password = encoder.encode(password);
		req.setPassword(password);
		
		Member newMember = new Member(
				req.getEmail(),
				req.getPassword(),	//해시한 패스워드를 저장
				req.getName(),
				new Date()
				);
		memberDao.insert(newMember);		
	}
}
