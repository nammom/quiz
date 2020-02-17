package spring;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import encrypt.Sha256Util;

public class ChangePasswordService {
	private MemberDao memberDao;
	
	public ChangePasswordService(MemberDao memberDao){
		this.memberDao = memberDao;
	}
	
	private PasswordEncoder encoder;
	public void setPassWordEncoder(PasswordEncoder encoder) {
		
	}
	
	@Transactional
	public void changePassword(String email, String oldPwd, String newPwd){
		Member member = memberDao.selectByEmail(email);
		if(member == null){
			throw new MemberNotFoundException();
		}
		
//		String dbPass = member.getPassword();
//		String oldSalt = dbPass.split("\\$")[1];  // 해시값 \$ salt 정규표현식 => $ 
//		
//		//사용자가 입력한 비밀번호와 기존 디비에 있는 salt값을 합쳐서  해싱-
//		StringBuffer eop = new StringBuffer();
//		eop.append(Sha256Util.getEncrypt(oldPwd, oldSalt)); //사용자가 입력한 이전 비밀번호와 디비에 저장된  salt값으로 해시생성 
//		eop.append("\\$").append(oldSalt);//해시 \$ salt 저장
//		oldPwd = eop.toString();
//		
//		//사용자가 입력한 새로운 비밀번호를 해싱
//		StringBuffer encryptPasword = new StringBuffer();
//		String newSalt = Sha256Util.genSalt();
//	
//		encryptPasword.append(Sha256Util.getEncrypt(newPwd, newSalt));
//		encryptPasword.append("\\$").append(newSalt);
//		System.out.println(encryptPasword.toString());
		
		newPwd = encoder.encode(newPwd);

		
		member.changePassword(oldPwd,  newPwd);
		memberDao.update(member);
	}
}
