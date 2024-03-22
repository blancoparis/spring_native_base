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

```groovy
bootBuildImage {

	builder = "dashaun/builder:tiny"
	environment = [
			"BP_NATIVE_IMAGE" : "true",
			"BP_JVM_VERSION" : "21"
	]
}
```




> Para mas información podemos mirar el siguiente repositorio (https://github.com/dashaun/paketo-arm64)
> Para windows p linux hay que usar otro builder (**paketobuildpacks/builder-jammy-tiny**), para poder usar jdk21 

## Como crear la imagen para docker:

Para crear la imagen, tenemos que ejecutar la tarea (bootBuildImage)

```bash
./gradlew clean bootBuildImage
```
> Con este comando ya hemos conseguido crear una image para docker
## Como ejecutar el proyecto en docker

Con el siguiente comando podemos lanzar en docker

```bash
docker run --rm -p 8080:8080 docker.io/library/base:0.0.1-SNAPSHOT
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

```xml
    <profiles>
        <profile>
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



