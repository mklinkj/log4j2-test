package org.mklinkj.ldapserver;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:ldapserver-config.properties")
@Getter
public class LdapServerConfig {
  /** 실행시킬 명령어 */
  @Value("${ldaptest.remote.command:calc}")
  private String commandString;

  /** LDAP 서버 IP */
  @Value("${ldaptest.server.ip:127.0.0.1}")
  private String serverIp;

  /** LDAP 서버 포트 */
  @Value("${ldaptest.server.port:19090}")
  private int serverPort;
}
