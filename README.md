# Spring & Open Policy Agent Example

## Running

Start OPA server

```
cd infra && docker-compose up
```

Start Spring application

```
mvn spring-boot:run
```

During the startup, the `src/main/resources/policies/hello.rego` policy will export to the OPA server via REST-API automatically

## Fire some request

### 200

```
curl --location --request GET 'http://localhost:8080/hello' \
--header 'X-Username: bernie'
```

```
HTTP/1.1 200 OK
Content-Type: text/plain;charset=UTF-8
Content-Length: 3
 
Hi!
```


### 403

```
curl --location --request GET 'http://localhost:8080/hello' \
--header 'X-Username: not-bernie'
```

```
HTTP/1.1 403 Forbidden
Content-Type: text/plain;charset=UTF-8
Content-Length: 3
 
:-(
```

## TODO

- [ ] add OPA result to the 403 response 
- [ ] add Spring Security
