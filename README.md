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
