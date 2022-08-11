package org.mklinkj.ldapserver;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import java.net.InetAddress;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LdapServer extends InMemoryOperationInterceptor {

  private final TomcatController tomcat;
  private final LdapServerConfig config;
  private InMemoryDirectoryServer ds;

  @Override
  public void processSearchResult(InMemoryInterceptedSearchResult result) {
    String base = result.getRequest().getBaseDN();

    try {
      tomcat.sendResult(result, base);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }

  @PostConstruct
  public void init() throws Exception {
    LOGGER.info("Starting LDAP server on {}:{}", config.getServerIp(), config.getServerPort());
    InMemoryDirectoryServerConfig serverConfig =
        new InMemoryDirectoryServerConfig("dc=example,dc=com");
    serverConfig.setListenerConfigs(
        new InMemoryListenerConfig(
            "listen",
            InetAddress.getByName(config.getServerIp()),
            config.getServerPort(),
            ServerSocketFactory.getDefault(),
            SocketFactory.getDefault(),
            (SSLSocketFactory) SSLSocketFactory.getDefault()));

    serverConfig.addInMemoryOperationInterceptor(this);
    ds = new InMemoryDirectoryServer(serverConfig);
  }

  /** 서버 시작 */
  public void start() throws Exception {
    ds.startListening();
    LOGGER.info("[LDAP Server Start]");
  }

  /** 서버 종료 */
  @PreDestroy
  public void close() {
    ds.shutDown(true);
    LOGGER.info("[LDAP Server Shutdown]");
  }
}
