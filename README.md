# spring_native_base

Es un proyecto ejemplo de spring native de hola mundo

La idea de utilizar spring-native, es para que los servicios consuman menos recursos y sean mas rapidos.

> https://www.baeldung.com/dockerizing-spring-boot-application  Dokenizar spring boot.

## Preparar el mac para spring native

Para poder arrancarlo en mac, para poder utilizar todas las herramientas hemos tenido que 
seguir los siguientes pasos:

1. Instalar sdk.
2. instalar graalvm.

```bash
sdk install java 21.0.2-graal
```

3. instalar gcc

```bash
brew install gcc

```

4. pack cli

```bash
brew install buildpacks/tap/pack
```

5. instalar zlib

```bash
brew install zlib
```

## Cambios para compilar.

A diferencia de los proyectos en windows o linux, es necesario cambiar el builder por el siguiente (dashaun/builder:tiny) 
en este caso lo que tenemos que cambiar el builder de paketo en la tarea de bootBuilderImage:

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <profiles>${sb.profiles}</profiles>
        <image>
            <name>dblanco80/spring-native-base</name>
            <!--suppress UnresolvedMavenProperty -->
            <builder>${paketo.builder}</builder>
            <publish>false</publish>
        </image>
    </configuration>
</plugin>
```
* **profile**: nos sirve para indicar los profiles activos que nos interesan.
* **name**: Es el nombre de la imagen <dockerHubId>/<repositorio> --> Tiene que coincidir con el mac
* **builder**: Es el builder que vamos a usar para montar la imagen.
  * (profile -> amdIntel (Defecto)  el builder (**paketobuildpacks/builder-jammy-tiny**), es el que usamos para compilar en windows/linux con graalvm21
  * (profile -> mac) el builder (**dashaun/builder:tiny**), es el usuamos para construir la imagen de mac.
* **publish**: Si lo vamos a publicar, en este caso no.


> Para mas información podemos mirar el siguiente repositorio (https://github.com/dashaun/paketo-arm64)
> Para windows p linux hay que usar otro builder (**paketobuildpacks/builder-jammy-tiny**), para poder usar jdk21 

## Como crear la imagen para docker:

Para crear la imagen, tenemos que ejecutar la tarea (bootBuildImage)

```bash
./mvnw clean spring-boot:build-image  -Pdev 
```

> Con este comando ya hemos conseguido crear una image para docker

## Publicar en el repositorio:

Lo primero que tenemos que hacer es configurar en el composer dos propiedades, a nivel del servicio

* build: Para indicar de donde obtenerlo, en este caso lo obtendremos del build de maven nos lo indica (**docker.io/dblanco80/spring-native-base**) que suele ser docker.io/<image> 
* imagen: Donde le indicaremos donde crearlo. <dokerhubid>/<repositorio> esta es la forma de indicarle que lo publique en dockerhub

En nuestro caso lo vamos a configurar en el docker composer, quedaria asi

```yaml
version: "3.9"
name: "local"
services:
  web:
    container_name: "base-native"
    build: "docker.io/dblanco80/spring-native-base"
    image: "dblanco80/spring-native-base:latest"
```

Una vez configurado, simplemente tenemos que llamar al push.

```bash
 docker compose -f ./infra/docker-compose.yml -f ./infra/docker-compose.dev.yml push
```
 
## Como ejecutar el proyecto en docker

En este caso tenemos que decidir si lo vamos hacer en nuestro docker local o en de otra maquina.

### Docker local

```bash
docker compose -f ./infra/docker-compose.yml -f ./infra/docker-compose.sit.yml up -d 
```

### Desplegar en una remoto

En este caso lo que tenemos que hacer es indicarle, al cliente de docker, donde se encuentra el repositorio, en nuestro caso es un ssh2

* Utilizaremos la variable de entorno DOCKER_HOST
* Levantamos.
* Borramos la variable de entorno.

```bash
export DOCKER_HOST=ssh://<user>@<ip>
docker compose -f ./infra/docker-compose.yml -f ./infra/docker-compose.sit.yml up -d 
unset DOCKER_HOST 
```


## Como configurar jenkins

A la instalación de jenkis tenemos que revisar este articulo

https://stackoverflow.com/questions/40043004/docker-command-not-found-mac-mini-only-happens-in-jenkins-shell-step-but-wo/58688536#58688536

## Montar el build:

* versiones
* build: Construimos la imagen
* desplegamos: Lanzamos la construcción.
* test end to end: Los test end to end.
* desmontar: Se encarga de desmontar.

## Configurar las sondas de kubernetes:

* La sonda de actividad. (Es la sonda que usa para determinar cuando reiniciar el contenedor)
* La sonda de preparación. (Esta lista para recibir solicitudes)

```properties
# Configuracion heath de kubernetes
management.endpoint.health.probes.enabled=true
management.health.livenessState.enabled=true
management.health.readinessState.enabled=true
```

> https://www.baeldung.com/spring-liveness-readiness-probes

## Configurar los entornos

### Build maven

 Lo primero que vamos hacer es crear un profile por cada entorno y pondremos el dev por defecto.

 > Por otro lado vamos a mapear con la propiedad ${sb.profiles} para pasarselo a spring.
 > Paketo.builder es el builder que vamos a usar. 

```xml
    <profiles>
        <profile>
            <profile>
                <id>amdIntel</id>
                <activation>
                    <activeByDefault>true</activeByDefault>
                </activation>
                <properties>
                    <paketo.builder>paketobuildpacks/builder-jammy-tiny</paketo.builder>
                </properties>
            </profile>
            <profile>
                <id>mac</id>
                <properties>
                    <paketo.builder>dashaun/builder:tiny</paketo.builder>
                </properties>
            </profile>
            <id>dev</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <junit.exlcude>Integracion</junit.exlcude>
                <sb.profiles>dev</sb.profiles>
            </properties>
        </profile>
        <profile>
            <id>uat</id>
            <properties>
                <junit.exlcude>Integracion</junit.exlcude>
                <sb.profiles>uat</sb.profiles>
            </properties>
        </profile>
        <profile>
            <id>sit</id>
            <properties>
                <junit.exlcude>Integracion</junit.exlcude>
                <sb.profiles>sit</sb.profiles>
            </properties>
        </profile>
        <profile>
            <id>prod</id>
            <properties>
                <junit.exlcude>Integracion</junit.exlcude>
                <sb.profiles>prod</sb.profiles>
            </properties>
        </profile>
        <profile>
            <id>endToEnd</id>
            <properties>
                <junit.exlcude>Unitarios</junit.exlcude>
                <sb.profiles>sit</sb.profiles>
            </properties>
        </profile>
    </profiles>
```

### Configurar los profiles de spring

En este caso simplemente le tenemos que pasar la variable anterior, al pluging de spring.

```xml
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <profiles>${sb.profiles}</profiles>
                </configuration>
            </plugin>
```

> En la variable profiles de la configuración pasamos ${sb.profiles}

### Como le indicamos en el contenedor el profile.

Ahora el problema es que se lo tenemos que indicar dentro del contenedor, para esto utilizaremos los parametros de enviroment 
que en este caso es **SPRING_PROFILES_ACTIVE** 

```yaml
version: "3.9"
name: "uat"
services:
  web:
    container_name: "base-native-uat"
    environment:
      - "SPRING_PROFILES_ACTIVE=uat"
    ports:
      - "8092:8080"
```

### Configuramos el status

En este caso lo que haces es configurar un endpoint status, para pasar la información correspondiente.

```java
    public record Status(String status,String env){}	

    @RequestMapping("/status")
	Status status(){
		var entornos = String.join(",",environment.getActiveProfiles());
		return new Status("OK",entornos);
	}
```



