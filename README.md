## Nodes
  After `mvn compile` you can run

### one or two seed nodes
  - seed1
     `mvn -q -P seed1 exec:java`
  - seed2
     `mvn -q -P seed2 exec:java`

### several frontend and backend nodes
  - frontend
     `mvn -q -P frontend exec:java`
  - backend
     `mvn -q -P backend exec:java`

###
  - remote
     `mvn -q -P remote exec:java`

## Hosts and ports
Default hostname is `localhost`, `seed1` use port 2551, `seed2` use port 2552, remote uses port 12345
for frontend and backend nodes port is dynamically selected.

Node host could be set with `HOST` environment variable, and you can use
different seed hosts with `SEED1` and `SEED2` environment variables.

Example:
```
  SEED2=r2 mvn -q -P seed1 exec:java
  SEED2=r2 HOST=r2 mvn -q -P seed2 exec:java
  SEED2=r2 mvn -q -P frontend exec:java
  SEED2=r2 mvn -q -P backend exec:java
```
## SSL config

### Generate SSL certificate

Use 'keytool' to generate secure keystore, provide password to protect it:

    keytool -genkeypair -alias my_akka -keyalg RSA -validity 1500 -keystore keystore

Check that you can view certificate:

    keytool -list -v -keystore keystore

Export the certificate:

    keytool -export -alias my_akka -keystore keystore -rfc -file my_akka.cer

Import it into the truststore:

    keytool -import -alias my_akka_cert -file my_akka.cer -keystore truststore

Check that you can view it:

    keytool -list -v -keystore truststore

### akka config

  netty {
      ssl {
        hostname = "127.0.0.1"
        port = 12345
        enable-ssl = true
        security {
          enable = on
          key-password = "???"
          key-store = "pathto/keystore"
          key-store-password = "???"
          trust-store = "pathto/truststore"
          trust-store-password = "???"
          protocol = "TLSv1"
          enabled-algorithms = ["TLS_RSA_WITH_AES_128_CBC_SHA"]
          }
      }
    }

