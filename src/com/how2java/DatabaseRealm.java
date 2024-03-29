package com.how2java;
import java.util.Set;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class DatabaseRealm extends AuthorizingRealm {

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		//能进入到这里，表示账号已经通过验证了
		String userName =(String) principalCollection.getPrimaryPrincipal();
		//通过DAO获取角色和权限
		Set<String> permissions = new DAO().listPermissions(userName);
		Set<String> roles = new DAO().listRoles(userName);
		
		//授权对象
		SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();
		//把通过DAO获取到的角色和权限放进去
		s.setStringPermissions(permissions);
		s.setRoles(roles);
		return s;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//获取账号密码
		UsernamePasswordToken t = (UsernamePasswordToken) token;
		String userName= token.getPrincipal().toString();
		String password= new String( t.getPassword());
		//获取数据库中的密码
		String passwordInDB = new DAO().getPassword(userName);

		
		//如果为空就是账号不存在，如果不相同就是密码错误，但是都抛出AuthenticationException，而不是抛出具体错误原因，免得给破解者提供帮助信息
		if(null==passwordInDB || !passwordInDB.equals(password)) 
			throw new AuthenticationException();
		
		//认证信息里存放账号密码, getName() 是当前Realm的继承方法,通常返回当前类名 :databaseRealm
		SimpleAuthenticationInfo a = new SimpleAuthenticationInfo(userName,password,getName());
		return a;
	}

}
